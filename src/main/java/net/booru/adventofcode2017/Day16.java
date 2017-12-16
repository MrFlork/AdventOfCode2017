package net.booru.adventofcode2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class Day16
{

    static void run() throws IOException
    {
        test();

        final String value = part1(16, Files.readAllLines(Paths.get("day16.in")).get(0));

        System.out.println("part1: " + value);
    }

    static String part1(final int programCount, final String dance) throws IOException
    {
        char[] programs = makePrograms(programCount);

        final String[] steps = dance.split(",");
        programs = doSteps(steps, programs);

        return String.valueOf(programs, 0, programs.length);
    }

    static char[] doSteps(final String[] steps, char[] programs)
    {
        for (String step : steps)
        {
            final char[] nextPrograms;
            final char action = step.charAt(0);
            final String argument = step.substring(1, step.length());
            switch (action)
            {
                case 's':
                    nextPrograms = spin(Integer.parseInt(argument), programs);
                    break;
                case 'x':
                    final String[] positions = argument.split("/");
                    exchange(Integer.parseInt(positions[0]), Integer.parseInt(positions[1]), programs);  // mutable programs

                    nextPrograms = programs;
                    break;
                case 'p':
                    final String[] names = argument.split("/");
                    partner(names[0].charAt(0), names[1].charAt(0), programs); // mutable programs

                    nextPrograms = programs;
                    break;
                default: throw new IllegalStateException();
            }

            programs = nextPrograms;
        }
        return programs;
    }

    static char[] makePrograms(final int programCount)
    {
        final char[] programs = new char[programCount];
        for (int i = 0; i < programCount; i++)
        {
            programs[i] = (char)('a' + i);
        }

        return programs;
    }

    static char[] spin(final int steps, final char[] programs)
    {
        char[] a = new char[programs.length];
        int index = steps;
        for (char program : programs)
        {
            a[index++ % programs.length] = program;
        }
        
        return a;
    }

    static void exchange(final int position1, final int position2, final char[] programs)
    {
        char tmp = programs[position1];
        programs[position1] = programs[position2];
        programs[position2] = tmp;
    }

    static void partner(final char programA, final char programB, final char[] programs)
    {
        int indexA = getIndex(programA, programs);
        int indexB = getIndex(programB, programs);
        exchange(indexA, indexB, programs);
    }

    static int getIndex(final char program, final char[] programs)
    {
        for (int i = 0; i < programs.length; i++)
        {
            if (programs[i] == program)
                return i;
        }
        
        return -1;
    }

    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------

    private static void test() throws IOException
    {
        //Util.require(part1(""), 1);

        System.out.println("All tests ok");
    }
}
