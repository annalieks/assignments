import numpy as np


class Edge:
    """ Directed edge """

    def __init__(self, s, e):
        self.s = s
        self.e = e
        self.next = None
        self.prev = None
        self.reversed = None

    def combine(self, other):
        if self == other:
            return

        self.next.prev, other.next.prev = other, self
        self.next, other.next = other.next, self.next


def right_for(p, edge):
    return diff(p, edge) > 0


def left_for(p, edge):
    return diff(p, edge) < 0


def diff(p, edge):
    a, b = edge.s, edge.e
    return (a[0] - p[0]) * (b[1] - p[1]) - (a[1] - p[1]) * (b[0] - p[0])


def in_circumcircle(a, b, c, d):
    a1, a2 = a[0] - d[0], a[1] - d[1]
    b1, b2 = b[0] - d[0], b[1] - d[1]
    c1, c2 = c[0] - d[0], c[1] - d[1]
    a3, b3, c3 = a1 ** 2 + a2 ** 2, b1 ** 2 + b2 ** 2, c1 ** 2 + c2 ** 2
    det = a1 * b2 * c3 + a2 * b3 * c1 + a3 * b1 * c2 - (a3 * b2 * c1 + a1 * b3 * c2 + a2 * b1 * c3)
    return det < 0


class Delaunay:

    def __init__(self):
        self.edges = set()

    def create_edge(self, s, e):
        fwd = Edge(s, e)
        back = Edge(e, s)
        fwd.reversed, back.reversed = back, fwd
        fwd.next, fwd.prev = fwd, fwd
        back.next, back.prev = back, back
        self.edges.add(fwd)
        return fwd

    def connect_vertices(self, a, b):
        edge = self.create_edge(a.e, b.s)
        edge.combine(a.reversed.prev)
        edge.reversed.combine(b)
        return edge

    def delete_edge(self, edge):
        edge.combine(edge.prev)
        edge.reversed.combine(edge.reversed.prev)
        self.edges.discard(edge)
        self.edges.discard(edge.reversed)

    def triangulate(self, points):
        if len(points) < 2:
            return set()

        points = np.array(points)
        dtype = points.dtype

        points.view(dtype=[('x', dtype), ('y', dtype)]).sort(order=['x', 'y'], axis=0)
        points = np.unique(points, axis=0)

        self.__triangulate(points)
        return [(tuple(edge.s), tuple(edge.e)) for edge in self.edges]

    def __triangulate(self, points):
        if len(points) == 2:
            edge = self.create_edge(points[0], points[1])
            return edge, edge.reversed

        if len(points) == 3:
            p1, p2, p3 = points[0], points[1], points[2]
            a = self.create_edge(p1, p2)
            b = self.create_edge(p2, p3)
            a.reversed.combine(b)

            if right_for(p3, a):
                self.connect_vertices(b, a)
                return a, b.reversed
            elif left_for(p3, a):
                c = self.connect_vertices(b, a)
                return c.reversed, c
            else:
                return a, b.reversed

        middle = len(points) // 2
        left, right = points[:middle], points[middle:]
        left_outside, left_inside = self.__triangulate(left)
        right_inside, right_outside = self.__triangulate(right)

        while True:
            if right_for(right_inside.s, left_inside):
                left_inside = left_inside.reversed.next
            elif left_for(left_inside.s, right_inside):
                right_inside = right_inside.reversed.prev
            else:
                break

        base = self.connect_vertices(left_inside.reversed, right_inside)

        if np.array_equal(left_inside.s, left_outside.s):
            left_outside = base
        if np.array_equal(right_inside.s, right_outside.s):
            right_outside = base.reversed

        # Merge two sets along the splitting line
        while True:
            rc, lc = base.reversed.next, base.prev
            rc_valid, lc_valid = right_for(rc.e, base), right_for(lc.e, base)
            if not (rc_valid or lc_valid):
                break

            if rc_valid:
                while right_for(rc.next.e, base) and in_circumcircle(base.e, base.s, rc.e, rc.next.e):
                    temp = rc.next
                    self.delete_edge(rc)
                    rc = temp

            if lc_valid:
                while right_for(lc.prev.e, base) and in_circumcircle(base.e, base.s, lc.e, lc.prev.e):
                    temp = lc.prev
                    self.delete_edge(lc)
                    lc = temp

            if not rc_valid or (lc_valid and in_circumcircle(rc.e, rc.s, lc.s, lc.e)):
                base = self.connect_vertices(lc, base.reversed)
            else:
                base = self.connect_vertices(base.reversed, rc.reversed)

        return left_outside, right_outside
