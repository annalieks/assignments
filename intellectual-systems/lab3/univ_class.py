class Class:
    def __init__(self, id, dept, course):
        self._id = id
        self._dept = dept
        self._course = course
        self._teacher = None
        self._meeting_time = None
        self._day = None
        self._room = None

    def get_id(self): return self._id

    def get_dept(self): return self._dept

    def get_course(self): return self._course

    def get_teacher(self): return self._teacher

    def get_meeting_time(self): return self._meeting_time

    def get_room(self): return self._room

    def get_day(self): return self._day

    def set_instructor(self, instructor): self._teacher = instructor

    def set_meeting_time(self, meetingTime): self._meeting_time = meetingTime

    def set_day(self, day): self._day = day

    def set_room(self, room): self._room = room

    def __str__(self):
        return str(self._dept.get_name()) + "," + str(self._course.get_number()) + "," + \
               str(self._room.get_number()) + "," + str(self._teacher.get_id()) + "," + str(
            self._meeting_time.get_time()) + "," + str(self._day.get_day())
