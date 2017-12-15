package net.booru.adventofcode2017;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        if (args.length == 0)
        {
            Day10.run();
            Day10_2.run();
        }
        else
        {
            switch (args[0])
            {
                case "1"    : Day1.run(); break;
                case "2"    : Day2.run(); break;
                case "3"    : Day3.run(); break;
                case "4"    : Day4.run(); break;
                case "5"    : Day5.run(); break;
                case "6"    : Day6.run(); break;
                case "7"    : Day7.run(); break;
                case "8"    : Day8.run(); break;
                case "9"    : Day9.run(); break;
                case "9_2"  : Day9_2.run(); break;
                case "10"   : Day10.run(); break;
                case "10_2" : Day10_2.run(); break;
            }
        }
    }
}


