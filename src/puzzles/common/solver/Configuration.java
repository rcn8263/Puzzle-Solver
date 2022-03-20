package puzzles.common.solver;

import java.util.Collection;

/**
 * Configuration interface that represents a single configuration of a puzzle.
 * The Solver class uses this configuration to solve a puzzle.
 */
public interface Configuration {
    /**
     * @return ArrayList of all possible successors.
     */
    Collection<Configuration> getSuccessors();

    /**
     * @return true if the current configuration is the goal configuration.
     * Returns false otherwise.
     */
    boolean isGoal();
}

