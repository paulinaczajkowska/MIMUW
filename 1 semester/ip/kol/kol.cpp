// Autor: Paulina Czajkowska
// Code review'er: Piotr Łaba
// Implementacja kolejki pochodzi z wykładu, a część funkcji z moodla z labu 10
#include <cstdlib>
#include <vector>
#include "kol.h"

using namespace std;

// Kolejki z zadania są reprezentowane przez dwustronne kolejki ze scalaniem i odwracaniem
// Head i tail są atrapami
typedef struct lista {
	interesant *head, *tail;
} lista;

// Tworzymy pustą kolejkę, gdzie dwie atrapy (head i tail) wskazują na siebie
lista create_lista() {
	lista res;
	res.head = (interesant*)malloc(sizeof(interesant));
	res.tail = (interesant*)malloc(sizeof(interesant));
	res.head->l1 = NULL;
	res.head->l2 = res.tail;
	res.tail->l1 = NULL;
	res.tail->l2 = res.head;
	return res;
}

// Kolejka jest pusta, gdy atrapy (czyli head i tail) wskazują na siebie
bool isempty(const lista l) {
	return l.head->l2 == l.tail;
}

interesant *front(const lista l) {
	return l.head->l2;
}

interesant *back(const lista l) {
	return l.tail->l2;
}

// Aby zmienić interesanta, na którego wskazuje el, musimy sprawdzić, z której strony el jest poprzedni interesant, ponieważ
// w kolejce dwustronnej ze scalaniem i odwracaniem nie jest określony kierunek dowiązań
void link(interesant *el, interesant *old, interesant *nw) {
	if (el) {
		if (el->l1 == old) {
			el->l1 = nw;
		}
		else {
			el->l2 = nw;
		}
	}
}

// Wstawienie interesanta z numerem x na koniec kolejki
void push_back(lista &l, int x) {
	interesant *el = (interesant*)malloc(sizeof(interesant));
	el->nr = x;
	el->l1 = l.tail;
	el->l2 = l.tail->l2;
	link(l.tail->l2, l.tail, el);
	l.tail->l2 = el;
}

// Usunięcie interesanta z kolejki poprzez "złączenie" interesantów obok niego
void remove_interesant(interesant *el) {
	link(el->l1, el, el->l2);
	link(el->l2, el, el->l1);
}

// Jeeli kolejka nie jest pusta, to pierwszy interesant zostaje usunięty
void pop_front(lista &l) {
	if (!isempty(l)) {
		remove_interesant(l.head->l2);
	}
}

// Zamienienie atrap miejscami powoduje odwrócenie kolejki
void reverse(lista &l) {
	interesant *tmp = l.head;
	l.head = l.tail;
	l.tail = tmp;
}

// Dołączenie do pierwszej kolejki drugiej, a druga zostaje pusta
void append(lista &l1, lista &l2) {
	interesant *b = l1.tail;
	interesant *f = l2.head;
	link(b->l2, b, f->l2);
	link(f->l2, f, b->l2);
	b->l2 = f;
	f->l2 = b;
	l1.tail = l2.tail;
	l2.head = f;
	l2.tail = b;
}

// Kolejki są po kolei w vectorze
vector<lista> KOLEJKA;
int NR;

// Wypełnienie vectora pustymi kolejkami i ustawienie numeru na 0
void otwarcie_urzedu(int m) {
	KOLEJKA.resize(m);
	for (int i = 0; i < m; ++i) {
		KOLEJKA[i] = create_lista();
	}
	NR = 0;
}

// Dodanie interesanta do k-tej kolejki
interesant *nowy_interesant(int k) {
	push_back(KOLEJKA[k], NR++);
	return back(KOLEJKA[k]);
}

int numerek(interesant *i) {
	return i->nr;
}

// Jeśli kolejka nie jest pusta, to usunięcie pierwszego interesanta
interesant *obsluz(int k) {
	if (isempty(KOLEJKA[k])) {
		return NULL;
	}
	interesant *el = front(KOLEJKA[k]);
	pop_front(KOLEJKA[k]);
	return el;
}

// Przeniesienie interesanta i na koniec k-tej kolejki
void zmiana_okienka(interesant *i, int k) {
	remove_interesant(i);
	i->l1 = KOLEJKA[k].tail;
	i->l2 = (KOLEJKA[k].tail)->l2;
	link((KOLEJKA[k].tail)->l2, KOLEJKA[k].tail, i);
	(KOLEJKA[k].tail)->l2 = i;
}

// Wszyscy interesanci z k1-wszej kolejki zostają przeniesieni (z zachowaniem kolejności) do kolejki k2
void zamkniecie_okienka(int k1, int k2) {
	append(KOLEJKA[k2], KOLEJKA[k1]);
}

// Funkcja pomocnicza do fast_track
// Od interesanta i1 równoległe szukanie interesanta i2 w dwie strony, ponieważ nie ma określonego kierunku dowiązań
// Poprzedni_aktualny to interesant przed interesantem tmp w danym kierunku sprawdzania
void szukanie_drugiego_interesanta(interesant **tmp, interesant **poprzedni_aktualny, vector<interesant *> &v) {
	if ((*tmp)->l1 == *poprzedni_aktualny && (*tmp)->l1 != NULL) {
		*poprzedni_aktualny = *tmp;
		*tmp = (*tmp)->l2;
		v.push_back(*tmp);
	}
	else if ((*tmp)->l1 != NULL) {
		*poprzedni_aktualny = *tmp;
		*tmp = (*tmp)->l1;
		v.push_back(*tmp);
	}
}

// Funkcja pomocnicza do fast_track
// Po usunięciu z kolejki interesantów od i1 do i2 należy połączyć ze sobą interesantów przed i1 i za i2
void laczenie_kolejki(interesant **tmp, interesant **poprzedni_aktualny, interesant **poprzedni, interesant *i1, interesant *i2) {
	interesant *nastepny;
	if ((*tmp)->l1 == *poprzedni_aktualny) {
		nastepny = (*tmp)->l2;
	}
	else {
		nastepny = (*tmp)->l1;
	}
	link(*poprzedni, i1, nastepny);
	link(nastepny, i2, *poprzedni);
}

// Usunięcie interesantów od i1 do i2 z kolejki
vector<interesant *> fast_track(interesant *i1, interesant *i2) {
	vector<interesant *> v1, v2;
	// Przypadek, gdy i1 to i2
	if (i1 == i2) {
		v1.push_back(i1);
		remove_interesant(i1);
		return v1;
	}
	// Odpowiednio dla 1 i 2 kierunku poprzedniX to interesant przed i1, poprzedni_aktualnyX to interesant przed tmpX,
	// a tmpX służy do szukania interesanta i2
	interesant *poprzedni1 = i1->l1;
	interesant *poprzedni_aktualny1 = i1->l1;
	interesant *tmp1 = i1;
	interesant *poprzedni2 = i1->l2;
	interesant *poprzedni_aktualny2 = i1->l2;
	interesant *tmp2 = i1;
	v1.push_back(i1);
	v2.push_back(i1);
	// Szukanie interesanta i2 jednocześnie w obu kierunkach
	while (tmp1 != i2 && tmp2 != i2) {
		szukanie_drugiego_interesanta(&tmp1, &poprzedni_aktualny1, v1);
		szukanie_drugiego_interesanta(&tmp2, &poprzedni_aktualny2, v2);
	}
	// W zależności od kierunku, w którym znaleźliśmy interesanta i2, złączamy odpowiednich interesantów i zwracamy wynik
	if (tmp1 == i2) {
		laczenie_kolejki(&tmp1, &poprzedni_aktualny1, &poprzedni1, i1, i2);
		return v1;
	}
	else {
		laczenie_kolejki(&tmp2, &poprzedni_aktualny2, &poprzedni2, i1, i2);
		return v2;
	}
}

// Odwrócenie kolejności interesantów w kolecje k
void naczelnik(int k) {
	reverse(KOLEJKA[k]);
}

// Dodanie do v pozostałych interesantów w odpowiedniej kolejności oraz zwolnienie pamięci po atrapach w kolejkach
vector<interesant *> zamkniecie_urzedu() {
	vector<interesant *> v;
	for (int i = 0; i < (int)KOLEJKA.size(); ++i) {
		while (!isempty(KOLEJKA[i])) {
			v.push_back(obsluz(i));
		}
		free(KOLEJKA[i].head);
		free(KOLEJKA[i].tail);
	}
	return v;
}
