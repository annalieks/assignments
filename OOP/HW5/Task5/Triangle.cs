using System;

namespace Task5
{
    public class Triangle : Figure
    {
        private double SideA { get; set; }
        private double SideB { get; set; }
        private double SideC { get; set; }

        public Triangle(double sideA, double sideB, double sideC)
        {
            SetSides(sideA, sideB, sideC);
        }
        
        private void SetSides(double sideA, double sideB, double sideC)
        {
            if (!IsValid(sideA, sideB, sideC))
            {
                throw new ArgumentException("Cannot create a triangle with incorrect sides");
            }
            SideA = sideA;
            SideB = sideB;
            SideC = sideC;
        }
        
        private static bool IsValid(double sideA, double sideB, double sideC)
        {
            return (sideA + sideB > sideC)
                   && (sideA + sideC > sideB)
                   && (sideB + sideC > sideA)
                   && (sideA > 0) && (sideB > 0) && (sideC > 0);
        }
        
        public override double CalculateArea()
        {
            var semiPerimeter = CalculatePerimeter() / 2;
            var area = Math.Round(Math.Sqrt(semiPerimeter
                                    * (semiPerimeter - SideA)
                                    * (semiPerimeter - SideB)
                                    * (semiPerimeter - SideC)), 4);
            return area;
        }

        public override double CalculatePerimeter()
        {
            return SideA + SideB + SideC;
        }
    }
}