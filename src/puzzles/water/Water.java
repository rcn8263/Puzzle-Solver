package puzzles.water;

import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.util.ArrayList;
import java.util.List;

/**
 * Simulates the water puzzle. Uses the Solver to go through all possible
 * configurations of the puzzle and attempts to find a solution. Prints out
 * starting arguments, number of total configurations, number of unique
 * configurations, and the solution if there is one
 */
public class Water {
    /**
     * The main method that runs the puzzle and outputs the solution
     * if there is one
     *
     * @param args Must be at least 3 integer arguments. The 1st is the
     *             desired amount of water you want in a bucket, the
     *             remaining integer arguments represent the buckets
     *             with their maximum capacity.
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println(("Usage: java Water amount bucket1 bucket2 ..."));
        }
        else {
            //Gets desired amount and bucket capacities from arguments
            int desiredAmount = Integer.valueOf(args[0]);
            ArrayList<Integer> bucketCapacities = new ArrayList<>();
            for (int i = 1; i < args.length; i++) {
                bucketCapacities.add(Integer.valueOf(args[i]));
            }
            ArrayList<Integer> bucketAmounts = new ArrayList<>();
            for (int i: bucketCapacities) {
                bucketAmounts.add(0);
            }

            //Makes a new solver and starting configuration
            Solver waterSolver = new Solver();
            WaterConfig start = new WaterConfig(
                    desiredAmount, bucketCapacities, bucketAmounts);

            //Get path if there is one
            List<Configuration> solution = waterSolver.doBFS(start);

            //Print output
            System.out.println("Amount: " + start.getDesiredAmount() +
                    ", Buckets: " + start.getBucketCapacities().toString());
            System.out.println("Total configs: " +
                    waterSolver.getTotalConfigs());
            System.out.println("Unique configs: " +
                    waterSolver.getUniqueConfigs());
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
