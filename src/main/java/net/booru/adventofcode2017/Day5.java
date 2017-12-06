package net.booru.adventofcode2017;

import java.io.IOException;

class Day5
{
    static void run() throws IOException
    {
        test();

        int steps = escape(Util.readLinesAsIntegers("day5.in"));
        System.out.println("Steps: " + steps);

        int steps2 = escape2(Util.readLinesAsIntegers("day5.in"));
        System.out.println("Steps: " + steps2);
    }

    private static int escape(final int[] maze)
    {
        int steps = 0;
        int cursor = 0;
        while (cursor < maze.length)
        {
            final int offset = maze[cursor];
            maze[cursor] += 1;
            cursor = cursor + offset;
            steps++;
        }

        return steps;
    }

    private static int escape2(final int[] maze)
    {
        int steps = 0;
        int cursor = 0;
        while (cursor < maze.length)
        {
            final int offset = maze[cursor];
            maze[cursor] += offset >= 3 ? -1 : 1;
            cursor = cursor + offset;
            steps++;
        }

        return steps;
    }

    private static void test()
    {
        Util.require(escape(new int[]{0,3,0,1,-3}), 5);
        Util.require(escape(new int[]{1,0,2,5,1}), 5);

        Util.require(escape2(new int[]{0,3,0,1,-3}), 10);

        System.out.println("All tests ok");
    }

}
