package net.booru.adventofcode2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

class Day16_2
{
    static void run() throws IOException
    {
        test();

        final String value = part2(16, Files.readAllLines(Paths.get("day16.in")).get(0));

        System.out.println("part2: " + value);
    }

    static String part2(final int programCount, final String dance) throws IOException
    {
        final String[] steps = dance.split(",");

        int mod = getRepeatIndex(steps, programCount);

        final int actualRepetitions = (int)1e9 % mod;

        char[] currentPrograms = Day16.makePrograms(programCount);
        for (int i = 0; i < actualRepetitions; i++)     // the sequence repeats after 30
        {
            currentPrograms = Day16.doSteps(steps, currentPrograms);
        }

        return String.valueOf(currentPrograms, 0, currentPrograms.length);
    }

    static int getRepeatIndex(final String[] steps, final int programCount)
    {
        final char[] initial = Day16.makePrograms(programCount);

        // 1e9 is too large, expect repetitions
        //  We could cache all calculations until it it repeats then get the result at position 1e9 % mod...
        //   but immutable crap needs to be taken into account by copying the arrays and it takes more memory
        //   and since we don't know beforehand how many repetitions we have...
        //
        int mod = 1;
        char[] currentPrograms = Day16.makePrograms(programCount);
        for (; mod <= 1e9; mod++)
        {
            currentPrograms = Day16.doSteps(steps, currentPrograms);

            if (Arrays.equals(currentPrograms, initial))
            {
                break;
            }
        }

        return mod; // indexed from 0
    }

    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------

    static void test() throws IOException
    {
        //Util.require(part1(""), 1);

        System.out.println("All tests ok");
    }
}
