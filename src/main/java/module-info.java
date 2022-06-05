module ChineseChess {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;


    opens chess to javafx.fxml;
    exports chess;
    exports chess.controls;
    opens chess.controls to javafx.fxml;
}