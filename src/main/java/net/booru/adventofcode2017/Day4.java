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
        System.out.println("Valid: " + valid );
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
}
