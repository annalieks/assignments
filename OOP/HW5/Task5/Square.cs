using System;

namespace Task5
{
    public class Square : Figure
    {
        private double _side;

        private double Side
        {
            get => _side;
            set
            {
                if (!IsValid(value))
                {
                    throw new ArgumentException("Cannot create a square with incorrect side");
                }
                _side = value;
            }
        }

        public Square(double side)
        {
            Side = side;
        }

        private static bool IsValid(double side)
        {
            return side > 0;
        }
        
        public override double CalculateArea()
        {
            return Side * Side;
        }

        public override double CalculatePerimeter()
        {
            return 4 * Side;
        }
    }
}