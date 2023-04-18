class Course:
    def __init__(self, number, name, teachers, max_num_of_students, grade, is_lab=False):
        self._number = number
        self._name = name
        self._max_num_of_students = max_num_of_students
        self._teachers = teachers
        self._grade = grade
        self._isLab = is_lab

    def get_number(self): return self._number

    def get_name(self): return self._name

    def get_teachers(self): return self._teachers

    def get_max_num_of_students(self): return self._max_num_of_students

    def get_grade(self): return self._grade

    def get_is_lab(self): return self._isLab

    def __str__(self): return self._name
