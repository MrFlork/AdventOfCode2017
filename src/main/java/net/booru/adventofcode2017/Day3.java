package net.booru.adventofcode2017;


class Day3
{
    static void run()
    {
        test();

        final int distance = getSteps(312051);
        System.out.println("Distance: " + distance);
    }

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
            return sideSize % 2 == 0 ? sideSize/2 : (sideSize-1) / 2; // even and odd layers have the same distance to 1 (layer 2 (of size2) and 3 etc.)
        }
        else
        if (number == layerMax || number == layerMin || number == layerCorner)
        {
            return 1 + getSteps(number-1);
        }
        else
        {
            // find distance to closes middle point
            final int distanceToMiddle = Math.min(Math.abs(sideMiddleHigh - number), Math.abs(sideMiddleLow - number));
            return distanceToMiddle + getSteps(sideMiddleLow);
        }
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
