using System;

namespace Task4
{
    class Program
    {
        private static string _greeting = "Hi! Available commands:\n" +
                                          "1 - create triangle\n" +
                                          "2 - create equilateral triangle\n" +
                                          "3 - quit\n";
        
        static void Main(string[] args)
        {
            Console.WriteLine(_greeting);
            var input = Console.ReadLine();
            while (input != "3")
            {
                try
                {
                    var triangle = CreateTriangle(input);
                    if (input == "1" || input == "2")
                    {
                        Console.WriteLine("Perimeter: " + triangle.CalculatePerimeter());
                        Console.WriteLine("Angles: " +
                                          string.Join(", ", triangle.CalculateAngles()));
                    }

                    if (input == "2")
                    {
                        var equilateralTriangle = (EquilateralTriangle) triangle;
                        Console.WriteLine("Area: " + equilateralTriangle.CalculateArea());
                    }
                }
                catch (ArgumentException argumentException)
                {
                    Console.WriteLine(argumentException.Message);
                }
                catch (FormatException formatException)
                {
                    Console.WriteLine(formatException.Message);
                }

                Console.WriteLine("\nEnter command: ");
                input = Console.ReadLine();
            }
        }

        static Triangle CreateTriangle(string input)
        {
            if (input == "1")
            {
                Console.WriteLine("Enter side A: ");
                var a = Convert.ToDouble(Console.ReadLine());
                Console.WriteLine("Enter side B: ");
                var b = Convert.ToDouble(Console.ReadLine());
                Console.WriteLine("Enter side C: ");
                var c = Convert.ToDouble(Console.ReadLine());
                return new Triangle(a, b, c);
            }
            else if (input == "2")
            {
                Console.WriteLine("Enter side: ");
                var side = Convert.ToDouble(Console.ReadLine());
                return new EquilateralTriangle(side);
            }

            return null;
        }
    }
}