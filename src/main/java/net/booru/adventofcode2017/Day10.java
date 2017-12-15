package net.booru.adventofcode2017;

class Day10
{
    // this is really quick and dirty, terrible code, and mutable *ugh*

    static void run()
    {
        test();

        int value = part1(makeInputArray(256), 106,16,254,226,55,2,1,166,177,247,93,0,255,228,60,36);

        System.out.println("part1: " + value);
    }

    private static int part1(final int[] numbers, int... lengths)
    {
        int currentPosition = 0, skipSize = 0;

        for (int length : lengths)
        {
            reverseSubArray(numbers, length, currentPosition);

            currentPosition = (currentPosition + length + skipSize) % numbers.length;
            skipSize++;
        }

        return numbers[0] * numbers[1];
    }

    private static void reverseSubArray(final int[] numbers, final int length, final int fromIndex)
    {
        if (length <= 1)
        {
            return;
        }

        final int half = length / 2;
        for (int i = 0; i < half; i++)
        {
            final int indexFromLeft = (fromIndex + i) % numbers.length;
            final int indexFromRight = (fromIndex + length - 1 - i) % numbers.length;

            final int tmp = numbers[indexFromLeft];
            numbers[indexFromLeft] = numbers[indexFromRight];
            numbers[indexFromRight] = tmp;
        }
    }

    private static int[] makeInputArray(final int size)
    {
        int[] numbers = new int[size];
        for (int i = 0; i < size; i++)
        {
            numbers[i] = i;
        }
        return numbers;
    }


    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------

    private static void test()
    {
        Util.require(part1(makeInputArray(5), 3, 4, 1, 5), 12);

        System.out.println("All tests ok");
    }
}
