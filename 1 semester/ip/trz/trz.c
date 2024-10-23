// autor: Paulina Czajkowska
// code review'er: Piotr Łaba

#include <stdio.h>
#include <math.h>
#include <stdlib.h>

int min(int a, int b) {
    if (a < b) {
        return a;
    }
    return b;
}

int max(int a, int b) {
    if (a > b) {
        return a;
    }
    return b;
}

void trzeci_hotel_max(int** t, int* dmax, int **mi, int i, int j) {
    for (int k = mi[i][1] + 1; k < mi[j][1]; ++k) {
        if (mi[i][0] != t[k][0] && mi[j][0] != t[k][0] && min(t[k][1] - t[mi[i][1]][1], t[mi[j][1]][1] - t[k][1]) > *dmax) {
            *dmax = min(t[k][1] - t[mi[i][1]][1], t[mi[j][1]][1] - t[k][1]);
        }
    }
}

void trzeci_hotel_min(int** t, int* dmin, int l, int m, int r) {
    if (max(t[m][1] - t[l][1], t[r][1] - t[m][1]) < *dmin) {
        *dmin = max(t[m][1] - t[l][1], t[r][1] - t[m][1]);
    }
}

void trzy_rozne_skrajne(int** t, int i, int** mi, int p) {
    if (!mi[p+1][1] && t[i][0] != mi[p][0]) {
        mi[p+1][0] = t[i][0];
        mi[p+1][1] = i;
    }
    else if (mi[p+1][1] && !mi[p+2][1] && t[i][0] != mi[p][0] && t[i][0] != mi[p+1][0]) {
        mi[p+2][1] = i;
        mi[p+2][0] = t[i][0];
    }
}

void przesun_indeksy(int **t, int n, int* l, int* m, int* r) {
    // lewy i prawy w zaleznosci od srodkowego
    *l = *m - 1;
    *r = *m + 1;
        while (*r != n && t[*m][0] == t[*r][0]) {
            *r +=1;
        }
        while (*r != n && t[*l][0] == t[*r][0]) {
            // lewy i srodkowy w zaleznosci od prawego
            *l = *r - 1;
            *m = *r;
            *r = *m + 1;
            while (*r != n && t[*m][0] == t[*r][0]) {
                *r +=1;
            }
        }
}

int najdalsza_trojka(int n, int **t, int *czy_trzy) {
    int dmax = 0;
    // tablica par motel - indeks, pierwsza polowa par (0 - 2) to pierwsze hotele, druga (3 - 5) to ostatnie
    int** mi = malloc(6 * sizeof(int*));
    for (int i = 0; i < 6; ++i) {
        mi[i] = malloc(2 * sizeof(int));
        mi[i][1] = 0;
    }
    mi[3][1] = n - 1;
    // znalezienie trzech pierwszy roznych
    mi[0][0] = t[0][0];
    for (int i = 1; i < n; i++) {
        trzy_rozne_skrajne(t, i, mi, 0);
    }
    // wynik 0 0, gdy nie ma co najmniej trzech roznych hoteli
    if (!mi[1][1] || !mi[2][1]) {
        *czy_trzy = 0;
        free(mi);
        return 0;
    }
    // znalezienie trzech ostatnich roznych
    mi[3][0] = t[n - 1][0];
    for (int i = n - 1; i >= 0; --i) {
        trzy_rozne_skrajne(t, i, mi, 3);
    }
    for (int i = 0; i < 3; ++i) {
        for (int j = 3; j < 6; ++j) {
            if (mi[i][0] != mi[j][0]) {
                trzeci_hotel_max(t, &dmax, mi, i, j);
            }
        }
    }
    free(mi);
    return dmax;
}

int najblizsza_trojka(int n, int **t) {
    int l = 0, m = 1, r;
    int dmin = t[n - 1][1];
    // szukanie srodkowego motelu
    while (t[l][0] == t[m][0]) {
        m++;
    }
    przesun_indeksy(t, n, &l, &m, &r);
    while (r < n) {
        while (m < r) {
            trzeci_hotel_min(t, &dmin, l, m, r);
            m++;
        }
            przesun_indeksy(t, n, &l, &m, &r);
    }
    return dmin;
}

int main()
{
    int n;
    scanf("%d", &n);
    // wynik 0 0, gdy nie ma moteli
    if (!n) {
        printf("%d %d", 0, 0);
        return 0;
    }
    int** t = malloc((size_t)n * sizeof(int*));
    for (int i = 0; i < n; ++i) {
        t[i] = malloc(2 * sizeof(int));
    }
    for (int i = 0; i < n; ++i) {
        scanf("%d%d", &t[i][0], &t[i][1]);
    }
    int czy_trzy = 1;
    int dmax = najdalsza_trojka(n, t, &czy_trzy);
    if (!czy_trzy) {
        printf("0 0");
        free(t);
        return 0;
    }
    int dmin = najblizsza_trojka(n, t);
    printf("%d %d", dmin, dmax);
    free(t);
    return 0;
}