package net.booru.adventofcode2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class Day21
{
    static void run() throws IOException
    {
        test();

        final int value = part1(Files.readAllLines(Paths.get("day21.in")), 5);

        System.out.println("part1: " + value);
    }

    static int part1(final List<String> lines, final int iterations)
    {
        final HashMap<Matrix, Matrix> ruleMap = partRules(lines);

        Matrix nextMatrix = Matrix.parse(".#./..#/###");
        for (int i = 0; i < iterations; i++)
        {
            final List<Matrix> subMatrices = nextMatrix.split();
            final List<Matrix> transformed = subMatrices.stream().map(ruleMap::get).collect(Collectors.toList());
            nextMatrix = Matrix.mergeSubMatrices(transformed);
        }

        return nextMatrix.countFilled();
    }

    static HashMap<Matrix, Matrix> partRules(final List<String> lines)
    {
        final HashMap<Matrix, Matrix> ruleMap = new HashMap<>(lines.size() * 2 * 8);
        for (String line : lines)
        {
            // ../.. => .##/#../..#
            final String[] parts = line.split(" => ");
            final Matrix ruleKey = Matrix.parse(parts[0]);
            final Matrix ruleValue = Matrix.parse(parts[1]);

            // add all transformed keys
            Transforms.allValues().map(t->t.transform(ruleKey)).forEach(matrix -> ruleMap.put(matrix, ruleValue));
        }
        return ruleMap;
    }

    public enum Transforms
    {
        Id, R90, R180, R270, R90_FV, R90_FH, FV, FH;
        static Stream<Transforms> allValues() { return Arrays.stream(Transforms.values()); }

        public Matrix transform(final Matrix matrix)
        {
            switch (this)
            {
                case Id:     return matrix;
                case R90:    return matrix.rotate();
                case R180:   return matrix.rotate().rotate();
                case R270:   return matrix.rotate().rotate().rotate();
                case FV:     return matrix.flip();
                case FH:     return matrix.flip().rotate().rotate();
                case R90_FV: return matrix.rotate().flip();
                case R90_FH: return matrix.rotate().flip().rotate().rotate();
                default: throw new IllegalStateException();
            }
        }
    }

    static class Matrix
    {
        private final byte[] iMatrix;
        private final int iDimension;

        public Matrix(final byte[] matrix)
        {
            iMatrix = matrix;
            iDimension = (int)Math.sqrt(iMatrix.length);
        }

        private static int getIndex(final int x, final int y, final int dimension) { return y * dimension + x; }
        private static int getX(final int index, final int dimension)              { return index % (dimension); }
        private static int getY(final int index, final int dimension)              { return (int)Math.floor(index / dimension); }
        private byte get(final int x, final int y)                                 { return iMatrix[getIndex(x, y, getDimension())]; }
        private void set(final int x, final int y, final byte b)                   { iMatrix[getIndex(x, y, getDimension())] = b; }
        public int getDimension()                                                  { return iDimension; }

        public int countFilled() { return IntStream.range(0, iMatrix.length).map(i -> iMatrix[i]).sum(); }

        public List<Matrix> split()
        {
            if (getDimension() == 3) { return Arrays.asList(this); }

            final int subMatricesPerDimension; // how many sub-matrices to split into in each dimension
            final int sliceDimension;          // the dimension of a sub-matrix
            if (getDimension() % 2 == 0)
            {
                subMatricesPerDimension = getDimension() / 2;
                sliceDimension = 2;
            }
            else
            {
                subMatricesPerDimension = getDimension() / 3;
                sliceDimension = 3;
            }

            final List<Matrix> subMatrices = IntStream.range(0, subMatricesPerDimension * subMatricesPerDimension)
                                                      .mapToObj(index -> getSlice(index, subMatricesPerDimension, sliceDimension))
                                                      .collect(Collectors.toList());
            return subMatrices;
        }

        /**
         * @param subMatricesIndex which of the sub-matrices in the large matrix do we want. numbered 0..N eg. row-0: (0 1), row-1: (2 3)
         * @param subMatricesDimension the dimension of the matrix of sub-matrices
         * @param subDimension the dimension of a sub-matrix
         * @return a sub-matrix
         */
        public Matrix getSlice(final int subMatricesIndex, final int subMatricesDimension, final int subDimension)
        {
            int startX = getX(subMatricesIndex, subMatricesDimension) * subDimension;
            int startY = getY(subMatricesIndex, subMatricesDimension) * subDimension;

            final byte[] subMatrix = new byte[subDimension * subDimension];

            for (int y = 0; y < subDimension; y++)
            {
                final int indexFull = getIndex(startX, startY + y, getDimension());
                final int indexSlice = getIndex(0, y, subDimension);

                System.arraycopy(iMatrix, indexFull, subMatrix, indexSlice, subDimension);
            }

            return new Matrix(subMatrix);
        }

        public Matrix rotate()
        {
            final int dimension = getDimension();
            final Matrix rotatedMatrix = new Matrix(new byte[iMatrix.length]);

            for (int y = 0; y < dimension; y++)
            {
                for (int x = 0; x < dimension; x++)
                {
                    final byte value = get(y, (dimension - x - 1));
                    rotatedMatrix.set(x, y, value);
                }
            }

            return rotatedMatrix;
        }

        public Matrix flip()
        {
            final int dimension = getDimension();
            final byte[] flippedMatrix = new byte[iMatrix.length];

            for (int y = 0; y < dimension; y++)
            {
                final int indexSource = getIndex(0, y, dimension);
                final int transformedY = (dimension - y - 1);
                final int indexFlipped = getIndex(0, transformedY, dimension);

                System.arraycopy(iMatrix, indexSource, flippedMatrix, indexFlipped, dimension);
            }

            return new Matrix(flippedMatrix);
        }

        public static Matrix mergeSubMatrices(final List<Matrix> subMatrices)
        {
            if (subMatrices.size() == 1) { return subMatrices.get(0); }
            
            final int subMatricesDimension = (int)Math.sqrt(subMatrices.size());
            final int subDimension = subMatrices.get(0).getDimension();
            final int dimension = subMatricesDimension * subDimension;
            final Matrix mergedMatrix = new Matrix(new byte[dimension * dimension]);

            for (int subMatricesIndex = 0; subMatricesIndex < subMatrices.size(); subMatricesIndex++)
            {
                final Matrix subMatrix = subMatrices.get(subMatricesIndex);
                mergedMatrix.assimilateSubMatrix(subMatrix, subMatricesIndex, subMatricesDimension);
            }

            return mergedMatrix;
        }

        private void assimilateSubMatrix(final Matrix subMatrix, final int subMatricesIndex, final int subMatricesDimension)
        {
            // get top left corner in the large mergedMatrix: (startX, startY)
            final int subDimension = subMatrix.getDimension();
            int startX = getX(subMatricesIndex, subMatricesDimension) * subDimension;
            int startY = getY(subMatricesIndex, subMatricesDimension) * subDimension;

            for (int y = 0; y < subDimension; y++)
            {
                final int index = getIndex(0, y, subDimension);
                final int indexFull = getIndex(startX, startY + y, getDimension());
                System.arraycopy(subMatrix.iMatrix, index, iMatrix, indexFull, subDimension); // one row at a time
            }
        }

        public static Matrix parse(final String line)
        {
            final int dimension = (int)Math.floor(Math.sqrt(line.length()));
            final Matrix matrix = new Matrix(new byte[dimension * dimension]);
            int y = 0, x = 0;
            for (int i = 0; i < line.length(); i++)
            {
                final char c = line.charAt(i);
                if (c == '/')
                {
                    ++y;
                    x = 0;
                }
                else
                {
                    matrix.set(x, y, (c == '#') ? (byte)1 : (byte)0);
                    ++x;
                }
            }

            return matrix;
        }

        public boolean equals(final Object o)
        {   // should check o instanceof but we know it is and.. meh, advent-of-code
            return this == o || Arrays.equals(iMatrix, ((Matrix)o).iMatrix);
        }
        public int hashCode() { return Arrays.hashCode(iMatrix); }
    }

    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------

    private static void test() throws IOException
    {
        final List<String> lines = Arrays.asList("../.# => ##./#../...",
                                                 ".#./..#/### => #..#/..../..../#..#");
        Util.require(part1(lines, 2), 12);
        System.out.println("All tests ok");
    }
}
