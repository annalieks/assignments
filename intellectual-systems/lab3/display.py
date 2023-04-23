import collections

from prettytable import prettytable


class Display:
    def __init__(self, data):
        self.data = data

    def print_schedule(self, schedule):
        classes = schedule.get_classes()
        table = prettytable.PrettyTable(
            ['Class #', 'Course (number, max # of students)', 'Is Practice', 'Room (Capacity)',
             'Instructor (Id)', 'Meeting Time', 'day'])
        for i in range(0, len(classes)):
            table.add_row([str(i),
                           classes[i].get_course().get_name() + " (" +
                           classes[i].get_course().get_number() + ", " +
                           str(classes[i].get_course().get_max_num_of_students()) + ")",
                           str(classes[i].get_course().get_is_lab()),
                           classes[i].get_room().get_number() + " (" + str(
                               classes[i].get_room().get_capacity()) + ")",
                           classes[i].get_teacher().get_name() + " (" + str(
                               classes[i].get_teacher().get_id()) + ")",
                           classes[i].get_meeting_time().get_time(), classes[i].get_day().get_day()])
        print(table)

    def print_classes(self, classes):
        table = prettytable.PrettyTable(
            ['Group', 'Course', 'Is Practice', 'Room (Capacity)', 'Teacher', 'Class Time', 'Day'])

        classes_dict = collections.OrderedDict()
        for day in self.data.DAYS:
            classes_dict[day] = []

        for cl in classes:
            classes_dict[cl.get_day().get_day()].append(cl)

        for k in classes_dict:
            classes_dict[k].sort(key=lambda c: c.get_meeting_time().get_time())

        for day in classes_dict.keys():
            for cl in classes_dict[day]:
                table.add_row([cl.get_course().get_grade(), cl.get_course().get_name(),
                               str(cl.get_course().get_is_lab()), cl.get_room().get_number() + " (" + str(
                        cl.get_room().get_capacity()) + ")", cl.get_teacher().get_name(),
                               cl.get_meeting_time().get_time(), cl.get_day().get_day()])
        print(table)
