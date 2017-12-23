package net.booru.adventofcode2017;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("Duplicates")
class Day23_2
{
    static void run() throws IOException
    {
        final long value = part2();

        System.out.println("part2: " + value);
    }

    static long part2()
    {
        return refactoredAssembler();
    }

    static long refactoredAssembler()
    {
        long c, h = 0;
        boolean f;
        c = 109900 + 17000;

        int i=1;
        for (int b = 109900; b <= c; b += 17) // G23 loop
        {
            f = true;
            for (int d = 2; d < b && f ; d++)   // G13 loop
            {
                f = getF_opt(b, d);   // G8 loop can be reduced to constant time operation
            }

            if (!f)
                h++;

            System.out.println("i= " + i + " b=" + b + " h="+h);
            ++i;
        }

        return h;
    }

    private static boolean getF_opt(final long b, final long d)
    {
        // 0 = d * e - b;
        // b = d * e;
        // b / d = e;
        // i.e - if e is an integer return false
        return b % d != 0;
    }

    // for documentation
    private static long getF(final long b, final long d, final long f)
    {
        if (f == 0)
        {
            return f;
        }

        long g;
        for (int e = 2; e < b; e++)
        {
            g = d * e - b;
            if (g == 0)
            {
                return 0;
            }
        }
        return f;
    }

    // for documentation
    static long assemblerToCode()
    {
        /* Original Assembler
               0:  set b 99
               1:  set c b
               2:  if ( a != 0 ) goto 4
               3:  goto 5
               4:  mul b 100
               5:  sub b -100000
               6:  set c b
               7:  sub c -17000

        G23    8:  set f 1
               9:  set d 2

        G13    10: set e 2

        G8     11: set g d
               12: mul g e
               13: sub g b
               14: jnz g 2
               15: set f 0
               16: sub e -1
               17: set g e
               18: sub g b
               19: jnz g -8   (19-8 => pc = 11 @ G8)

               20: sub d -1
               21: set g d
               22: sub g b
               23: jnz g -13  (23-13 => pc = 10 @ G13)

               24: jnz f 2
               25: sub h -1

               26: set g b
               27: sub g c
               28: jnz g 2
               29: jnz 1 3    (EXIT)

               30: sub b -17
               31: jnz 1 -23  (31-23 => pc = 8 @ G23)
         */

        long b, c, d, e, f, g, h = 0;

        b = 99 * 100 + 100000;
        c = b + 17000;

        labelG_23: while(true)
        {
            f = 1;
            d = 2;

            labelG_13: while(true)
            {
                e = 2;
                labelG_8: while(true)
                {
                    g = d * e - b;

                    if (g == 0)
                        f = 0;

                    e++;
                    g = e - b;

                    if (g != 0) continue labelG_8;  // this is a for loop for (int e = 2 : e < b; e++)

                    d += 1;
                    g = d - b;

                    if (g != 0) continue labelG_13; // this is a for loop for (int d = 2 : d < b; d++)

                    if (f == 0)
                        h++;

                    g = b;
                    g = g - c;

                    if (g == 0)
                        return h;

                    b += 17;
                    continue labelG_23;
                }
            }
        }
    }
}
