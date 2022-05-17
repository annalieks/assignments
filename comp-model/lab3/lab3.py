import math
from decimal import Decimal

import matplotlib.pyplot as plt

# u1 = y 
# u2 = y'

# u1' = u2
# u2'' = u1 + 2u2 - 2xe^x


h = Decimal("0.1")


def cauchi_iter(x, u):
    _u = u[0]
    u[0] = u[0] + h * u[1]
    u[1] = u[1] + h * (_u - Decimal(2) * u[1] + Decimal(2) * x * Decimal(math.exp(x)))
    return u


def solve_u(u2):
    l = Decimal("0")
    r = Decimal("1")
    u1 = Decimal(0)  # y(0) = 0

    u = [u1, u2]
    _l = l
    res = [u1]
    while _l < r:
        u = cauchi_iter(_l, u)
        _l = _l + h
        res.append(u[0])
    return res


def phi(u2):
    us = solve_u(u2)
    return us[len(us) - 1] - Decimal("2.71828")  # y(1) = 2.71828


#
# При c_0 = 1, різниця між знайденою та шуканою f(9) = 2.147586681789306449670609744 > 0
print(phi(Decimal(4)))
print(phi(Decimal(0)))
print(phi(Decimal(-5)))
print(phi(Decimal(9)))
print(phi(Decimal(12)))
c0 = Decimal(9)

#
# При c_0 = -3, різниця між знайденою та шуканою f(4) = -0.393583379210693550329390256 < 0
c1 = Decimal(4)

# Метод ділення навпіл
l = c0
r = c1
eps = Decimal("0.0000001")

while l - r > eps:
    mid = (l + r) / Decimal(2)

    v = phi(mid)
    if v > 0:
        l = mid
    else:
        r = mid

# Отже шукане y'(0) = l
ys = solve_u(l)
xs = [0]

_x = Decimal(0)
while _x < Decimal(1):
    _x = _x + h
    xs.append(_x)

for i in zip(xs, ys):
    print('y(' + str(i[0]) + ') = ' + str(i[1]))

plt.plot(xs, ys)

plt.show()
