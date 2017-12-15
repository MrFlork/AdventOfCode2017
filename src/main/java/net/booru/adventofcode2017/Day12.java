package net.booru.adventofcode2017;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

class Day12
{
    static void run() throws IOException
    {
        test();

        int value = part1(Files.readAllLines(Paths.get("day12.in")));
        int value2 = part2(Files.readAllLines(Paths.get("day12.in")));

        System.out.println("part1: " + value);
        System.out.println("part2: " + value2);
    }

    private static int part1(final List<String> lines) throws IOException
    {
        final HashMap<Integer, Node> nodeMap = parseNodes(lines);

        int count = depthFirstSearch(nodeMap.get(0));

        return count;
    }

    private static int part2(final List<String> lines) throws IOException
    {
        final HashMap<Integer, Node> nodeMap = parseNodes(lines);

        int groupCount = 0;
        while (!nodeMap.isEmpty())
        {
            final Node startNode = nodeMap.entrySet().iterator().next().getValue();
            depthFirstSearch(startNode); // will set nodes as visited
            groupCount++;

            // remove visited nodes.
            for (Node node : nodeMap.values().stream().filter(Node::isVisited).toArray(Node[]::new))
            {
                if (node.isVisited())
                {
                    nodeMap.remove(node.getId());
                }
            }
        }

        return groupCount;
    }

    private static int depthFirstSearch(final Node node)
    {
        node.setVisited();
        int nodeCount = 1; // current node
        for (Node neighbour : node.getNeighbours())
        {
            if (!neighbour.isVisited())
            {
                nodeCount += depthFirstSearch(neighbour);
            }
        }

        return nodeCount;
    }

    private static HashMap<Integer, Node> parseNodes(final List<String> lines) throws IOException
    {
        final HashMap<Integer, Node> nodeMap = new HashMap<>(lines.size() * 2);
        for (String nodeString : lines)
        {
            final String[] tmp = nodeString.split("<->");
            int nodeId = Integer.parseInt(tmp[0].trim());
            nodeMap.putIfAbsent(nodeId, new Node(nodeId));
        }

        for (String nodeString : lines)
        {
            final String[] tmp = nodeString.split("<->");
            final Node node = nodeMap.get(Integer.parseInt(tmp[0].trim()));

            final Stream<String> stream = Arrays.stream(tmp[1].split(","));
            final Node[] nodes = stream.map((String nodeId) -> nodeMap.get(Integer.parseInt(nodeId.trim()))).toArray(Node[]::new);
            node.setNeighbors(nodes);
        }

        return nodeMap;
    }


    static class Node
    {
        private final int iId;
        private boolean iVisited;
        private Node[] iNeighbours;

        public Node(final int id)
        {
            iId = id;
            iNeighbours = null;
        }

        public void setNeighbors(final Node[] neighbors)
        {
            iNeighbours = neighbors;
        }

        public Node[] getNeighbours()
        {
            return iNeighbours;
        }

        public void setVisited()
        {
            iVisited = true;
        }

        public boolean isVisited()
        {
            return iVisited;
        }

        @Override
        public boolean equals(final Object node)
        {
            return iId == ((Node)node).iId;
        }

        @Override
        public int hashCode()
        {
            return Integer.hashCode(iId);
        }

        public int getId()
        {
            return iId;
        }
    }


    //--------------------------------------------------------------------------------------------------
    //--------------------------------------------------------------------------------------------------

    private static void test() throws IOException
    {
        final List<String> lines = Arrays.asList("0 <-> 2",
                                                 "1 <-> 1",
                                                 "2 <-> 0, 3, 4",
                                                 "3 <-> 2, 4",
                                                 "4 <-> 2, 3, 6",
                                                 "5 <-> 6",
                                                 "6 <-> 4, 5");

        Util.require(part1(lines), 6);

        System.out.println("All tests ok");
    }
}
