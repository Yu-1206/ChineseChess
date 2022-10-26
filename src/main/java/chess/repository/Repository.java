package chess.repository;
import chess.player.GameHistory;

import java.util.ArrayList;
import java.util.List;

public abstract class Repository {

    protected List<GameHistory> elements;

    protected Repository() {
        elements = new ArrayList<>();
    }


}
