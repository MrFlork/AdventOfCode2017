package net.booru.adventofcode2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

class Day18
{

    static void run() throws IOException
    {
        //test();

        final long value = part1(Files.readAllLines(Paths.get("day18.in")));

        System.out.println("part1: " + value);
    }

    static long part1(final List<String> instructions)
    {
        final Program program = Program.of(instructions);
        program.execute();
        return program.getReceived();
    }

    static class Program
    {
        enum Command
        {
            snd, set, add, mul, mod, rcv, jgz;
        }

        final HashMap<String, Long> iRegisters = new LinkedHashMap<>(128);
        long iRecovered;
        long iLastPlayed = 0;

        private final Instruction[] iInstructions;
        private long iReceived;

        public Program(final Instruction[] instructions)
        {
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

        static Program of(List<String> lines)
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

        public long getReceived()
        {
            return iReceived;
        }

        private void execute()
        {
            for (int i = 0; i < iInstructions.length; i++)
            {
                final Instruction instruction = iInstructions[i];
                System.out.println("i="+i + ": " + instruction);
                switch (instruction.getCommand())
                {
                    case snd:
                    {
                        final String register = instruction.getRegister(0);
                        iLastPlayed = iRegisters.get(register); // only registers in the input never int
                        if (iLastPlayed > 0)
                        {
                            iRecovered = iLastPlayed;
                        }
                        break;
                    }
                    case set:
                    {
                        final String register = instruction.getRegister(0);
                        long instructionValue = instruction.getInstructionValue(1, iRegisters);
                        iRegisters.put(register, instructionValue);
                        break;
                    }
                    case add:
                    {
                        final String register = instruction.getRegister(0);
                        long instructionValue = instruction.getInstructionValue(1, iRegisters);
                        final long newValue = iRegisters.get(register) + instructionValue;
                        iRegisters.put(register, newValue);
                        break;
                    }
                    case mul:
                    {
                        final String register = instruction.getRegister(0);
                        long instructionValue = instruction.getInstructionValue(1, iRegisters);
                        final long newValue = iRegisters.get(register) * instructionValue;
                        iRegisters.put(register, newValue);
                        break;
                    }
                    case mod:
                    {
                        final String register = instruction.getRegister(0);
                        final long instructionValue = instruction.getInstructionValue(1, iRegisters);
                        final Long value = iRegisters.get(register);
                        long newValue = value % instructionValue;
                        iRegisters.put(register, newValue);
                        break;
                    }
                    case rcv:
                    {
                        iReceived = iRecovered;
                        return;
                    }
                    case jgz:
                    {
                        final long value = instruction.getInstructionValue(0, iRegisters);
                        if (value > 0)
                        {
                            final Long offset = instruction.getInstructionValue(1, iRegisters);
                            i += offset - 1;
                        }
                    }
                }
            }
        }

        static class Instruction
        {
            final Command iCommand;
            final String[] iArguments;
            final Long[] iValues;


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

            @Override
            public String toString()
            {
                return iCommand + " " + iArguments[0] + " " + iArguments[1];
            }

            public Command getCommand()
            {
                return iCommand;
            }

            public String getRegister(final int index)
            {
                return iArguments[index];
            }

            public Long getValue(final int index)
            {
                return iValues[index];
            }

            public boolean isValue(final int index)
            {
                return iValues[index] != null;
            }

            private long getInstructionValue(final int index, final HashMap<String, Long> registers)
            {
                return isValue(index)
                    ? getValue(index)
                    : registers.get(getRegister(index));
            }

        }
    }

    //--------------------------------------------------------------------------------------------------
    //---------------------------;-----------------------------------------------------------------------

    private static void test() throws IOException
    {
//        Util.require(part1(3), 638);

        System.out.println("All tests ok");
    }
}
