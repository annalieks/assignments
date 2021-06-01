import numpy as np

N = 5


def subsec_array():
    result = np.zeros(N)

    for i in range(1, N + 1):
        result[i - 1] = i
    return result


def eval_row(v, the_only_cube):
    ans = 0
    for i in range(N):
        if i == the_only_cube:
            ans += v[i] * v[i] * v[i]
        else:
            ans += v[i] * v[i]

    return ans


right_side = np.zeros(N)


def F(v):
    result = np.array([])

    for i in range(N):
        result = np.append(result, [eval_row(v, i)])

    return result - right_side


for i in range(N):
    right_side[i] = eval_row(subsec_array(), i)

v = [0.8, 2.3, 3.5, 4.3, 5.1002]

#
# tau < 2 / max(||F'(x)||)
#
# max(||F'(x)||) = max(
#   2|x_1| + 2|x_2| + ... + 3|x_i^2| + 2|x_(i+1)| + ... + 2|x_n|
# )
#
# Нехай усі числа належать проміжку від 0 до N+1
# тоді max(||F'(x)||) = 2 * (N-1) * (N+1) + 3 * (N+1)^2
# нехай tau = 0.999 * (2 / (2 * (N-1) * (N+1) + 3 * (N+1)^2))
#

tau = 0.999 * (2 / (2 * (N - 1) * (N + 1) + 3 * (N + 1) * (N + 1)))


def iteration(v):
    return v - tau * F(v)


eps = 1e-9
cnt = 0

while np.linalg.norm(F(v)) > eps:
    v = iteration(v)

print('Рішення:', v)

print('Зв\'язка:', F(v))
