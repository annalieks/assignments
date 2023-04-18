from course import Course
from day import Day
from department import Department
from class_time import ClassTime
from room import Room
from teacher import Teacher


class Data:
    ROOMS = [["306", 120], ["705", 30], ["1", 120], ["232", 45], ["212", 25], ["317", 45]]
    DAYS = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"]
    MEETING_TIMES = ["8:40 - 10:15", "10:35 - 12:10", "12:20 - 13:55", "14:05 - 15:40"]
    TEACHERS = [["1", "Golubeva K.M."], ["2", "Golubeva K.M."], ["3", "Marynych O.V."],
                ["4", "Chentsov O.I."], ["5", "Shishatska O.V."], ["6", "Cholyi V.Y."],
                ["7", "Livinska G.V."], ["8", "Karnaukh T.O."], ["9", "Panchenko T.V."],
                ["10", "Petrushchenkov S.P."], ["11", "Zavadsky I.O."], ["12", "Slabospytskyi O.S."],
                ["13", "Dolenko G.O."], ["14", "Veres M.M."], ["15", "Golubeva K.M."],
                ["16", "Kashpur O.F."], ["17", "Pichkur V.V."], ["18", "Mostovy V.S."],
                ["19", "Zavadsky I.O."], ["20", "Korobova M.V."], ["21", "Veres M.M."]]

    def __init__(self):
        self._rooms = []
        self._meeting_times = []
        self._teachers = []
        self._days = []
        self._courses = []

        for i in range(0, len(self.ROOMS)):
            self._rooms.append(Room(self.ROOMS[i][0], self.ROOMS[i][1]))

        for i in range(0, len(self.TEACHERS)):
            self._teachers.append(Teacher(self.TEACHERS[i][0], self.TEACHERS[i][1]))

        for i in range(0, len(self.MEETING_TIMES)):
            self._meeting_times.append(ClassTime(self.MEETING_TIMES[i]))

        for i in range(0, len(self.DAYS)):
            self._days.append(Day(self.DAYS[i]))

        self._courses.append(Course("1", "Numerical methods", [self._teachers[0]], 100, "MCC"))
        self._courses.append(Course("2", "Computer networks", [self._teachers[1]], 35, "MCC", True))
        self._courses.append(Course("3", "Algebraic structures", [self._teachers[2]], 35, "MCC", True))
        self._courses.append(Course("4", "Operational systems", [self._teachers[3]], 100, "MCC"))
        self._courses.append(Course("5", "Theory of programming", [self._teachers[4]], 35, "MCC", True))
        self._courses.append(Course("6", "Scientific image of the world", [self._teachers[5]], 35, "MCC", True))
        self._courses.append(Course("7", "Probability theory", [self._teachers[6]], 100, "MCC"))
        self._courses.append(Course("8", "Programming paradigms", [self._teachers[7]], 100, "MCC"))
        self._courses.append(Course("9", "WEB technologies", [self._teachers[8]], 100, "MCC"))
        self._courses.append(Course("10", "Philosophy", [self._teachers[9]], 100, "MCC"))
        self._courses.append(Course("11", "Theory of quantum computing", [self._teachers[10]], 35, "MCC", True))

        self._courses.append(Course("12", "Data Analysis", [self._teachers[11]], 100, "DO"))
        self._courses.append(Course("13", "System optimization", [self._teachers[12]], 35, "DO"))
        self._courses.append(Course("14", "Theory of functions of a complex variable",
                                    [self._teachers[13]], 35, "DO", True))
        self._courses.append(Course("15", "Basics of calculation methods", [self._teachers[14]], 35, "DO", True))
        self._courses.append(Course("16", "Equations of mathematical physics", [self._teachers[16]], 35, "DO", True))
        self._courses.append(Course("17", "Management theory", [self._teachers[16]], 35, "DO"))
        self._courses.append(Course("18", "AI models and algorithms", [self._teachers[17]], 35, "DO", True))
        self._courses.append(Course("19", "Databases and information systems", [self._teachers[18]], 35, "DO"))
        self._courses.append(Course("20", "Ecological and economic processes", [self._teachers[19]], 35, "DO", True))
        self._courses.append(Course("21", "Ruby programming", [self._teachers[20]], 35, "DO", True))

        KNU = Department(["KNU"], self._courses)
        self._depts = [KNU]

        self._numberOfClasses = 0
        for i in range(0, len(self._depts)):
            self._numberOfClasses += len(self._depts[i].get_courses())

    def get_rooms(self):
        return self._rooms

    def get_teachers(self):
        return self._teachers

    def get_courses(self):
        return self._courses

    def get_depts(self):
        return self._depts

    def get_class_times(self):
        return self._meeting_times

    def get_days(self):
        return self._days

    def get_num_of_classes(self):
        return self._numberOfClasses
