package puzzles.common.solver;

import java.util.*;

/**
 * Solves puzzles using a BFS algorithm.
 */
public class Solver {
    private int totalConfigs = 1;
    private int uniqueConfigs = 1;

    /**
     * Creates new solver object
     */
    public Solver() {}

    /**
     * Runs a BFS algorithm to go through all possible configurations of a
     * puzzle and generates a path from the starting config to the ending
     * config. Keeps track of the total number of configurations and the
     * number of unique configurations.
     *
     * @param start Starting configuration
     * @return A list that represents the path from the starting config
     * to the ending config.
     */
    public List<Configuration> doBFS(Configuration start) {
        //Make queue of configurations to go through
        List<Configuration> queue = new LinkedList<>();
        queue.add(start);

        //Make map of predecessors for already visited configurations
        Map<Configuration, Configuration> predecessors = new HashMap<>();
        predecessors.put(start, start);

        //Construct predecessors map
        Configuration end = null;
        while (!queue.isEmpty()) {
            Configuration current = queue.remove(0);
            if (current.isGoal()) {
                end = current;
                break;
            }
            for (Configuration successor: current.getSuccessors()) {
                totalConfigs++;
                if (!predecessors.containsKey(successor)) {
                    uniqueConfigs++;
                    predecessors.put(successor, current);
                    queue.add(successor);
                }
            }
        }

        //Construct path
        List<Configuration> path = new LinkedList<>();
        if (predecessors.containsKey(end)) {
            Configuration current = end;
            while (!current.equals(start)) {
                path.add(0, current);
                current = predecessors.get(current);
            }
            path.add(0, start);
        }

        return path;
    }

    /**
     * @return Total number of configurations generated during doBFS
     */
    public int getTotalConfigs() {
        return totalConfigs;
    }

    /**
     * @return Number of unique configurations generated during doBFS
     */
    public int getUniqueConfigs() {
        return uniqueConfigs;
    }

}
