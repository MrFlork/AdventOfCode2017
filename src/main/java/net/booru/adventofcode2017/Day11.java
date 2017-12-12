package net.booru.adventofcode2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class Day11
{
    static void run() throws IOException
    {
        test();

        int value = part1(Files.readAllLines(Paths.get("day11.in")).get(0));

        System.out.println("part1: " + value);
        System.out.println("part2: " + Hex.cMaxDistance);
    }

    private static int part1(final String input) throws IOException
    {
        List<Move> moves = Arrays.stream(input.split(",")).map(Move::valueOf).collect(Collectors.toList());

        Hex childHex = new Hex(0,0);
        for(Move move : moves)
        {
            childHex = childHex.moveBy(move);
        }
        
        return childHex.getDistanceFromOrigo();
    }


    /*

                       N
                   .  0,1   .
            NW      .      .         NE
              -1,0   *----*     1,1
                    .      .
                   .        .
              ----*          *----
                   .        .
             -1,-1  .      .   1,0
            SW       *----*        SE
                    .      .                         X-axis is NW<->SE,   Y-axis is N-S
                   .  0,-1  .
                       S

     */

    enum Move
    {
        n,s,nw,ne,sw,se;
    }

    static class Hex
    {
        static int cMaxDistance = 0;
        final int iX;
        final int iY;

        public Hex(final int x, final int y)
        {
            iX = x;
            iY = y;
        }

        public Hex(final int x, final int y, final int z)
        {
            iX = x;
            iY = y;
        }

        public Hex add(int x, int y)
        {
            final Hex hex = new Hex(iX + x, iY + y);
            final int distanceFromOrigo = hex.getDistanceFromOrigo();
            cMaxDistance = cMaxDistance < distanceFromOrigo ? distanceFromOrigo : cMaxDistance;

            return hex;
        }

        public Hex moveBy(final Move direction)
        {
            // Axial coordinates
            switch (direction)
            {
                case n:  return add(0,1);
                case s:  return add(0,-1);

                case ne: return add(1,1);
                case sw: return add(-1,-1);

                case nw: return add(-1,0);
                case se: return add(1,0);

                default:throw new IllegalStateException();
            }
        }

        public int getDistanceFromOrigo()
        {
            // Six regions (between the diagonal (NE<->SW) and the X and Y axis,
            // but only three after symmetry.
            // CW naming starting with positive Y-axis and the positive diagonal :
            // A is between the positive Y-axis and the positive diagonal
            // B is between the positive X-axis and the positive diagonal
            // C is between the positive X-axis and the negative diagonal
            // A' (is the symmetric region of A) is between negative Y and negative diagonal.
            // B' is between negative X-axis and negative diagonal.
            // C' is between positive Y-axis and the negative X-axis

            if (iX == 0) return Math.abs(iY);
            if (iY == 0) return Math.abs(iX);
            if (iX == iY) return Math.abs(iX);

            final boolean isGroupA = (iX > 0 && iY > iX) || (iX < 0 && iY < iX);
            final boolean isGroupB = (iX > 0 && iY > 0 && iY < iX) || (iX < 0 && iY < 0 && iY > iX);
            final boolean isGroupC = (iX > 0 && iY < 0) || (iX < 0 && iY > 0);

            if (isGroupA)
            {
                return Math.abs(iY - iX) + Math.abs(iX);
            }
            else
            if (isGroupB)
            {
                return Math.abs(iX);
            }
            else if (isGroupC)
            {
                return Math.abs(iX) + Math.abs(iY);
            }
            throw new IllegalStateException();
            
        }

        @Override
        public String toString()
        {
            return "Hex{" + iX +", " + iY +'}';
        }
    }


    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------

    private static void test() throws IOException
    {
        Util.require(part1("ne,ne,ne"),3);
        Util.require(part1("ne,ne,sw,sw"),0);
        Util.require(part1("ne,ne,s,s"),2);
        Util.require(part1("se,sw,se,sw,sw"),3);

        System.out.println("All tests ok");
    }
}
