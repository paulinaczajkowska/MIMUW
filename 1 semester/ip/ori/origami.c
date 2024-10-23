// Autor: Paulina Czajkowska
// Code review'er: Piotr Łaba

#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <stdbool.h>

typedef struct punkt {
    double x, y;
} punkt;

// W zależności od typu, wykorzystywane są tylko poszczególne elementy struktury kartka
// Dla typu P używamy: typ, P1, P2; dla typu K: typ, P1, r; dla typu Z: typ, k, P1, P2
typedef struct kartka {
    char typ;
    punkt P1, P2;
    double r;
    int k;
} kartka;

void wczytajDane(int n, int q, kartka* t, int* nr, punkt* P) {
    for (int i = 0; i < n; ++i) {
        kartka c;
        scanf(" %c", &c.typ);
        if (c.typ == 'P') {
            scanf("%lf%lf%lf%lf", &c.P1.x, &c.P1.y, &c.P2.x, &c.P2.y);
        }
        else if (c.typ == 'K') {
            scanf("%lf%lf%lf", &c.P1.x, &c.P1.y, &c.r);
        }
        else {
            scanf("%d%lf%lf%lf%lf", &c.k, &c.P1.x, &c.P1.y, &c.P2.x, &c.P2.y);
        }
        t[i] = c;
    }
    for (int i = 0; i < q; ++i) {
        scanf("%d%lf%lf", &nr[i], &P[i].x, &P[i].y);
    }
}

double iloczynSkalarny (punkt P1, punkt P2) {
    double iloczyn;
    iloczyn = P1.x * P2.x + P1.y * P2.y;
    return iloczyn;
}

double iloczynWektorowy (punkt P1, punkt P2) {
    double iloczyn;
    iloczyn = P1.x * P2.y - P2.x * P1.y;
    return iloczyn;
}

// Izometria ustawiająca punkt P1 na (0, 0) (punkt P można również interpretować jako wektor P1 P2)
punkt przesunieciePunktu(punkt P1, punkt P2) {
    punkt P;
    P.x = P2.x - P1.x;
    P.y = P2.y - P1.y;
    return P;
}

void wyznaczPunktPrzeciwny(punkt P1, punkt M2, punkt M, punkt *Pp) {
    // Odległość punkt P od prostej P1 P2 to iloczyn wektorowy P1 P2 i P1 P, podzielony przez długość odcinka P1 i P2
    // Funckja fabs niepotrzebna, ponieważ P (czyli po przesunięciu M) na lewo od prostej P1 P2 (wynika to z funkcji liczbaWarstw)
    double odleglosc = iloczynWektorowy(M2, M) / sqrt(iloczynSkalarny(M2, M2));
    // Obrocenie wektora o -90 stopni: [a, b] --> [b, -a] (M2 rozpatrujemy jako wektor)
    double temp = M2.x;
    M2.x = M2.y;
    M2.y = -temp;
    double dWektora = sqrt(iloczynSkalarny(M2, M2));
    // Modyfikujemy wektor M2 tak, aby jego długość była równa dwukrotnej odległości punktu przebicia od danej prostej
    M2.x = (M2.x / dWektora) * odleglosc * 2;
    M2.y = (M2.y / dWektora) * odleglosc * 2;
    // Aby uzyskać punkt przeciwny, punkt M musimy powrócić na jego pierwotne miejsce (P) oraz przesunać o wektor M2
    punkt m;
    m.x = M.x + P1.x + M2.x;
    m.y = M.y + P1.y + M2.y;
    *Pp = m;
}

bool czyZero(double x) {
    return fabs(x) < 1e-10;
}

bool czyNieMniejsze(double x, double y) {
    return (czyZero(x-y) || x > y);
}

int liczbaWarstw(int i, kartka* t, int nr, punkt P) {
    if (t[nr].typ == 'P') {
        if (czyNieMniejsze(P.x, t[nr].P1.x) && czyNieMniejsze(t[nr].P2.x, P.x)
         && czyNieMniejsze(P.y, t[nr].P1.y) && czyNieMniejsze(t[nr].P2.y, P.y)) {
            return 1;
        }
        return 0;
    }
    if (t[nr].typ == 'K') {
        punkt M1 = przesunieciePunktu(P, t[nr].P1);
        if (czyNieMniejsze(t[nr].r * t[nr].r, iloczynSkalarny(M1, M1))) {
            return 1;
        }
        return 0;
    }
    punkt M2 = przesunieciePunktu(t[nr].P1, t[nr].P2);
    punkt M = przesunieciePunktu(t[nr].P1, P);
    double strona = iloczynWektorowy(M2, M);
    if (czyZero(strona)) {
        // Jeżli punkt jest na prostej to szpilka przebija tyle warstw ile przed złożeniem kartki
        return liczbaWarstw(i, t, t[nr].k - 1, P);
    }
    if (strona < 0) {
        // Jeżli punkt jest po prawej stronie prostej to nie przebija kartki
        return 0;
    }
    punkt Pp;
    wyznaczPunktPrzeciwny(t[nr].P1, M2, M, &Pp);
    // Jeżli punkt jest po lewej stronie prostej to szpilka przebija tyle warstw ile przed złożeniem powiększone o liczbę
    // warstw, które przebija szpilka w punkcie przeciwnym względem danej prostej
    return liczbaWarstw(i, t, t[nr].k - 1, P) + liczbaWarstw(i, t, t[nr].k - 1, Pp);
}

int main()
{
    int n, q;
    scanf("%d%d", &n, &q);
    kartka* t = (kartka*) malloc((size_t)n * sizeof(kartka));
    int* nr = (int*) malloc((size_t)q * sizeof(int));
    punkt* P = (punkt*) malloc((size_t)q * sizeof(punkt));
    wczytajDane(n, q, t, nr, P);
    for (int i = 0; i < q; ++i) {
        printf("%d\n", liczbaWarstw(i, t, nr[i] - 1, P[i]));
    }
    free(nr);
    free(P);
    free(t);
    return 0;
}