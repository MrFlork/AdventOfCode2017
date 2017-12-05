package net.booru.adventofcode2017;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static net.booru.adventofcode2017.Util.readMatrix;
import static net.booru.adventofcode2017.Util.require;

class Day2
{
    static void run() throws IOException
    {
        test();

        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("day2.in")))
        {
            final ArrayList<int[]> rows = readMatrix(bufferedReader, "\t");

            final int sum = rows.stream().map(Day2::getChecksum).reduce(0, (a, b) -> a + b);
            System.out.println("checksum: " + sum);

            final int sumOfDivisible = rows.stream().map(Day2::getDivisible).reduce(0, (a, b) -> a + b);
            System.out.println("sumDivisible: " + sumOfDivisible);
        }
    }

    private static int getChecksum(final int[] row)
    {
        int max = row[0], min = row[0];
        for (int i = 1; i < row.length; i++)
        {
            max = Math.max(max, row[i]);
            min = Math.min(min, row[i]);
        }

        return max - min;
    }

    private static int getDivisible(final int[] row)
    {
        for (int i = 0; i < row.length - 1; i++)
        {
            for (int j = i+1; j < row.length; j++)
            {
                final double a = row[i];
                final double b = row[j];;
                if ((a/b) % 1.0 == 0)
                {
                    return (int)(a / b);
                }
                if ((b/a) % 1.0 == 0)
                {
                    return (int)(b / a);
                }
            }
        }

        throw new IllegalStateException();
    }

    private static void test() throws IOException
    {
        //5 1 9 5
        //7 5 3
        //2 4 6 8

        require(getChecksum(new int[]{5,1,9,5}), 8);
        require(getChecksum(new int[]{7,5,3}), 4);
        require(getChecksum(new int[]{2,4,6,8}), 6);

        final ArrayList<int[]> rows = new ArrayList<>();
        rows.add(new int[]{5,1,9,5});
        rows.add(new int[]{7,5,3});
        rows.add(new int[]{2,4,6,8});

        require(rows.stream().map(Day2::getChecksum).reduce(0, (a, b) -> a + b), 18);

        System.out.println("All tests ok");
    }

}
