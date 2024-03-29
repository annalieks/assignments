from tabulate import tabulate
from decimal import *


def func(x, y):
    return Decimal('0.4') * y + \
           Decimal('0.002') * x * (Decimal('1') - Decimal('0.2') * x)


EPS = 10 ** (-5)


def create_table(x0, y0, xn, h):
    x, y = x0, y0
    x_vec, y_vec = [x0], [y0]
    while x < xn:
        x_new = x + h
        y_predicted = predict(x, y, h)
        y_corrected = correct(x, y, x_new, y_predicted, h)
        x = x_new
        y = y_corrected

        x_vec.append(x_new)
        y_vec.append(y)
    return x_vec, y_vec


def predict(x, y, h):
    return y + h * func(x, y)


def correct(x, y, x_new, y_predicted, h):
    y_corrected = y_predicted

    while abs(y_corrected - y_predicted) > EPS + 1:
        y_predicted = y_corrected
        y_corrected = y + 0.5 * h * (func(x, y) + func(x_new, y_predicted))

    # every iteration is correcting the value
    # of y using average slope
    return y_corrected


if __name__ == '__main__':
    x0 = Decimal('0.1')
    y0 = Decimal('1.0408')
    xn = Decimal('1')
    h = Decimal('0.1')

    x_vec, y_vec = create_table(x0, y0, xn, h)
    rows = [['x', 'y']]
    for x, y in zip(x_vec, y_vec):
        rows.append([x, y])
    print(tabulate(rows))
