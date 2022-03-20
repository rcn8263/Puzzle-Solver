package puzzles.hoppers.model;

import puzzles.common.solver.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Single configuration of the hoppers puzzle
 */
public class HoppersConfig implements Configuration {
    private final String FROG_GREEN = "G";
    private final String FROG_RED = "R";
    private final String SPACE_EMPTY = ".";
    private final String SPACE_INVALID = "*";

    private int numRows;
    private int numCols;
    private String[][] board;


    public HoppersConfig (int rows, int col, String[][] board) {
        this.numRows = rows;
        this.numCols = col;
        this.board = board;
    }

    /**
     * Checks if the space the frog is going to move to is valid
     *
     * @param currRow current row of the frog
     * @param currCol current col of the frog
     * @param newRow row that frog will move to
     * @param newCol col that frog will move to
     * @return true if move is valid, false otherwise
     */
    public boolean validMove(int currRow, int currCol,
                              int newRow, int newCol) {
        //Check if new position is on board
        //returns false if not
        if (newRow >= this.numRows || newRow < 0 ||
            newCol >= this.numCols || newCol < 0) {
            return false;
        }
        //Check if the frog being jumped over is green
        //return false if it is not a green frog
        if (!board[currRow + (newRow-currRow)/2]
                [currCol + (newCol-currCol)/2].equals(FROG_GREEN)) {
            return false;
        }
        //Check if the new position already has a frog
        if (board[newRow][newCol].equals(FROG_GREEN) ||
                board[newRow][newCol].equals(FROG_RED)) {
            return false;
        }
        //Check if frog moves an even number of spaces
        if (((newRow-currRow)%2 != 0) || ((newCol-currCol)%2 != 0)) {
            return false;
        }
        //Check that end space is not directly next to current space
        if ((newRow-currRow <= 1 && newRow-currRow >= -1) &&
                (newCol-currCol <= 1 && newCol-currCol >= -1)) {
            return false;
        }

        return true;
    }

    /**
     * Copies the current 2d board array and changes it to a
     * successor configuration
     *
     * @param currRow int row of the frog that will move
     * @param currCol int col of the frog that will move
     * @param newRow int row of where the frog will move
     * @param newCol int col of where the frog will move
     * @return new board after a frog makes a move
     */
    public String[][] boardSuccessor(int currRow, int currCol,
                                      int newRow, int newCol) {
        //Copies current board
        String[][] newBoard = new String[this.numRows][this.numCols];
        for (int row = 0; row < this.numRows; row++) {
            for (int col = 0; col < this.numCols; col++) {
                newBoard[row][col] = this.board[row][col];
            }
        }

        //Change spaces based on move frog made
        newBoard[newRow][newCol] = newBoard[currRow][currCol];
        newBoard[currRow][currCol] = this.SPACE_EMPTY;
        newBoard[currRow + (newRow-currRow)/2][currCol + (newCol-currCol)/2]
                = this.SPACE_EMPTY;

        return newBoard;
    }

    /**
     * Checks if the given coordinates has a frog to select
     *
     * @param row row of current board to check
     * @param col col of current board to check
     * @return true if there is a frog at the given coordinates, false otherwise
     */
    public boolean validSelection(int row, int col) {
        return (board[row][col].equals(this.FROG_RED) ||
                board[row][col].equals(this.FROG_GREEN));
    }

    /**
     * Changes the current board to a new one
     * @param board 2d array of new board
     */
    public void setBoard(String[][] board) {
        this.board = board;
    }

    /**
     * @return current 2d board array
     */
    public String[][] getBoard() {
        return this.board;
    }

    /**
     * @return number of rows on board
     */
    public int getNumRows() {
        return this.numRows;
    }

    /**
     * @return number of columns on board
     */
    public int getNumCols() {
        return this.numCols;
    }

    /**
     * Changes the current board to a new one
     * @param rows number of rows on new board
     * @param cols number of columns on new board
     * @param board 2d array of new board
     */
    public void setGame(int rows, int cols, String[][] board) {
        this.numRows = rows;
        this.numCols = cols;
        this.board = board;
    }

    /**
     * @return an ArrayList of all possible successor configurations for
     * the current configuration
     */
    @Override
    public Collection<Configuration> getSuccessors() {
        Collection<Configuration> successors = new ArrayList<>();

        for (int row = 0; row < this.numRows; row++) {
            for (int col = 0; col < this.numCols; col++) {
                if (board[row][col].equals(this.FROG_RED) ||
                    board[row][col].equals(this.FROG_GREEN)) {
                    //Move Up-Left
                    if (validMove(row, col, row-2, col-2)) {
                        successors.add(new HoppersConfig(this.numRows, this.numCols,
                                boardSuccessor(
                                        row, col, row-2, col-2)));
                    }
                    //Move Up-Right
                    if (validMove(row, col, row-2, col+2)) {
                        successors.add(new HoppersConfig(this.numRows, this.numCols,
                                boardSuccessor(
                                        row, col, row-2, col+2)));
                    }
                    //Move Down-Right
                    if (validMove(row, col, row+2, col+2)) {
                        successors.add(new HoppersConfig(this.numRows, this.numCols,
                                boardSuccessor(
                                        row, col, row+2, col+2)));
                    }

                    //Move Down-Left
                    if (validMove(row, col, row+2, col-2)) {
                        successors.add(new HoppersConfig(this.numRows, this.numCols,
                                boardSuccessor(
                                        row, col, row+2, col-2)));
                    }


                    //Frog can move in all 8 directions
                    if (row%2 == 0 && col%2 == 0) {
                        //Move Up
                        if (validMove(row, col, row-4, col)) {
                            successors.add(new HoppersConfig(this.numRows, this.numCols,
                                    boardSuccessor(row, col, row-4, col)));
                        }
                        //Move Right
                        if (validMove(row, col, row, col+4)) {
                            successors.add(new HoppersConfig(this.numRows, this.numCols,
                                    boardSuccessor(row, col, row, col+4)));
                        }
                        //Move Down
                        if (validMove(row, col, row+4, col)) {
                            successors.add(new HoppersConfig(this.numRows, this.numCols,
                                    boardSuccessor(row, col, row+4, col)));
                        }
                        //Move Left
                        if (validMove(row, col, row, col-4)) {
                            successors.add(new HoppersConfig(this.numRows, this.numCols,
                                    boardSuccessor(row, col, row, col-4)));
                        }
                    }
                }
            }
        }

        return successors;
    }

    /**
     * @return true if there is a red frog and no green frogs on the board.
     * Otherwise, returns false.
     */
    @Override
    public boolean isGoal() {
        boolean hasRed = false;
        boolean hasGreen = false;

        for (int row = 0; row < this.numRows; row++) {
            for (int col = 0; col < this.numCols; col++) {
                if (board[row][col].equals(this.FROG_GREEN)) {
                    hasGreen = true;
                }
                else if (board[row][col].equals(this.FROG_RED)) {
                    hasRed = true;
                }
            }
        }
        return hasRed && !hasGreen;
    }

    /**
     * Compares current board config to the given board config
     *
     * @param other other config to compare to
     * @return true if both board configurations are equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof HoppersConfig) {
            HoppersConfig h = (HoppersConfig) other;
            result = Arrays.deepEquals(this.board, h.board);
        }
        return result;
    }

    /**
     * @return integer representing the hashcode of the 2d board array
     */
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(this.board);
    }

    /**
     * @return String that represents the current board
     * configuration in a grid format
     */
    @Override
    public String toString() {
        String result = "";

        for (int row = 0; row < this.numRows; row++) {
            for (int col = 0; col < this.numCols; col++) {
                result += " " + this.board[row][col];
            }
            result += "\n";
        }

        return result;
    }
}
