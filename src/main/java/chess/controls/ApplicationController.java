package chess.controls;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ApplicationController extends Application {


    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ApplicationController.class
                .getResource("/fxml/game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Chinese Chess");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
