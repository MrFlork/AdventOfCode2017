package net.booru.adventofcode2017;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class Day1
{
    static void run() throws IOException
    {
        test();
        test2();

        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("day1.in")))
        {
            final int[] data = Util.readIntegersFromChars(bufferedReader);
            final int sum = getSum(data, 1);
            System.out.println("sum1: " + sum);

            final int sum2 = getSum(data, data.length / 2);
            System.out.println("sum2: " + sum2);
        }
    }

    private static int getSum(final int[] data, int offset)
    {
        int sum = 0;
        for (int i = 0; i < data.length; i++)
        {
            if (data[i] == data[(i + offset) % data.length])
            {
                sum += data[i];
            }
        }

        return sum;
    }

    private static void test()
    {
        Util.require(getSum(new int[]{1, 1, 2, 2}, 1), 3);
        Util.require(getSum(new int[]{1, 1, 1, 1}, 1), 4);
        Util.require(getSum(new int[]{1, 2, 3, 4}, 1), 0);
        Util.require(getSum(new int[]{9, 1, 2, 1, 2, 1, 2, 9}, 1), 9);

        System.out.println("All tests ok");
    }

    private static void test2()
    {
        //1212 produces 6: the list contains 4 items, and all four digits match the digit 2 items ahead.
        //1221 produces 0, because every comparison is between a 1 and a 2.
        //123425 produces 4, because both 2s match each other, but no other digit has a match.
        //123123 produces 12.
        //12131415 produces 4.

        Util.require(getSum(new int[]{1, 2, 1, 2}, 2), 6);
        Util.require(getSum(new int[]{1, 2, 2, 1}, 2), 0);
        Util.require(getSum(new int[]{1, 2, 3, 4, 2, 5}, 3), 4);
        Util.require(getSum(new int[]{1, 2, 3, 1, 2, 3}, 3), 12);
        Util.require(getSum(new int[]{1, 2, 1, 3, 1, 4, 1, 5}, 4), 4);

        System.out.println("All tests ok");
    }
}
