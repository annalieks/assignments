using System.Collections.Generic;
using System.Linq;

namespace Task2_OOP
{
    class Group
    {
        private string name;
        private List<Student> students = new List<Student>();

        public Group(string name)
        {
            this.name = name;
        }

        public string GetName()
        {
            return name;
        }

        public void AddStudent(Student st)
        {
            students.Add(st);
        }

        public string GetInfo()
        {
            return "Group: " + name + "\n" +
                "Students: \n" + string.Join('\n', GetStudentsNames());
        }

        public string GetFullInfo()
        {
            return "Group: " + name + "\n" +
                "Students: \n" + string.Join('\n', GetAllStudentsInfo());
        }

        private List<string> GetStudentsNames()
        {
            return students
                .Select(s => s.GetName())
                .ToList();
        }

        private List<string> GetAllStudentsInfo()
        {
            return students
                .Select(s => s.GetName() + ',' + s.GetState())
                .ToList();
        }
    }
}
