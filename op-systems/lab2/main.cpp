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
    this_thread::sleep_for(seconds(2));
    return 2 * x;
}

int g(int x) {
    this_thread::sleep_for(seconds(12));
    return 4 * x;
}

template<typename T>
class AggregateFuture {
    vector<future<T>> futures;
    vector<int> results;
    vector<thread> threads;

    mutex m;
    condition_variable cv;
    atomic<int> finishedThreads;

    void wait_for_one(future<T> &f, long millis) {
        f.wait_for(milliseconds(millis));

        if (isTaskFinished(f)) {
            int result = f.get();
            addResult(result);

            if (result == 0) {
                cv.notify_all();
                return;
            }
        }

        finishedThreads++;
        notifyIfFinished();
    }

    void addResult(int result) {
        unique_lock<mutex> lock(m);
        results.push_back(result);
    }

    void notifyIfFinished() {
        if (finishedThreads == threads.size()) {
            cv.notify_all();
        }
    }

public:
    AggregateFuture(future<T> &&f1, future<T> &&f2) {
        futures.emplace_back(std::move(f1));
        futures.emplace_back(std::move(f2));
    }

    future_status wait_for(long millis) {
        finishedThreads = 0;
        threads.clear();
        for (auto &f : futures) {
            if (f.valid()) {
                threads.emplace_back(
                        thread(&AggregateFuture<T>::wait_for_one, this, std::ref(f), millis)
                );
            }
        }
        for (auto &t : threads) {
            t.detach();
        }
        unique_lock<mutex> lock(m);
        if (!threads.empty())
            cv.wait(lock);
        return getStatus();
    }

    future_status getStatus() {
        for (auto &res : results) {
            if (res == 0) return future_status::ready;
        }
        return results.size() == futures.size() ? future_status::ready : future_status::timeout;
    }

    static bool isTaskFinished(future<T> &f) {
        auto status = f.wait_for(std::chrono::seconds(0));
        return status == future_status::ready;
    }

    long getResult() {
        unique_lock<mutex> lock(m);
        long answer = 1;
        for (auto &res : results) {
            answer *= res;
        }
        return answer;
    }
};

int main() {
    int x;
    long answer;
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
            answer = result.getResult();
            finished = true;
        }
    }
    cout << "Computation result: " << answer << endl;
    exit(0);
}
