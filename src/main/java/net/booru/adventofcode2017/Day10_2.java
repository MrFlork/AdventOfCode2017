package net.booru.adventofcode2017;

class Day10_2
{
    // this is really quick and dirty, terrible code, and mutable *ugh*

    static void run()
    {
        test();

        final String input = "106,16,254,226,55,2,1,166,177,247,93,0,255,228,60,36";
        String value = part2(makeInputArray(256), makeLengthArray(input));

        System.out.println("part2: " + value);
    }

    private static String part2(final int[] numbers, int[] lengths)
    {
        final int rounds = 64;
        int currentPosition = 0, skipSize = 0;

        for (int i = 0; i < rounds; i++)
        {
            for (int length : lengths)
            {
                reverseSubArray(numbers, length, currentPosition);

                currentPosition = (currentPosition + length + skipSize) % numbers.length;
                skipSize++;
            }
        }

        final int[] denseHash = getDenseHash(numbers);

        final String s = getHexString(denseHash);
        return s;
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

    private static String getHexString(final int[] denseHash)
    {
        final StringBuilder sb = new StringBuilder(32);
        for (int i : denseHash)
        {
            sb.append(String.format("%02x", i));
        }

        return sb.toString();
    }

    private static int[] getDenseHash(final int[] numbers)
    {
        // groups of 16 are xor:ed
        final int[] denseHash = new int[(int)Math.ceil(numbers.length/16)];
        for (int i = 0; i < denseHash.length; i++)
        {
            denseHash[i] = getXored(numbers, i*16, 16);
        }

        return denseHash;
    }

    private static int getXored(final int[] numbers, final int fromIndex, final int length)
    {
        int xor = numbers[fromIndex];
        final int endIndex = length + fromIndex;
        for (int i = fromIndex + 1; i < endIndex && i < numbers.length; i++)
        {
            xor = xor ^ numbers[i];
        }

        return xor;
    }

    private static int[] makeLengthArray(final String input)
    {
        final int[] postFix = {17, 31, 73, 47, 23};
        int[] numbers = new int[input.length() + postFix.length];
        for (int i = 0; i <input.length() ; i++)
        {
            numbers[i] = input.charAt(i);
        }

        System.arraycopy(postFix, 0, numbers, numbers.length-postFix.length, postFix.length);

        return numbers;
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

        Util.requireArray(makeLengthArray("1,2,3"), new int[]{49,44,50,44,51,17,31,73,47,23});
        Util.require(getXored(new int[]{1,2,3,4}, 0, 4), 4);
        Util.require(getXored(new int[]{0,1,2,3,4}, 1, 4), 4);

        Util.require(part2(makeInputArray(256), makeLengthArray("")), "a2582a3a0e66e6e86e3812dcb672a272");
        Util.require(part2(makeInputArray(256), makeLengthArray("AoC 2017")), "33efeb34ea91902bb2f59c9920caa6cd");
        Util.require(part2(makeInputArray(256), makeLengthArray("1,2,3")), "3efbe78a8d82f29979031a4aa0b16a9d");
        Util.require(part2(makeInputArray(256), makeLengthArray("1,2,4")), "63960835bcdc130f0b66d7ff4f6a5a8e");

        System.out.println("All tests ok");
    }
}
