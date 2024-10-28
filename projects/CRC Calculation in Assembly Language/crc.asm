section .bss
    ; Bufor o rozmiarze 2^16 bajtów, aby zawsze dało się wczytać przynajmniej jeden fragemnt.
    buffer              resb 65536

    polynomial          resq 1
    polynomial_length   resb 1

    ; Rezerwujemy 256 * 64 bitów na lookup table.
    lookup_table        resq 256

    data_length         resq 1
    offset              resq 1
    file_descriptor     resq 1
    buffer_curr_size    resq 1
    crc_value           resq 1


section .text
    global _start

_start:
    ; Sprawdzenie liczby argumentów.
    ; Argumenty to po kolei: nazwa programu, nazwa pliku, wielomian CRC - łącznie 3.
    mov     rdi, [rsp]
    cmp     rdi, 0x3
    jne     error

    ; Sprawdzenie poprawności wielomianu CRC - czy nie jest pusty.
    mov     rsi, [rsp + 24]
    mov     al, [rsi]
    test    al, al
    jz      error

    ; Do wykonania sprawdzenia potrzebujemy wyzerowanych rejestrów rdi, rax i dl.
    xor     rdi, rdi
    xor     rax, rax
    xor     dl, dl

    ; Sprawdzenie poprawności wielomianu CRC - czy zawiera same 0 i 1.
validate_loop:
    mov     al, [rsi]
    test    al, al
    jz      set_polynomial
    add     dl, 0x1

    ; Sprawdzenie pojedynczego znaku.
    cmp     al, '0'
    je      next_char
    cmp     al, '1'
    je      next_char

    ; Niepoprawny znak - błedny wielomian CRC.
    jmp error

next_char:
    ; Zapisanie bitu wielomianu CRC do rejestru rdi.
    shl     rdi, 1
    sub     al, '0'
    add     rdi, rax

    ; Przejście do następnego znaku wielomianu CRC.
    inc     rsi
    jmp     validate_loop

set_polynomial:
    mov     [polynomial_length], dl
    mov     cl, 64
    sub     cl, dl
    shl     rdi, cl
    mov     [polynomial], rdi

open_file:
    mov     rdi, [rsp + 16]
    mov     rsi, 0
    mov     rax, 2
    syscall

    ; Sprawdzenie poprawności.
    cmp     rax, 0
    js      error

    ; Zachowanie deskryptora pliku
    mov     [file_descriptor], rax

    ; Do pętli potrzebny jest wyzerowany rejestr rsi.
    xor     rsi, rsi
    lea     rdx, [lookup_table]

lookup_table_loop:
    mov     rax, rsi
    shl     rax, 56
    mov     rcx, 8

crc_bit_by_bit_loop:
    shl     rax, 1
    jnc     no_xor
    xor     rax, [polynomial]

no_xor:
    loop    crc_bit_by_bit_loop

    mov     [rdx + rsi * 8], rax  
    inc     rsi
    cmp     rsi, 256
    jne     lookup_table_loop

    ; Wczytywanie danych do bufora i liczenie CRC.
    ; Ustawienie początkowego przesunięcia i aktualnego rozmiaru bufora na 0.
    xor     rsi, rsi
    mov     QWORD [buffer_curr_size], 0

read_loop:
    ; Odczytanie długości danych.
    xor     rax, rax
    mov     rdx, 2
    mov     rsi, data_length
    mov     rdi, [file_descriptor]
    syscall

    ; Sprawdzenie poprawności odczytu.
    cmp     rax, 2
    jne     error_close_file

    ; Sprawdzenie, czy nowe dane zmieszczą się do bufora.
    mov     r10, [data_length]
    add     r10, [buffer_curr_size]
    cmp     r10, 65536

    ; Jeśli danych jest więcej niż mamy wolnego miejsca, to kończymy czytanie danych.
    ja      fix_read_position

    ; Odczytanie danych do bufora.
    xor     rax, rax
    mov     rdx, [data_length]
    mov     rsi, buffer
    add     rsi, [buffer_curr_size]
    mov     rdi, [file_descriptor]
    syscall

    ; Sprawdzenie poprawności odczytu.
    cmp     rax, [data_length]
    jne     error_close_file

    ; W rejestrze rax jest ilość przeczytanych danych.
    add     QWORD [buffer_curr_size], rax

    ; Odczytanie długości przesunięcia
    xor     rax, rax
    mov     rdx, 4
    mov     rsi, offset
    mov     rdi, [file_descriptor]
    syscall

    ; Sprawdzenie poprawności odczytu.
    cmp     rax, 4
    jne     error_close_file

    ; Ustawienie bieżącej pozycji odczytu na początek fragmentu.
    mov     rax, 8
    mov     esi, [offset]
    movsxd  rsi, esi
    mov     rdx, 1
    mov     rdi, [file_descriptor]
    syscall

    ; Sprawdzenie poprawności przesunięcia bieżącej pozycji.
    test    rax, rax
    js      error_close_file

    ; Jeżeli fragment wskazuje na samego siebie to wychodzimy z pętli.
    xor     rdi, rdi
    mov     ecx, [offset]
    add     ecx, 6
    add     ecx, [data_length]
    mov     rcx, 1
    jz      calculate_crc
    jmp     read_loop

fix_read_position:
    mov     rax, 8
    mov     rsi, -2
    mov     rdx, 1
    mov     rdi, [file_descriptor]
    syscall

    ; Sprawdzenie poprawności przesunięcia bieżącej pozycji.
    test    rax, rax
    js      error_close_file

    ; Do pętli obliczającej CRC jest potrzebny wyzerowany rejestr rcx.
    xor     rcx, rcx

    ; Link do strony z algorytmem do obliczania CRC znajduje się w komentarzu do zadania.
calculate_crc:
    mov     al, [buffer + rdi]
    shl     rax, 56
    xor     rax, [crc_value]
    shr     rax, 56
    shl     QWORD [crc_value], 8
    mov     rax, [lookup_table + rax * 8]
    xor     [crc_value], rax

    inc     rdi
    cmp     rdi, QWORD [buffer_curr_size]
    je      check_if_last
    jmp     calculate_crc

check_if_last:
    cmp     rcx, 1
    je      read_end
    mov     QWORD [buffer_curr_size], 0
    jmp     read_loop

read_end:
    ; Zamknięcie pliku.
    mov     rax, 3
    mov     rdi, [file_descriptor]
    syscall

    ; Sprawdzenie poprawności.
    test    rax, rax
    js      error
    
    ; Wypisanie wyniku CRC (bufor już nie jest potrzebny).
    mov     rcx, 63
    mov     rsi, 0

print_loop:
    mov     rax, [crc_value]
    shr     rax, cl
    and     rax, 1
    add     rax, '0'
    mov     [buffer + rsi], al
    inc     rsi
    dec     rcx
    jns     print_loop

    ; Dodanie znaku nowej linii.
    mov     rax, [polynomial_length]
    mov     [buffer + rax], byte `\n`

    mov     rdi, 1
    mov     rsi, buffer
    mov     rdx, [polynomial_length]
    inc     rdx
    mov     rax, 1
    syscall

    ; Sprawdzenie poprawności.
    test    rax, rax
    js      error

    ; Zakończenie programu.
    mov     rax, 60
    xor     rdi, rdi
    syscall

error_close_file:
    ; Zamknięcie pliku.
    mov     rax, 0x3
    mov     rdi, [file_descriptor]
    syscall

    ; Nie sprawdzamy błędu, bo i tak wychodzimy z kodem błędu.

error:
    ; Zakończenie programu z kodem błędu.
    mov     rax, 60
    mov     rdi, 1
    syscall