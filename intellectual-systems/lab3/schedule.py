import random as rnd

import numpy as np

from univ_class import Class


class Schedule:
    def __init__(self, data):
        self._data = data
        self._classes = []
        self._num_of_conflicts = 0
        self._fitness = -1
        self._class_num = 0
        self._fitness_changed = True

    def get_classes(self):
        self._fitness_changed = True
        return self._classes

    def get_num_of_conflicts(self):
        return self._num_of_conflicts

    def get_fitness(self):
        if self._fitness_changed is True:
            self._fitness = self.calculate_fitness()
            self._fitness_changed = False
        return self._fitness

    def initialize(self):  # Define the classes objects Data
        depts = self._data.get_depts()
        for i in range(0, len(depts)):
            courses = depts[i].get_courses()
            for j in range(0, len(courses)):
                new_class = Class(self._class_num, depts[i], courses[j])
                self._class_num += 1
                new_class.set_meeting_time(
                    self._data.get_class_times()[rnd.randrange(0, len(self._data.get_class_times()))])
                new_class.set_day(self._data.get_days()[rnd.randrange(0, len(self._data.get_days()))])
                new_class.set_room(self._data.get_rooms()[rnd.randrange(0, len(self._data.get_rooms()))])
                new_class.set_instructor(
                    courses[j].get_teachers()[rnd.randrange(0, len(courses[j].get_teachers()))])
                self._classes.append(new_class)
        return self

    def calculate_fitness(self):
        self._num_of_conflicts = 0
        classes = self.get_classes()
        for i in range(0, len(classes)):
            if classes[i].get_room().get_capacity() < classes[i].get_course().get_max_num_of_students():
                self._num_of_conflicts += 1
            for j in range(i, len(classes)):
                if (classes[i].get_meeting_time() == classes[j].get_meeting_time()
                        and classes[i].get_day() == classes[j].get_day()
                        and classes[i].get_id() != classes[j].get_id()

                        and classes[i].get_course().get_grade() == classes[j].get_course().get_grade()):
                    if (np.any(np.in1d(classes[i].get_dept().get_name(), classes[j].get_dept().get_name()))
                            and not (classes[i].get_course().get_is_lab() is True
                                     and classes[j].get_course().get_is_lab() is True)):
                        self._num_of_conflicts += 1

                    if classes[i].get_room() == classes[j].get_room():
                        self._num_of_conflicts += 1
                if classes[i].get_meeting_time() == classes[j].get_meeting_time() \
                        and classes[i].get_day() == classes[
                    j].get_day() and classes[i].get_id() != classes[j].get_id():
                    if classes[i].get_teacher() == classes[j].get_teacher():
                        self._num_of_conflicts += 1
        return 1 / (1.0 * self._num_of_conflicts + 1)

    def __str__(self):
        value = ""
        for i in range(0, len(self._classes) - 1):
            value += str(self._classes[i]) + ", "
        value += str(self._classes[len(self._classes) - 1])
        return value
