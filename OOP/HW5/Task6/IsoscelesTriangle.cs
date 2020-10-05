using System;

namespace Task6
{
    public class IsoscelesTriangle : Triangle
    {
        public IsoscelesTriangle(double side, double angle)
            : base(side, side, angle)
        { }

        public override double CalculateArea()
        {
            var angle =  Math.PI * Angle / 180.0;
            return SideA * SideA * Math.Sin(angle) / 2;
        }

        public override double CalculatePerimeter()
        {
            var angle = Math.PI * Angle / 180.0;
            var sideC = SideA * Math.Sqrt(2 * (1 - Math.Cos(angle)));
            return 2 * SideA + sideC;
        }
    }
}