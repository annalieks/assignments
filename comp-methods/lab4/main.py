import numpy as np
import matplotlib.pyplot as plt


class Interpolation:
    def target_func(self, x):
        return np.sin(x)

    def linear_method(self, x, y):
        pass

    @staticmethod
    def lagrange(x0, x, y):
        sum = 0
        for j in range(0, len(x)):
            l = 1
            for m in range(len(x)):
                if m != j:
                    l *= (x0 - x[m]) / (x[j] - x[m])
            sum += y[j] * l
        return sum

    @staticmethod
    def newton(x0, x, diffs):
        n = len(diffs) - 1
        sum = diffs[n]
        for j in range(n - 1, -1, -1):
            sum = sum * (x0 - x[j]) + diffs[j]
        return sum

    @staticmethod
    def diff(x, y):
        """ Calculate Newton diff """
        a = np.copy(y)
        m = len(x)
        for k in range(1, m):
            a[k: m] = (a[k:m] - a[k - 1]) / (x[k:m] - x[k - 1])
        return a

    @staticmethod
    def newton_basis(x0, x, j):
        result = 1
        for i in range(j):
            result *= (x0 - x[j])
        return result


class Plot:
    title: str = 'Interpolation'
    x_label: str = 'x'
    y_label: str = 'y'

    def __init__(self):
        plt.xlabel(self.x_label)
        plt.ylabel(self.y_label)
        plt.title(self.title)

    def add(self, x, y, color, **kwargs):
        plt.plot(x, y, color, **kwargs)

    def points(self, x, y):
        plt.scatter(x, y)

    def show(self):
        plt.show()


if __name__ == '__main__':
    a, b = 1, 15
    n = 10
    plot = Plot()
    interpolation = Interpolation()

    # x coordinates
    x = np.linspace(a, b, n * 40)
    # y coordinates
    y = [interpolation.target_func(xi) for xi in x]

    # interpolation knots
    x_init = np.linspace(a, b, n)
    y_init = [interpolation.target_func(xi) for xi in x_init]
    plot.points(x_init, y_init)

    # Newton divided diffs
    diffs = Interpolation.diff(x_init, y_init)

    # y1 = interpolation.linear_method(x, y)
    y2 = [interpolation.lagrange(x0, x_init, y_init) for x0 in x]
    y3 = [interpolation.newton(x0, x_init, diffs) for x0 in x]

    plot.add(x, y, 'r')
    plot.add(x, y2, 'g')
    plot.add(x, y3, 'b')

    plot.show()
