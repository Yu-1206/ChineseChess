package chess.controls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LaunchController{

    @FXML
    private Button newGameButton;

    public void startGame() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationController.class
                .getResource("/fxml/login-view.fxml"));

        Stage stage = new Stage();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Chinese Chess");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        Stage main = (Stage) newGameButton.getScene().getWindow();
        main.close();
    }

    @FXML
    private void checkHistoryBoard() throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/gamehistory.fxml")));
        Stage gameStage = new Stage();//create a new stage.
        gameStage.setTitle("History Board");// set the name.
        Scene scene = new Scene(root);
        gameStage.setScene(scene);
        gameStage.setResizable(false);
        gameStage.show();//show the new stage.
    }

    @FXML
    private void loadData() throws IOException{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/fxml/history.fxml")));
        Stage gameStage = new Stage();//create a new stage.
        gameStage.setTitle("Load interface");// set the name.
        Scene scene = new Scene(root);
        gameStage.setScene(scene);
        gameStage.setResizable(false);
        gameStage.show();//show the new stage.
    }
}
