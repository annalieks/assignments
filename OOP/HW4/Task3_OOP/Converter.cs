using System;
using System.Collections.Generic;
using System.Text;

namespace Task3_OOP
{
    class Converter
    {
        double usd;
        double eur;
        public Converter(double usd, double eur)
        {
            this.usd = usd;
            this.eur = eur;
        }

        public double UAHToUSD(double sum)
        {
            return sum / usd;
        }

        public double UAHToEUR(double sum)
        {
            return sum / eur;
        }

        public double USDToUAH(double sum)
        {
            return sum * usd;
        }

        public double EURToUAH(double sum)
        {
            return sum * eur;
        }
    }
}
