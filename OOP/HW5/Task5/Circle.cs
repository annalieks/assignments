using System;

namespace Task5
{
    public class Circle : Figure
    {
        private double _radius;

        private double Radius
        {
            get => _radius;
            set
            {
                if (!IsValid(value))
                {
                    throw new ArgumentException("Cannot create a circle with incorrect radius");
                } 
                _radius = value;
            }
        }

        public Circle(double radius)
        {
            Radius = radius;
        }
        
        private bool IsValid(double radius)
        {
            return radius > 0;
        }
        
        public override double CalculateArea()
        {
            return Math.PI * Radius * Radius;
        }

        public override double CalculatePerimeter()
        {
            return 2 * Math.PI * Radius;
        }
    }
}