using System;
using System.Collections.Generic;
using System.Text;

namespace Task2_OOP
{
    class InputProcessor
    {

        private List<Group> groups = new List<Group>();

        public void AddGroup()
        {
            Console.WriteLine("Enter group number: ");
            string groupNumber = Console.ReadLine();
            groups.Add(new Group(groupNumber));
            Console.WriteLine("Group created!");
        }

        public void AddStudent(string type)
        {
            Console.WriteLine("Enter student's group number: ");
            string groupNumber = Console.ReadLine();
            var group = groups.Find(g => g.GetName() == groupNumber);
            if(group == null)
            {
                Console.WriteLine("No such group!");
                return;
            }
            Console.WriteLine("Enter student's name: ");
            string name = Console.ReadLine();
            if (type == "good")
                group.AddStudent(new GoodStudent(name));
            else if (type == "bad")
                group.AddStudent(new BadStudent(name));
            else
            {
                Console.WriteLine("No such student type.");
            }
        }

        public void ShowInfo(string type)
        {
            Console.WriteLine("Enter group number: ");
            string groupNumber = Console.ReadLine();
            var group = groups.Find(g => g.GetName() == groupNumber);
            if (group == null)
            {
                Console.WriteLine("No such group!");
                return;
            }

            if (type == "basic")
            {
                Console.WriteLine(group.GetInfo());
            } else if(type == "full")
            {
                Console.WriteLine(group.GetFullInfo());
            } else
            {
                Console.WriteLine("No such info type.");
            }
        }
    }
}
