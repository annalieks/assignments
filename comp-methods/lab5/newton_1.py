import numpy as np


def F(v):
    x = v[0]
    y = v[1]
    return np.array([
        x * x / y * y - np.cos(y) - 2,
        x * x + y * y - 6
    ])


def Jacobian(v):
    x = v[0]
    y = v[1]
    return np.array([
        [2 * x / y * y, np.sin(y) - 2 * x * x / (y * y * y)],
        [2 * x, 2 * y]
    ])


v = [1.8, 0.1]


def iteration(v):
    Az = Jacobian(v)
    z = np.linalg.solve(Az, F(v))
    v_new = v - z

    return v_new


eps = 1e-15

if __name__ == '__main__':
    while np.linalg.norm(F(v)) > eps:
        v = iteration(v)

    print('Розв\'язок:', v)

    print('Зв\'язка:', F(v))
