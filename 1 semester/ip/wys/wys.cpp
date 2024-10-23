// Autor: Paulina Czajkowska
// Code review'er: Piotr Łaba

#include <iostream>
#include <vector>
#include <unordered_map>
#include <algorithm>
#include <climits>
#include "wys.h"

using namespace std;

int n, k, g;
// Dla stanu (a właściwie jego unikalnej wagi) trzymamy optymalne pytanie oraz liczbę pytań potrzebnych do odgadnięcia liczby
unordered_map<int, pair<int, int> > states;
vector<int> currentState;

// Każdy stan ma swoją unikalną wagę (ponieważ wagi danych pozycji są potęgami liczby k + 2)
int weightedSum(vector<int> v) {
	int ans = 0;
	int weight = 1;
	for (int i = 1; i <= n; ++i) {
		ans += (v[i] * weight);
		weight *= (k + 2);
	}
	return ans;
}

int firstGoodQuestion(vector<int> v) {
	for (int i = 1; i <= n; ++i) {
		if (v[i] < k + 1) {
			return i + 1;
		}
	}
	return -1;
}

int lastGoodQuestion(vector<int> v) {
	for (int i = n; i >= 1; --i) {
		if (v[i] < k + 1) {
			return i;
		}
	}
	return -1;
}

bool oneLeft(vector<int> v) {
	int cnt = 0;
	for (int i = 1; i <= n; ++i) {
		if (v[i] < k + 1) {
			cnt++;
		}
	}
	return cnt == 1;
}

bool isPossible(vector<int> v) {
	for (int i = 1; i <= n; ++i) {
		if (v[i] < k + 1) {
			return true;
		}
	}
	return false;
}

// Sprawdzenie, czy wszystkie elementy danego stanu są takie same
bool constSequence(vector<int> v) {
	for (int i = 1; i <= n; ++i) {
		if (v[1] != v[i]) {
			return false;
		}
	}
	return true;
}

// W zależności od odpowiedzi na pytanie "czy mniejsza" odpowiednio zmieniamy stan
vector<int> changeState(vector<int> v, int x, bool ans) {
	// Pytanie: czy < x?, ustawiamy first i last na odpowiedź NIE, ewentualnie zmieniamy na TAK
	int first = 1, last = x - 1;
	if (ans) {
		first = x;
		last = n;
	}
	for (int i = first; i <= last; ++i) {
		v[i]++;
		// Wszystkie na pewno złe liczby będa miały przypisane k + 1
		if (v[i] > k + 1) {
			v[i] = k + 1;
		}
	}
	return v;
}

vector<int> reverseVector(vector<int> v) {
	for (int i = 1; i <= n / 2; ++i) {
		int tmp = v[i];
		v[i] = v[n - i + 1];
		v[n - i + 1] = tmp;
	}
	return v;
}

int makeMap(vector<int> v) {
	// Jeżeli wszystkie wartość w v są większe niż k to nie liczymy już tego stanu
	if (!isPossible(v)) {
		return INT_MAX;
	}
	// Jeżeli została tylko jedna liczba to nie potrzebujemy już żadnych pytań
	if (oneLeft(v)) {
		return 0;
	}
	// Jeżeli dany stan jest już w mapie, to nie liczymy dalej, tylko zwracamy już policzoną wartość
	if (states.find(weightedSum(v)) != states.end()) {
		return states[weightedSum(v)].second;
	}
	// Da się ustalić optymalne pytanie znając pytanie dla stanu odwrotnego
	if (states.find(weightedSum(reverseVector(v))) != states.end()) {
		return states[weightedSum(reverseVector(v))].second;
	}
	vector<int> v_yes = v;
	vector<int> v_no = v;
	int first = firstGoodQuestion(v);
	int last = lastGoodQuestion(v);
	// Dla takich stanów tylko pytanie "w środek" ma sens
	if (constSequence(v)) {
		first = n / 2 + 1;
		last = n / 2 + 1;
	}
	int mini = INT_MAX;
	int question, cur_mini;
	for (int i = first; i <= last; ++i) {
		v_yes = changeState(v, i, true);
		v_no = changeState(v, i, false);
		// Bierzemy maxa z dwóch pytań, czyli mniej korzystną scieżkę
		cur_mini = max(makeMap(v_yes), makeMap(v_no));
		// Odpowiedzią dla danego stanu będzie najbardziej optymalne pytanie, czyli minimum ze znalezionych maxów
		if (cur_mini < mini) {
			mini = cur_mini;
			question = i;
		}
	}
	states[weightedSum(v)] = make_pair(question, mini + 1);
	return mini + 1;
}

int lastNumber(vector<int> v) {
	for (int i = 1; i <= n; ++i) {
		if (v[i] < k + 1) {
			return i;
		}
	}
	return -1;
}

void play() {
	if (oneLeft(currentState)) {
		odpowiedz(lastNumber(currentState));
	}
	else {
		int cur_question;
		// Rozważamy dwa przypadki, w zależności od tego, czy w mapie znajduje się dany stan, czy jego odwrotność
		if (states.find(weightedSum(currentState)) != states.end()) {
			cur_question = states[weightedSum(currentState)].first;
		}
		if (states.find(weightedSum(reverseVector(currentState))) != states.end()) {
			cur_question = n + 2 - states[weightedSum(reverseVector(currentState))].first;
		}
		if (mniejszaNiz(cur_question)) {
			currentState = changeState(currentState, cur_question, true);
		}
		else {
			currentState = changeState(currentState, cur_question, false);
		}
		play();
	}
}

int main()
{
	dajParametry(n, k, g);
	currentState.resize(n + 1, 0);
	makeMap(currentState);
	while (g--) {
		fill(currentState.begin(), currentState.end(), 0);
		play();
	}
}