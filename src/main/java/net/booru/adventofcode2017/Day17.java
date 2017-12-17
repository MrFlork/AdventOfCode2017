package net.booru.adventofcode2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class Day17
{

    static void run() throws IOException
    {
        //test();

        final int value = part1(371);

        System.out.println("part1: " + value);
    }

    static int part1(final int stepsPerInsert)
    {
        final List<Integer> numbers = new ArrayList<>(2018);
        numbers.add(0);
        int currentPosition = 0;

        for (int i = 1; i < 2018; i++)
        {
            final int insertPoint = (currentPosition + stepsPerInsert) % numbers.size() + 1;
            currentPosition = insertPoint;
            numbers.add(insertPoint, i);
        }

        return numbers.get((currentPosition + 1) % numbers.size());
    }

    //--------------------------------------------------------------------------------------------------
    //---------------------------;-----------------------------------------------------------------------

    private static void test() throws IOException
    {
        Util.require(part1(3), 638);

        System.out.println("All tests ok");
    }
}
