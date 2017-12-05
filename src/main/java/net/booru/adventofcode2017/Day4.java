package net.booru.adventofcode2017;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;

class Day4
{
    static void run() throws IOException
    {
        int valid = countValid();
        System.out.println("Valid: " + valid);

        int valid2 = countValid2();
        System.out.println("Valid2: " + valid2);
    }

    private static int countValid() throws IOException
    {
        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("day4.in")))
        {
            int valid = 0;
            for(String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine())
            {
                final String[] words = line.split(" ");
                final HashSet<String> unique = new HashSet<>(words.length*2);
                unique.addAll(Arrays.asList(words));
                valid += unique.size() == words.length ? 1 : 0;
            }

            return valid;
        }
    }

    private static int countValid2() throws IOException
    {
        try (final BufferedReader bufferedReader = Files.newBufferedReader(Paths.get("day4.in")))
        {
            int valid = 0;
            for(String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine())
            {
                final HashSet<OrderedWord> unique = new HashSet<>(10000);
                final String[] words = line.split(" ");
                Arrays.stream(words).map(OrderedWord::new).forEach(unique::add);

                valid += unique.size() == words.length ? 1 : 0;
            }

            return valid;
        }
    }

    private static class OrderedWord
    {
        private final String iWord;

        public OrderedWord(final String word)
        {
            final char[] lettersInOrder = word.toCharArray();
            Arrays.sort(lettersInOrder);
            iWord = String.valueOf(lettersInOrder);
        }

        @Override
        public boolean equals(final Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (!(o instanceof OrderedWord))
            {
                return false;
            }

            final OrderedWord that = (OrderedWord)o;

            return iWord.equals(that.iWord);
        }

        @Override
        public int hashCode()
        {
            return iWord.hashCode();
        }
    }
}
