package chess.player;

import chess.model.Chess;
import chess.model.Position;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class GameHistory {

    private String playerRed;
    private String playerBlack;

    private String chessName;
    private Integer row;
    private Integer col;
    private String color;
    private Integer ID;
    private Integer step;
    private ArrayList<String> movementList;
}
