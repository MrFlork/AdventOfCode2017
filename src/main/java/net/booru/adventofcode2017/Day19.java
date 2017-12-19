package net.booru.adventofcode2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class Day19
{
    static void run() throws IOException
    {
        test();

        final String value = part1(parseMap(Files.readAllLines(Paths.get("day19.in"))));

        System.out.println("part1: " + value);
    }

    static String part1(final Map map)
    {
        final int startX = getStartX(map);

        final StringBuilder accumulator = new StringBuilder();
        follow(new State(new Position(startX, 0), Direction.Down), map, accumulator);

        return accumulator.toString();
    }

    enum Direction
    {
        Up {
            Position getPosition()  { return new Position(0,-1); }
            Direction getOpposite() { return Down; }
        },
        Down {
            Position getPosition()  { return new Position(0,1); }
            Direction getOpposite() { return Up; }
        },
        Left {
            Position getPosition()  { return new Position(-1,0); }
            Direction getOpposite() { return Right; }
        },
        Right{
            Position getPosition()  { return new Position(1,0); }
            Direction getOpposite() { return Left; }
        };

        abstract Position getPosition();
        abstract Direction getOpposite();
    }

    static class Position
    {
        final int iX;
        final int iY;

        public Position(final int x, final int y) { iX = x; iY = y; }
        public Position add(final Position position) { return new Position(iX + position.iX, iY + position.iY); }
        public int getX() { return iX; }
        public int getY() { return iY; }
        public boolean equals(final Position p) { return iX == p.iX && iY == p.iY; }
        public String toString() { return "("+iX+", "+iY+")"; }
    }

    static class Map
    {
        final char[][] iMap;

        public Map(final char[][] map) { iMap = map; }
        public int getWidth(){ return iMap.length; }
        public int getHeight(){ return iMap[0].length; }

        public boolean isEmpty(final Position position) { return get(position) == ' '; }
        public boolean isTurn(final Position position) { return get(position) == '+'; }
        public char get(final Position position)
        {
            if (position.getY() < 0 || position.getX() >= getWidth() || position.getY() < 0 || position.getY() >= getHeight())
            {
                return ' ';
            }
            
            return iMap[position.getX()][position.getY()];
        }

        public void println(final Position position, final Position position2)
        {
            for (int y = 0; y < getHeight(); y++)
            {
                for (int x = 0; x < getWidth(); x++)
                {
                    final Position p = new Position(x, y);
                    if (p.equals(position))
                    {
                        System.out.printf("(%s)", get(p));
                    }
                    else
                    if (p.equals(position2))
                    {
                        System.out.printf(" %s^", get(p));
                    }
                    else
                    {
                        System.out.printf(" %s ", get(p));
                    }
                }
                System.out.println("");
            }
            System.out.println("*************************************************");
        }
    }

    static class State
    {
        final static State END = new State(null, null);

        final Position iPosition;
        final Direction iCurrentDirection;

        public State(final Position position, final Direction currentDirection)
        {
            iPosition = position;
            iCurrentDirection = currentDirection;
        }

        public boolean isEndState()
        {
            return this == END;
        }

        public State follow(final Map map)
        {
            ArrayList<State> candidates = new ArrayList<>(4);

            for (Direction direction : Direction.values())
            {
                if (direction == iCurrentDirection.getOpposite())
                {
                    continue;
                }

                final Position position = iPosition.add(direction.getPosition());
                //map.println(iPosition, position);
                if (!map.isEmpty(position))
                {
                    if (map.isTurn(position))
                    {
                        candidates.add(new State(position, iCurrentDirection));
                    }
                    else
                    {
                        candidates.add(new State(position, direction));
                    }
                }
            }

            if (candidates.isEmpty())
            {
                return END;
            }
            else if (candidates.size() == 1)
            {
                return candidates.get(0);
            }
            else
            {
                // choose same direction
                return candidates.stream()
                                 .filter(state -> state.iCurrentDirection == iCurrentDirection )
                                 .findFirst().get();
            }

        }

        public Optional<Character> getLetter(Map map)
        {
            final char character = map.get(iPosition);
            if (character != ' ' && character != '|' && character != '-' && character != '+')
            {
                return Optional.of(character);
            }
            
            return Optional.empty();
        }

    }

    private static void follow(final State state, final Map map, final StringBuilder accumulator)
    {
        state.getLetter(map).ifPresent(accumulator::append);
        final State nextState = state.follow(map);

        if (!nextState.isEndState())
        {
            follow(nextState, map, accumulator);
        }
    }

    private static int getStartX(final Map map)
    {
        for (int i = 0; i < map.getWidth(); i++)
        {
            if (map.get(new Position(i,0)) == '|')
            {
                return i;
            }
        }

        return -1;
    }

//    char[][] readInput()
//    {}

    //--------------------------------------------------------------------------------------------------
    //---------------------------;-----------------------------------------------------------------------

    private static void test() throws IOException
    {
        final Map map = parseMap(Arrays.asList(
            "     |         ",
            "     |  +--+   ",
            "     A  |  C   ",
            " F---|----E|--+",
            "     |  |  |  D",
            "     +B-+  +--+"));

        Util.require(part1(map), "ABCDEF");

        System.out.println("All tests ok");
    }

    private static Map parseMap(final List<String> lines) throws IOException
    {
        final int width = lines.get(0).length();
        final int height = lines.size();
        final char[][] map = new char[width][height];

        for (int y = 0; y < height; y++)
        {
            String line = lines.get(y);
            for (int x = 0; x < width; x++)
            {
                map[x][y] = line.charAt(x);
                System.out.print(map[x][y]);
            }
            System.out.println("");
        }
        return new Map(map);
    }
}
