import random
import time

from tabulate import tabulate

random.seed(9002)

week_days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"]
g_names = ["IPS", "DO", "TTP"]
s_names = ["Equations of mathematical physics",
           "Management theory",
           "AI models and algorithms",
           "Databases and information systems",
           "Ecological and economic processes",
           "Ruby programming",
           "Numerical methods",
           "Computer networks",
           "Algebraic structures",
           "Operational systems"]

t_names = ["Petrushchenkov S.P.",
           "Kashpur O.F.",
           "Zavadsky I.O.",
           "Slabospytskyi O.S.",
           "Dolenko G.O.",
           "Veres M.M.",
           "Pichkur V.V.",
           "Mostovy V.S.",
           "Korobova M.V.",
           "Veres M.M."]


class Schedule:
    n_days = 5
    n_lessons = 4
    total_lessons = n_days * n_lessons

    n_teachers = 4
    n_groups = 3
    n_rooms = 5
    n_subjects = 6

    n_s_per_t = 4  # number of subjects per teacher
    n_s_per_g = 3  # number of subjects per group

    def __init__(self):
        self.rooms = ['247', '1', '53', '232', '407']
        self.groups = g_names[:self.n_groups]
        self.subjects = s_names[:self.n_subjects]
        self.teachers = t_names[:self.n_teachers]

        self.rooms_dests = ["lec"] * self.n_rooms
        for i in range(0, self.n_rooms, 2):
            self.rooms_dests[i] = "lab"

        self.teacher_specs = [set([(si ** 2 + ti ** 2) % self.n_subjects for si in range(self.n_s_per_t)])
                              for ti in range(self.n_teachers)]
        self.learning_plan = [set([(si ** 2 + gi) % self.n_subjects for si in range(self.n_s_per_g)])
                              for gi in range(self.n_groups)]
        self.rooms_for_lesson = [[None] * self.n_groups for _ in range(self.total_lessons)]
        self.subjects_for_lesson = [[None] * self.n_groups for _ in range(self.total_lessons)]
        self.teachers_for_lesson = [[None] * self.n_groups for _ in range(self.total_lessons)]
        self.cnt = 0

    def is_used(self):
        return all([not any(lg is None for lg in l) for l in self.rooms_for_lesson])

    def check_constraints(self):
        self.cnt += 1
        if self.is_used():
            for g in range(self.n_groups):
                class_types_per_subjects = {s: set() for s in self.learning_plan[g]}
                for l in range(self.total_lessons):
                    class_types_per_subjects[self.subjects_for_lesson[l][g]].add(
                        self.rooms_dests[self.rooms_for_lesson[l][g]])
                if any([len(s) != 2 for s in class_types_per_subjects.values()]):
                    return False

        for teacher in self.teachers_for_lesson:  # a teacher can be assigned to only one class at a time
            for i in range(self.n_groups - 1):
                if teacher[i] is not None and teacher[i] in teacher[i + 1:]:
                    return False
        for room in self.rooms_for_lesson:  # a room can be assigned to only one class at a time
            for i in range(self.n_groups - 1):
                if room[i] is not None and room[i] in room[i + 1:]:
                    return False
        return True

    def set_values(self, l, g, t, r, s):
        self.teachers_for_lesson[l][g] = t
        self.rooms_for_lesson[l][g] = r
        self.subjects_for_lesson[l][g] = s

    def select_unassigned_var(self):
        for l in range(self.total_lessons):
            for g in range(self.n_groups):
                if self.teachers_for_lesson[l][g] is None:
                    return l, g

    def choose_random_class(self, g):
        for t in random.sample(range(self.n_teachers), self.n_teachers):
            available_subjects = list(self.learning_plan[g].intersection(self.teacher_specs[t]))
            for r in random.sample(range(self.n_rooms), self.n_rooms):
                for s in random.sample(available_subjects, len(available_subjects)):
                    yield t, r, s

    def degree_heuristic(self):
        """
        Обираємо пари з найбільшою кількістю незаповнених сусідніх пар (степінь)
        """
        none_list = []
        for l in range(self.total_lessons):
            none_list.append(sum([self.teachers_for_lesson[l][g] is None for g in range(self.n_groups)]))
        l = none_list.index(max(none_list))
        for g in range(self.n_groups):
            if self.teachers_for_lesson[l][g] is None:
                return l, g

    def mrv_heuristic(self):
        """
        Min Remaining Values
        Обираємо групу, яке має найбільшу кількість назначених занять, що йдуть в цей самий час,
        але в іншої групи/викладача
        """
        for d in range(self.n_days):
            for l in range(self.n_lessons):
                l = d * self.n_lessons + l
                for g in range(self.n_groups):
                    if self.teachers_for_lesson[l][g] is None:
                        return l, g

    def lcv_heuristic(self, g):
        """
        Евристика з найменшим обмежуючим значенням
        Обираємо викладачів, предмети та аудиторії, щоб вибір викладача для групи
        накладав найменше обмежень для іншої групи.
        """
        teacher_scores = []
        for t in range(self.n_teachers):
            teacher_scores.append([0, t])
            for gi in range(self.n_groups):
                if gi != g:
                    teacher_scores[-1][0] += len(self.teacher_specs[t].intersection(self.learning_plan[gi]))

        for _, t in sorted(teacher_scores, key=lambda sc: sc[0]):
            available_subjects = list(self.learning_plan[g].intersection(self.teacher_specs[t]))
            for r in random.sample(range(self.n_rooms), self.n_rooms):
                for s in random.sample(available_subjects, len(available_subjects)):
                    yield t, r, s

    def forward_check(self, l, g):
        """
        При виборі перевіряється, чи не будуть ці значення конфліктувати з обмеженнями у майбутньому
        """
        for t in random.sample(range(self.n_teachers), self.n_teachers):
            if t not in self.teachers_for_lesson[l]:
                available_subjects = list(self.learning_plan[g].intersection(self.teacher_specs[t]))
                for r in random.sample(range(self.n_rooms), self.n_rooms):
                    if r not in self.rooms_for_lesson[l]:
                        for s in random.sample(available_subjects, len(available_subjects)):
                            yield t, r, s

    def backtracking(self):
        # var = self.select_unassigned_var()
        # var = self.mrv_heuristic()
        var = self.degree_heuristic()

        if var is None:
            return True

        l, g = var

        # for t, r, s in self.choose_random_class(g):
        for t, r, s in self.lcv_heuristic(g):
        # for t, r, s in self.forward_check(l, g):
            self.set_values(l, g, t, r, s)

            if self.check_constraints():
                res = self.backtracking()
                if res:
                    return True
            self.set_values(l, g, None, None, None)
        return False

    def print_schedule(self):
        table = dict(indices=["Day", "Group"] + list(range(1, self.n_lessons + 1)))
        for d in range(self.n_days):
            table[(d, 0)] = [week_days[d]]
            for g in range(1, self.n_groups):
                table[(d, g)] = [""]
            for g in range(self.n_groups):
                table[(d, g)].append(g_names[g])
                for l in range(self.n_lessons):
                    l = d * self.n_lessons + l
                    lesson = (f"{self.subjects[self.subjects_for_lesson[l][g]]}\n"
                              f"({self.rooms_dests[self.rooms_for_lesson[l][g]]})\n"
                              f"Room: {self.rooms[self.rooms_for_lesson[l][g]]}\n"
                              f"Teacher: {self.teachers[self.teachers_for_lesson[l][g]]}")
                    table[(d, g)].append(lesson)
            table[(d, -1)] = [""] * (2 + self.n_lessons)
        print(tabulate(table, tablefmt="grid"))


schedule = Schedule()
start = time.time()
schedule.backtracking()
print(f"Time: {time.time() - start} seconds")
print(f"Constraints checked {schedule.cnt} times")
schedule.print_schedule()
