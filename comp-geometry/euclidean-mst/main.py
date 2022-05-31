from typing import List

import matplotlib.pyplot as plt
import numpy as np

from kruskal import kruskal_mst
from triangulation import Delaunay


def init_plot():
    plt.axis('equal')
    plt.xlim((-5, 105))
    plt.ylim((-5, 105))


def plot(points, edges):
    init_plot()

    for p in points:
        plt.plot(p[0], p[1], marker="o", markersize=4, color="red")

    for e in edges:
        plt.plot([e[0][0], e[1][0]], [e[0][1],e[1][1]])

    plt.show()


def generate_points(n):
    points_x = np.random.randint(0, 100, n, dtype=np.int64)
    points_y = np.random.randint(0, 100, n, dtype=np.int64)
    return [(points_x[i], points_y[i]) for i in range(0, n)]


if __name__ == '__main__':
    n = int(input('Input number of vertices: '))
    ans, points = '', []
    while ans != 'y' and ans != 'n':
        ans = input('Would you like to generate points? (y - generate, n - input manually) ')

    if ans == 'y':
        points = generate_points(n)
    else:
        for i in range(0, n):
            x = int(input(f'x_{i}: '))
            y = int(input(f'y_{i}: '))
            points.append((x, y))

    print(f'Points:', ', '.join([str(p) for p in points]))
    triangulation = Delaunay().triangulate(points)
    res = kruskal_mst(triangulation)
    print(f'Result: {res}')
    plot(points, res)
