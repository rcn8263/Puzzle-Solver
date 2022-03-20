package puzzles.clock;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.List;

/**
 * Simulates the clock puzzle. Uses the Solver to go through all possible
 * configurations of the puzzle and attempts to find a solution. Prints out
 * starting arguments, number of total configurations, number of unique
 * configurations, and the solution if there is one
 */
public class Clock {
    /**
     * The main method that runs the puzzle and outputs the solution
     * if there is one
     *
     * @param args Must be 3 integer arguments. The 1st is the number of
     *             hours on the clock, the 2nd is the starting hour, and
     *             the 3rd is the ending hour to stop on.
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java Clock hours start stop");
        }
        else {
            //Makes a solver and a starting configuration
            Solver clockSolver = new Solver();
            Configuration start = new ClockConfig(
                    Integer.parseInt(args[0]),
                    Integer.parseInt(args[1]),
                    Integer.parseInt(args[2]));

            //Get path if there is one
            List<Configuration> solution = clockSolver.doBFS(start);

            //Print output
            System.out.println("Hours: " + args[0] + ", Start: " + args[1] +
                    ", End: " + args[2]);
            System.out.println("Total configs: " +
                    clockSolver.getTotalConfigs());
            System.out.println("Unique configs: " +
                    clockSolver.getUniqueConfigs());
            if (!solution.isEmpty()) {
                for (int i = 0; i < solution.size(); i++) {
                    System.out.println("Step " + i + ": " +
                            solution.get(i).toString());
                }
            }
            else {
                System.out.println("No solution");
            }
        }
    }
}