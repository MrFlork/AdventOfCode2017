package net.booru.adventofcode2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Day9
{
    // this is really quick and dirty, terrible code, and mutable *ugh*

    static void run() throws IOException
    {
        test();

        int value = part1(Files.readAllLines(Paths.get("day9.in")).get(0));

        System.out.println("part1: " + value);
        System.out.println("part2: ");
    }

    private static int part1(final String line) throws IOException
    {
        final LinkedList<Character> characterStream = getCharacters(line);

        LinkedList<Integer> values = getValues(characterStream, 1, new LinkedList<>());
        return values.stream().mapToInt(i->i).sum();
    }

    private static LinkedList<Integer> getValues(final LinkedList<Character> characters, final int currentScore, final LinkedList<Integer> values)
    {
        if (characters.isEmpty())
        {
            return values;
        }

        final char c = characters.removeFirst();
        switch (c)
        {
            case '{':
                values.add(currentScore);
                return getValues(characters, currentScore+1, values);
            case '}': return getValues(characters, currentScore-1, values);
            case ',': return getValues(characters, currentScore, values);

            default: throw new IllegalStateException();
        }
    }

    private static LinkedList<Character> getCharacters(final String line)
    {
        final String cleanedIgnore = line.replaceAll("!.", "");
        final String cleanedIrrelevant = cleanedIgnore.replaceAll("[^{|^}|^,|^<|^>]","")
                                                      .replaceAll("<(.*?)>", "")
                                                      .replaceAll(Pattern.quote("{,"), "{")
                                                      .replaceAll(Pattern.quote(",}"), "}");

        return cleanedIrrelevant.chars()
                                .mapToObj(c -> ((char)c))
                                .collect(Collectors.toCollection(LinkedList::new));
    }

    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------

    private static void test() throws IOException
    {
        Util.require(part1("{}"), 1);
        Util.require(part1("{<ab>}"), 1);
        Util.require(part1("{<a>,<a>,<a>,<a>,{},<b>,{},<c>}"), 5);
        Util.require(part1("{<<<>}"), 1);
        Util.require(part1("{<<<>,{}}"), 3);
        Util.require(part1("{<ab!>>}"), 1);
        Util.require(part1("{}"), 1);
        Util.require(part1("{{{}}}"), 6);
        Util.require(part1("{{},{}}"), 5);
        Util.require(part1("{{{},{},{{}}}}"), 16);
        Util.require(part1("{<a>,<a>,<a>,<a>}"), 1);
        Util.require(part1("{<a>,{},<a>,{},<a>}"), 5);
        Util.require(part1("{{<ab>},{<ab>},{<ab>},{<ab>}}"), 9);
        Util.require(part1("{{<!!>},{<!!>},{<!!>},{<!!>}}"), 9);
        Util.require(part1("{{<a!>},{<a!>},{<a!>},{<ab>}}"), 3);

        System.out.println("All tests ok");
    }
}
