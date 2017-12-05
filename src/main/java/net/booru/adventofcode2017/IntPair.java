package net.booru.adventofcode2017;

class IntPair
{
    final int iFirst;
    final int iSecond;

    IntPair(final int first, final int second)
    {
        iFirst = first;
        iSecond = second;
    }

    @Override
    public String toString()
    {
        return "{" + iFirst + ", " + iSecond + '}';
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof IntPair))
        {
            return false;
        }

        final IntPair intPair = (IntPair)o;

        if (iFirst != intPair.iFirst)
        {
            return false;
        }
        return iSecond == intPair.iSecond;
    }

    @Override
    public int hashCode()
    {
        int result = iFirst;
        result = 31 * result + iSecond;
        return result;
    }
}
