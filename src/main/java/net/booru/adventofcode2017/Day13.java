package net.booru.adventofcode2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Day13
{
    static void run() throws IOException
    {
        test();

        int value = part1(Files.readAllLines(Paths.get("day13.in")));

        System.out.println("part1: " + value);
    }

    private static int part1(final List<String> lines) throws IOException
    {
        final List<Layer> layers = lines.stream().map(Layer::of).collect(Collectors.toList());
        int severitySum = layers.stream().mapToInt(Layer::getSeverity).sum();

        return severitySum;
    }

    static class Layer
    {
        final int iDepth;
        final int iRange;

        public Layer(final int depth, final int range)
        {
            iDepth = depth;
            iRange = range;
        }

        public int getSeverity()
        {
            return isCaught() ? iDepth * iRange : 0;
        }

        public boolean isCaught()
        {
            // Note that the actual packet position is implicit. The packet position
            //  is the same as the depth of each layer.
            // 2 * (range -1) is the number of steps it takes to go from 0 and back
            // eg: range=4  gives 6 positions : 0,1,2,3,2,1
            return iDepth % (2 * (iRange - 1)) == 0;
        }

        public static Layer of(final String input)
        {
            final String[] parts = input.split(": ");
            return new Layer(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
        }
    }

    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------

    private static void test() throws IOException
    {
        final List<String> lines = Arrays.asList("0: 3", "1: 2", "4: 4", "6: 4");

        Util.require(part1(lines), 24);

        System.out.println("All tests ok");
    }
}
