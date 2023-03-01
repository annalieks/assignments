import pygame

import a_star_algorithm
import bfs_algorithm
from entities import *
from display import *
from layout import *
from maze_generator import *
from pacman_manager import *

MAZE_WIDTH = 23
MAZE_HEIGHT = 13
PACMAN_START = [1, 1]
GHOST1_START = [7, 9]
GHOST2_START = [7, 13]


class Game:

    def __init__(self):
        self.pacman = Pacman(0, PACMAN_START[0], PACMAN_START[1])
        self.pacman_manager = PacmanManager()
        self.ghosts = self.init_ghosts()
        self.maze_generator = MazeGenerator()
        self.grid = self.maze_generator.get_generated_grid(MAZE_HEIGHT, MAZE_WIDTH)
        self.grid.food[PACMAN_START[0]][PACMAN_START[1]] = '0'
        self.display_info = DisplayInfo(MAZE_HEIGHT, MAZE_WIDTH)
        self.display = Display()
        self.score = 0
        self.win = 0
        self.keys_pressed = []
        self.house_closed = False
        self.prev = [[1, 1], [1, 1]]

    def init_ghosts(self):
        start_pos = [GHOST1_START, GHOST2_START]
        return [Ghost(1, start_pos[0][0], start_pos[0][1], "astar"),
                Ghost(1, start_pos[1][0], start_pos[1][1], "greedy")]

    def run(self):
        pygame.init()
        win = pygame.display.set_mode((self.display_info.display_width, self.display_info.display_height))
        self.display.draw_window(win, self.grid, self.display_info, self.pacman, self.ghosts, pygame, self.score,
                                 [[], []], [])

        run = True
        while run:
            pygame.time.delay(100)

            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    run = False

            self.run_ghosts()

            if self.if_game_over():
                break

            ghosts_xy = []
            for ghost in self.ghosts:
                ghosts_xy.append([ghost.x, ghost.y])

            self.run_pacman()
            self.display.draw_window(win, self.grid, self.display_info,
                                     self.pacman, self.ghosts, pygame, self.score, None, None)

            if self.if_game_over():
                break

        self.display.draw_game_over(win, self.display_info, pygame, self.win)
        while run:
            pygame.time.delay(100)

            for event in pygame.event.get():
                if event.type == pygame.QUIT:
                    run = False

    def one_move(self, direction):
        self.score += int(self.grid.food[self.pacman.x][self.pacman.y])
        self.grid.food[self.pacman.x][self.pacman.y] = '0'
        self.pacman.change_direction(direction)

    def if_game_over(self):
        if self.score == self.grid.food_amount - 1:
            self.win = True
            return True
        if self.if_pacman_met_ghost():
            self.win = False
            self.score = self.score - self.grid.food_amount
            return True
        return False

    def if_pacman_met_ghost(self):
        for ghost in self.ghosts:
            if self.pacman.x == self.ghosts[ghost.player - 1].x and self.pacman.y == self.ghosts[ghost.player - 1].y:
                return True

    def run_ghosts(self):
        for ghost in self.ghosts:
            nodes = self.grid.find_graph_nodes()
            nodes.append([self.pacman.x, self.pacman.y])
            nodes.append([self.ghosts[0].x, self.ghosts[0].y])
            nodes.append([self.ghosts[1].x, self.ghosts[1].y])

            if ghost.type == "astar":
                path = a_star_algorithm.astar(self.grid.walls,
                                              (ghost.x, ghost.y),
                                              (self.pacman.x, self.pacman.y)
                                              )
            else:
                path = bfs_algorithm.bfs(self.grid.walls,
                                         (ghost.x, ghost.y),
                                         (self.pacman.x, self.pacman.y))

            if len(path) >= 1:
                direction = path[0]
            else:
                direction = 'left'

            self.ghosts[self.ghosts.index(ghost)].move_to(direction, self.display_info, self.ghosts.index(ghost))

    def run_pacman(self):
        keys = pygame.key.get_pressed()
        if keys[pygame.K_LEFT]:
            self.pacman_manager.key_pressed("left")
        elif keys[pygame.K_RIGHT]:
            self.pacman_manager.key_pressed("right")
        elif keys[pygame.K_DOWN]:
            self.pacman_manager.key_pressed("down")
        elif keys[pygame.K_UP]:
            self.pacman_manager.key_pressed("up")

        self.pacman_manager.move_pacman(self, self.pacman, self.grid, self.display_info)
