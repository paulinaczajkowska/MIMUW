/* autor: Paulina Czajkowska  */
/* code review'er: Piotr Łaba */

#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include "ary.h"

#define epsilon 1e-10

bool czy_rowne(double x, double y) {
    return (fabs(x - y) < epsilon);
}

void swap(wartosc *a, wartosc *b){
  wartosc temp = *a;
  *a = *b;
  *b = temp;
}

void wewnetrzny_niepusty(wartosc *w) {
    w -> wewnatrz = 1;
    w -> pusty = 0;
}

wartosc wartosc_dokladnosc(double x, double p) {
    wartosc w;
    w.minimum = x - fabs(x) * (p / 100);
    w.maximum = x + fabs(x) * (p / 100);
    wewnetrzny_niepusty(&w);
    return w;
}

wartosc wartosc_od_do(double x, double y) {
    wartosc w;
    w.minimum = x;
    w.maximum = y;
    wewnetrzny_niepusty(&w);
    return w;
}

wartosc wartosc_dokladna(double x) {
    wartosc w;
    w.minimum = x;
    w.maximum = x;
    wewnetrzny_niepusty(&w);
    return w;
}

bool in_wartosc(wartosc w, double y) {
    if (w.pusty) {
        return 0;
    }
    else if (w.wewnatrz && y > w.minimum - epsilon && y < w.maximum + epsilon) {
        return 1;
    }
    else if (!w.wewnatrz && (y < w.minimum + epsilon || y > w.maximum - epsilon)) {
        return 1;
    }
    return 0;
}

double min_wartosc(wartosc w) {
    if (w.pusty) {
        return NAN;
    }
    else if (w.wewnatrz) {
        return w.minimum;
    }
    return -HUGE_VAL;
}

double max_wartosc(wartosc w) {
    if (w.pusty) {
        return NAN;
    }
    else if (w.wewnatrz) {
        return w.maximum;
    }
    return HUGE_VAL;
}

double sr_wartosc(wartosc w) {
    if (w.pusty || !w.wewnatrz) {
        return NAN;
    }
    return (w.minimum + w.maximum) / 2;
}

wartosc plus(wartosc a, wartosc b) {
    if (a.pusty) {
        return a;
    }
    if (b.pusty) {
        return b;
    }
    if (a.wewnatrz && b.wewnatrz) {
        a.minimum += b.minimum;
        a.maximum += b.maximum;
        return a;
    }
    /* jeżeli jeden z a i b jest wewnętrzny, a drugi nie, to chcemy, aby b był wewnętrzny */
    if (a.wewnatrz && !b.wewnatrz) {
        swap(&a, &b);
    }
    wartosc w;
    w.minimum = a.minimum + b.maximum;
    w.maximum = a.maximum + b.minimum;
    wewnetrzny_niepusty(&w);
    if ((!a.wewnatrz && !b.wewnatrz) || (w.minimum > w.maximum || czy_rowne(w.minimum, w.maximum))) {
        w.minimum = -HUGE_VAL;
        w.maximum = HUGE_VAL;
        return w;
    }
    w.wewnatrz = 0;
    return w;
}

/* odejmowanie a i b to dodawanie a i -b */
wartosc minus(wartosc a, wartosc b) {
    if (b.pusty) {
        return plus(a, b);
    }
    wartosc c = b;
    c.minimum = b.maximum * (-1);
    c.maximum = b.minimum * (-1);
    return plus(a, c);
}

double zero_nan(double a) {
    if (czy_rowne(a, 0) || isnan(a)) {
        return 0;
    }
    return a;
}

wartosc razy_wewnetrzne(wartosc a, wartosc b) {
    double c, d, e, f;
    c = a.minimum * b.minimum;
    d = a.minimum * b.maximum;
    e = a.maximum * b.minimum;
    f = a.maximum * b.maximum;
    /* 0 * (-INFINITE) oraz 0 * INFINITE są równe NAN, a chemy 0 */
    c = zero_nan(c);
    d = zero_nan(d);
    e = zero_nan(e);
    f = zero_nan(f);
    wartosc w;
    w.minimum = fmin(fmin(c, d), fmin(e, f));
    w.maximum = fmax(fmax(c, d), fmax(e, f));
    wewnetrzny_niepusty(&w);
    return w;
}

wartosc laczenie(wartosc a, wartosc b) {
    if (!a.wewnatrz && !b.wewnatrz) {
        a.minimum = fmax(a.minimum, b.minimum);
        a.maximum = fmin(a.maximum, b.maximum);
        if (a.minimum > a.maximum || czy_rowne(a.minimum, b.maximum)) {
            a.minimum = -HUGE_VAL;
            a.maximum = HUGE_VAL;
            a.wewnatrz = 1;
            return a;
        }
        return a;
    }
    if ((!isfinite(a.minimum) && !isfinite(a.maximum)) || (!isfinite(b.minimum) && !isfinite(b.maximum))) {
        a.minimum = -HUGE_VAL;
        a.maximum = HUGE_VAL;
        return a;
    }
    /* chcemy, aby dla przypadku (-INFINITY, x], [y, INFINITY) to !isfinite(a.minimum) */
    if (isfinite(a.minimum) && !isfinite(a.maximum) && !isfinite(b.minimum) && isfinite(b.maximum)) {
        swap(&a, &b);
    }
    if (!isfinite(a.minimum) && isfinite(a.maximum) && isfinite(b.minimum) && !isfinite(b.maximum)) {
        if (a.maximum > b.minimum || czy_rowne(a.maximum, b.minimum)) {
            a.maximum = HUGE_VAL;
            return a;
        }
        a.minimum = a.maximum;
        a.maximum = b.minimum;
        a.wewnatrz = 0;
        return a;
    }
    /* przypadek, gdy dokładnie jeden z przedziałów ma nieskończony lewy koniec */
    if (!isfinite(a.minimum) && !isfinite(b.minimum)) {
        a.maximum = fmax(a.maximum, b.maximum);
        return a;
    }
    /* przypadek, gdy dwa przedziały mają nieskończone prawe końce */
    if (!isfinite(a.maximum) && !isfinite(b.maximum)) {
        a.minimum = fmax(a.minimum, b.minimum);
        return a;
    }
    if (!isfinite(a.minimum) || !isfinite(b.minimum)) {
        a.minimum = -HUGE_VAL;
        a.maximum = fmax(a.maximum, b.maximum);
        return a;
    }
    if (!isfinite(a.maximum) || !isfinite(b.maximum)) {
        a.maximum = HUGE_VAL;
        a.minimum = fmin(a.minimum, b.minimum);
        return a;
    }
    wartosc w;
    w.minimum = fmin(a.minimum, b.minimum);
    w.maximum = fmax(a.maximum, b.maximum);
    w.wewnatrz = 1;
    w.pusty = 0;
    return w;
}

wartosc razy(wartosc a, wartosc b) {
    if (a.pusty) {
        return a;
    }
    if (b.pusty) {
        return b;
    }
    a.minimum = zero_nan(a.minimum);
    a.maximum = zero_nan(a.maximum);
    b.minimum = zero_nan(b.minimum);
    b.maximum = zero_nan(b.maximum);
    if (a.wewnatrz && b.wewnatrz) {
        return razy_wewnetrzne(a, b);
    }
    if (a.wewnatrz && !b.wewnatrz) {
        swap(&a, &b);
    }
    wartosc l1, p1;
    l1.minimum = -HUGE_VAL;
    l1.maximum = a.minimum;
    wewnetrzny_niepusty(&l1);
    p1.minimum = a.maximum;
    p1.maximum = HUGE_VAL;
    wewnetrzny_niepusty(&p1);
    if (!a.wewnatrz && b.wewnatrz) {
        return laczenie(razy_wewnetrzne(l1, b), razy_wewnetrzne(p1, b));
    }
    wartosc l2, p2;
    l2.minimum = -HUGE_VAL;
    l2.maximum = b.minimum;
    wewnetrzny_niepusty(&l2);
    p2.minimum = b.maximum;
    p2.maximum = HUGE_VAL;
    wewnetrzny_niepusty(&p2);
    return laczenie(laczenie(razy_wewnetrzne(l1, l2), razy_wewnetrzne(l1, p2)), laczenie(razy_wewnetrzne(p1, l2), razy_wewnetrzne(p1, p2)));
}

wartosc odwrotnosc(wartosc w) {
    w.minimum = zero_nan(w.minimum);
    w.maximum = zero_nan(w.maximum);
    if (w.pusty) {
        return w;
    }
    if (czy_rowne(w.minimum, 0) && czy_rowne(w.maximum, 0)) {
        w.minimum = 0;
        w.maximum = 0;
        w.wewnatrz = 0;
        w.pusty = 1;
        return w;
    }
    if (w.wewnatrz) {
        if (czy_rowne(w.maximum, 0)) {
            w.maximum = 1 / w.minimum;
            w.minimum = -HUGE_VAL;
            return w;
        }
        if (czy_rowne(w.minimum, 0)) {
            w.minimum = 1 / w.maximum;
            w.maximum = HUGE_VAL;
            return w;
        }
        wartosc p = w;
        if ((w.minimum > 0 && w.maximum > 0) || (w.minimum < 0 && w.maximum < 0)) {
            p.minimum = 1 / w.maximum;
            p.maximum = 1 / w.minimum;
            return p;
        }
        w.minimum = 1 / w.minimum;
        w.maximum = 1 / w.maximum;
        w.wewnatrz = 0;
        return w;
    }
    if (czy_rowne(w.minimum, 0)) {
        w.minimum = -HUGE_VAL;
        w.maximum = 1 / w.maximum;
        w.wewnatrz = 1;
        return w;
    }
    if (czy_rowne(w.maximum, 0)) {
        w.minimum = 1 / w.minimum;
        w.maximum = HUGE_VAL;
        w.wewnatrz = 1;
        return w;
    }
    if (w.minimum < 0 && w.maximum > 0) {
        w.minimum = 1 / w.minimum;
        w.maximum = 1 / w.maximum;
        w.wewnatrz = 1;
        return w;
    }
    wartosc c = w;
    c.minimum = 1 / w.maximum;
    c.maximum = 1 / w.minimum;
    return c;
}

wartosc podzielic(wartosc a, wartosc b){
    return razy(a, odwrotnosc(b));
}
