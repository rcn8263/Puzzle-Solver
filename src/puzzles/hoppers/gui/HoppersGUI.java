package puzzles.hoppers.gui;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersClientData;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class HoppersGUI extends Application implements Observer<HoppersModel, HoppersClientData> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    // Images used in the game
    private Image redFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"red_frog.png"));
    private Image greenFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green_frog.png"));
    private Image lilyPad = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"lily_pad.png"));
    private Image water = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"water.png"));

    //Symbols to represent spaces on board
    private final String FROG_GREEN = "G";
    private final String FROG_RED = "R";
    private final String LILY_PAD = ".";
    private final String WATER = "*";

    //Model for the view and controller
    private HoppersModel model;

    //Label that displays a message towards the top of the window
    private Label labelMessage;

    //List of buttons on the board
    private ArrayList<Button> buttonsBoard = new ArrayList<>();

    //Load button
    private Button load;

    //Reset button
    private Button reset;

    //Hint button
    private Button hint;

    //BorderPane that contains scene elements
    //BorderPane borderPane;

    //Parts of the GUI
    Stage stage;
    Scene scene;
    BorderPane borderPane;

    //Board size
    private int rows;
    private int cols;

    //2d array of board
    private String[][] board;

    /**
     * Helper function. Makes a grid of buttons that are put into
     * the center of the GridPane.
     *
     * @return GridPane of buttons
     */
    private GridPane makeGridPane() {
        GridPane gridPane = new GridPane();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Button button = new Button();
                if (this.board[row][col].equals(FROG_GREEN)) {
                    button.setGraphic(new ImageView(greenFrog));
                }
                else if (this.board[row][col].equals(FROG_RED)) {
                    button.setGraphic(new ImageView(redFrog));
                }
                else if (this.board[row][col].equals(LILY_PAD)) {
                    button.setGraphic(new ImageView(lilyPad));
                }
                else if (this.board[row][col].equals(WATER)) {
                    button.setGraphic(new ImageView(water));
                }
                int finalRow = row;
                int finalCol = col;
                button.setOnAction(event -> {
                    this.model.select(finalRow, finalCol);
                });
                buttonsBoard.add(button);
                gridPane.add(button, col, row);
            }
        }
        return gridPane;
    }

    /**
     * Changes the image on the buttons when the board is updated.
     */
    private void updateBoard() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (this.board[row][col].equals(FROG_GREEN)) {
                    this.buttonsBoard.get((row * this.cols) + col).
                            setGraphic(new ImageView(greenFrog));
                } else if (this.board[row][col].equals(FROG_RED)) {
                    this.buttonsBoard.get((row * this.cols) + col).
                            setGraphic(new ImageView(redFrog));
                } else if (this.board[row][col].equals(LILY_PAD)) {
                    this.buttonsBoard.get((row * this.cols) + col).
                            setGraphic(new ImageView(lilyPad));
                } else if (this.board[row][col].equals(WATER)) {
                    this.buttonsBoard.get((row * this.cols) + col).
                            setGraphic(new ImageView(water));
                }
            }
        }
    }

    /**
     * Initializes the view
     */
    public void init() {
        String filename = getParameters().getRaw().get(0);

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

            this.rows = numRows;
            this.cols = numCols;
            this.board = board;

            this.model = new HoppersModel(filename, numRows, numCols, board);
            model.addObserver(this::update);
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Constructs the layout for the game. The scene contains a BorderPane
     * where the top has a label that displays a message. The center
     * contains a GridPane of buttons that represent the game board.
     * The bottom has an HBox that has the 3 buttons load, reset, and hint.
     *
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.borderPane = new BorderPane();

        this.labelMessage = new Label(" top label");
        borderPane.setTop(labelMessage);
        BorderPane.setAlignment(labelMessage, Pos.CENTER);

        borderPane.setCenter(makeGridPane());

        HBox bottomButtons = new HBox();
        this.load = new Button();
        this.load.setText("Load");
        this.load.setOnAction(event -> {
            try {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.setInitialDirectory(new File("./data/hoppers"));
                String filename = "data/hoppers/" +
                        fileChooser.showOpenDialog(null).getName();
                this.model.load(filename);
            }
            catch (NullPointerException e) {
                this.model.load("");
            }
        });
        this.reset = new Button();
        this.reset.setText("Reset");
        this.reset.setOnAction(event -> this.model.reset());
        this.hint = new Button();
        this.hint.setText("Hint");
        this.hint.setOnAction(event -> this.model.hint());
        bottomButtons.getChildren().add(this.load);
        bottomButtons.getChildren().add(this.reset);
        bottomButtons.getChildren().add(this.hint);
        bottomButtons.setAlignment(Pos.CENTER);

        borderPane.setBottom(bottomButtons);
        BorderPane.setAlignment(bottomButtons, Pos.CENTER);

        this.scene = new Scene(borderPane);
        this.stage = stage;
        this.stage.setScene(scene);
        this.stage.setTitle("Hoppers GUI");
        this.stage.setResizable(false);
        this.stage.show();
    }

    /**
     * Updates the UI. The label at the top of the gridPane changes based
     * on the message given with hoppersClientData. The images on the buttons
     * in the center of the gridPane change based on the changes done to
     * the board.
     *
     * @param hoppersModel the model that has changed
     * @param hoppersClientData gives a message to display in the label.
     */
    @Override
    public void update(HoppersModel hoppersModel, HoppersClientData hoppersClientData) {
        this.board = this.model.getCurrentConfig().getBoard();
        this.rows = this.model.getCurrentConfig().getNumRows();
        this.cols = this.model.getCurrentConfig().getNumCols();

        String message = hoppersClientData.getMessage();
        this.labelMessage.setText(message);
        if (message.contains("Loaded:")) {
            this.buttonsBoard = new ArrayList<>();
            this.borderPane.setCenter(makeGridPane());
            this.stage.sizeToScene();
        }
        else {
            updateBoard();
        }
    }

    /**
     * launches the JavaFX GUI.
     *
     * @param args name of initial file
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}
