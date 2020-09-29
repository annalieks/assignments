using System;
using System.Collections.Generic;
using System.Text;

namespace Task2_OOP
{
    class GoodStudent : Student
    {
        public GoodStudent(string name): base(name)
        {
            this.state += "good";
        }

        public override void Study()
        {
            Read();
            Write();
            Read();
            Write();
            Relax();
        }
    }
}
