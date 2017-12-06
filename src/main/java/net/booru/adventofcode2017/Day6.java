package net.booru.adventofcode2017;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;

class Day6
{
    static void run() throws IOException
    {
        test();

        int cycles = reAllocate(Util.readIntegers("day6.in", "\t"), 0);
        System.out.println("Cycles: " + cycles);

        int cycles2 = reAllocate(Util.readIntegers("day6.in", "\t"), 1);
        System.out.println("Cycles2: " + cycles2);
    }

    private static int reAllocate(final int[] memoryBanks, int allowedLoops)
    {
        final HashSet<String> seenStates = new HashSet<>(10000);
        seenStates.add(Arrays.toString(memoryBanks));

        int cycles = 0;
        while (true)
        {
            cycles++;
            redistribute(memoryBanks); // side-effect in-mem redistribute

            final String state = Util.toString(memoryBanks);
            if (seenStates.contains(state))
            {
                if (allowedLoops <= 0)
                {
                    return cycles;
                }
                else
                {
                    return loopUntil(state, memoryBanks);
                }
            }

            seenStates.add(state);
        }
    }

    private static int loopUntil(final String targetState, final int[] memoryBanks)
    {
        int cycles = 0;
        while (true)
        {
            cycles++;
            redistribute(memoryBanks); // side-effect in-mem redistribute

            final String state = Util.toString(memoryBanks);
            if (targetState.equals(state))
            {
                return cycles;
            }
        }
    }

    private static void redistribute(final int[] memoryBanks)
    {
        int maxIndex = 0;
        for (int i = 0; i < memoryBanks.length; i++)
        {
            if (memoryBanks[i] > memoryBanks[maxIndex])
            {
                maxIndex = i;
            }
        }

        int blocks = memoryBanks[maxIndex];
        memoryBanks[maxIndex] = 0;
        
        int nextIndex = (maxIndex + 1) % memoryBanks.length;
        for (int i=nextIndex; blocks-->0; i++)
        {
            memoryBanks[i % memoryBanks.length] += 1;
        }
    }

    private static void test()
    {
        Util.require(reAllocate(new int[]{0, 2, 7, 0}, 0), 5);
        Util.require(reAllocate(new int[]{0, 2, 7, 0}, 1), 4);

        System.out.println("All tests ok");
    }

}
