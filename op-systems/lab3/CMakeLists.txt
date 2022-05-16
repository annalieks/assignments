cmake_minimum_required(VERSION 3.17)
project(lab3)

set(CMAKE_CXX_STANDARD 14)
set(PROJECT_BINARY_DIR .)

add_executable(hacker hacker.cpp)
add_executable(victim victim.cpp)
add_executable(hook hook.cpp)