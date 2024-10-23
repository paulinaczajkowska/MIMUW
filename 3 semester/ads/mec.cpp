#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

int max_set(vector<unsigned long long> labels, int n) {
    sort(labels.begin(), labels.end());
    int maxi = 1;
    int curr_max = 1;
    unsigned long long l = labels[0];
    for (int i = 1; i < n; ++i) {
        if (labels[i] == l) {
            curr_max++;
        }
        else {
            l = labels[i];
            maxi = max(maxi, curr_max);
            curr_max = 1;
        }
    }
    maxi = max(maxi, curr_max);
    return maxi;
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);
    
    int n, m;
    cin >> n >> m;

    vector<unsigned long long> labels(n, 1);
    int x;
    for (int i = 0; i < m; ++i) {            
        for (int j = 0; j < n / 2; ++j) {
            cin >> x;
            x--;
            labels[x]  <<= 1;
        }
        for (int j = n / 2; j < n; ++j) {
            cin >> x;
            x--;
            labels[x] <<= 1;
            labels[x] += 1;
        }
    }

    cout << max_set(labels, n);

    return 0;
}