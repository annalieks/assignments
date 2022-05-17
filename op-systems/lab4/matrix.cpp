#include <iostream>
#include <thread>
#include <vector>
#include <chrono>
#include <random>

using namespace std;
using namespace chrono;

std::random_device rd;
std::mt19937 rng(rd());
std::uniform_int_distribution<int> uni(-100, 100); // guaranteed unbiased

vector<vector<int>> matrixA;
vector<vector<int>> matrixB;
vector<vector<int>> matrixC;

vector<vector<int>> createMatrix(int rows, int cols) {
    vector<vector<int>> m;
    for (int i = 0; i < rows; ++i) {
        m.emplace_back();
        for (int j = 0; j < cols; j++) {
            m[i].push_back(0);
        }
    }
    return m;
}

int randomNumber() {
    return uni(rng);
}

void inputMatrix(vector<vector<int>> &m, int r, int c) {
    for (int i = 0; i < r; i++) {
        for (int j = 0; j < c; j++) {
            cin >> m[i][j];
        }
    }
}

void generateMatrix(vector<vector<int>> &m, int r, int c) {
    for (int i = 0; i < r; i++) {
        for (int j = 0; j < c; j++) {
            m[i][j] = randomNumber();
        }
    }
}

void multiply(int row, int col, int m) {
    int sum = 0;
    for (int i = 0; i < m; i++) {
        sum += matrixA[row][i] * matrixB[i][col];
    }
    printf("C[%d][%d] = %d\n", row, col, sum);
    matrixC[row][col] = sum;
}

void printMatrix(const vector<vector<int>> &m) {
    for (const auto &i : m) {
        for (int j : i) {
            cout << j << " ";
        }
        cout << endl;
    }
}

int main() {
    int n, m, k, g = 0;
    cout << "Input n m k for A[n x k], B[m x k]:" << endl;
    cin >> n >> m >> k;
    cout << "Do you want to input or generate the matrix? (1 - input, 2 - generate)" << endl;
    while (g != 1 && g != 2) {
        cin >> g;
    }

    matrixA = createMatrix(n, m);
    matrixB = createMatrix(m, k);
    matrixC = createMatrix(n, k);

    if (g == 1) {
        cout << "Input matrix A: " << endl;
        inputMatrix(matrixA, n, m);
        cout << "Input matrix B: " << endl;
        inputMatrix(matrixB, m, k);
    } else if (g == 2) {
        generateMatrix(matrixA, n, m);
        cout << "Generated matrix A:" << endl;
        printMatrix(matrixA);
        generateMatrix(matrixB, m, k);
        cout << "Generated matrix B:" << endl;
        printMatrix(matrixB);
    }

    auto start = steady_clock::now();
    vector<thread> threads;
    for (int row = 0; row < n; row++) {
        for (int col = 0; col < k; col++) {
            threads.emplace_back(multiply, row, col, m);
        }
    }

    for (auto &th: threads) {
        th.join();
    }

    auto timePassed = duration_cast<milliseconds>(steady_clock::now() - start).count();

    cout << "Result: " << endl;
    printMatrix(matrixC);
    cout << "Time: " << timePassed << "ms" << endl;
    return 0;
}