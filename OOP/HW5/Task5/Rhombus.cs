using System;

namespace Task5
{
    public class Rhombus : Figure
    {
        private double Side { get; set; }
        private double Angle { get; set; }

        public Rhombus(double side, double angle)
        {
            SetParams(side, angle);
        }

        private void SetParams(double side, double angle)
        {
            if (!IsValid(side, angle))
            {
                throw new ArgumentException("Cannot create a rhombus with incorrect params");
            }
            Side = side;
            Angle = angle;
        }

        private static bool IsValid(double side, double angle)
        {
            return side > 0 && angle > 0 && angle < 180;
        }
        
        public override double CalculateArea()
        {
            var angle = Math.PI * Angle / 180.0;
            return Side * Side * Math.Sin(angle);
        }

        public override double CalculatePerimeter()
        {
            return 4 * Side;
        }
    }
}