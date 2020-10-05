using System;

namespace Task4
{
    public class EquilateralTriangle : Triangle
    {
        private double _area;

        public EquilateralTriangle(double side)
            : base(side, side, side)
        {
            _area = CalculateArea();
        }
        
        public double CalculateArea()
        {
            return Math.Round(SideA * SideA * Math.Sqrt(3) / 4, 4);
        }
    }
}