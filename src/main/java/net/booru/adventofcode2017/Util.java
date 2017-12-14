package net.booru.adventofcode2017;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

class Util
{
    static <T> void require(T value, T expected)
    {
        if (!value.equals(expected))
        {
            throw new AssertionError("test failed: value = " + value + ", expected = " + expected);
        }
    }

    static void requireArray(int[] value, int[] expected)
    {
        if (value.length == expected.length)
        {
            for (int i = 0; i < value.length; i++)
            {
                if(value[i] != expected[i])
                {
                    throw new AssertionError("test failed: value["+i+"] = " + value[i] + ", expected["+i+"] = " + expected[i]);
                }
            }
            return;
        }

        throw new AssertionError("test failed: value.length = " + value.length + ", expected.length = " + expected.length);
    }
    
    public static void requireNot(final int value, final int expected)
    {
        if (value == expected)
        {
            throw new AssertionError("test failed: value = " + value + ", expected != " + expected);
        }
    }

    static int[] readIntegersFromChars(final BufferedReader bufferedReader) throws IOException
    {
        final String line = bufferedReader.readLine();
        if (line == null)
        {
            return null;
        }

        final char[] dataChars = line.toCharArray();
        final int[] data = new int[dataChars.length];
        for (int i = 0; i < dataChars.length; i++)
        {
            data[i] = Character.getNumericValue(dataChars[i]);
        }
        return data;
    }

    static int[] readIntegers(final String fileName, final String separator) throws IOException
    {
        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(fileName)))
        {
            return readIntegers(bufferedReader, separator);
        }
    }
    static int[] readIntegers(final BufferedReader bufferedReader, final String separator) throws IOException
    {
        final String line = bufferedReader.readLine();
        if (line == null)
        {
            return null;
        }

        final String[] words = line.split(separator);
        int[] ints = new int[words.length];
        for (int i = 0; i < words.length; i++)
        {
            ints[i] = Integer.parseInt(words[i]);
        }

        return ints;
    }

    static ArrayList<int[]> readMatrix(final BufferedReader bufferedReader, final String separator) throws IOException
    {
        final ArrayList<int[]> rows = new ArrayList<>();
        int[] data = readIntegers(bufferedReader, separator);
        while(data != null)
        {
            rows.add(data);
            data = readIntegers(bufferedReader, separator);
        }
        return rows;
    }

    public static int[] readLinesAsIntegers(final String fileName) throws IOException
    {
        return Files.readAllLines(Paths.get(fileName)).stream().mapToInt(Integer::parseInt).toArray();
    }

    public static String toString(final int[] intArray)
    {
        final StringBuilder buffer = new StringBuilder(intArray.length);
        for (final int n : intArray)
        {
            buffer.append(n);
        }
        return buffer.toString();
    }

    static int[] makeSequentialIntArray(final int size)
    {
        int[] numbers = new int[size];
        for (int i = 0; i < size; i++)
        {
            numbers[i] = i;
        }
        return numbers;
    }

    static void printMatrix(final int[][] matrix, final int pad)
    {
        for (int i = 0; i < matrix.length; i++)
        {
            for (int j = 0; j < matrix[i].length; j++)
            {
                System.out.print(String.format("%"+pad+"d ", matrix[i][j]));
            }
            System.out.println("");
        }
        System.out.println("");
    }

    private static void printMatrix(final boolean[][] matrix, final int pad)
    {
        for (int i = 0; i < matrix.length; i++)
        {
            for (int j = 0; j < matrix[i].length; j++)
            {
                System.out.print(String.format("%"+pad+"d ", (matrix[i][j] ? 1 : 0) ));
            }
            System.out.println("");
        }
        System.out.println("");
    }
}
