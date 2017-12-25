package net.booru.adventofcode2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
class Day24
{
    static void run() throws IOException
    {
        //test();
        final int value = part1(Files.readAllLines(Paths.get("day24.in")));

        System.out.println("part1: " + value);
    }

    static int part1(final List<String> strings)
    {
        final List<Component> components = strings.stream().map(Component::of).collect(Collectors.toCollection(ArrayList::new));
        final List<Bridge> startBridges = components.stream()
                                                    .filter(c -> c.getLeftPort() == 0 || c.getRightPort() == 0)
                                                    .map(Bridge::start)
                                                    .collect(Collectors.toList());

        final List<Bridge> allBridges = findStrongestBridge(startBridges, components, new ArrayList<>(components.size()));
        int maxStrength = allBridges.stream().mapToInt(Bridge::getStrength).max().getAsInt();
        return maxStrength;
    }

    private static List<Bridge> findStrongestBridge(final List<Bridge> bridges, final List<Component> allComponents, final List<Bridge> allBridgesAccumulator)
    {
        for (final Bridge currentBridge : bridges)
        {
            // This is stupidly expensive, but simple...
            //   The input is very small so it works. Alternative is to keep track of unused e.g. in Bridge
            final List<Bridge> extensionsOfBridge = allComponents.stream()
                                                                 .map(currentBridge::extend).filter(Optional::isPresent)
                                                                 .map(Optional::get).collect(Collectors.toList());

            if (extensionsOfBridge.isEmpty())
            {
                allBridgesAccumulator.add(currentBridge);
            }
            else
            {
                findStrongestBridge(extensionsOfBridge, allComponents, allBridgesAccumulator);
            }
        }

        return allBridgesAccumulator;
    }


    static class Bridge
    {
        private final LinkedHashSet<Component> iComponents;
        private final Component iEnd;

        private Bridge(final Component component, final LinkedHashSet<Component> components)
        {
            iEnd = component;
            iComponents = components;
            iComponents.add(component);
        }

        public static Bridge start(final Component c)
        {
            return c.getLeftPort() == 0 ? new Bridge(c, new LinkedHashSet<>(0)) : new Bridge(c.flip(), new LinkedHashSet<>(0));
        }

        public Optional<Bridge> extend(final Component c)
        {
            if (!iComponents.contains(c)) // no need to check c.flip since we base equality and hash on ID
            {
                if (iEnd.getRightPort() == c.getLeftPort())
                {
                    return Optional.of(new Bridge(c, new LinkedHashSet<>(iComponents)));
                }
                else if (iEnd.getRightPort() == c.getRightPort())
                {
                    return Optional.of(new Bridge(c.flip(), new LinkedHashSet<>(iComponents)));
                }
            }

            return Optional.empty();
        }

        public int getStrength()
        {
            return iComponents.stream().mapToInt(Component::getStrength).sum();
        }

        @Override
        public String toString()
        {
            return "Bridge{" + iComponents + '}';
        }
    }

    static class Component
    {
        private final static AtomicInteger cID = new AtomicInteger(0);
        private final int iId; // will need ID if there are multiple Components with the same port set-up
        private final int iLeftPort;
        private final int iRightPort;

        public Component(final int leftPort, final int rightPort, final int id)
        {
            iLeftPort = leftPort;
            iRightPort = rightPort;
            iId = id;
        }

        static Component of(final String line)
        {
            final int[] ints = Arrays.stream(line.split("/")).mapToInt(Integer::parseInt).toArray();
            final int minPort = Math.min(ints[0], ints[1]);
            final int maxPort = Math.max(ints[0], ints[1]);

            return new Component(minPort, maxPort, cID.getAndIncrement());
        }

        public Component flip()
        {
            return new Component(getRightPort(), getLeftPort(), iId); // flip has the same id
        }

        public int getLeftPort()
        {
            return iLeftPort;
        }

        public int getRightPort()
        {
            return iRightPort;
        }
        
        public int getStrength()
        {
            return iLeftPort + iRightPort;
        }

        @Override
        public String toString()
        {
            return "[("+iId+") " + iLeftPort + "/" + iRightPort +"]";
        }

        @Override
        public boolean equals(final Object o)
        {
            return (this == o) || iId == ((Component)o).iId; // Advent-of-code meh... no type checking! =)
        }

        @Override
        public int hashCode()
        {
            return iId; // Ony use ID since we want flip to match, same in equals
        }
    }

    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------

    private static void test() throws IOException
    {
        final List<String> lines = Arrays.asList(
            "0/2",
            "2/2",
            "2/3",
            "3/4",
            "3/5",
            "0/1",
            "10/1",
            "9/10");

        Util.require(part1(lines), 31);

        System.out.println("All tests ok");
    }
}
