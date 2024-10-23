#include <iostream>
#include <string>
#include <algorithm>
#include <limits.h>

using namespace std;

int main() {
    string word;
    cin >> word;

    // We are looking for the shortest section between two different letters.
    // The answer is the length of the word minus the length of this fragment.
    // If there's only one letter the answer is 1.

    int answer = 1;
    int word_length = word.size();
    char previous_letter;
    char current_letter;
    int distance = 0;
    int min_distance = INT_MAX;
    int index;

    // Founding the first letter.
    bool is_found = false;
    for (int i = 0; i < word_length && !is_found; ++i) {
        if (word[i] != '*') {
            is_found = true;
            previous_letter = word[i];
            index = i + 1;
        }
    }

    if (!is_found) {
        cout << answer;
        return 0;
    }
    
    for (int i = index; i < word_length; ++i) {
        current_letter = word[i];
        if (current_letter == '*') {
            distance++;
        }
        else if (current_letter == previous_letter) {
            distance = 0;
        }
        else {
            min_distance = min(min_distance, distance);
            distance = 0;
            previous_letter = current_letter;
        }
    }

    if (min_distance != INT_MAX) {
        answer = word_length - min_distance;
    }

    cout << answer;

    return 0;
}