#include <iostream>
#include <Windows.h>

using namespace std;

bool checkPassword(const string& input, const string& expected) {
    return input == expected;
}

int main() {
    string userInput;
    auto* secretPassword = new string("SECRET PASSWORD IS README");
    string testString("THIS IS A STRING ON STACK");
    cin >> userInput;
    cout << (checkPassword(userInput, *secretPassword) ? "Auth successful!" : "Please, try again") << endl;
    delete secretPassword;
    return 0;
}
