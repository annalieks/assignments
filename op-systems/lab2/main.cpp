#include <iostream>
#include <future>
#include <chrono>

using namespace std;
using namespace chrono;


/**
 * Function to handle timeout in computation
 * @return whether to ignore further timeouts
 */
bool handleTimeout() {
    int code;
    bool correctInput = false;
    cout << "The timeout of 10s was reached. Select further action:\n"
         << "0 - continue the computation\n"
         << "1 - abort the computation\n"
         << "2 - continue and do not ask again" << endl;
    while (!correctInput) {
        cin >> code;
        if (code != 0 && code != 1 && code != 2) {
            cout << "Unknown option. Please, input 0, 1, or 2: ";
            continue;
        }
        correctInput = true;
        if (code == 1) {
            cout << "Computation was aborted by user" << endl;
            exit(1);
        } else if (code == 2) {
            return true;
        } else if (code == 0) {
            return false;
        }
    }
    return false;
}

int f(int x) {
    this_thread::sleep_for(seconds(5));
    return 2 * x;
}

int g(int x) {
    this_thread::sleep_for(seconds(35));
    return 4 * x;
}

template<typename T>
class AggregateFuture {
    vector<future<T>> futures;

    void wait_for_one(future<T> &f, long millis) {
        f.wait_for(milliseconds(millis));
    }

public:
    AggregateFuture(future<T> &&f1, future<T> &&f2) {
        futures.emplace_back(std::move(f1));
        futures.emplace_back(std::move(f2));
    }

    future_status wait_for(long millis) {
        vector<thread> threads;
        for (auto &f : futures) {
            if (!isTaskFinished(f)) {
                threads.emplace_back(
                        thread(&AggregateFuture<T>::wait_for_one, this, std::ref(f), millis)
                );
            }
        }
        for (auto &t : threads) {
            t.join();
        }
        return getStatus();
    }

    future_status getStatus() {
        for (auto &f : futures) {
            if (!isTaskFinished(f)) return future_status::timeout;
        }
        return future_status::ready;
    }

    static bool isTaskFinished(future<T> &f) {
        auto status = f.wait_for(std::chrono::seconds(0));
        return status == future_status::ready;
    }

    vector<future<int>> &getFutures() {
        return futures;
    }
};

int main() {
    int x;
    long answer = 1;
    bool finished = false, ignoreTimeout = false;
    cout << "Input x:" << endl;
    cin >> x;

    // start f and g computation in new threads
    AggregateFuture<int> result(std::async(std::launch::async, f, x),
                                std::async(std::launch::async, g, x));

    while (!finished) {
        auto status = result.wait_for(10000);
        if (status != future_status::ready && !ignoreTimeout) {
            ignoreTimeout = handleTimeout();
        } else {
            auto &futures = result.getFutures();
            for (auto &f : futures) {
                int res = f.get();
                answer *= res;
            }
            finished = true;
        }
    }
    cout << "Computation result: " << answer << endl;
    return 0;
}
