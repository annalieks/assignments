import numpy as np
import matplotlib.pyplot as plt
from numpy.linalg import solve


class EquationSolver:
    @staticmethod
    def jacobi(A, b, x0, tol, n_iterations=300):
        n = A.shape[0]
        x = x0.copy()
        x_prev = x0.copy()
        counter = 0
        x_diff = tol + 1

        while (x_diff > tol) and (counter < n_iterations):
            for i in range(0, n):
                s = 0
                for j in range(0, n):
                    if i != j:
                        s += A[i, j] * x_prev[j]

                x[i] = (b[i] - s) / A[i, i]
            counter += 1
            x_diff = (np.sum((x - x_prev) ** 2)) ** 0.5
            x_prev = x.copy()
        return x


class Interpolation:
    def target_func(self, x):
        return np.sin(x)

    @staticmethod
    def linear_method(x, x_init, c):
        n = len(x_init)
        y = c[n - 1]
        for j in range(n - 1, 0, -1):
            y = y * x + c[j - 1]
        return y

    @staticmethod
    def linear_coeffs(x, y):
        n = len(x)
        A = np.vstack([x ** j for j in range(n)]).T
        return solve(A, y)

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
    def cubic_spline(x0, x, y):
        x = np.array(x)
        y = np.array(y)

        size = len(x)
        xdiff = np.diff(x)
        ydiff = np.diff(y)
        Li = np.empty(size)
        Li_1 = np.empty(size - 1)
        z = np.empty(size)

        Li[0] = np.sqrt(2 * xdiff[0])
        Li_1[0] = 0.0
        B0 = 0.0
        z[0] = B0 / Li[0]

        for i in range(1, size - 1, 1):
            Li_1[i] = xdiff[i - 1] / Li[i - 1]
            Li[i] = np.sqrt(2 * (xdiff[i - 1] + xdiff[i]) - Li_1[i - 1] * Li_1[i - 1])
            Bi = 6 * (ydiff[i] / xdiff[i] - ydiff[i - 1] / xdiff[i - 1])
            z[i] = (Bi - Li_1[i - 1] * z[i - 1]) / Li[i]

        i = size - 1
        Li_1[i - 1] = xdiff[-1] / Li[i - 1]
        Li[i] = np.sqrt(2 * xdiff[-1] - Li_1[i - 1] * Li_1[i - 1])
        Bi = 0.0
        z[i] = (Bi - Li_1[i - 1] * z[i - 1]) / Li[i]

        i = size - 1
        z[i] = z[i] / Li[i]
        for i in range(size - 2, -1, -1):
            z[i] = (z[i] - Li_1[i - 1] * z[i + 1]) / Li[i]

        index = x.searchsorted(x0)
        np.clip(index, 1, size - 1, index)

        xi1, xi0 = x[index], x[index - 1]
        yi1, yi0 = y[index], y[index - 1]
        zi1, zi0 = z[index], z[index - 1]
        hi1 = xi1 - xi0

        f0 = zi0 / (6 * hi1) * (xi1 - x0) ** 3 + \
             zi1 / (6 * hi1) * (x0 - xi0) ** 3 + \
             (yi1 / hi1 - zi1 * hi1 / 6) * (x0 - xi0) + \
             (yi0 / hi1 - zi0 * hi1 / 6) * (xi1 - x0)
        return f0


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
    n = 20
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

    # Linear coeffs
    c = Interpolation.linear_coeffs(x_init, y_init)

    y1 = interpolation.linear_method(x, x_init, c)
    y2 = [interpolation.lagrange(x0, x_init, y_init) for x0 in x]
    y3 = [interpolation.newton(x0, x_init, diffs) for x0 in x]
    y4 = interpolation.cubic_spline(x, x_init, y_init)

    plot.add(x, y, 'r')
    # plot.add(x, y1, 'y')
    # plot.add(x, y2, 'g')
    # plot.add(x, y3, 'b')
    plot.add(x, y4, 'g')

    plot.show()
