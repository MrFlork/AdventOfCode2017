package net.booru.adventofcode2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("Duplicates")
class Day25
{
    static void run() throws IOException
    {
        test();
        final int value = part1(Files.readAllLines(Paths.get("day25.in")));

        System.out.println("part1: " + value);
    }

    static int part1(final List<String> lines)
    {
        final ParseResult parseResult = parseStates(lines);
        final HashMap<String, State> statesMap = parseResult.iStatesMap;

        final Tape tape = new Tape(0);

        State currentState = statesMap.get(parseResult.iStartStateName);
        for (int i = 0; i < parseResult.iIterations; i++)
        {
            final String nextStateName = currentState.evaluate(tape);
            currentState = statesMap.get(nextStateName);
        }

        return tape.getChecksum();
    }

    static class ParseResult
    {
        public final String iStartStateName;
        public final int iIterations;
        final HashMap<String, State> iStatesMap;

        public ParseResult(final String startStateName, final int iterations, final HashMap<String, State> statesMap)
        {
            iStartStateName = startStateName;
            iIterations = iterations;
            iStatesMap = statesMap;
        }
    }
    
    static class Tape
    {
        private int iCursor;
        private final HashMap<Integer, Integer> iTape = new HashMap<>(16000);

        public Tape(final int cursor)
        {
            iCursor = cursor;
        }

        public void move(Move move)
        {
            iCursor += move.getDelta();
        }

        public int read()
        {
            return iTape.getOrDefault(iCursor, 0);
        }

        public void write(final int value)
        {
            if (value != 0 || read() != 0)
            {
                iTape.put(iCursor, value);
            }
        }

        public int getChecksum()
        {
            return iTape.values().stream().mapToInt(a->a).sum();
        }
    }

    enum Move
    {
        right  { public int getDelta() { return 1; }},
        left   { public int getDelta() { return -1; }};
        public abstract int getDelta();
    }

    static class State
    {
        private final String iName;
        private final int iComparison;
        private final int[] iWriteValue;
        private final Move[] iMove;
        private final String[] iContinueState;

        public State(final String name, final int comparison,
                     final int write0, final Move move0, final String iContinueState0,
                     final int write1, final Move move1, final String iContinueState1)
        {
            iName = name;
            iComparison = comparison;

            iWriteValue = new int[]{write0, write1};
            iMove = new Move[]{move0, move1};
            iContinueState = new String[]{iContinueState0, iContinueState1};
        }

        public static State of(final List<String> lines)
        {
            final String[] data = lines.stream().map(State::getLastWord).toArray(String[]::new);

            final String name           = data[0];
            final int comparison        = Integer.parseInt(data[1]);

            final int write0            = Integer.parseInt(data[2]);
            final Move move0            = Move.valueOf(data[3]);
            final String continueState0 = data[4];

            // skip comparison at index 5 since implicit
            final int write1            = Integer.parseInt(data[6]);
            final Move move1            = Move.valueOf(data[7]);
            final String continueState1 = data[8];

            return new State(name, comparison, write0, move0, continueState0, write1, move1, continueState1);
        }

        public static String getLastWord(String line)
        {
            final String[] words = line.split(" ");
            final String word = words[words.length - 1];
            return word.substring(0, word.length()-1);
        }

        public String getName()
        {
            return iName;
        }

        public String evaluate(final Tape mutableTape)
        {
            final int currentValue = mutableTape.read();
            final int branch = (currentValue == iComparison) ? 0 : 1;

            // Write, Move, Continue
            mutableTape.write(iWriteValue[branch]);
            mutableTape.move(iMove[branch]);

            return iContinueState[branch];
        }
    }


    private static ParseResult parseStates(final List<String> allInputLines)
    {
        final HashMap<String, State> statesMap = new HashMap<>(2 * allInputLines.size() / 9);

        final ArrayDeque<String> linesDeque = new ArrayDeque<>(allInputLines);

        final String startStateLine = linesDeque.pop();
        final String iterationsLine = linesDeque.pop();

        while (!linesDeque.isEmpty())
        {
            linesDeque.pop(); // eat empty line

            final ArrayList<String> stateLines = new ArrayList<>(9);
            for (int i = 0; i < 9; i++)
            {
                stateLines.add(linesDeque.pop());
            }

            final State state = State.of(stateLines);
            statesMap.put(state.getName(), state);
        }

        final String startStateName = State.getLastWord(startStateLine);
        final String[] words = iterationsLine.split(" ");
        final int iterations = Integer.parseInt(words[words.length - 2]);

        return new ParseResult(startStateName, iterations, statesMap);
    }

    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------

    private static void test() throws IOException
    {
        final List<String> lines = Files.readAllLines(Paths.get("day25_test.in"));
        Util.require(part1(lines), 3);

        System.out.println("All tests ok");
    }
}
