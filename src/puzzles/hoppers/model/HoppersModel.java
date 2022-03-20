package puzzles.hoppers.model;

import puzzles.common.Observer;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Model of the hoppers game. Contains the configuration of the game and
 * all of the available commands. Notifies the view when a change is made
 */
public class HoppersModel {
    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, HoppersClientData>> observers = new LinkedList<>();

    /** the current configuration */
    private HoppersConfig currentConfig;

    /** the name of the file that has the starting configuration */
    private String filename;

    /** indicates if a frog is selected and where that frog is located on the board*/
    private boolean isSelected = false;
    private int selectedRow;
    private int selectedCol;

    /**
     * Create a new instance of the model for the hoppers puzzle
     *
     * @param filename name of file to read
     * @param rows number of rows on the board
     * @param cols number of columns on the board
     * @param board 2d array of the board
     */
    public HoppersModel(String filename, int rows, int cols, String[][] board) {
        this.filename = filename;
        this.currentConfig = new HoppersConfig(rows, cols, board);
    }

    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<HoppersModel, HoppersClientData> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void alertObservers(HoppersClientData data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }

    /**
     * If the current state of the puzzle is solvable, the puzzle
     * advances to the next step in the solution with an indication that
     * it was successful. Otherwise the puzzle remains in the same state
     * and indicate there is no solution.
     */
    public void hint() {
        if (this.currentConfig.isGoal()) {
            alertObservers(new HoppersClientData("Already solved!"));
        }
        else {
            Solver hopperSolver = new Solver();
            List<Configuration> solution = hopperSolver.doBFS(this.currentConfig);
            if (solution.isEmpty()) {
                alertObservers(new HoppersClientData("No solution!"));
            } else {
                this.currentConfig = (HoppersConfig) solution.get(1);
                alertObservers(new HoppersClientData("Next Step!"));
            }
        }
    }

    /**
     * The user will provide the path and name of a puzzle file for the
     * game to load. If the file is readable the new puzzle file is loaded
     * and displayed, along with an indication of success. If the file cannot
     * be read, an error message is displayed and the previous puzzle
     * file remains loaded.
     *
     * @param filename name of file to load in
     */
    public void load(String filename) {
        this.filename = filename;
        try {
            //Read the file specified from input
            BufferedReader in = new BufferedReader(
                    new FileReader(filename));

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

            this.getCurrentConfig().setGame(numRows, numCols, board);
            alertObservers(new HoppersClientData("Loaded: " + filename));
        }
        catch (IOException e) {
            alertObservers(new HoppersClientData("Failed to load: " + filename));
        }

    }

    /**
     * Selection works in two parts.
     * For the first selection, the user selects a cell
     * on the board with the intention of selecting the piece at that location.
     * If there is a piece there, there is an indication and selection
     * advances to the second part. Otherwise if there is no piece there
     * an error message is displayed and selection ends.
     *
     * For the second selection, the user selects another cell
     * on the board with the intention of moving the previously selected piece
     * to this location. If the move is valid, the move is made and the board
     * is updated and displays a message for the user. If the move is
     * invalid, and error message is displayed.
     *
     * @param row selected row on the board
     * @param col selected col on the board
     */
    public void select(int row, int col) {
        if (this.currentConfig.isGoal()) {
            alertObservers(new HoppersClientData("Already solved!"));
        }
        else {
            //No frog is already selected and player selects a space with a frog
            if (this.currentConfig.validSelection(row, col) &&
                    this.isSelected == false) {
                isSelected = true;
                selectedRow = row;
                selectedCol = col;

                alertObservers(new HoppersClientData(
                        "Selected (" + row + ", " + col + ")"));
            }
            //Frog is selected and move is valid
            else if (this.currentConfig.validMove(this.selectedRow,
                    this.selectedCol, row, col) && this.isSelected == true) {
                isSelected = false;

                String[][] newBoard = this.currentConfig.boardSuccessor(
                        this.selectedRow, this.selectedCol, row, col);
                this.currentConfig.setBoard(newBoard);

                alertObservers(new HoppersClientData(
                        "Jumped from (" + this.selectedRow + ", " +
                                this.selectedCol + ")" + " to " +
                                "(" + row + ", " + col + ")"));
            }
            //Selection is not valid
            else {
                if (isSelected) {
                    isSelected = false;
                    alertObservers(new HoppersClientData(
                            "Can't jump from (" + this.selectedRow + ", " +
                                    this.selectedCol + ")" + " to " +
                                    "(" + row + ", " + col + ")"));
                } else {
                    alertObservers(new HoppersClientData(
                            "Invalid selection (" + row + ", " + col + ")"));
                }
            }
        }
    }

    /**
     * User exits the game and the program ends
     */
    public void quit() {
        System.exit(0);
    }

    /**
     * The previously loaded file is loaded and the board is reset to its
     * original configuration.
     */
    public void reset() {
        load(this.filename);
        alertObservers(new HoppersClientData("Puzzle reset!"));
    }

    /**
     * @return current configuration of HoppersConfig
     */
    public HoppersConfig getCurrentConfig() {
        return currentConfig;
    }
}
