// Autor: Paulina Czajkowska
// Code review'er: Piotr Łaba

#include <vector>
#include <climits>
#include <algorithm>
#include <memory>
#include "prev.h"

using namespace std;

// struktura implementująca drzewo przedziałowe
typedef struct node {
    int val;
    shared_ptr<node> left;
    shared_ptr<node> right;
    int mini, maxi;
    node(int vall, int minii, int maxii) {
        val = vall;
        mini = minii;
        maxi = maxii;
        left = nullptr;
        right = nullptr;
    }
} node;

typedef shared_ptr<node> tree;

vector<tree> root;

// utworzenie gałęzi dla elementów z wektora seq
void init(const vector<int> &seq) {
    for (size_t i = 0; i < seq.size(); ++i) {
        pushBack(seq[i]);
    }
}


int prevInRange(tree t, int lo, int hi) {
    int res = -1;
    // jeżeli <t->mini, t->maxi> zawiera się w danym przedziale, to zwracamy wartość w danym nodzie i nie sprawdzamy dalej
    if (lo <= t->mini && hi >= t->maxi) {
        return t->val;
    }
    // sprawdzamy, czy odpowiednie nody mają wartości pokrywające się z przediałem <lo, hi>
    // jeeli tak, to wywołujemy funkcję prevInRange dla odpowiedniego dziecka
    if (t->left != nullptr && (lo <= (t->left)->maxi || hi >= (t->left)->mini)) {
        res = max(res, prevInRange(t->left, lo, hi));
    }
    if (t->right != nullptr && (lo <= (t->right)->maxi || hi >= (t->right)->mini)) {
        res = max(res, prevInRange(t->right, lo, hi));
    }
    return res;
}

// max{ j : 0 <= j <= i && seq[j] \in [lo..hi] } or -1
int prevInRange(int i, int lo, int hi) {
    return prevInRange(root[i], lo, hi);
}

// zwraca mniejszą wartość z dwóch środkowych w danym przedziale
long long middleInRange(int mini, int maxi) {
    long long mid;
    if ((long long)mini + (long long)maxi < 0) {
        // jeżeli suma mini i maxi jest ujemna, to aby otrzymać mniejszą z dwóch średnich wartości
        // naley od tej sumy odjąć 1
        mid = ((long long)mini + (long long)maxi - 1) / 2;
    }
    else {
        mid = ((long long)mini + (long long)maxi) / 2;
    }
    return mid;
}

void pushBack(int value) {
    tree t = make_shared<node>(root.size(), INT_MIN, INT_MAX);
    tree tmp = t;
    // prv to root do poprzedniego indeksu
    tree prv;
    if (root.size()) {
        prv = root[root.size() - 1];
    }
    int mini = INT_MIN, maxi = INT_MAX;
    // zmienna is_prev informuje, czy nadal musimy dołączać nody z poprzednich gałęzi
    bool is_prev = 1;
    // dodawanie nowego noda dopóki nie znajdziemy się w liściu drzewa
    while (tmp->mini != tmp->maxi) {
        if (!root.size()) {
            is_prev = 0;
        }
        // ustawianie nowego zakresu w zależności od tego, w którym znajduje się value
        long long mid = middleInRange(mini, maxi);
        if (value <= mid) {
            maxi = (int)mid;
        }
        else {
            mini = (int)(mid + 1);
        }
        tree child = make_shared<node>(root.size(), mini, maxi);
        // jeżeli lewe krańce przedziałów sie pokrywają, to tmp->left wskazuje na child
        if (tmp->mini == child->mini) {
            tmp->left = child;
            // łączenie nowej gałęzi z poprzednią (gdy jest to moliwe, to znaczy nie jest nullptr)
            if (is_prev && prv->right) {
                tmp->right = prv->right;
            }
            // przejście na odpowiedni node dla poprzdniej gałęzi
            if (is_prev && prv->left) {
                prv = prv->left;
            }
            // prv->left i prv->right to nullptr, więc nie ma już co dołączać
            else {
                is_prev = 0;
            }
        }
        // analogicznie do poprzedniego przypadku, tylko w drugą stronę
        else {
            tmp->right = child;
            if (is_prev && prv->left) {
                tmp->left = prv->left;
            }
            if (is_prev && prv->right) {
                prv = prv->right;
            }
            else {
                is_prev = 0;
            }
        }
        tmp = child;
    }
    root.push_back(t);
}

// usunięcie rootów powoduje usunięcie wszystkich nodów w drzewie, dzięki użyciu shared_ptr
void done() {
    root.clear();
}