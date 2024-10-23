global mdiv

; Zmienna   *x    znajduje się w    rdi
; Zmienne   x[a]  znajdują się w    [rdi + a*8]
; Zmienna   n     znajduje się w    rsi
; Zmienna   y     znajduje się w    rdx

mdiv:
    ; Przeniesienie wartości rejestru rdx na r10, ponieważ rejestr rdx będzie potrzebny do dzielenia.
    mov     r10, rdx

    ; Rejestry r8 i r9 będą przechowywały pierwotne wartości x[n - 1] oraz y
    mov     r8, [rdi + rsi*8 - 8]
    test    r8, r8

    ; Zmienienie wartość dzielnej na dodatnią (tylko jeżeli jest ujemna).
    jns     .overflow

.change_sign:

    ; Rejestru r9 jest używany w pętli, więc na początku musi być wyzerowany.
    xor     r9, r9
    mov     rcx, rsi

    ; Ustawienie CF na 1, aby w pierwszym wykonaniu pętli dodać 1.
    stc

.change_sign_loop:
    not     QWORD [rdi + r9*0x8]
    adc     QWORD [rdi + r9*0x8], 0x0
    inc     r9
    loop    .change_sign_loop

    ; Po dzieleniu r10 i rdx są różne, ponieważ reszta z dzielenia jest różna od dzielnika.
    cmp    r10, rdx
    jne    .exit

.overflow:

    ; Overflow może wystąpić tylko w przypadku, gdy dzielnik jest równy -1.
    cmp     r10, 0xFFFFFFFFFFFFFFFF
    jne     .divider

    ; Przesunięcie o -1, ponieważ do ustalenia znaku ważna jest tylko końcówka.
    mov     al, BYTE [rdi + rsi*8 - 1]
    test    al, al

    ; Po zanegowaniu, jedyną liczbą, która pozostaje ujemna, jest najmniejsza możliwa liczba ujemna.
    jns     .divider

    ; Rejestr rcx ma wartość 0, ponieważ skończyła się change_sign_loop.
    div     rcx

.divider:
    mov     r9, r10
    test    r9, r9

    ; Zmiana wartości dzielnika na dodatnią.
    jns     .division
    neg     r10

.division:
    xor     rdx, rdx
    mov     rcx, rsi

.loop:
    mov     rax, QWORD [rdi + rcx*8 - 8]
    div     r10
    mov     QWORD [rdi + rcx*8 - 8], rax
    loop     .loop

    ; Resza z dzielenia ma być zwrócona na rejestrze rax.
    mov     rax, rdx

    ; Dzielna jest tego samego znaku co reszta z dzielenia.
    test    r8, r8
    jns     .result
    neg     rax

.result:

    ; Wynik dzielenia jest tego samego znaku, co iloczyn dzielnej i dzielnika.
    xor     r9, r8
    jns     .exit
    jmp     .change_sign     

.exit:
    ret