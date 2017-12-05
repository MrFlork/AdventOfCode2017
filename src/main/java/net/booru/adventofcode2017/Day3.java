package net.booru.adventofcode2017;

import java.util.HashMap;

class Day3
{
    private static final boolean DEBUG = false;

    static void run()
    {
        test();
        test2();

        final int distance = getSteps(312051);
        System.out.println("Distance: " + distance);

        final int largerSum = getFirstLargerSum(312051);
        System.out.println("LargerSum: " + largerSum);
    }

    /**
     * Part 1
     * @param number
     * @return
     */
    private static int getSteps(final int number)
    {
        if (number == 1)
        {
            return 0;
        }

        // Each new layer is two sides, define the transitions to the previous layer
        //  e.g. the next layer after 1 is 2,3,4 where 2 is the right side, 3 is the middleCorner and 4 is the top side
        //
        final int sideSize = (int)Math.ceil(Math.sqrt(number));
        final int layerMax = sideSize * sideSize;
        final int layerMin = layerMax - 2 * sideSize + 2;
        final int layerCorner = (layerMax - layerMin) / 2 + layerMin;
        final int sideMiddleLow = (int)Math.floor((layerCorner - layerMin) / 2.0) + layerMin;
        final int sideMiddleHigh = layerMax - (int)Math.floor((layerMax - layerCorner) / 2.0);

        if (number == sideMiddleLow || number == sideMiddleHigh)
        {
            // at a mid-side point, we are done.
            return sideSize % 2 == 0 ? sideSize / 2 : (sideSize - 1) / 2; // even and odd layers have the same distance to 1 (layer 2 (of size2) and 3 etc.)
        }
        else if (number == layerMax || number == layerMin || number == layerCorner)
        {
            return 1 + getSteps(number - 1);
        }
        else
        {
            // find distance to closes middle point
            final int distanceToMiddle = Math.min(Math.abs(sideMiddleHigh - number), Math.abs(sideMiddleLow - number));
            return distanceToMiddle + getSteps(sideMiddleLow);
        }
    }

    /**
     * Part 2
     * @param target
     * @return
     */
    private static int getFirstLargerSum(int target)
    {
        // All sums up to the sum we seek must be computed... so, iterate and cache.
        //
        final HashMap<IntPair, IntPair> mutableMatrix = new HashMap<>(1024);
        mutableMatrix.put(new IntPair(0, 0), new IntPair(1, 1)); // 1
        display(mutableMatrix);
        int position = 2;
        int x = 1;
        int y = 0;

        while (true)
        {
            final int sideSize = (int)Math.ceil(Math.sqrt(position));

            // same x-coordinate
            final int yDirection = y > 0 ? -1 : 1;
            for (int i = 0; i < sideSize - 1; i++)
            {
                final Integer sum = getAndCacheSum(x, y, position, target, mutableMatrix);
                if (sum != null)
                {
                    return sum;
                }

                ++position;
                y += yDirection;
            }

            // same y-coordinate
            final int xDirection = x > 0 ? -1 : 1;
            for (int i = 0; i < sideSize; i++)
            {
                final Integer sum = getAndCacheSum(x, y, position, target, mutableMatrix);
                if (sum != null)
                {
                    return sum;
                }

                ++position;
                x += xDirection;
            }
        }
    }

    private static Integer getAndCacheSum(final int x, final int y, final int position, final int target,
                                          final HashMap<IntPair, IntPair> mutableMatrix)
    {
        final IntPair key = new IntPair(x, y);
        final int sum = getSum(key, position, mutableMatrix);

        mutableMatrix.put(key, new IntPair(position, sum));
        display(mutableMatrix);

        if (sum > target)
        {
            return sum;
        }

        return null;
    }

    private static int getSum(final IntPair key, final int position, final HashMap<IntPair, IntPair> matrix)
    {
        // simple approach: look up all neighbors, only use non-null and with smaller position
        final int x = key.iFirst;
        final int y = key.iSecond;

        int sum = 0;
        for (int dy = -1; dy <= 1; dy++)
        {
            for (int dx = -1; dx <= 1; dx++)
            {
                final IntPair neighbourKey = new IntPair(x + dx, y + dy);
                final IntPair neighbour = matrix.get(neighbourKey);
                if (neighbour != null && neighbour.iFirst < position)
                {
                    sum += neighbour.iSecond;
                }
            }
        }

        Util.requireNot(sum, 0);
        return sum;
    }

    private static void display(final HashMap<IntPair, IntPair> matrix)
    {
        if (!DEBUG)
        {
            return;
        }

        int minX = 100000;
        int minY = 100000;
        int maxX = -100000;
        int maxY = -100000;
        for (IntPair intPair : matrix.keySet())
        {
            maxX = Math.max(intPair.iFirst, maxX);
            maxY = Math.max(intPair.iSecond, maxY);
            minX = Math.min(intPair.iFirst, minX);
            minY = Math.min(intPair.iSecond, minY);
        }

        for (int y = maxY + 1; y >= minX - 1; --y)
        {
            final StringBuilder line = new StringBuilder((maxX - minX) * 10);
            for (int x = minX - 1; x <= maxX + 1; x++)
            {
                final IntPair entry = matrix.get(new IntPair(x, y));
                if (entry == null)
                {
                    line.append("[        ]");
                }
                else
                {
                    final Integer sum = entry.iSecond;
                    line.append(String.format("[%8d]", sum));
                }
                line.append(' ');
            }
            System.out.println(line);
        }
        System.out.println("");
        System.out.println("--------------------------------");
    }

    static void test2()
    {
        //    147  142  133  122   59
        //    304    5    4    2   57
        //    330   10    1    1   54
        //    351   11   23   25   26
        //    362  747  806--->   ...

        Util.require(getFirstLargerSum(1), 2);
        Util.require(getFirstLargerSum(5), 10);
        Util.require(getFirstLargerSum(747), 806);

    }

    static void test()
    {
        // 37  36   35  34  33   32   31
        //     ---------------------|
        // 38  17 | 16  15  14   13 | 30
        //        | -----------|    |
        // 39  18 |  5   4   3 | 12 | 29
        //        |            |    |
        // 40  19 |  6   1   2 | 11 | 28
        //        |            |    |
        // 41  20 |  7   8   9 | 10 | 27
        //        |---------------  |
        // 42  21   22  23  24   25 | 26
        //
        // 43  44   45  46  47  48    49

        Util.require(getSteps(1), 0);

        Util.require(getSteps(2), 1);
        Util.require(getSteps(3), 2);
        Util.require(getSteps(4), 1);

        Util.require(getSteps(5), 2);
        Util.require(getSteps(6), 1);
        Util.require(getSteps(7), 2);
        Util.require(getSteps(8), 1);
        Util.require(getSteps(9), 2);

        Util.require(getSteps(10), 3);
        Util.require(getSteps(11), 2);
        Util.require(getSteps(12), 3);
        Util.require(getSteps(13), 4);
        Util.require(getSteps(14), 3);
        Util.require(getSteps(15), 2);
        Util.require(getSteps(16), 3);

        Util.require(getSteps(17), 4);
        Util.require(getSteps(18), 3);
        Util.require(getSteps(19), 2);
        Util.require(getSteps(20), 3);
        Util.require(getSteps(21), 4);
        Util.require(getSteps(22), 3);
        Util.require(getSteps(23), 2);
        Util.require(getSteps(24), 3);
        Util.require(getSteps(25), 4);

        Util.require(getSteps(35), 4);
        Util.require(getSteps(48), 5);

        System.out.println("All tests ok");
    }

}
