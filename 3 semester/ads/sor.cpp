#include <iostream>

using namespace std;

void printArray(long long **arr, int n) {
    for (int i = 0; i < n; ++i) {
        for (int j = 0; j < n; ++j) {
            cout << arr[i][j] << ' ';
        }
        cout << endl;
    }
}

int main() {
    int n;
    cin >> n;
    int *tab = new int [n];
    for (int i = 0; i < n; ++i) {
        cin >> tab[i];
    }

    if (n == 1) {
        cout << 1;
        return 0;
    }

    long long **left = new long long *[n];
    long long **right = new long long *[n];
    long long **all = new long long *[n];
    for (int i = 0; i < n; ++i) {
        left[i] = new long long [n];
        right[i] = new long long [n];
        all[i] = new long long [n];
    }

    for (int i = 0; i < n - 1; ++i) {
        if (tab[i] < tab[i + 1]) {
            left[i][i + 1] = 1;
            right[i][i + 1] = 1;
            all[i][i + 1] = 2;
        }
        else {
            left[i][i + 1] = 0;
            right[i][i + 1] = 0;
            all[i][i + 1] = 0;
        }
    }

    for (int k = 2; k < n; ++k) {
        for (int i = 0; i + k < n; ++i) {
            // Policzenie left[i][i + k].
            long long sum = 0;
            if (tab[i] < tab[i + 1]) {
                sum += left[i + 1][i + k];
            }
            if (tab[i] < tab[i + k]) {
                sum += right[i + 1][i + k];
            }
            left[i][i + k] = sum % 1000000000;

            // Policzenie right[i][i + k].
            sum = 0;
            if (tab[i + k] > tab[i]) {
                sum += left[i][i + k - 1];
            }
            if (tab[i + k] > tab[i + k - 1]) {
                sum += right[i][i + k - 1];
            }
            right[i][i + k] = sum % 1000000000;

            all[i][i + k] = (left[i][i + k] + right[i][i + k]) % 1000000000;
        }

    }

    cout << all[0][n - 1];

    delete [] tab;
    for (int i = 0; i < n; ++i) {
        delete [] left[i];
        delete [] right[i];
        delete [] all[i];
    }
    delete [] left;
    delete [] right;
    delete [] all;

    return 0;
}