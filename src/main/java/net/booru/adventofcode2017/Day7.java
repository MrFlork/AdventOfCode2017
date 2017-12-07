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

        private Program(final String name, final int weight, final ArrayList<String> programsAbove)
        {
            iName = name;
            iWeight = weight;
            iWeightTotal = weight;
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

        final boolean isBalanced = programs.stream().allMatch(p -> programs.get(0).iWeightTotal == p.iWeightTotal);
        if (isBalanced)
        {
            return weightTotal;
        }
        else
        {
            // we don't know if the unbalanced node is too heavy or too light.
            final ArrayList<Program> programsOrdered = new ArrayList<>(programs);
            programsOrdered.sort(Comparator.comparing(p -> p.iWeightTotal));

            final Program first = programsOrdered.get(0);
            final Program last = programsOrdered.get(programsOrdered.size() - 1);
            final int unbalance = last.iWeightTotal - first.iWeightTotal;

            final boolean isLastProgramUnbalanced = first.iWeightTotal == programsOrdered.get(1).iWeightTotal;
            if (isLastProgramUnbalanced)
            {
                throw new Result(last.iWeight - unbalance);
            }
            else
            {
                throw new Result(first.iWeight + unbalance);
            }
        }
    }

    // yey exception for flow-control :(
    private static class Result extends Exception
    {
        int result;

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
        ArrayList<Program> programs = new ArrayList<>(Arrays.asList(Program.of("pbga (66)"),
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

        Util.require(getHeaviest(programs).iName, "tknk");

        programs = new ArrayList<>(Arrays.asList(Program.of("pbga (66)"),
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
        Util.require(findUnbalance(programs), 60);

        System.out.println("All tests ok");
    }

}
