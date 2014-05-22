import java.util.*;


/**
 * Created by Langley on 5/21/14.
 * uses a connection graph to look up neighbours.
 */
public final class GraphSearch {
    private Map<Integer, Set<Integer>> graph;

    /**
     * Construct a new ConnectionGraphSearch.
     *
     * @param graph The connection graph in the form of a map from ID to the set of neighbouring IDs.
     */
    public GraphSearch(Map<Integer, Set<Integer>> graph) {
        setGraph(graph);
    }

    /**
     * Set the neighbourhood graph.
     *
     * @param newGraph The new neighbourhood graph.
     */
    public void setGraph(Map<Integer, Set<Integer>> newGraph) {
        this.graph = newGraph;
    }

    /**
     * Get the neighbourhood graph.
     *
     * @return The neighbourhood graph.
     */
    public Map<Integer, Set<Integer>> getGraph() {
        return graph;
    }

    /**
     * Do a breadth first search from one location to the closest (in terms of number of nodes) of a set of goals.
     *
     * @param start The location we start at.
     * @param goals The set of possible goals.
     * @return The path from start to one of the goals, or null if no path can be found.
     */
    public List<Integer> breadthFirstSearch(Integer start, Integer... goals) {
        return breadthFirstSearch(start, Arrays.asList(goals));
    }

    /**
     * Do a breadth first search from one location to the closest (in terms of number of nodes) of a set of goals.
     *
     * @param start The location we start at.
     * @param goals The set of possible goals.
     * @return The path from start to one of the goals, or null if no path can be found.
     */
    public List<Integer> breadthFirstSearch(Integer start, Collection<Integer> goals) {
        List<Integer> open = new LinkedList<Integer>();
        Map<Integer, Integer> ancestors = new HashMap<Integer, Integer>();
        open.add(start);
        Integer next = null;
        boolean found = false;
        ancestors.put(start, start);
        do {
            next = open.remove(0);
            if (isGoal(next, goals)) {
                found = true;
                break;
            }
            Collection<Integer> neighbours = graph.get(next);
            if (neighbours.isEmpty()) {
                continue;
            }
            for (Integer neighbour : neighbours) {
                if (isGoal(neighbour, goals)) {
                    ancestors.put(neighbour, next);
                    next = neighbour;
                    found = true;
                    break;
                } else {
                    if (!ancestors.containsKey(neighbour)) {
                        open.add(neighbour);
                        ancestors.put(neighbour, next);
                    }
                }
            }
        } while (!found && !open.isEmpty());
        if (!found) {
            // No path
            return null;
        }
        // Walk back from goal to start
        Integer current = next;
        List<Integer> path = new LinkedList<Integer>();
        do {
            path.add(0, current);
            current = ancestors.get(current);
            if (current == null) {
                throw new RuntimeException("Found a node with no ancestor!");
            }
        } while (current != start);

        //Insert start node
        if (!path.isEmpty()) {
            path.add(0, start);
        }
        return path;
    }

    private boolean isGoal(Integer e, Collection<Integer> test) {
        return test.contains(e);
    }

}
