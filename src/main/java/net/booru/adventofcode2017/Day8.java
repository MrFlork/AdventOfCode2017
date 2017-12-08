package net.booru.adventofcode2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

class Day8
{
    // this is really quick and dirty, terrible code, and mutable *ugh*

    static void run() throws IOException
    {
        test();

        int value = part1(Files.readAllLines(Paths.get("day8.in")));

        System.out.println("part1: " + value);

    }

    private static int part1(final List<String> lines) throws IOException
    {
        final HashMap<String, Integer> registers = parseRegisters(lines);
        final Stream<Instruction> instructions = lines.stream().map(Instruction::new);

        instructions.forEach(instruction -> instruction.execute(registers));

        return registers.values().stream().max(Integer::compareTo).get(); // the value of the largest register
    }

    private static HashMap<String, Integer> parseRegisters(final List<String> strings)
    {
        final HashMap<String, Integer> registers = new HashMap<>(strings.size() * 2);
        for (String string : strings)
        {
            final String[] tokens = string.split(" ");
            final String register1 = tokens[0];
            final String register2 = tokens[4];
            registers.putIfAbsent(register1, 0);
            registers.putIfAbsent(register2, 0);
        }

        return registers;
    }

    private static class Instruction
    {
        private final Operator iOperator;
        private final Comparator iComparator;

        public Instruction(final String s)
        {
            // Register Operator Value IF Register Comparator Value
            final String[] tokens = s.split(" ");

            String register1 = tokens[0];
            int value1 = Integer.parseInt(tokens[2]);
            iOperator = new Operator(tokens[1], register1, value1);

            String register2 = tokens[4];
            int value2 = Integer.parseInt(tokens[6]);
            iComparator = new Comparator(tokens[5], register2, value2);
        }

        public void execute(HashMap<String, Integer> registers)
        {
            if (iComparator.evaluate(registers))
            {
                iOperator.execute(registers);
            }
        }
    }

    static class Comparator
    {
        private final String iRegister;
        private final String iComparator;
        private final int iValue;

        public boolean evaluate(final HashMap<String, Integer> registers)
        {
            final Integer registerValue = registers.get(iRegister);
            switch (iComparator)
            {
                case "<"  : return registerValue <  iValue;
                case ">"  : return registerValue >  iValue;
                case "<=" : return registerValue <= iValue;
                case ">=" : return registerValue >= iValue;
                case "==" : return registerValue == iValue;
                case "!=" : return registerValue != iValue;
                default: throw new IllegalStateException();
            }
        }

        Comparator(final String comparator, final String register, final int value)
        {
            iComparator = comparator;
            iRegister = register;
            iValue = value;
        }
    }

    static class Operator
    {
        private final String iRegister;
        private final int iValueUpdate;

        public void execute(final HashMap<String, Integer> registers)
        {
            int registerValue = registers.get(iRegister) + iValueUpdate;

            registers.put(iRegister, registerValue);
        }

        Operator(final String operator, final String register, final int value)
        {
            switch (operator)
            {
                case "inc": iValueUpdate = value; break;
                case "dec": iValueUpdate = -value; break;
                default: throw new IllegalStateException();
            }

            iRegister = register;
        }
    }

    private static void test() throws IOException
    {
        final List<String> instructions = Arrays.asList(
            "b inc 5 if a > 1",
            "a inc 1 if b < 5",
            "c dec -10 if a >= 1",
            "c inc -20 if c == 10");

        Util.require(part1(instructions), 1);

        System.out.println("All tests ok");
    }
}
