package chess.model;

import lombok.Data;

@Data
public class Record {
    private Chess chess;
    private Position start;
    private Position end;
    private Chess removedChess;

    public Record(){}

    public Record (Chess chess, Position start, Position end){
        this.chess = chess;
        this.start = start;
        this.end = end;
    }

    public Record (Chess chess, Position start, Position end, Chess removedChess){
        this.chess = chess;
        this.start = start;
        this.end = end;
        this.removedChess = removedChess;
    }

}
