package net.booru.adventofcode2017;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        if (args.length == 0)
        {
            Day3.run();
        }
        else
        {
            switch (args[0])
            {
                case "1"    : Day1.run(); break;
                case "2"    : Day2.run(); break;
                case "3"    : Day3.run(); break;
            }
        }
    }
}


