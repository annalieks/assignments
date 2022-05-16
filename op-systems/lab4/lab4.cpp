#include <iostream>
#include <thread>
#include <mutex>
#include <chrono>

using namespace std;
using namespace chrono;

int counter = 0;
bool useMutex = false;
mutex m;

void increment() {
    for (int i = 0; i < 100000; i++) {
        if (useMutex) m.lock();
        counter++;
        if (useMutex) m.unlock();
    }
}

int main(int argc, char **argv) {
    useMutex = argv[1];
    auto start = steady_clock::now();

    thread th1(increment);
    thread th2(increment);

    th1.join();
    th2.join();

    auto timePassed = duration_cast<nanoseconds>(steady_clock::now() - start).count();
    cout << "Counter value: " << counter << " Time spent: " << timePassed << "ns" << endl;
    return 0;
}
