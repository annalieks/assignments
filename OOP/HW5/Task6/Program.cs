using System;
using System.Collections.Generic;
using System.Drawing;

namespace Task6
{
    class Program
    {
        static void Main(string[] args)
        {
            try
            {
                var triangles = new Dictionary<string, Triangle>();

                triangles.Add("Isosceles", new IsoscelesTriangle(3, 60));
                triangles.Add("Right", new RightTriangle(3, 4));
                
                foreach (var triangle in triangles)
                {
                    Console.WriteLine(triangle.Key);
                    Console.WriteLine("Area: " + triangle.Value.CalculateArea());
                    Console.WriteLine("Perimeter: " + triangle.Value.CalculatePerimeter() + "\n");
                }
            }
            catch (ArgumentException argumentException)
            {
                Console.WriteLine(argumentException.Message);
            }
        }
    }
}