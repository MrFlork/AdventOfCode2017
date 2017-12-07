package net.booru.adventofcode2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;

class Day7
{
    // this is really quick and dirty, terrible code, and mutable *ugh*

    static void run() throws IOException
    {
        test();

        final Program bottom = getHeaviest(getProgramsFromFile());
        System.out.println("name: " + bottom.iName);
    }

    static class Program
    {
        final String iName;
        int iWeight;
        final ArrayList<String> iProgramsAbove;

        private Program(final String name, final int weight, final ArrayList<String> programsAbove)
        {
            iName = name;
            iWeight = weight;
            iProgramsAbove = programsAbove;
        }

        static Program of(final String data)
        {
            final String[] split = data.split(" ");

            final String name = split[0];
            final int weight = Integer.parseInt(split[1].substring(1, split[1].length() - 1));

            final String[] possibleAbove = data.split("->");
            if (possibleAbove.length > 1)
            {
                final ArrayList<String> above =
                    Arrays.stream(possibleAbove[1].split(",")).map(String::trim).collect(Collectors.toCollection(ArrayList::new));

                return new Program(name, weight, above);
            }

            return new Program(name, weight, new ArrayList<>(0));
        }

        public ArrayList<Program> getProgramsAbove(final HashMap<String, Program> programsMap)
        {
            return iProgramsAbove.stream().map(programsMap::get).collect(Collectors.toCollection(ArrayList::new));
        }

        @Override
        public String toString()
        {
            return "" + iName + " (" + iWeight + ") " + iProgramsAbove.toString();
        }
    }

    private static Program getHeaviest(final ArrayList<Program> programs)
    {
        final HashMap<String, Program> programsMap = new HashMap<>(5000);
        programs.forEach(program -> programsMap.put(program.iName, program));

        final int totalWeight = findSubWeight(programs, programsMap); // mutable programs

        final Optional<Program> max = programs.stream().max(Comparator.comparing(program -> program.iWeight));

        return max.get();
    }

    private static int findSubWeight(final ArrayList<Program> programs, final HashMap<String, Program> programsMap)
    {
        if (programs.isEmpty())
        {
            return 0;
        }

        int weightTotal = 0;
        for (Program program : programs)
        {
            final ArrayList<Program> nextLevel = program.getProgramsAbove(programsMap);

            final int subWeight = findSubWeight(nextLevel, programsMap);
            final int programTotalWeight = program.iWeight + subWeight;
            program.iWeight = programTotalWeight;

            weightTotal += programTotalWeight;
        }

        return weightTotal;
    }

    private static ArrayList<Program> getProgramsFromFile() throws IOException
    {
        return Files.readAllLines(Paths.get("day7.in")).stream().map(Program::of)
                    .collect(Collectors.toCollection(ArrayList::new));
    }

    private static void test()
    {
        Util.require(getHeaviest(new ArrayList<>(Arrays.asList(Program.of("pbga (66)"),
                                                               Program.of("xhth (57)"),
                                                               Program.of("ebii (61)"),
                                                               Program.of("havc (66)"),
                                                               Program.of("ktlj (57)"),
                                                               Program.of("fwft (72) -> ktlj, cntj, xhth"),
                                                               Program.of("qoyq (66)"),
                                                               Program.of("padx (45) -> pbga, havc, qoyq"),
                                                               Program.of("tknk (41) -> ugml, padx, fwft"),
                                                               Program.of("jptl (61)"),
                                                               Program.of("ugml (68) -> gyxo, ebii, jptl"),
                                                               Program.of("gyxo (61)"),
                                                               Program.of("cntj (57)")))).iName, "tknk");

        System.out.println("All tests ok");
    }

}
