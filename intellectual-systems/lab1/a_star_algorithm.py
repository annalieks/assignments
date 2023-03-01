import heapq


# Heuristic function
def manhattan_distance(x1, y1, x2, y2):
    return abs(x1 - x2) + abs(y1 - y2)


def astar(grid, start, goal):
    visited = set()

    queue = [(0, start)]

    g_score = {start: 0}

    parent = {start: None}

    while queue:
        _, current = heapq.heappop(queue)

        if current == goal:
            path = []
            while current != start:
                x, y = current
                px, py = parent[current]
                if x < px:
                    path.append('up')
                elif x > px:
                    path.append('down')
                elif y < py:
                    path.append('left')
                else:
                    path.append('right')
                current = parent[current]
            path.reverse()
            return path

        visited.add(current)

        x, y = current
        neighbors = [(x - 1, y), (x + 1, y), (x, y - 1), (x, y + 1)]

        for neighbor in neighbors:
            if (0 <= neighbor[0] < len(grid)) and (0 <= neighbor[1] < len(grid[0])) and (neighbor not in visited) and \
                    grid[neighbor[0]][neighbor[1]] != '1':
                g = g_score[current] + 1

                if neighbor in [n[1] for n in queue]:
                    idx = [n[1] for n in queue].index(neighbor)
                    if g < g_score[neighbor]:
                        g_score[neighbor] = g
                        parent[neighbor] = current
                        queue[idx] = (g + manhattan_distance(*neighbor, *goal), neighbor)
                        heapq.heapify(queue)
                else:
                    g_score[neighbor] = g
                    parent[neighbor] = current
                    f = g + manhattan_distance(*neighbor, *goal)
                    heapq.heappush(queue, (f, neighbor))

    return None
