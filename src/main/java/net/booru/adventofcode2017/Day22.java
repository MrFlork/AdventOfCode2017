package net.booru.adventofcode2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Day22
{
    static void run() throws IOException
    {
        //test();

        final int value = part1(Files.readAllLines(Paths.get("day22.in")), 10000);

        System.out.println("part1: " + value);
    }

    static int part1(final List<String> lines, final int bursts)
    {
        final HashMap<Position, Boolean> infectionMap = parseInfectionMap(lines);
        System.out.println("start ");
        //printMap(infectionMap);

        Virus virus = new Virus(new Position(0, 0), Direction.Up, 0);
        for (int i = 0; i < bursts; i++)
        {
            virus = virus.update(infectionMap);
            //printMap(infectionMap);
        }

        return virus.getInfectionCount();
    }

    private static HashMap<Position, Boolean> parseInfectionMap(final List<String> lines)
    {
        int rowCount = lines.size();
        int columnCount = lines.get(0).length();
        int rowOffset = -(rowCount - 1) / 2;
        int columnOffset = -(columnCount - 1) / 2;

        final HashMap<Position, Boolean> infectionMap = new HashMap<>(lines.size() * lines.size());
        int row = 0;
        for (final String line : lines)
        {
            for (int column = 0; column < line.length(); column++)
            {
                if (line.charAt(column) == '#')
                {
                    final Position position = new Position(column + columnOffset, row + rowOffset);
                    infectionMap.put(position, Boolean.TRUE);
                }
            }
            ++row;
        }

        return infectionMap;
    }

    private static void printMap(HashMap<Position, Boolean> map)
    {
        int minX = Integer.MAX_VALUE;
        int minY = Integer.MAX_VALUE;
        int maxX = -Integer.MAX_VALUE;
        int maxY = -Integer.MAX_VALUE;

        for (final Map.Entry<Position, Boolean> entry : map.entrySet())
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
                if (map.getOrDefault(new Position(x, y), Boolean.FALSE))
                {
                    System.out.print("#");
                }
                else
                {
                    System.out.print(".");
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

        public Virus update(final HashMap<Position, Boolean> infectionMap)
        {
            final int infectionCount;
            final Direction newDirection;
            final boolean isInfected = infectionMap.getOrDefault(iPosition, Boolean.FALSE);
            if (isInfected)
            {
                infectionCount = iInfectionCount;
                newDirection = iDirection.turnRight();
                infectionMap.remove(iPosition); // clean
            }
            else
            {
                infectionCount = iInfectionCount + 1;
                newDirection = iDirection.turnLeft();
                infectionMap.put(iPosition, Boolean.TRUE); // infect
            }

            final Position newPosition = iPosition.add(newDirection.getPosition());
            return new Virus(newPosition, newDirection, infectionCount);
        }

        public int getInfectionCount()
        {
            return iInfectionCount;
        }
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

        Util.require(part1(lines, 7), 5);
        Util.require(part1(lines, 70), 41);

        System.out.println("All tests ok");
    }
}
