package puzzles.hoppers.solver;

import puzzles.common.solver.Solver;
import puzzles.common.solver.Configuration;
import puzzles.hoppers.model.HoppersConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Simulates the hoppers puzzle. Uses the Solver to go through all possible
 * configurations of the puzzle and attempts to find a solution. Prints out
 * starting arguments, number of total configurations, number of unique
 * configurations, and the solution if there is one
 */
public class Hoppers {
    /**
     * The main method that runs the puzzle and outputs the solution
     * if there is one
     *
     * @param args the first and only argument should be the name of a
     *             file that has the dimensions of the board and starting
     *             configuration of the board
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Hoppers filename");
        }
        else {
            try {
                //Read the file specified from input
                BufferedReader in = new BufferedReader(
                        new FileReader(args[0]));

                System.out.println("File: " + args[0]);

                //Get dimensions of the board and create it
                String[] nextLine = in.readLine().split(" ");
                int numRows = Integer.parseInt(nextLine[0]);
                int numCols = Integer.parseInt(nextLine[1]);
                String[][] board = new String[numRows][numCols];

                //Sets up all of the frogs and spaces on the board
                for (int i = 0; i < numRows; i++) {
                    nextLine = in.readLine().split(" ");
                    for (int j = 0; j < nextLine.length; j++) {
                        board[i][j] = nextLine[j];
                        System.out.print(nextLine[j] + " ");
                    }
                    System.out.println();
                }

                //Make a new solver and starting configuration
                Solver hopperSolver = new Solver();
                HoppersConfig start = new HoppersConfig(
                        numRows, numCols, board);

                //Get path if there is one
                List<Configuration> solution = hopperSolver.doBFS(start);

                //Print output
                System.out.println("Total configs: " +
                        hopperSolver.getTotalConfigs());
                System.out.println("Unique configs: " +
                        hopperSolver.getUniqueConfigs());
                if (!solution.isEmpty()) {
                    for (int i = 0; i < solution.size(); i++) {
                        System.out.println("Step " + i + ": \n" +
                                solution.get(i).toString());
                    }
                }
                else {
                    System.out.println("No solution");
                }

            }
            catch (IOException e) {System.out.println(e.getMessage());}

        }
    }
}
