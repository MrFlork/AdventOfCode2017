package net.booru.adventofcode2017;

import java.io.IOException;
import java.util.Arrays;

class Day14
{

    static void run() throws IOException
    {
        test();

        int value = part1("stpzcrnm");

        System.out.println("part1: " + value);
    }

    static int part1(final String input) throws IOException
    {
        int usedTotal = 0;
        for (int i = 0; i < 128; i++)
        {
            final int[] row = makeBinaryRow(input, i);
            usedTotal += Arrays.stream(row).sum();
        }
        return usedTotal;
    }

    static int[] makeBinaryRow(final String input, final int row)
    {
        final String hash = Day10_2.part2(Util.makeSequentialIntArray(256), Day10_2.makeLengthArray(input + "-" + row));

        int[] binary = new int[128];
        for (int i = 0, index = 0; i < hash.length(); i++)
        {
            final String binaryString = makeBinary(hash.charAt(i));
            for (int j = 0; j < binaryString.length(); j++)
            {
                binary[index++] = (binaryString.charAt(j) == '0') ? 0 : 1;
            }
        }

        return binary;
    }

    static String makeBinary(final char hexCharacter)
    {
        final int asInteger = Integer.parseInt("" + hexCharacter, 16);
        return String.format("%4s", Integer.toBinaryString(asInteger)).replaceAll(" ", "0"); // uggly and slow, but simple
    }

    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------

    private static void test() throws IOException
    {
        Util.require(makeBinary('0'), "0000");
        Util.require(makeBinary('1'), "0001");
        Util.require(makeBinary('e'), "1110");
        Util.require(makeBinary('f'), "1111");

        final int[] row0 = Arrays.copyOf(makeBinaryRow("flqrgnkx", 0), 8);
        Util.requireArray(row0, new int[]{1, 1, 0, 1, 0, 1, 0, 0});
        final int[] row1 = Arrays.copyOf(makeBinaryRow("flqrgnkx", 1), 8);
        Util.requireArray(row1, new int[]{0, 1, 0, 1, 0, 1, 0, 1}); //.#.#.#.#
        final int[] row6 = Arrays.copyOf(makeBinaryRow("flqrgnkx", 6), 8);
        Util.requireArray(row6, new int[]{0, 1, 0, 0, 0, 1, 0, 0}); //.#...#..
        final int[] row7 = Arrays.copyOf(makeBinaryRow("flqrgnkx", 7), 8);
        Util.requireArray(row7, new int[]{1, 1, 0, 1, 0, 1, 1, 0}); //##.#.##.

        Util.require(part1("flqrgnkx"), 8108);

        System.out.println("All tests ok");
    }
}
