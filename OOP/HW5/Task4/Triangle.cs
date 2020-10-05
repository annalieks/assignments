using System;

namespace Task4
{
    public class Triangle
    {
        protected double SideA { get; private set; }
        private double SideB { get; set; }
        private double SideC { get; set; }

        public Triangle(double sideA, double sideB, double sideC)
        {
            SetSides(sideA, sideB, sideC);
        }

        private void SetSides(double sideA, double sideB, double sideC)
        {
            if (!IsValidTriangle(sideA, sideB, sideC))
            {
                throw new ArgumentException("Cannot create a triangle with incorrect sides");
            }
            SideA = sideA;
            SideB = sideB;
            SideC = sideC;
        }
        
        public double[] CalculateAngles()
        {
            var result = new double[3];
            
            result[0] = Math.Round(Math.Acos((SideA * SideA + SideB * SideB - SideC * SideC)
                                     / (2 * SideA * SideB)) * 180 / Math.PI, 4);
            result[1] = Math.Round(Math.Acos((SideB * SideB + SideC * SideC - SideA * SideA)
                                             / (2 * SideB * SideC)) * 180 / Math.PI, 4);
            result[2] = Math.Round(Math.Acos((SideA * SideA + SideC * SideC - SideB * SideB)
                                             / (2 * SideA * SideC)) * 180 / Math.PI, 4);
            
            return result;
        }

        public double CalculatePerimeter()
        {
            return SideA + SideB + SideC;
        }

        private static bool IsValidTriangle(double sideA, double sideB, double sideC)
        {
            return (sideA + sideB > sideC)
                    && (sideA + sideC > sideB)
                    && (sideB + sideC > sideA)
                   && (sideA > 0) && (sideB > 0) && (sideC > 0);
        }
    }
}
