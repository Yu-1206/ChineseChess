package chess.controls;

import chess.model.Chess;
import chess.model.GameModel;
import chess.model.Position;
import chess.model.Record;
import chess.player.GameHistory;
import chess.player.GameHistoryRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

@Data
public class GameController {

    @FXML
    private GridPane board;

    @FXML
    private ListView<String> listView;

    GameModel gameModel = new GameModel();

    private Chess selectedChess;

    private String playerRed;

    private String playerBlack;

    private String gameState;

    private int currentPlayer = 0;

    private LinkedList<Record> regretList = new LinkedList<>();

    private final String[] alphabet = {"A","B","C","D","E","F","G","H","I","J"};

    private int regretRedRemainTimes = 3;

    private int regretBlackRemainTimes = 3;

    private LocalTime startTime;

    private Long duration;

    private ArrayList<String> movementList = new ArrayList<>();

    @FXML
    private Label winLabel;

    @FXML
    private Button regretButton;

    @FXML
    private Button resignButton;

    @FXML
    private Button drawButton;

    @FXML
    private Button quitButton;

    @FXML
    private Button saveButton;

    private String path;
    @FXML
    public void initialize() throws IOException {
        drawButton.setDisable(true);
        listView.getStyleClass().add("list-view");
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                var square = createSquare();
                //square.getStyleClass().add("selected");
                board.add(square, j, i);
            }
        }

        //load part
        Map<Integer,String> data = GameHistoryController.dataMap;

        System.out.println(data);
        if (data.get(1)!=null && data.get(2)!=null){
            setPlayerRed(data.get(1));
            setPlayerBlack(data.get(2));
            setCurrentPlayer(Integer.parseInt(data.get(3)));
            setPath("src/main/resources/load/" + data.get(1) +"-"+ data.get(2)+".json");
            loadData(path);

        }else{
            //new game part
            createChess();
        }

        addChessOnBoard();
        winLabel.setVisible(false);

        startTime = LocalTime.now();

        setGameState("Incomplete");

    }


    private StackPane createSquare() {
        StackPane stackPane = new StackPane();
        stackPane.setOnMouseClicked(this::handleMouseClick);
        return stackPane;
    }

    @FXML
    private void handleMouseClick(MouseEvent event) {
        var square = (StackPane) event.getSource();
        var row = GridPane.getRowIndex(square);
        var col = GridPane.getColumnIndex(square);
        System.out.println("{" + row + "," + col + "}");

        if (!gameModel.getIsGameOver()) {

            if (selectedChess == null) {

                //select
                if (gameModel.getChessFromPoint(row, col) != null && gameModel.getChessFromPoint(row, col).getPlayer().equals(calculateCurrentPlayer(currentPlayer))) {
                    selectedChess = gameModel.getChessFromPoint(row, col);
                    selected(selectedChess);
                    System.out.println("selected " + selectedChess.getName());
                }

            } else if (selectedChess.getPlayer().equals(calculateCurrentPlayer(currentPlayer))) {
                //move
                Chess c = gameModel.getChessFromPoint(row, col);

                if (c == null) {

                    if (gameModel.Move(selectedChess, row, col)) {
                        System.out.println("move");

                        //record move history
                        Record record = new Record();
                        record.setChess(selectedChess);
                        record.setStart(selectedChess.getPosition());
                        record.setEnd(new Position(row, col));
                        regretList.add(record);

                        showMovement(selectedChess,row,col);
                        changePosition(selectedChess, row, col);

                        unSelected(selectedChess);
                        selectedChess = null;
                        currentPlayer += 1;
                        System.out.println("unselected");


                    }

                } else if (!c.getPlayer().equals(selectedChess.getPlayer())) {
                    //capture
                    if (gameModel.Move(selectedChess, row, col)) {
                        System.out.println("eat" + c.getPlayer());

                        //record move history
                        Record record = new Record();
                        record.setChess(selectedChess);
                        record.setStart(selectedChess.getPosition());
                        record.setEnd(new Position(row, col));
                        record.setRemovedChess(c);
                        regretList.add(record);

                        showMovement(selectedChess,row,col); 
                        removeChess(c);
                        changePosition(selectedChess, row, col);
                        unSelected(selectedChess);

                        //

                        System.out.println("unselected" + selectedChess.getName());
                        selectedChess = null;
                        currentPlayer += 1;
                    }

                } else if (c.getPlayer().equals(selectedChess.getPlayer())) {
                    unSelected(selectedChess);
                    System.out.println("unselected" + selectedChess.getName());
                    selectedChess = null;

                }
            }

            if (gameModel.getIsGameOver()) {
                showMovement();
                System.out.println("Game Over");
                System.out.println("Winner is " + gameModel.getWinner());

                winLabel.setVisible(true);
                winLabel.setText(gameModel.getWinner() + " Win!");

                setGameState("Completed");

                regretButton.setDisable(true);
                resignButton.setDisable(true);
                drawButton.setDisable(true);
                saveButton.setDisable(true);

                LocalTime endTime = LocalTime.now();
                duration = Duration.between(startTime, endTime).getSeconds();
            }
        }
    }

    private void createChess() {
        Position[] chessPositions = {new Position(0, 0), new Position(0, 1), new Position(0, 2), new Position(0, 3)
                    , new Position(0, 4), new Position(0, 5), new Position(0, 6), new Position(0, 7), new Position(0, 8)
                    , new Position(2, 1), new Position(2, 7), new Position(3, 0), new Position(3, 2), new Position(3, 4),
                    new Position(3, 6), new Position(3, 8),
                    new Position(9, 0), new Position(9, 1), new Position(9, 2), new Position(9, 3), new Position(9, 4), new Position(9, 5), new Position(9, 6),
                    new Position(9, 7), new Position(9, 8), new Position(7, 1), new Position(7, 7), new Position(6, 0), new Position(6, 2), new Position(6, 4),
                    new Position(6, 6), new Position(6, 8)};

        for (int i = 0; i < gameModel.getPieceName().length; i++) {
            Chess chess = new Chess();
            chess.setName(gameModel.getPieceName()[i]);
            chess.setPosition(chessPositions[i]);
            chess.setPlayer("B");
            chess.setCode(i);
            gameModel.addChessBlack(chess, i);
        }

        for (int i = 0; i < gameModel.getPieceName().length; i++) {
            Chess chess = new Chess();
            chess.setName(gameModel.getPieceName()[i]);
            chess.setPosition(chessPositions[i + 16]);
            chess.setPlayer("R");
            chess.setCode(i);
            gameModel.addChessRed(chess, i);
        }
    }


    private StackPane drawChess(Chess chess) {
        Image image = new Image("img/" + chess.getName() + "_" + chess.getPlayer() + ".png");
        StackPane stackPane = new StackPane();
        ImageView imageView = new ImageView(image);

        imageView.setFitHeight(70);
        imageView.setFitWidth(70);

        stackPane.getChildren().add(imageView);
        stackPane.setOnMouseClicked(this::handleMouseClick);
        stackPane.setId(chess.getCode() + chess.getPlayer());

        chess.setImage(stackPane);
        return stackPane;
    }

    private void addChessOnBoard() {
        for (Chess chess : gameModel.getChessBlack()) {
            var piece = drawChess(chess);
            board.add(piece, chess.getPosition().col(), chess.getPosition().row());
        }

        for (Chess chess : gameModel.getChessRed()) {
            var piece = drawChess(chess);
            board.add(piece, chess.getPosition().col(), chess.getPosition().row());
        }
    }

    private void removeChess(Chess chess){
        gameModel.removeChess(chess);
        board.getChildren().removeIf(child -> Objects.equals(child.getId(), chess.getCode() + chess.getPlayer()));
    }

    private void changePosition(Chess chess,int row, int col){
        chess.setPosition(new Position(row, col));
        GridPane.setColumnIndex(chess.getImage(), col);
        GridPane.setRowIndex(chess.getImage(), row);
    }

    private String calculateCurrentPlayer(Integer integer){
        if (integer % 2 == 0){
            return "R";
        }else{
            return "B";
        }
    }

    private void selected(Chess chess){
        StackPane stackPane;
        stackPane = chess.getImage();

        stackPane.getStyleClass().add("selected");

        showMovablePosition(chess);
    }

    private void unSelected(Chess chess){
        StackPane stackPane;
        stackPane = chess.getImage();

        stackPane.getStyleClass().remove("selected");

        hideMovablePositions();
    }

    private void showMovablePosition(Chess chess){
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                if (gameModel.Move(chess,i,j)){
                    if (gameModel.getChessFromPoint(i, j) == null || !gameModel.getChessFromPoint(i,j).getPlayer().equals(chess.getPlayer())){
                        var square = getSquare(new Position(i,j));
                        square.getStyleClass().add("movable");
                    }

                }
            }
        }
    }

    private void hideMovablePositions() {
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                var square = getSquare(new Position(i,j));
                square.getStyleClass().remove("movable");
            }
        }
    }

    private StackPane getSquare(Position position) {
        for (var child : board.getChildren()) {
            if (GridPane.getRowIndex(child) == position.row() && GridPane.getColumnIndex(child) == position.col()) {
                return (StackPane) child;
            }
        }
        //Logger.error("Cannot get square on board (Square out of range)");
        throw new AssertionError();
    }

    public void regret(){
        Record record = regretList.pollLast();
        if (record != null){
            if (record.getRemovedChess() != null){
                //draw removed chess
                var piece = drawChess(record.getRemovedChess());
                if (record.getRemovedChess().getPlayer().equals("R") && regretBlackRemainTimes > 0){
                    gameModel.addChessRed(record.getRemovedChess(), record.getRemovedChess().getCode());
                    //regretBlackRemainTimes--;
                    System.out.println("Black player regret, remaining: " + regretBlackRemainTimes + " times");
//                    System.out.println("Red player regret, remaining: " + regretRedRemainTimes + " times");
                }else if(record.getRemovedChess().getPlayer().equals("B") && regretRedRemainTimes > 0){
                    gameModel.addChessBlack(record.getRemovedChess(), record.getRemovedChess().getCode());
                    //regretRedRemainTimes--;
                    System.out.println("Red player regret, remaining: " + regretRedRemainTimes + " times");
                }
                board.add(piece, record.getEnd().col(), record.getEnd().row());
            }
            //undo the selection
            if (selectedChess!= null && selectedChess.getPlayer().equals(calculateCurrentPlayer(currentPlayer))){
                unSelected(selectedChess);
                selectedChess = null;
            }

            if (calculateCurrentPlayer(currentPlayer).equals("R") && regretBlackRemainTimes >= 1){
                regretBlackRemainTimes--;
                //undo the movement
                changePosition(record.getChess(), record.getStart().row(),record.getStart().col());
                System.out.println("Black player regret, remaining: " + regretBlackRemainTimes + " times");
            }else if (calculateCurrentPlayer(currentPlayer).equals("B") && regretRedRemainTimes >= 1){
                regretRedRemainTimes--;
                //undo the movement
                changePosition(record.getChess(), record.getStart().row(),record.getStart().col());
                System.out.println("Red player regret, remaining: " + regretRedRemainTimes + " times");
            }

            if (calculateCurrentPlayer(currentPlayer).equals("R")){
                regretButton.setDisable(regretRedRemainTimes <= 0);
            }else {
                regretButton.setDisable(regretBlackRemainTimes <= 0);
            }
            currentPlayer--;
        }
    }

    public void resign(){
        if (calculateCurrentPlayer(currentPlayer).equals("R")){
            gameModel.setWinner("Black");
        }else{
            gameModel.setWinner("Red");
        }
        gameModel.setIsGameOver(true);
        winLabel.setVisible(true);
        winLabel.setText(gameModel.getWinner() + " Win!");

        setGameState("Completed");
        showMovement();
        saveButton.setDisable(true);
        regretButton.setDisable(true);
        resignButton.setDisable(true);
        drawButton.setDisable(true);

    }

    public void quit(){
        Stage main = (Stage) quitButton.getScene().getWindow();//get the stage of current's button(startButton).
        main.close(); //close current stage.
        System.out.println("-------Player Exit the Game!-------");
        setGameState("Give up");
    }

//    public void saveData() throws IOException {
//        System.out.println("-------Saving the Game!-------");
//        var repository = new GameHistoryRepository();
//        var gameHistory = GameHistory.builder()
//                .playerRed(playerRed)
//                .playerBlack(playerBlack)
//                .gameState(gameState)
//                .winner(gameModel.getWinner())
//                .duration(duration)
//                .build();
//        repository.add(gameHistory);
//        repository.loadFromFile(new File("src/main/resources/gameHistory.json"));
//        repository.saveToFile(new File("src/main/resources/gameHistory.json"));
//
//    }

    public void save() throws IOException {
        var repository = new GameHistoryRepository();
        gameModel.getChessBlack().stream().map(chess -> GameHistory.builder()
                .chessName(chess.getName())
                .row(chess.getPosition().row())
                .col(chess.getPosition().col())
                .color("B")
                .ID(chess.getCode())
                .build()).forEach(repository::add);

        gameModel.getChessRed().stream().map(chess -> GameHistory.builder()
                .chessName(chess.getName())
                .row(chess.getPosition().row())
                .col(chess.getPosition().col())
                .color("R")
                .ID(chess.getCode())

                .build()).forEach(repository::add);

        repository.loadFromFile(new File("src/main/resources/load/" + getPlayerRed() + "-" +getPlayerBlack() + ".json"));
        repository.saveToFile(new File("src/main/resources/load/" + getPlayerRed() + "-" +getPlayerBlack() + ".json"));

        var repository_1 = new GameHistoryRepository();
        var gameHistory_1 = GameHistory.builder()
                .playerRed(playerRed)
                .playerBlack(playerBlack)
                .step(currentPlayer)
                .movementList(movementList)
                .build();
        repository_1.add(gameHistory_1);
        repository_1.loadFromFile(new File("src/main/resources/load/history.json"));
        repository_1.saveToFile(new File("src/main/resources/load/history.json"));

    }

    @FXML
    private void loadData(String path) throws IOException {
        var repository = new GameHistoryRepository();

        GameHistory[] gameHistory;
        if (new File(path).exists()){
            System.out.println("exist");
        }

        gameHistory = repository.getJson(new File(path));

        for (GameHistory history : gameHistory) {
            Chess chess = new Chess();
            chess.setName(history.getChessName());
            chess.setPosition(new Position(history.getRow(),history.getCol()));
            chess.setPlayer(history.getColor());
            chess.setCode(history.getID());
            if (history.getColor().equals("B")) {
                gameModel.getChessBlack().add(chess);
            } else {
                gameModel.getChessRed().add(chess);
            }

        }
    }

    private void showMovement(Chess chess,int row, int col){
        String str = "Movement " + movementList.size() + ": "+ chess.getName() + "_" + chess.getPlayer()
                + ", From (" + (chess.getPosition().col() + 1) + "," +alphabet[chess.getPosition().row()] + ") to (" + (col + 1) + "," + alphabet[row] + ")";

        movementList.add(str);

        ObservableList<String> strList = FXCollections.observableArrayList(movementList);
        listView.setItems(strList);
    }

    private void showMovement(){
        String str = gameModel.getWinner() + " Player Win!";
        movementList.add(str);

        ObservableList<String> strList = FXCollections.observableArrayList(movementList);
        listView.setItems(strList);
    }
}

