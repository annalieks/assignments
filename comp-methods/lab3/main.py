import numpy as np

"""
P(перейти на и тый сайт) на т+1 шаге = Р(перейти нечаянно) + Р(перейти с другого сайта) 
(сумма вероятностей, которые ведут на этот сайт)
"""


def length_norm(v):
    sz = 0
    for i in v:
        sz += i
    return sz


def page_rank(M, eps):
    # To make sure that the difference between the 
    # current and the previous value is more than eps 
    # at the beginning 
    diff = eps * 5
    sz = M.size

    # enter the probabilities that are equal (0.25, 0.25, ... for 4 probabilities)
    b_k = np.full(M.shape[1], fill_value=1 / sz)

    while diff >= eps:
        prev = b_k

        b_k1 = np.dot(M, b_k)
        # norm the probabilities (from 0 to 1)
        b_k = b_k1 / np.linalg.norm(b_k1)

        diff_vector = b_k - prev
        diff = np.linalg.norm(diff_vector)

    return b_k


n = int(input('Number of the elements: '))
d = float(input('Dumping factor: '))
print('Enter the (u,v) -- vertices connected in a directed graph:')
print('Enter -1 -1, when you want to stop the prompt')
A = np.zeros(shape=(n, n))

# Матрица смежности
while True:
    [a, b] = input().split(' ')
    v1 = int(a)
    v2 = int(b)

    if v1 == -1:
        break
    elif v1 == v2:
        print('You can not draw the loops from vertex to itself!')
    else:
        A[v2 - 1][v1 - 1] = 1

cnt = np.zeros(shape=(n))

for i in range(n):
    for j in range(n):
        cnt[j] += A[i][j]

for i in range(n):
    for j in range(n):
        if cnt[j] != 0:
            A[i][j] /= cnt[j]

ones_matrix = np.ones(shape=(n, n))

eps = 1e-6
M = d * A + ones_matrix * (1 - d) / n

ranks = page_rank(M, eps)

print(ranks / length_norm(ranks))

"""
3 0.85

1 2
2 3
1 3
-1 -1
"""
