using System;

namespace Task2_OOP
{
    class Program
    {
        const string options = "1 - create group\n" +
            "2 - add student (good) to group,\n" +
            "3 - add students (bad) to group\n" +
            "4 - show students info\n" +
            "5 - show full students info\n" +
            "6 - exit.";

        static void Main(string[] args)
        {
            Console.WriteLine(options);
            int code = Convert.ToInt32(Console.ReadLine());
            InputProcessor processor = new InputProcessor();
            while (code != 6)
            {
                switch (code)
                {
                    case 1: 
                        processor.AddGroup();
                        break;
                    case 2:
                        processor.AddStudent("good");
                        break;
                    case 3:
                        processor.AddStudent("bad");
                        break;
                    case 4:
                        processor.ShowInfo("basic");
                        break;
                    case 5:
                        processor.ShowInfo("full");
                        break;
                    default:
                        Console.WriteLine("Unknown option");
                        break;
                }
                Console.WriteLine(options);
                code = Convert.ToInt32(Console.ReadLine());
            }

        }
    }
}
