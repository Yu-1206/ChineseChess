package chess.controls;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    Button startButton;

    @FXML
    TextField playerRed;

    @FXML
    TextField playerBlack;

    public void initialize() {

    }
    public void startGame() throws IOException {
        if(!playerRed.getText().isEmpty() && !playerBlack.getText().isEmpty()){
            FXMLLoader fxmlLoader = new FXMLLoader(ApplicationController.class
                    .getResource("/fxml/game-view.fxml"));

            Stage stage = new Stage();
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle(playerRed.getText() + " VS " + playerBlack.getText());
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            fxmlLoader.<GameController>getController().setPlayerRed(playerRed.getText());
            fxmlLoader.<GameController>getController().setPlayerBlack(playerBlack.getText());

            Stage main = (Stage) startButton.getScene().getWindow();
            main.close();
        }
    }
}
