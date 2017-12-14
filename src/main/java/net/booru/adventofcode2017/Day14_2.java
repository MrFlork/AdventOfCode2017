package net.booru.adventofcode2017;

import java.io.IOException;

class Day14_2
{
    static void run() throws IOException
    {
        test();

        int value = part2("stpzcrnm");

        System.out.println("part2: " + value);
    }

    static int part2(final String input) throws IOException
    {
        final int[][] matrix = new int[128][128];
        for (int row = 0; row < 128; row++)
        {
            matrix[row] = makeBinaryRow(input, row);
        }

        return countRegions(matrix);
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
        // Alternatively using streams...
        //    int[] binary = IntStream.range(0, hash.length()).flatMap((int i) ->
        //                   {
        //                       final String binaryString = makeBinary(hash.charAt(i));
        //                       return IntStream.range(0, binaryString.length())
        //                                       .map((int n) -> (binaryString.charAt(n) == '0') ? 0 : 1);
        //                   }).toArray();
        return binary;
    }

    static String makeBinary(final char hexCharacter)
    {
        final int asInteger = Integer.parseInt("" + hexCharacter, 16);
        return String.format("%4s", Integer.toBinaryString(asInteger)).replaceAll(" ", "0"); // uggly and slow, but simple
    }

    static int countRegions(final int[][] matrix)
    {
        final int dimension = matrix.length; // assumes square matrix
        final boolean[][] visited = new boolean[dimension][dimension];
        
        int regionCount = 0;
        for (int row = 0; row < dimension; row++)
        {
            for (int column = 0; column < dimension; column++)
            {
                if (matrix[row][column] == 1)
                {
                    regionCount += findConnectedOnesDFS(column, row, matrix, visited, false) ? 1 : 0;
                }
            }
        }

        return regionCount;
    }

    static boolean findConnectedOnesDFS(int column, int row, final int[][] matrix, final boolean[][] visited, final boolean isFound)
    {
        // make sure row and col are inside the image
        final boolean isOutside = row < 0 || column < 0 || row >= matrix.length || column >= matrix.length;
        if (isOutside || visited[row][column] || matrix[row][column] == 0) // only count regions of ONEs
        {
            return isFound;
        }

        visited[row][column] = true;

        // must visit all directions to mark the entire region...
        final boolean above = findConnectedOnesDFS(column, row - 1, matrix, visited, true);
        final boolean below = findConnectedOnesDFS(column, row + 1, matrix, visited, true);
        final boolean left = findConnectedOnesDFS(column - 1, row, matrix, visited, true);
        final boolean right = findConnectedOnesDFS(column + 1, row, matrix, visited, true);

        return above || below || left || right;
    }

    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------

    private static void test() throws IOException
    {
        int[][] matrix = new int[][]{
            new int[]{1, 1, 0, 0, 0},
            new int[]{0, 1, 0, 1, 1},
            new int[]{0, 0, 1, 1, 1},
            new int[]{1, 1, 0, 1, 1},
            new int[]{1, 1, 0, 1, 0}
        };
        Util.printMatrix(matrix, 3);

        Util.require(countRegions(matrix), 3);
        Util.require(part2("flqrgnkx"), 1242);

        System.out.println("All tests ok");
    }

}
