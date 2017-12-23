package net.booru.adventofcode2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("Duplicates")
class Day23
{
    static void run() throws IOException
    {
        final long value = part1(Files.readAllLines(Paths.get("day23.in")));
        System.out.println("part1: " + value);
    }

    static long part1(final List<String> instructions)
    {
        final Program program = Program.of(instructions);
        program.execute();

        return program.getMulCounter();
    }

    static class Program
    {
        enum Command { set, sub, mul, jnz }

        private int iProgramCounter;
        private long iMulCounter;
        private final HashMap<String, Long> iRegisters = new HashMap<>(32);
        private final Instruction[] iInstructions;

        public Program(final Instruction[] instructions)
        {
            iMulCounter = 0;
            iProgramCounter = 0;
            iInstructions = instructions;
            for (Instruction instruction : Arrays.asList(iInstructions))
            {
                final String register = instruction.getRegister(0);
                if (!Util.isNumeric(register))
                {
                    iRegisters.putIfAbsent(register, 0L);
                }
            }
        }

        static Program of(final List<String> lines)
        {
            final Instruction[] instructions =  lines.stream().map(
                (String line) ->
                {
                    final String[] parts = line.split(" ");
                    final Command command = Command.valueOf(parts[0]);
                    final String argument1 = parts[1];
                    final String argument2 = (parts.length > 2) ? parts[2] : "";

                    return new Instruction(command, argument1, argument2);
                }).toArray(Instruction[]::new);

            return new Program(instructions);
        }

        private void execute()
        {
            while(iProgramCounter >= 0 && iProgramCounter < iInstructions.length)
            {
                final Instruction instruction = iInstructions[iProgramCounter];
                switch (instruction.getCommand())
                {
                    case set:
                    {
                        final String register = instruction.getRegister(0);
                        long instructionValue = instruction.getInstructionValue(1, iRegisters);

                        iRegisters.put(register, instructionValue);
                        break;
                    }
                    case sub:
                    {
                        final String register = instruction.getRegister(0);
                        long instructionValue = instruction.getInstructionValue(1, iRegisters);
                        final long newValue = iRegisters.get(register) - instructionValue;

                        iRegisters.put(register, newValue);
                        break;
                    }
                    case mul:
                    {
                        iMulCounter += 1;

                        final String register = instruction.getRegister(0);
                        long instructionValue = instruction.getInstructionValue(1, iRegisters);
                        final long newValue = iRegisters.get(register) * instructionValue;

                        iRegisters.put(register, newValue);
                        break;
                    }
                    case jnz:
                    {
                        final long value = instruction.getInstructionValue(0, iRegisters);
                        if (value != 0L)
                        {
                            final int offset = instruction.getInstructionValue(1, iRegisters).intValue();
                            iProgramCounter += offset - 1;
                        }
                    }
                }

                iProgramCounter++;
            }
        }

        public long getMulCounter()
        {
            return iMulCounter;
        }

        static class Instruction
        {
            private final Command iCommand;
            private final String[] iArguments;
            private final Long[] iValues;


            public Instruction(final Command command, final String argument1, final String argument2)
            {
                iCommand = command;
                iArguments = new String[]{argument1, argument2};
                iValues = new Long[]
                    {
                        (Util.isNumeric(argument1)) ? Long.parseLong(argument1) : null,
                        (Util.isNumeric(argument2)) ? Long.parseLong(argument2) : null
                    };
            }

            public Command getCommand() { return iCommand; }
            public String getRegister(final int index) { return iArguments[index]; }
            public Long getValue(final int index) { return iValues[index]; }
            public boolean isValue(final int index) { return iValues[index] != null; }

            private Long getInstructionValue(final int index, final HashMap<String, Long> registers)
            {
                return isValue(index) ? getValue(index) : registers.get(getRegister(index));
            }

            @Override
            public String toString() {  return iCommand + " " + iArguments[0] + " " + iArguments[1]; }
        }
    }
}
