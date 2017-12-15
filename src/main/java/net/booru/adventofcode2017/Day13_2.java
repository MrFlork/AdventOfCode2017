package net.booru.adventofcode2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Day13_2
{
    static void run() throws IOException
    {
        test();

        int value = part2(Files.readAllLines(Paths.get("day13.in")));
        System.out.println("part2: " + value);
    }

    private static int part2(final List<String> lines) throws IOException
    {
        final List<Layer> layers = lines.stream().map(Layer::of).collect(Collectors.toList());

        for (int delay = 0; delay <= 100000000; delay++)
        {
            if (!isCaughtInLayers(layers, delay))
            {
                return delay;
            }
        }

        return -1;
    }

    private static boolean isCaughtInLayers(final List<Layer> layers, final int delay)
    {
        for(Layer layer : layers)
        {
            if (layer.isCaught(delay))
            {
                return true;
            }
        }
        return false;
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

        public boolean isCaught(final int delay)
        {
            // Note that the actual packet position is implicit.
            // The packet position is (when starting at delay=0) the same as the depth of each layer.
            // Adding a delay is just a displacement of the depth.
            return (iDepth + delay) % (2 * (iRange - 1)) == 0;
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

        Util.require(part2(lines), 10);

        System.out.println("All tests ok");
    }
}
