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

        {
            final Program bottom = getHeaviest(getProgramsFromFile());
            System.out.println("name: " + bottom.iName);
        }

        {
            final int unbalance = findUnbalance(getProgramsFromFile());

            System.out.println("unbalance: " + unbalance);
        }
    }

    static class Program
    {
        final String iName;
        final int iWeight;
        int iWeightTotal;
        final ArrayList<String> iProgramsAbove;

        private Program(final String name, final int weight, final int weightTotal, final ArrayList<String> programsAbove)
        {
            iName = name;
            iWeight = weight;
            iWeightTotal = weightTotal;
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

                return new Program(name, weight, -1, above);
            }

            return new Program(name, weight, -1, new ArrayList<>(0));
        }

        public ArrayList<Program> getProgramsAbove(final HashMap<String, Program> programsMap)
        {
            return iProgramsAbove.stream().map(programsMap::get).collect(Collectors.toCollection(ArrayList::new));
        }

        @Override
        public String toString()
        {
            return "" + iName + " (" + iWeight + ", " + iWeightTotal + ") " + iProgramsAbove.toString();
        }
    }

    private static Program getHeaviest(final ArrayList<Program> programs)
    {
        final HashMap<String, Program> programsMap = getProgramMap(programs);
        final int totalWeight = findSubWeight(programs, programsMap); // mutable programs
        final Optional<Program> max = programs.stream().max(Comparator.comparing(program -> program.iWeightTotal));

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
            program.iWeightTotal = program.iWeight + subWeight;

            weightTotal += program.iWeightTotal;
        }

        return weightTotal;
    }

    private static int findUnbalance(final ArrayList<Program> programs)
    {
        try
        {
            final HashMap<String, Program> programsMap = getProgramMap(programs);
            findUnbalanceRecursive(programs, programsMap);
        }
        catch (Result r)
        {
            return r.result;
        }

        return 0;
    }

    private static int findUnbalanceRecursive(final ArrayList<Program> programs, final HashMap<String, Program> programsMap) throws Result
    {
        if (programs.isEmpty())
        {
            return 0;
        }

        int weightTotal = 0;
        for (Program program : programs)
        {
            final ArrayList<Program> nextLevel = program.getProgramsAbove(programsMap);

            final int subWeight = findUnbalanceRecursive(nextLevel, programsMap);
            program.iWeightTotal = program.iWeight + subWeight;

            weightTotal += program.iWeightTotal;
        }

        final int firstWeight = programs.get(0).iWeightTotal;
        final boolean isBalanced = programs.stream().allMatch(p -> p.iWeightTotal == firstWeight);
        if (isBalanced)
        {
            return weightTotal;
        }
        else
        {
            final int normalWeight = getNormalWeight(programs);
            final Program incorrectWeight = programs.stream().filter(program -> program.iWeightTotal != normalWeight).findFirst().get();
            final int difference = incorrectWeight.iWeightTotal - normalWeight;

            throw new Result(incorrectWeight.iWeight - difference);
        }
    }

    private static int getNormalWeight(final ArrayList<Program> programs)
    {
        // Only one is different
        for (int i = 0; i < programs.size()-1; i++)
        {
            if (programs.get(i).iWeightTotal == programs.get(i+1).iWeightTotal)
            {
                return programs.get(i).iWeightTotal;
            }
        }
        return 0; // this will never happen if the input is correct
    }

    // yey \o/ exception for flow-control :(
    private static class Result extends Exception
    {
        final int result;
        public Result(final int result)
        {
            this.result = result;
        }
    }

    private static ArrayList<Program> getProgramsFromFile() throws IOException
    {
        return Files.readAllLines(Paths.get("day7.in")).stream().map(Program::of)
                    .collect(Collectors.toCollection(ArrayList::new));
    }

    private static HashMap<String, Program> getProgramMap(final ArrayList<Program> programs)
    {
        final HashMap<String, Program> programsMap = new HashMap<>(5000);
        programs.forEach(program -> programsMap.put(program.iName, program));
        return programsMap;
    }

    private static void test()
    {
        final ArrayList<Program> programs =
            new ArrayList<>(Arrays.asList(Program.of("pbga (66)"),
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
                                          Program.of("cntj (57)")));

        Util.require(getHeaviest(TEST_copy(programs)).iName, "tknk");
        Util.require(findUnbalance(TEST_copy(programs)), 60);

        System.out.println("All tests ok");
    }

    // since mutable programs *sigh*
    static ArrayList<Program> TEST_copy(final ArrayList<Program> programs)
    {
        return programs.stream().map(p->new Program(p.iName, p.iWeight, p.iWeightTotal, p.iProgramsAbove))
                       .collect(Collectors.toCollection(ArrayList::new));
    }
}
