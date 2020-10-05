using System;

namespace Task6
{
    public class RightTriangle : Triangle
    {
        public RightTriangle(double sideA, double sideB)
            : base(sideA, sideB, 90)
        { }

        public override double CalculateArea()
        {
            return SideA * SideB / 2;
        }

        public override double CalculatePerimeter()
        {
            var sideC = Math.Sqrt(SideA * SideA + SideB * SideB);
            return SideA + SideB + sideC;
        }
    }
}