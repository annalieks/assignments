import numpy as np
import matplotlib.pyplot as plt


def func(x):
    return 1 / (x * (1 + x ** 2)) ** 0.5


def compute(a, b, func):
    EPS = 0.1
    n = 100
    e, e1 = 0, EPS + 1
    x, y, x_mid, sum = [], [], 0, 0
    while abs(e1 - e) > EPS:
        dx = (b - a) / n
        x = np.linspace(a, b, n + 1)
        y = [func(arg) if arg != 0 else func(arg + EPS) for arg in x]
        x_mid = (x[:-1] + x[1:]) / 2
        sum = np.sum(func(x_mid) * dx)
        e = e1
        e1 = (dx / 3) * sum
        n *= 2

    # plot
    plt.plot(x, y, 'b')
    y_mid = func(x_mid)
    plt.plot(x_mid, y_mid, 'b.', markersize=10)
    plt.bar(x_mid, y_mid, width=(b - a) / n, alpha=0.2, edgecolor='b')
    plt.show()
    return sum


if __name__ == '__main__':
    # compute(0, 5, func)
    print(compute(0, 5, func))
