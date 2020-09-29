using System;

namespace Task3_OOP
{
    class Program
    {
        const string options = 
            "1 - UAH to USD\n" +
            "2 - UAH to EUR\n" +
            "3 - USD to UAH\n" +
            "4 - EUR to UAN\n" + 
            "5 - exit";
        static void Main(string[] args)
        {
            Console.WriteLine("USD: ");
            double usd = Convert.ToDouble(Console.ReadLine());
            Console.WriteLine("EUR: ");
            double eur = Convert.ToDouble(Console.ReadLine());

            Converter converter = new Converter(usd, eur);

            Console.WriteLine(options);
            int code = Convert.ToInt32(Console.ReadLine());

            while(code != 5)
            {
                Console.WriteLine("Sum of money: ");
                double sum = Convert.ToDouble(Console.ReadLine());

                switch (code)
                {
                    case 1:
                        Console.WriteLine(converter.UAHToUSD(sum));
                        break;
                    case 2:
                        Console.WriteLine(converter.UAHToEUR(sum));
                        break;
                    case 3:
                        Console.WriteLine(converter.USDToUAH(sum));
                        break;
                    case 4:
                        Console.WriteLine(converter.EURToUAH(sum));
                        break;
                    default:
                        Console.WriteLine("Unknown option.");
                        break;
                }
                Console.WriteLine(options);
                code = Convert.ToInt32(Console.ReadLine());
            }

        }
    }
}
