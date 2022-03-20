package puzzles.hoppers.ptui;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersClientData;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.hoppers.model.HoppersModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Class definition for the text view and controller
 */
public class HoppersPTUI implements Observer<HoppersModel, HoppersClientData> {
    /** The model for the view and the controller */
    private HoppersModel model;

    /**
     * Constructs the PTUI
     *
     * @param filename name of file that contains dimensions of
     *                 the board and the layout of the board
     * @param rows number of rows on te board
     * @param cols number of columns on the board
     * @param board 2d array that represents the game board
     */
    public HoppersPTUI(String filename, int rows, int cols, String[][] board) {
        this.model = new HoppersModel(filename, rows, cols, board);
        displayBoard(board);
        displayHelp();
        initView();
    }

    /**
     * Repeatedly read commands from user
     */
    private void run() {
        Scanner in = new Scanner(System.in);
        for (; ; ) {
            String[] args = in.nextLine().split(" ");
            if (args.length > 0) {
                if (args[0].equals("h")) {
                    this.model.hint();
                } else if (args[0].equals("l") && args.length > 1) {
                    this.model.load(args[1]);
                } else if (args[0].equals("s") && args.length > 2) {
                    this.model.select(Integer.parseInt(args[1]),
                            Integer.parseInt(args[2]));
                } else if (args[0].equals("q")) {
                    this.model.quit();
                } else if (args[0].equals("r")) {
                    this.model.reset();
                } else {
                    displayHelp();
                }

            }
        }
    }

    /**
     * Initializes the view
     */
    public void initView() {
        this.model.addObserver(this);
        update(this.model, null);
    }

    /**
     * Prints the display of the board to standard output
     *
     * @param board 2d board array to display in output
     */
    private void displayBoard(String[][] board) {
        System.out.print("  ");
        for (int i = 0; i < board[0].length; i++) {
            System.out.print(" " + i);
        }
        System.out.print("\n  ");
        for (int i = 0; i < board[0].length; i++) {
            System.out.print("--");
        }
        System.out.println();

        for (int row = 0; row < board.length; row++) {
            System.out.print(row + "|");
            for (int col = 0; col < board[0].length; col++) {
                System.out.print(" " + board[row][col]);
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Prints out all available commands for the game
     */
    private void displayHelp() {
        System.out.println("h(int)          -- hint next move");
        System.out.println("l(oad) filename -- load new puzzle file");
        System.out.println("s(elect) r c    -- select cell at r, c");
        System.out.println("q(uit)          -- quit the game");
        System.out.println("r(eset)         -- reset the current game");
    }

    @Override
    public void update(HoppersModel model, HoppersClientData data) {
        if (data != null) {
            System.out.println(data.getMessage());
            displayBoard(model.getCurrentConfig().getBoard());
        }
    }

    /**
     * The main method used to play the game
     *
     * @param args 1 argument which is the name of the file used
     *             to build game board
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        }
        else {
            try {
                //Read the file specified from input
                BufferedReader in = new BufferedReader(
                        new FileReader(args[0]));

                System.out.println("Loaded: " + args[0]);

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
                    }
                }

                HoppersPTUI ptui = new HoppersPTUI(
                        args[0], numRows, numCols, board);
                ptui.run();
            }
            catch (IOException e) {System.out.println(e.getMessage());}
        }
    }
}
