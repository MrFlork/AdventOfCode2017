package net.booru.adventofcode2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class Day20_2
{
    static void run() throws IOException
    {
        //test();

        final int value = part2(Files.readAllLines(Paths.get("day20.in")));

        System.out.println("part1: " + value);
    }

    static int part2(final List<String> lines)
    {
        final AtomicInteger index = new AtomicInteger(0);
        final List<Particle> particles = lines.stream()
                                              .map(line -> Particle.of(line, index.getAndIncrement()))
                                              .collect(Collectors.toList());

        return simulate(particles);
    }

    private static int simulate(final List<Particle> allParticles)
    {
        List<Particle> remaining = allParticles;

        // simulation doesn't actually solve the problem in general... but this is advent of code...
        for (int i = 0; i < 1000 && !remaining.isEmpty(); i++) // note: 1000 was more than enough for this input instance
        {
            remaining.forEach(Particle::step);

            final HashMap<Vector3D, List<Particle>> collisionsMap = new HashMap<>(remaining.size() * 2);
            for (Particle particle : remaining)
            {
                collisionsMap.compute(particle.getPosition(), (position, collisionsList) ->
                {
                    collisionsList = (collisionsList != null) ? collisionsList : new ArrayList<>();
                    collisionsList.add(particle);
                    return collisionsList;
                });
            }

            remaining = collisionsMap.entrySet().stream()
                                     .filter(entry -> entry.getValue().size() == 1)   // no collisions
                                     .map(entry -> entry.getValue().get(0))           // get particle
                                     .collect(Collectors.toList());
        }

        return remaining.size();
    }


    static class Vector3D
    {
        private long iX, iY, iZ;

        public Vector3D(final long x, final long y, final long z)
        {
            iX = x;
            iY = y;
            iZ = z;
        }

        public void add(final Vector3D vector3D)
        {
            iX = iX + vector3D.iX;
            iY = iY + vector3D.iY;
            iZ = iZ + vector3D.iZ;
        }

        public long getDistance()
        {
            return Math.abs(iX) + Math.abs(iY) + Math.abs(iZ);
        }

        @Override
        public boolean equals(final Object object)
        {
            if (this == object) return true;

            if (object instanceof Vector3D)
            {
                final Vector3D other = (Vector3D)object;
                return  (iX == other.iX && iY == other.iY && iZ == other.iZ);
            }

            return false;
        }

        @Override
        public int hashCode()
        {
            int result = (int)(iX ^ (iX >>> 32));
            result = 31 * result + (int)(iY ^ (iY >>> 32));
            result = 31 * result + (int)(iZ ^ (iZ >>> 32));
            return result;
        }

        public String toString()
        {
            return "(" + iX + ", " + iY + ", " + iZ + ")";
        }

        public static Vector3D of(final String line)
        {
            //p=<1,2,3>
            final String substring = line.substring(3, line.length() - 1);
            final String[] integerLine = substring.split(",");
            final Long[] v = Arrays.stream(integerLine).map(String::trim).map(Long::parseLong).toArray(Long[]::new);

            return new Vector3D(v[0], v[1], v[2]);
        }
    }

    static class Particle
    {
        private final int iIndex;
        private final Vector3D iPosition;
        private final Vector3D iVelocity;
        private final Vector3D iAcceleration;

        public Particle(final Vector3D position, final Vector3D velocity, final Vector3D acceleration, final int index)
        {
            iIndex = index;
            iPosition = position;
            iVelocity = velocity;
            iAcceleration = acceleration;
        }

        public Vector3D getPosition()
        {
            return iPosition;
        }

        public void step()
        {
            iVelocity.add(iAcceleration);
            iPosition.add(iVelocity);
        }

        public static Particle of(final String line, final int index)
        {
            // p=<1,2,3>, v=<1,2,3>, a=<1,2,3>
            final String[] vectorLines = line.split(", ");
            final Vector3D[] v = Arrays.stream(vectorLines).map(Vector3D::of).toArray(Vector3D[]::new);
            return new Particle(v[0], v[1], v[2], index);
        }
    }

    //--------------------------------------------------------------------------------------------------
    //---------------------------;-----------------------------------------------------------------------

    private static void test() throws IOException
    {
        final List<String> lines = Arrays.asList(
            "p=<-6,0,0>, v=< 3,0,0>, a=< 0,0,0>",
            "p=<-4,0,0>, v=< 2,0,0>, a=< 0,0,0>",
            "p=<-2,0,0>, v=< 1,0,0>, a=< 0,0,0>",
            "p=< 3,0,0>, v=<-1,0,0>, a=< 0,0,0>"
        );
        Util.require(part2(lines), 1);

        System.out.println("All tests ok");
    }
}
