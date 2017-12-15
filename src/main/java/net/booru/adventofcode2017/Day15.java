package net.booru.adventofcode2017;

import java.io.IOException;

class Day15
{

    static void run() throws IOException
    {
        test();

        int value = part1(703, 516, 40000000);

        System.out.println("part1: " + value);
    }

    static int part1(final long initA, final long intiB, final int limit) throws IOException
    {
        final long mod = 2147483647;
        final long factorA = 16807;
        final long factorB = 48271;

        long previousA = initA;
        long previousB = intiB;

        int count = 0;
        for (int i = 0; i < limit; i++)
        {
            previousA = (previousA * factorA) % mod;
            previousB = (previousB * factorB) % mod;

            if ((short)(previousA & 0xFFFF) == (short)(previousB & 0xFFFF))
            {
                ++count;
            }
        }

        return count;
    }

    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------

    private static void test() throws IOException
    {
        Util.require(part1(65, 8921, 8), 1);

        System.out.println("All tests ok");
    }
}
