using System;

namespace Task6
{
    public abstract class Triangle
    {
        protected double SideA { get; private set; }
        protected double SideB { get; private set; }
        protected double Angle { get; private set; }

        public Triangle(double sideA, double sideB, double angle)
        {
            SetParams(sideA, sideB, angle);
        }

        private void SetParams(double sideA, double sideB, double angle)
        {
            if (!IsValid(sideA, sideB, angle))
            {
                throw new ArgumentException("Cannot create a triangle with incorrect params");
            }
            SideA = sideA;
            SideB = sideB;
            Angle = angle;
        }
        
        private static bool IsValid(double sideA, double sideB, double angle)
        {
            return (sideA > 0) && (sideB > 0) && (angle > 0) && (angle < 180);
        }

        public virtual double CalculateArea()
        {
            var angle = Math.PI * Angle / 180.0;
            return SideA * SideB * Math.Sin(angle) / 2;
        }

        public virtual double CalculatePerimeter()
        {
            var angle = Math.PI * Angle / 180.0;
            var sideC = Math.Sqrt(SideA * SideA + SideB * SideB 
                                  - 2 * SideA * SideB * Math.Cos(angle));
            return SideA + SideB + sideC;
        }
    }
}