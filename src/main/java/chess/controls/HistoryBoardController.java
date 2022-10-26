package chess.controls;

import chess.player.GameHistory;
import chess.player.GameHistoryRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.io.*;

/**
 * the class is to controller the display of the stonegame.stonegame.rank table.
 */
public class HistoryBoardController {

    @FXML
    private Button quitButton;

    @FXML
    private TableColumn<GameHistory, String> playerRed;

    @FXML
    private TableColumn<GameHistory, String> playerBlack;

    @FXML
    private TableColumn<GameHistory, String> gameState;

    @FXML
    private TableColumn<GameHistory, Long> duration;

    @FXML
    private TableColumn<GameHistory, String> winner;

    @FXML
    private TableView<GameHistory> tableView;



    @FXML
    private void quit() {
        Stage main = (Stage) quitButton.getScene().getWindow();//get the stage of current's button(startButton).
        main.close(); //close current stage.
    }

    @FXML
    private void initialize() throws IOException {
        var repository = new GameHistoryRepository();

        playerRed.setCellValueFactory(new PropertyValueFactory<>("playerRed"));
        playerBlack.setCellValueFactory(new PropertyValueFactory<>("playerBlack"));
        gameState.setCellValueFactory(new PropertyValueFactory<>("gameState"));
        duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        winner.setCellValueFactory(new PropertyValueFactory<>("winner"));

        var str = repository.getJson(new File("src/main/resources/gameHistory.json"));
        if(str != null){
            ObservableList<GameHistory> observableList = FXCollections.observableArrayList();
            observableList.addAll(str);
            tableView.setItems(observableList);
        }

    }



}