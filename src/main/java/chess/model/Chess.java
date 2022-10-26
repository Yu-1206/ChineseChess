package chess.model;

import javafx.scene.layout.StackPane;
import lombok.Data;

@Data
public class Chess{

    private String name;

    private String player;

    private Integer code;

    private Position position;

    private StackPane image;
}
