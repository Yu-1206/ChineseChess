package chess.controls;

import chess.player.GameHistory;
import chess.player.GameHistoryRepository;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameHistoryController{

    @FXML
    private TableColumn<GameHistory, String> playerRed;

    @FXML
    private TableColumn<GameHistory, String> playerBlack;

    @FXML
    private TableView<GameHistory> tableView;

    @FXML
    private Button loadButton;

    File file;

    public static Map<Integer,String> dataMap = new HashMap<>();

    @FXML
    private void initialize() throws IOException {
        var repository = new GameHistoryRepository();

        playerRed.setCellValueFactory(new PropertyValueFactory<>("playerRed"));
        playerBlack.setCellValueFactory(new PropertyValueFactory<>("playerBlack"));

        var str = repository.getJson(new File("src/main/resources/load/history.json"));
        if(str != null){
            ObservableList<GameHistory> observableList = FXCollections.observableArrayList();
            observableList.addAll(str);
            tableView.setItems(observableList);
        }else{
            loadButton.setDisable(true);
        }

        tableView.setRowFactory(tableView ->{
            TableRow<GameHistory> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())){
                    GameHistory rowData = row.getItem();
                    file = new File("src/main/resources/load/" + rowData.getPlayerRed() + "-" +rowData.getPlayerBlack() + ".json");
                    dataMap.put(1,rowData.getPlayerRed());
                    dataMap.put(2,rowData.getPlayerBlack());
                    dataMap.put(3, String.valueOf(rowData.getStep()));
                }
            });
            return row;
        });
    }

    public void startGame() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationController.class
                .getResource("/fxml/game-view.fxml"));

        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(playerRed.getText() + " VS " + playerBlack.getText());
        stage.setScene(scene);
        stage.setResizable(false);

        stage.show();
        Stage main = (Stage) loadButton.getScene().getWindow();
        main.close();
    }

}

