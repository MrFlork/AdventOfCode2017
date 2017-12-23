package net.booru.adventofcode2017;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        if (args.length == 0)
        {
            Day23.run();
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
                case "11"   : Day11.run(); break;
                case "12"   : Day12.run(); break;
                case "13"   : Day13.run(); break;
                case "13_2" : Day13_2.run(); break;
                case "14"   : Day14.run(); break;
                case "14_2" : Day14_2.run(); break;
                case "15"   : Day15.run(); break;
                case "15_2" : Day15_2.run(); break;
                case "16"   : Day16.run(); break;
                case "16_2" : Day16_2.run(); break;
                case "17"   : Day17.run(); break;
                case "17_2" : Day17_2.run(); break;
                case "18"   : Day18.run(); break;
                case "18_2" : Day18_2.run(); break;
                case "19"   : Day19.run(); break;
                case "19_2" : Day19_2.run(); break;
                case "20"   : Day20.run(); break;
                case "20_2" : Day20_2.run(); break;
                case "21"   : Day21.run(1); break;
                case "21_2" : Day21.run(2); break;
                case "21_3" : Day21.run(3); break;
                case "21_4" : Day21.run(4); break;
                case "21_5" : Day21.run(5); break;
                case "22"   : Day22.run(); break;
                case "22_2" : Day22_2.run(); break;
                case "23"   : Day23.run(); break;
            }
        }
    }
}


