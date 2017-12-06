package net.booru.adventofcode2017;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

class Util
{
    static void require(int value, int expected)
    {
        if (value != expected)
        {
            throw new AssertionError("test failed: value = " + value + ", expected = " + expected);
        }
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

}
