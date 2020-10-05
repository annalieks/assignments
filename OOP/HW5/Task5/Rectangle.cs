using System;

namespace Task5
{
    public class Rectangle : Figure
    {
        private double SideA { get; set; }
        private double SideB { get; set; }

        public Rectangle(double sideA, double sideB)
        {
            SetSides(sideA, sideB);
        }

        private void SetSides(double sideA, double sideB)
        {
            if (!IsValid(sideA, sideB))
            {
                throw new ArgumentException("Cannot create a rectangle with incorrect sides");
            }
            SideA = sideA;
            SideB = sideB;
        }

        private static bool IsValid(double sideA, double sideB)
        {
            return sideA > 0 && sideB > 0;
        }
        
        public override double CalculateArea()
        {
            return SideA * SideB;
        }

        public override double CalculatePerimeter()
        {
            return 2 * (SideA + SideB);
        }
    }
}