class UnionFind:
    def __init__(self):
        self.parent = {}

    def find_root(self, v):
        if v not in self.parent:
            return v
        self.parent[v] = self.find_root(self.parent[v])
        return self.parent[v]

    def union_set(self, u, v):
        u, v = self.find_root(u), self.find_root(v)
        if u != v:
            self.parent[u] = v

    def is_same_set(self, u, v):
        return self.find_root(u) == self.find_root(v)


def get_weight(e):
    return (e[0][0] - e[1][0]) ** 2 + (e[0][1] - e[1][1]) ** 2


def kruskal_mst(edges):
    uf = UnionFind()
    edges = sorted(edges, key=lambda e: get_weight(e))

    mst = []
    for edge in edges:
        v1, v2 = edge[0], edge[1]
        # check that adding an edge does not creates a cycle
        if not uf.is_same_set(v1, v2):
            mst.append(edge)
            uf.union_set(v1, v2)

    return mst
