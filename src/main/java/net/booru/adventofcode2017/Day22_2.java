package net.booru.adventofcode2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Day22_2
{
    static void run() throws IOException
    {
        test();

        final int value = part2(Files.readAllLines(Paths.get("day22.in")), 10000000);

        System.out.println("part2: " + value);
    }

    static int part2(final List<String> lines, final int bursts)
    {
        final HashMap<Position, Stage> infectionMap = parseInfectionMap(lines);
        //printMap(infectionMap);

        Virus virus = new Virus(new Position(0, 0), Direction.Up, 0);
        for (int i = 0; i < bursts; i++)
        {
            virus = virus.update(infectionMap);
            //printMap(infectionMap);
        }

        return virus.getInfectionCount();
    }

    private static HashMap<Position, Stage> parseInfectionMap(final List<String> lines)
    {
        int rowCount = lines.size();
        int columnCount = lines.get(0).length();
        int rowOffset = -(rowCount - 1) / 2;
        int columnOffset = -(columnCount - 1) / 2;

        final HashMap<Position, Stage> infectionMap = new HashMap<>(lines.size() * lines.size());
        int row = 0;
        for (final String line : lines)
        {
            for (int column = 0; column < line.length(); column++)
            {
                if (line.charAt(column) == '#')
                {
                    final Position position = new Position(column + columnOffset, row + rowOffset);
                    infectionMap.put(position, Stage.Infected);
                }
            }
            ++row;
        }

        return infectionMap;
    }

    private static void printMap(HashMap<Position, Stage> map)
    {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE, maxX = -Integer.MAX_VALUE, maxY = -Integer.MAX_VALUE;

        for (final Map.Entry<Position, Stage> entry : map.entrySet())
        {
            final Position position = entry.getKey();
            minX = Math.min(minX, position.getX());
            minY = Math.min(minY, position.getY());
            maxX = Math.max(maxX, position.getX());
            maxY = Math.max(maxY, position.getY());
        }

        for (int y = minY; y <= maxY; y++)
        {
            for (int x = minX; x <= maxX; x++)
            {
                final Stage stage = map.getOrDefault(new Position(x, y), Stage.Clean);
                switch (stage)
                {
                    case Clean:    System.out.print("."); break;
                    case Weakened: System.out.print("W"); break;
                    case Infected: System.out.print("I"); break;
                    case Flagged:  System.out.print("F"); break;
                }
            }
            System.out.println("");
        }
        System.out.println("");
    }

    static class Virus
    {
        final private Position iPosition;
        final private Direction iDirection;
        final private int iInfectionCount;

        public Virus(final Position position, final Direction direction, final int infectionCount)
        {
            iPosition = position;
            iDirection = direction;
            iInfectionCount = infectionCount;
        }

        public Virus update(final HashMap<Position, Stage> infectionMap)
        {
            final Stage currentStage = infectionMap.getOrDefault(iPosition, Stage.Clean);
            final Stage updatedStage = currentStage.transition();

            final Direction newDirection;
            switch (currentStage)
            {
                case Clean: newDirection = iDirection.turnLeft();
                    break;
                case Weakened: newDirection = iDirection;
                    break;
                case Infected: newDirection = iDirection.turnRight();
                    break;
                case Flagged: newDirection = iDirection.turnRight().turnRight(); // reverse direction
                    break;
                default: throw new IllegalStateException();                     
            }

            // update current position state
            if (updatedStage == Stage.Clean)
            {
                infectionMap.remove(iPosition);
            }
            else
            {
                infectionMap.put(iPosition, updatedStage);
            }

            final int infectionCount = (updatedStage == Stage.Infected) ? iInfectionCount + 1 : iInfectionCount;
            final Position newPosition = iPosition.add(newDirection.getPosition());
            return new Virus(newPosition, newDirection, infectionCount);
        }

        public int getInfectionCount()
        {
            return iInfectionCount;
        }
    }

    public enum Stage
    {
        Clean    { public Stage transition(){ return Weakened; }},    //nodes become weakened.
        Weakened { public Stage transition(){ return Infected; }}, // nodes become infected.
        Infected { public Stage transition(){ return Flagged; }}, // nodes become flagged.
        Flagged  { public Stage transition(){ return Clean; }};   // nodes become clean.

        public abstract Stage transition();
    }
    public enum Direction
    {
        Up {
            public Direction turnRight() { return Right; }
            public Position getPosition() { return new Position(0, -1); }
        },
        Down {
            public Direction turnRight() { return Left; }
            public Position getPosition() { return new Position(0, 1); }
        },
        Left {
            public Direction turnRight() { return Up; }
            public Position getPosition() { return new Position(-1, 0); }
        },
        Right {
            public Direction turnRight() { return Down; }
            public Position getPosition() { return new Position(1, 0); }
        };

        public abstract Position getPosition();
        public abstract Direction turnRight();

        public Direction turnLeft() { return turnRight().turnRight().turnRight(); }
    }

    static class Position
    {
        final int iX;
        final int iY;

        public Position(final int x, final int y)
        {
            iX = x;
            iY = y;
        }

        public Position add(final Position position) { return new Position(iX + position.iX, iY + position.iY); }
        public int getX() { return iX; }
        public int getY() { return iY; }
        @Override
        public String toString() { return "(" + iX + ", " + iY + ")"; }
        @Override
        public boolean equals(final Object o) { return this == o || iX == ((Position)o).iX && iY == ((Position)o).iY; } // should check instance of... but meh, advent-of-code
        @Override
        public int hashCode() { return 31 * iX + iY; }
    }

    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------

    private static void test() throws IOException
    {
        final List<String> lines = Arrays.asList("..#",
                                                 "#..",
                                                 "...");

        Util.require(part2(lines, 100), 26);

        System.out.println("All tests ok");
    }
}
