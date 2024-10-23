#include <iostream>

using namespace std;

int main() {
    long long n;
    cin >> n;
    long long x;
    long long * prices = new long long [n];
    for (long long i = 0; i < n; ++i) {
        cin >> prices[i];
    }
    long long m;
    cin >> m;
    long long * days = new long long [m];
    for (long long i = 0; i < m; ++i) {
        cin >> days[i];
    }

    long long * prefix_even_max = new long long [n];
    long long * prefix_odd_max = new long long [n];
    long long * suffix_even_min = new long long [n];
    long long * suffix_odd_min = new long long [n];
    long long * suffix_sum = new long long [n];

    // Preffix.

    if (prices[0] % 2 == 0) {
        prefix_even_max[0] = prices[0];
        prefix_odd_max[0] = -1;
    }
    else {
        prefix_even_max[0] = -1;
        prefix_odd_max[0] = prices[0];
    }

    for (long long i = 1; i < n; ++i) {
        if (prices[i] % 2 == 0) {
            prefix_even_max[i] = prices[i];
            prefix_odd_max[i] = prefix_odd_max[i - 1];
        }
        else {
            prefix_even_max[i] = prefix_even_max[i - 1];
            prefix_odd_max[i] = prices[i];
        }
    }

    // Suffix.

    if (prices[n - 1] % 2 == 0) {
        suffix_even_min[n - 1] = prices[n - 1];
        suffix_odd_min[n - 1] = -1;
    }
    else {
        suffix_even_min[n - 1] = -1;
        suffix_odd_min[n - 1] = prices[n - 1];
    }
    suffix_sum[n - 1] = prices[n - 1];

    for (long long i = n - 2; i >= 0; --i) {
        if (prices[i] % 2 == 0) {
            suffix_even_min[i] = prices[i];
            suffix_odd_min[i] = suffix_odd_min[i + 1];
        }
        else {
            suffix_even_min[i] = suffix_even_min[i + 1];
            suffix_odd_min[i] = prices[i];
        }
        suffix_sum[i] = prices[i] + suffix_sum[i + 1];
    }

    // Answering.

    for (long long i = 0; i < m; ++i) {
        long long number_of_products = days[i];
        long long index = n - number_of_products;
        long long sum = suffix_sum[index];
        if (sum % 2 == 1) {
            cout << sum << endl;
        }
        else {
            if (index == 0) {
                cout << -1 << endl;
            }
            else if ((prefix_even_max[index - 1] == -1 || suffix_odd_min[index] == -1) && (prefix_odd_max[index - 1] == -1 || suffix_even_min[index] == -1)) {
                cout << -1 << endl;
            }
            else if (prefix_even_max[index - 1] == -1 || suffix_odd_min[index] == -1) {
                sum += prefix_odd_max[index - 1] - suffix_even_min[index];
                cout << sum << endl;
            }
            else if (prefix_odd_max[index - 1] == -1 || suffix_even_min[index] == -1) {
                sum += prefix_even_max[index - 1] - suffix_odd_min[index];
                cout << sum << endl;
            }
            else {
                sum += max(prefix_even_max[index - 1] - suffix_odd_min[index], prefix_odd_max[index - 1] - suffix_even_min[index]);
                cout << sum << endl;
            }
        }
    }


    delete [] prices;
    delete [] days;
    delete [] prefix_even_max;
    delete [] prefix_odd_max;
    delete [] suffix_even_min;
    delete [] suffix_odd_min;
    delete [] suffix_sum;

    return 0;
}