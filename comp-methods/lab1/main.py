import math
import sympy as sp


class Calculator:
    eps: int = 1e-7
    approach: int = -1
    tau: int = -2 / 10.75

    def __init__(self):
        self.x = sp.Symbol('x')
        test_function = self.get_test_function()
        self.function = sp.lambdify(self.x, test_function)
        self.derivative = sp.lambdify(self.x, test_function.diff(self.x))

    def get_test_function(self):
        x = self.x
        return 2 * x ** 3 + 3 * x ** 2 - x + 1

    def dichotomy(self, left=-200, right=200):
        while right - left > self.eps:
            mid = (left + right) / 2
            if self.function(mid) * self.function(right) < 0:
                left = mid
            else:
                right = mid
        return (left + right) / 2

    def relaxation(self):
        current, next = 0, self.approach
        while math.fabs(next - current) > self.eps:
            current = next
            next = current + self.tau * self.function(current)
        return next

    def newton(self):
        current, next = 0, self.approach
        while math.fabs(next - current) > self.eps:
            current = next
            next = current - self.function(current) / self.derivative(current)
        return next


if __name__ == '__main__':
    calculator = Calculator()
    print(f'Dichotomy: {calculator.dichotomy()}')
    print(f'Newton: {calculator.newton()}')
    print(f'Relaxation: {calculator.relaxation()}')
