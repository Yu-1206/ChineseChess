package chess.controls;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ApplicationController extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/launch-view.fxml"));
        Parent root = fxmlLoader.load();
        Stage gameStage = new Stage();//create a new stage.

        gameStage.setTitle( "Chinese Game");// set the name.

        Scene scene = new Scene(root);
        gameStage.setScene(scene);
        gameStage.setResizable(false);
        gameStage.show();
}
}
