import numpy as np


def F(v):
    x = v[0]
    y = v[1]
    return np.array([
        x * x / y * y - np.cos(y) - 2,
        x * x + y * y - 6
    ])


v = [1.8, 0.1]

#
# tau < 2 / max(||F'(x)||)
#
# max(||F'(x)||) = max(
#   max(|2 * x / y*y| + |sin(y) - 2 * x * x / (y*y*y)|),
#   max(|2 * x| + |2 * y|)
# )
# Нехай x належить [0,4], y теж належить [0.1,4]
#
# Тоді max(|2 * x / y*y| + |sin(y) - 2 * x * x / (y*y*y)|) =
# |2 * 4 / 0.01| + |sin(0.1) - 2 * 4 * 4 / 0.001| =
# 1600 + |sin(0.1) - 32000| <= 34000
#
# max(|2 * x| + |2 * y|) = 8 + 8 <= 34000 також :)
#
# Отже tau < 2 / 34000 ~ 0.00005882352...
# Візьмемо tau = 2 / 34001
#

tau = 2 / 34001


def iteration(v):
    return v - tau * F(v)


eps = 1e-9

if __name__ == '__main__':
    while np.linalg.norm(F(v)) > eps:
        v = iteration(v)

    print('Рішення:', v)

    print('Зв\'язка:', F(v))
