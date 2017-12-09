package net.booru.adventofcode2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Day9_2
{
    // this is really quick and dirty, terrible code, and mutable *ugh*

    static void run() throws IOException
    {
        test();

        int value = part2(Files.readAllLines(Paths.get("day9.in")).get(0));

        System.out.println("part2: " + value);
    }

    private static int part2(final String line) throws IOException
    {
        final LinkedList<Character> characterStream = getCharacters(line);
        return getGarbageCount(characterStream);
    }

    private static int getGarbageCount(final LinkedList<Character> characterStream)
    {
        int counter = 0;
        boolean isCounting = false;
        for (Character character : characterStream)
        {
            switch (character)
            {
                case '<':
                    isCounting = true;
                    counter++;
                    break;

                case '>':
                    isCounting = false;
                    counter--;
                    break;

                default:
                    counter += isCounting ? 1 : 0;
            }
        }
        
        return counter;
    }

    private static LinkedList<Character> getCharacters(final String line)
    {
        final String cleanedIgnore = line.replaceAll("!.", "");

        return cleanedIgnore.chars()
                            .mapToObj(c -> ((char)c))
                            .collect(Collectors.toCollection(LinkedList::new));
    }

    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------

    private static void test() throws IOException
    {
        Util.require(part2("<>"), 0);
        Util.require(part2("<random characters>"), 17);
        Util.require(part2("<<<<>"), 3);
        Util.require(part2("<{!>}>"), 2);
        Util.require(part2("<!!>"), 0);
        Util.require(part2("<!!!>>"), 0);
        Util.require(part2("<{o\"i!a,<{i<a>"), 10);

        System.out.println("All tests ok");
    }
}