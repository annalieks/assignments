import numpy as np
from scipy.linalg import hilbert


class Calculator:
    epsilon: int = 1e-7
    max_iterations: int = 10000

    def __init__(self):
        pass

    def gaussian_method(self, a, b):
        p, l, u = self.lu(a)
        pb = np.dot(p, b)
        dimension = a.shape[0]
        temp = np.zeros(dimension)
        for i in range(dimension):
            temp[i] = pb[i] - np.sum(l[i][:i] * temp[:i])
        x = np.zeros(dimension)
        for i in range(dimension - 1, -1, -1):
            x[i] = (temp[i] - np.sum(u[i][(i + 1):] * x[(i + 1):])) / u[i][i]
        return x

    def jacobi_method(self, a, b):
        x = np.zeros_like(b)
        dimension = a.shape[0]
        for iteration in range(self.max_iterations):
            x_next = np.zeros_like(b)
            for i in range(dimension):
                sum = 0
                for j in range(dimension):
                    if i != j:
                        sum += a[i][j] * x[j]
                x_next[i] = (b[i] - sum) / a[i, i]
            if np.allclose(x_next, x, atol=self.epsilon, rtol=0):
                return x_next
            x = x_next.copy()
        return x

    def seidel_method(self, a, b):
        x, dimension = np.zeros_like(b), a.shape[0]
        for iteration in range(self.max_iterations):
            x_next = np.zeros_like(b)
            for i in range(dimension):
                sum = 0
                for j in range(i):
                    sum += a[i][j] * x_next[j]
                for j in range(i + 1, dimension):
                    sum += a[i][j] * x_next[j]
                x_next = (a[i] - sum) / a[i][i]
            if np.allclose(x_next, x, atol=self.epsilon, rtol=0):
                print(iteration)
                return x_next
            x = x_next
        return x

    def lu(self, matrix):
        a = matrix.copy()
        n = a.shape[0]  # number of rows
        p = np.identity(n)  # identity matrix

        for i in range(n):
            max, main = 0, i
            # select the main element
            for j in range(i, n):
                if abs(a[j][i]) > max:
                    max, main = abs(a[j][i]), j

            a[i], a[main] = a[main], a[i].copy()
            p[i], p[main] = p[main], p[i].copy()

            # make other elements equal to 0
            for j in range(i + 1, n):
                a[j][i] /= a[i][i]
                for k in range(i + 1, n):
                    a[j][k] -= a[j][i] * a[i][k]

        l, u = np.identity(n), np.zeros(a.shape)

        for i in range(n):
            for j in range(i, n):
                u[i][j] = a[i][j]
            for j in range(i):
                l[i][j] = a[i][j]

        return p, l, u


class Generator:
    delta: float = 1.1

    def gilbert_matrix(self):
        pass

    def random_matrix(self, size):
        a = (np.random.rand(size, size) * 20).round()
        b = (np.random.rand(size, 1) * 20).round()
        return a, b

    def jacobi_matrix(self, size):
        a, b = self.random_matrix(size)
        dimension = a.shape[0]
        for i in range(dimension):
            s = sum([abs(a[i][j]) for j in range(dimension) if i != j])
            if abs(a[i][i]) <= s:
                a[i][i] = s * self.delta
        return a, b

    def hilbert_matrix(self, size):
        a = hilbert(size)
        b = (np.random.rand(size, 1) * 20).round()
        return a, b

    def print(self, a, b, result):
        print('------------------')
        print('A: ', a)
        print('B: ', b)
        print('X: ', result)


if __name__ == '__main__':
    calculator = Calculator()
    generator = Generator()
    ra, rb = generator.random_matrix(3)
    ha, hb = generator.hilbert_matrix(3)
    ja, jb = generator.jacobi_matrix(3)

    gauss = calculator.gaussian_method(ra, rb)
    jacobi = calculator.jacobi_method(ja, jb)
    seidel = calculator.seidel_method(ha, hb)

    print('Gaussian method:')
    generator.print(ha, hb, gauss)
    print('Jacobi method:')
    generator.print(ja, jb, jacobi)
    print('Seidel method:')
    generator.print(ha, hb, seidel)
