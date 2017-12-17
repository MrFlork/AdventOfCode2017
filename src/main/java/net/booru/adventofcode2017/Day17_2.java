package net.booru.adventofcode2017;

class Day17_2
{
    static void run()
    {
        //test();

        final int value = part2(371, 50000000);

        System.out.println("part1: " + value);
    }

    static int part2(final int stepsPerInsert, final int numberCount)
    {
        // looking for value at index 1... so we have at least two elements
        // start at state [0, (1)]
        int size = 2;
        int currentPosition = 1;
        int valueAtIndex1 = 1;
        
        for (int i = 2; i < numberCount; i++)
        {
            final int insertPoint = (currentPosition + stepsPerInsert) % size + 1;
            if (insertPoint == 1)
            {
                valueAtIndex1 = i;
                System.out.println(i);
            }

            currentPosition = insertPoint;
            ++size;
        }

        return valueAtIndex1;
    }

    //--------------------------------------------------------------------------------------------------
    //---------------------------;-----------------------------------------------------------------------

    private static void test()
    {
        Util.require(part2(3, 2018), 1226);  // from part1

        System.out.println("All tests ok");
    }
}
