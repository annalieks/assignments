using System;
using System.Collections.Generic;

namespace Task5
{
    class Program
    {
        static void Main(string[] args)
        {
            try
            {
                var figures = new Dictionary<string, Figure>();

                figures.Add("Circle", new Circle(3));
                figures.Add("Rectangle", new Rectangle(2, 4));
                figures.Add("Rhombus", new Rhombus(5, 30));
                figures.Add("Square", new Square(4));
                figures.Add("Triangle", new Triangle(3, 4, 5));

                foreach (var figure in figures)
                {
                    Console.WriteLine(figure.Key);
                    Console.WriteLine("Area: " + figure.Value.CalculateArea());
                    Console.WriteLine("Perimeter: " + figure.Value.CalculatePerimeter() + "\n");
                }
            }
            catch (ArgumentException argumentException)
            {
                Console.WriteLine(argumentException.Message);
            }
        }
    }
}
