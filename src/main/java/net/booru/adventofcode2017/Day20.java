package net.booru.adventofcode2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class Day20
{
    static void run() throws IOException
    {
        //test();

        final int value = part1(Files.readAllLines(Paths.get("day20.in")));

        System.out.println("part1: " + value);
    }

    static int part1(final List<String> lines)
    {
        final AtomicInteger index = new AtomicInteger(0);
        final List<Particle> particles = lines.stream()
                                              .map(line -> Particle.of(line, index.getAndIncrement()))
                                              .collect(Collectors.toList());

        // Interpreting "the long run" as -> inf
        // The closest must be a particle with the lowest acceleration
        particles.sort(Comparator.comparing((Particle p) -> p.getAcceleration().getDistance()));

        final long acceleration = particles.get(0).getAcceleration().getDistance();
        final List<Particle> candidates = particles.stream()
                                                   .filter(p -> p.getAcceleration().getDistance() == acceleration)
                                                   .collect(Collectors.toList());

        // In general we could probably sort on other aspects... but then I would have to think more... and
        // in this particular problem instance there is but ONE particle with the lowest acceleration... so shortcut
        if (candidates.size() == 1)
        {
            return candidates.get(0).getIndex();
        }

        return simulate(candidates);
    }

    private static int simulate(final List<Particle> particles)
    {
        // simulation by arbitrary fixed number isn't a correct solution in general... *cough* advent of code
        for (int i = 0; i < 1000000; i++)
        {
            particles.forEach(Particle::step);
        }

        final Particle closestParticle = particles.stream().min(Comparator.comparing(Particle::getDistance)).get();
        return closestParticle.getIndex();
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

        public Vector3D getAcceleration()
        {
            return iAcceleration;
        }

        public Vector3D getPosition()
        {
            return iPosition;
        }

        public Vector3D getVelocity()
        {
            return iVelocity;
        }

        public long getDistance()
        {
            return iPosition.getDistance();
        }

        public int getIndex()
        {
            return iIndex;
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
        final List<String> lines = Arrays.asList("p=< 3,0,0>, v=< 2,0,0>, a=<-1,0,0>",
                                                 "p=< 4,0,0>, v=< 0,0,0>, a=<-2,0,0>");
        Util.require(part1(lines), 0);

        System.out.println("All tests ok");
    }
}
