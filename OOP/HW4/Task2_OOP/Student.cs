using System;
using System.Collections.Generic;
using System.Text;

namespace Task2_OOP
{
    abstract class Student
    {
        protected string name;
        protected string state;

        public Student(string name)
        {
            this.name = name;
            this.state = "";
        }
        public string GetName()
        {
            return name;
        }

        public string GetState()
        {
            return state;
        }

        public void Relax()
        {
            state += "Relax";
        }

        public void Read()
        {
            state += "Read";
        }

        public void Write()
        {
            state += "Write";
        }

        abstract public void Study();
    }
}
