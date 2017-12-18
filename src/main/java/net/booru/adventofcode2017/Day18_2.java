package net.booru.adventofcode2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class Day18_2
{

    static void run() throws IOException
    {
        test();

        final long value = part2(Files.readAllLines(Paths.get("day18.in")));

        System.out.println("part2: " + value);
    }

    static long part2(final List<String> instructions)
    {
        final Program program0 = Program.of(instructions, 0);
        final Program program1 = Program.of(instructions, 1);

        while(true)
        {
            //boolean isStep0 = program0.step();
            //boolean isStep1 = program1.step();
            while (program0.step());
            while (program1.step());

            if (program0.isBlockingOrDone() && program1.isBlockingOrDone())
            {
                return program1.getSentMessageCount();
            }
        }
    }

    static class Program
    {
        enum Command
        {
            snd, set, add, mul, mod, rcv, jgz;
        }

        //------------------
        final static ArrayDeque<Long> cMessageQueue0 = new ArrayDeque<>();
        final static ArrayDeque<Long> cMessageQueue1 = new ArrayDeque<>();

        static ArrayDeque<Long> getOtherMessageQueue(int myId)
        {
            return (myId == 0) ? cMessageQueue1 : cMessageQueue0;
        }
        static ArrayDeque<Long> getMessageQueue(int myId)
        {
            return (myId == 0) ? cMessageQueue0 : cMessageQueue1;
        }
        //------------------

        final int iProgramId;
        int iProgramCounter;
        long iSentMessageCount = 0;
        final HashMap<String, Long> iRegisters = new HashMap<>(32);
        private final Instruction[] iInstructions;

        public Program(final Instruction[] instructions, int programId)
        {
            iProgramCounter = 0;
            iProgramId = programId;
            iInstructions = instructions;
            for (Instruction instruction : Arrays.asList(iInstructions))
            {
                final String register = instruction.getRegister(0);
                if (!Util.isNumeric(register))
                {
                    iRegisters.putIfAbsent(register, 0L);
                }
            }

            iRegisters.put("p", (long)iProgramId);
        }

        static Program of(final List<String> lines, final int programId)
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

            return new Program(instructions, programId);
        }

        public boolean isBlockingOrDone()
        {
            final Instruction instruction = iInstructions[iProgramCounter];
            final boolean isBlocking = instruction.getCommand() == Command.rcv && getMessageQueue(iProgramId).isEmpty();
            final boolean isDone = (iProgramCounter >= iInstructions.length || iProgramCounter < 0); // no code to execute.

            return isBlocking || isDone;
        }

        public boolean step()
        {
            if (isBlockingOrDone())
            {
                return false;
            }

            final Instruction instruction = iInstructions[iProgramCounter];
            //System.out.println("i="+iProgramCounter + ": " + instruction);
            switch (instruction.getCommand())
            {
                case snd:
                {
                    iSentMessageCount++;
                    final long value = instruction.getInstructionValue(0, iRegisters);
                    final ArrayDeque<Long> otherMessageQueue = getOtherMessageQueue(iProgramId);

                    otherMessageQueue.addLast(value);
                    break;
                }
                case rcv:
                {
                    final ArrayDeque<Long> messageQueue = getMessageQueue(iProgramId);
                    if (messageQueue.isEmpty())
                    {
                        // do not increment the program counter, we are blocking on rcv
                        return false;
                    }

                    final long value = messageQueue.removeFirst();
                    final String register = instruction.getRegister(0);

                    iRegisters.put(register, value);
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
                    final long value = iRegisters.get(register);
                    final long newValue = value % instructionValue;

                    iRegisters.put(register, newValue);
                    break;
                }
                case jgz:
                {
                    final long value = instruction.getInstructionValue(0, iRegisters);
                    if (value > 0L)
                    {
                        final int offset = instruction.getInstructionValue(1, iRegisters).intValue();
                        iProgramCounter += offset - 1;
                    }
                }
            }

            iProgramCounter++;

            return true;
        }

        public long getSentMessageCount()
        {
            return iSentMessageCount;
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

            private Long getInstructionValue(final int index, final HashMap<String, Long> registers)
            {
                return isValue(index) ? getValue(index) : registers.get(getRegister(index));
            }

        }
    }

    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------

    private static void test() throws IOException
    {
        List<String> lines = Arrays.asList(
            "snd 1",
            "snd 2",
            "snd p",
            "rcv a",
            "rcv b",
            "rcv c",
            "rcv d"
        );
        Util.require(part2(lines), 3L);

        System.out.println("All tests ok");
    }
}
