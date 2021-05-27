import numpy as np
from scipy.linalg import hilbert


class Calculator:
    epsilon: int = 1e-7
    max_iterations: int = 10000

    def __init__(self):
        pass

    def gaussian_method(self, a, b):
        p, l, u = self.lu(a)
        # A(1) = P(1)A, b(1)= P(1)b
        pb = np.dot(p, b)
        dimension = a.shape[0]
        temp = np.zeros(dimension)
        # for lower triangular matrix
        for i in range(dimension):
            temp[i] = pb[i] - np.sum(l[i][:i] * temp[:i])
        x = np.zeros(dimension)
        # for upper triangular matrix
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
                sum1 = np.dot(a[i, :i], x_next[:i])
                sum2 = np.dot(a[i, (i + 1):], x[(i + 1):])
                x_next[i] = (b[i] - sum1 - sum2) / a[i][i]
            if np.allclose(x, x_next, rtol=self.epsilon):
                return x_next
            x = x_next
        return x

    def lu(self, matrix):
        c = matrix.copy()
        n = c.shape[0]  # number of rows
        p = np.identity(n)  # identity matrix

        for i in range(n):
            max, main = 0, i
            # select the main element from the elements not higher than i-th row
            for j in range(i, n):
                if abs(c[j][i]) > max:
                    max, main = abs(c[j][i]), j

            # swap the row with the main element with i-th row
            c[i], c[main] = c[main], c[i].copy()
            p[i], p[main] = p[main], p[i].copy()

            # all elements in a column lower than i-th row are
            # divided by the main element
            for j in range(i + 1, n):
                c[j][i] /= c[i][i]
                # subtract for all elements lower than i-th row and i-th column
                for k in range(i + 1, n):
                    c[j][k] -= c[j][i] * c[i][k]

        l, u = np.identity(n), np.zeros(c.shape)

        # c = l + u - e
        for i in range(n):
            for j in range(i):
                l[i][j] = c[i][j]
            for j in range(i, n):
                u[i][j] = c[i][j]

        return p, l, u


class Generator:
    delta: float = 1.1

    def random_matrix(self, size):
        a = (np.random.rand(size, size) * 20).round()
        c = np.ones(a.shape[0])
        b = a @ c
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
        c = np.ones(a.shape[0])
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
    ra, rb = generator.random_matrix(25)
    ha, hb = generator.hilbert_matrix(25)
    ja, jb = generator.jacobi_matrix(3)

    gauss = calculator.gaussian_method(ha, hb)
    jacobi = calculator.jacobi_method(ja, jb)
    seidel = calculator.seidel_method(ha, hb)

    print('Gaussian method:')
    generator.print(ra, rb, gauss)
    print('Jacobi method:')
    generator.print(ja, jb, jacobi)
    print('Seidel method:')
    generator.print(ha, hb, seidel)
