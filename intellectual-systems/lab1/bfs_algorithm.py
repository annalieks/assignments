from collections import deque


def get_neighbors(grid, row, col):
    directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]  # right, down, left, up
    neighbors = []
    for d in directions:
        r = row + d[0]
        c = col + d[1]
        if r >= 0 and r < len(grid) and c >= 0 and c < len(grid[0]) and grid[r][c] != '1':
            neighbors.append((r, c))
    return neighbors


def bfs(grid, start, end):
    queue = deque([(start, "")])
    visited = set()
    while queue:
        curr_pos, curr_path = queue.popleft()
        if curr_pos == end:
            return curr_path.split()
        if curr_pos in visited:
            continue
        visited.add(curr_pos)
        for neighbor in get_neighbors(grid, curr_pos[0], curr_pos[1]):
            queue.append((neighbor, curr_path + ' ' + get_move(curr_pos, neighbor)))
    return None


def get_move(curr_pos, next_pos):
    if curr_pos[0] == next_pos[0]:
        return 'right' if curr_pos[1] < next_pos[1] else 'left'
    else:
        return 'down' if curr_pos[0] < next_pos[0] else 'up'
