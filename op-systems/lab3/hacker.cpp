#include <iostream>
#include <vector>
#include <Windows.h>

using namespace std;

int createProcess(const char *command) {
    STARTUPINFO si;
    PROCESS_INFORMATION pi;
    ZeroMemory(&si, sizeof(si));
    si.cb = sizeof(si);
    ZeroMemory(&pi, sizeof(pi));
    if (!CreateProcess(nullptr,            // No module name (use command line)
                       (LPSTR) command,    // Command line
                       nullptr,            // Process handle not inheritable
                       nullptr,            // Thread handle not inheritable
                       FALSE,              // Set handle inheritance to FALSE
                       0,                  // No creation flags
                       nullptr,            // Use parent's environment block
                       nullptr,            // Use parent's starting directory
                       &si,                // Pointer to STARTUPINFO structure
                       &pi)                // Pointer to PROCESS_INFORMATION structure
            ) {
        printf("CreateProcess failed (%lu).\n", GetLastError());
        exit(1);
    }
    return pi.dwProcessId;
}

bool isRegionAccessible(MEMORY_BASIC_INFORMATION &info) {
    return (info.State == MEM_COMMIT)
           && (info.Protect == PAGE_READWRITE || info.Protect == PAGE_READONLY)
           && (info.Type == MEM_PRIVATE);
}

void scanMemory(HANDLE proc, PVOID start, SIZE_T regionSize) {
    size_t read = 0;
    auto buffer = new BYTE[regionSize];
    if (ReadProcessMemory(proc, start, buffer, regionSize, &read)) {
        for (int i = 0; i < read; i++) {
            cout << buffer[i];
        }
    } else if (GetLastError() != 0) {
        printf("Reading process memory failed (%lu).\n", GetLastError());
    }
}

int main() {
    // for write permissions, PROCESS_VM_WRITE and PROCESS_VM_OPERATION must be set
    DWORD access = PROCESS_VM_READ |
                   PROCESS_QUERY_INFORMATION;

    DWORD pid = createProcess(string("victim.exe").c_str());
    HANDLE proc = OpenProcess(access, FALSE, pid);
    if (!proc) {
        fprintf(stderr, "Couldn't open process %lu. "
                        "OpenProcess failed with error: %lu.\n", pid, GetLastError());
        exit(1);
    }

    MEMORY_BASIC_INFORMATION info;
    auto *addr = reinterpret_cast<uint8_t *>(0);

    while (VirtualQueryEx(proc, addr, &info, sizeof(info))) {
        if (isRegionAccessible(info)) {
            scanMemory(proc, info.BaseAddress, info.RegionSize);
        } else if (GetLastError() != 0) {
            printf("Reading process memory structure failed (%lu).\n", GetLastError());
        }
        addr += info.RegionSize;
    }
    CloseHandle(proc);
    return 0;
}


