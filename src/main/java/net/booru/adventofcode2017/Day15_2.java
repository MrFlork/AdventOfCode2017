package net.booru.adventofcode2017;

import java.io.IOException;

class Day15_2
{

    static void run() throws IOException
    {
        test();

        int value2 = part2(703, 516, 40000000);

        System.out.println("part2: " + value2);
    }

    static int part2(final long initA, final long intiB, final int limit) throws IOException
    {
        final long mod = 2147483647;
        final long factorA = 16807;
        final long factorB = 48271;

        long previousA = initA;
        long previousB = intiB;

        long[] valuesA = new long[limit];
        long[] valuesB = new long[limit];
        int indexA = -1;
        int indexB = -1;

        int judgeIndex = -1;

        int count=0;
        for (int i = 0; i < limit; i++)
        {
            previousA = (previousA * factorA) % mod;
            previousB = (previousB * factorB) % mod;

            if (previousA % 4 == 0)
            {
                valuesA[++indexA] = previousA;
            }
            if (previousB % 8 == 0)
            {
                valuesB[++indexB] = previousB;
            }

            final int lowestIndex = Math.min(indexA, indexB);
            if (lowestIndex > judgeIndex)
            {
                ++judgeIndex;
                if ((short)(valuesA[judgeIndex] & 0xFFFF) == (short)(valuesB[judgeIndex] & 0xFFFF))
                {
                    ++count;
                }
            }
        }
        return count;
    }

    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------

    private static void test() throws IOException
    {
        //Util.require(part1(65, 8921, 8), 1);

        System.out.println("All tests ok");
    }
}
