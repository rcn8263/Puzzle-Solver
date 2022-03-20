package puzzles.clock;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Single configuration of the clock puzzle.
 */
public class ClockConfig implements Configuration {
    private final int hours;
    private final int start;
    private final int end;

    /**
     * Constructor that makes a new configuration.
     *
     * @param hours number of hours on the clock
     * @param start current starting position on the clock
     * @param end ending position on the clock that you want to reach
     */
    public ClockConfig(int hours, int start, int end) {
        this.hours = hours;
        this.start = start;
        this.end = end;
    }

    /**
     * @return an ArrayList of all possible successor configurations for
     * the current configuration
     */
    @Override
    public Collection<Configuration> getSuccessors() {
        Collection<Configuration> successors = new ArrayList<>();
        if (start-1 == 0) {
            successors.add(new ClockConfig(
                    hours, hours, end));
        }
        else {
            successors.add(new ClockConfig(
                    hours, start-1, end));
        }
        successors.add(new ClockConfig(
                hours, (start%hours)+1, end));

        return successors;
    }

    /**
     * @return true if the goal configuration for the clock puzzle has
     * been reached
     */
    @Override
    public boolean isGoal() {
        if (start == end) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Compares current config to the given config
     *
     * @param other config to compare to
     * @return true if both configurations are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof ClockConfig) {
            ClockConfig c = (ClockConfig) other;
            result = this.hours == c.hours &&
                    this.start == c.start &&
                    this.end == c.end;
        }
        return result;
    }

    /**
     * @return integer representing the hashcode
     */
    @Override
    public int hashCode() {
        return hours + start + end;
    }

    /**
     * @return Starting position of current configuration
     */
    @Override
    public String toString() {
        return String.valueOf(start);
    }
}
