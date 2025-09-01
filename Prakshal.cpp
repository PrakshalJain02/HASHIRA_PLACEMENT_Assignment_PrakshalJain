#include <bits/stdc++.h>
using namespace std;

// Function to convert string of given base to decimal
long long convertToDecimal(const string &value, int base) {
    long long result = 0;
    for (char c : value) {
        int digit;
        if (isdigit(c)) digit = c - '0';
        else digit = tolower(c) - 'a' + 10;
        result = result * base + digit;
    }
    return result;
}

// Function to compute polynomial coefficients given roots
vector<long long> buildPolynomial(const vector<long long> &roots) {
    vector<long long> coeffs = {1}; // start with P(x) = 1
    for (long long root : roots) {
        vector<long long> newCoeffs(coeffs.size() + 1, 0);
        for (size_t i = 0; i < coeffs.size(); i++) {
            newCoeffs[i] += coeffs[i];
            newCoeffs[i + 1] -= coeffs[i] * root;
        }
        coeffs = newCoeffs;
    }
    return coeffs;
}

int main() {
    // Example test case (replace with JSON parsing if required)
    // First test case
    vector<pair<int,string>> inputs = {
        {10, "4"},
        {2, "111"},
        {10, "12"},
        {4, "213"}
    };

    vector<long long> roots;
    for (auto &p : inputs) {
        roots.push_back(convertToDecimal(p.second, p.first));
    }

    vector<long long> coeffs = buildPolynomial(roots);

    cout << "Polynomial Coefficients:" << endl;
    cout << "[ ";
    for (size_t i = 0; i < coeffs.size(); i++) {
        cout << coeffs[i];
        if (i != coeffs.size() - 1) cout << ", ";
    }
    cout << " ]" << endl;

    return 0;
}
