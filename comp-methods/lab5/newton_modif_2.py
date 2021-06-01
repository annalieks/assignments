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


def eval_jacobian_row(v, the_only_cube):
    result = np.zeros(N)

    for i in range(N):
        if i == the_only_cube:
            result[i] = 3 * v[i] * v[i]
        else:
            result[i] = 2 * v[i]

    return result


def F(v):
    result = np.array([])

    for i in range(N):
        result = np.append(result, [eval_row(v, i)])

    return result - right_side


def Jacobian(v):
    result = np.zeros((N, N))

    for i in range(N):
        row = eval_jacobian_row(v, i)
        for j in range(N):
            result[i][j] = row[j]

    return result


v = [0.8, 2.3, 3.5, 4.3, 5.1002]

Az = Jacobian(v)


def iteration(v):
    z = np.linalg.solve(Az, F(v))
    v_new = v - z

    return v_new


eps = 1e-15

if __name__ == '__main__':
    right_side = np.zeros(N)

    for i in range(N):
        right_side[i] = eval_row(subsec_array(), i)

    print(subsec_array())

    while np.linalg.norm(F(v)) > eps:
        v = iteration(v)

    print('Рішення:', v)

    print('Зв\'язка:', F(v))
