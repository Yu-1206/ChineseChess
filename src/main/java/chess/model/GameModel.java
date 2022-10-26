package chess.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GameModel {

    private final String[] pieceName = {
            "chariot", "horse", "elephant", "guard", "general", "guard", "elephant", "horse", "chariot"
            , "cannon", "cannon",
            "pawn", "pawn", "pawn", "pawn", "pawn"};

    private List<Chess> chessBlack = new ArrayList<>();

    private List<Chess> chessRed = new ArrayList<>();

    private Boolean isGameOver = false;

    private String winner = "null";

    public void addChessBlack(Chess chess, int i) {
        chessBlack.add(i, chess);
    }

    public void addChessRed(Chess chess, int i) {
        chessRed.add(i, chess);
    }

    public void removeChess(Chess chess) {
        if (chess.getPlayer().equals("B")) {
            chessBlack.remove(chess);
            if (chess.getName().equals("general")){
                setIsGameOver(true);
                setWinner("Red");
            }
        } else {
            chessRed.remove(chess);
            if (chess.getName().equals("general")){
                setIsGameOver(true);
                setWinner("Black");
            }
        }

    }

    public Chess getChessFromPoint(int row, int col) {
        for (Chess chess : chessBlack) {
            if (row == chess.getPosition().row() && col == chess.getPosition().col()) {
                return chess;
            }
        }

        for (Chess chess : chessRed) {
            if (row == chess.getPosition().row() && col == chess.getPosition().col()) {
                return chess;
            }
        }

        return null;
    }

    /**
     * rule of move.
     *
     * @param chess selected chess .
     * @param row   mouse clicked position row.
     * @param col   mouse clicked position column.
     * @return boolean.
     */
    public boolean Move(Chess chess, int row, int col) {
        switch (chess.getName()) {
            case "chariot":
                if (((chess.getPosition().row() == row && chess.getPosition().col() != col) || (chess.getPosition().row() != row && chess.getPosition().col() == col)) && chessCountBetweenTwoPosition(chess, row, col) == 0) {
                    System.out.println("Chariot move");
                    return true;
                }
                break;
            case "horse":
                if (((chess.getPosition().row() + 1 == row && chess.getPosition().col() + 2 == col) && getChessFromPoint(chess.getPosition().row(), chess.getPosition().col() + 1) == null)
                        || ((chess.getPosition().row() + 1 == row && chess.getPosition().col() - 2 == col) && getChessFromPoint(chess.getPosition().row(), chess.getPosition().col() - 1) == null)
                        || ((chess.getPosition().row() - 1 == row && chess.getPosition().col() + 2 == col) && getChessFromPoint(chess.getPosition().row(), chess.getPosition().col() + 1) == null)
                        || ((chess.getPosition().row() - 1 == row && chess.getPosition().col() - 2 == col) && getChessFromPoint(chess.getPosition().row(), chess.getPosition().col() - 1) == null)

                        || ((chess.getPosition().row() + 2 == row && chess.getPosition().col() + 1 == col) && getChessFromPoint(chess.getPosition().row() + 1, chess.getPosition().col()) == null)
                        || ((chess.getPosition().row() + 2 == row && chess.getPosition().col() - 1 == col) && getChessFromPoint(chess.getPosition().row() + 1, chess.getPosition().col()) == null)
                        || ((chess.getPosition().row() - 2 == row && chess.getPosition().col() + 1 == col) && getChessFromPoint(chess.getPosition().row() - 1, chess.getPosition().col()) == null)
                        || ((chess.getPosition().row() - 2 == row && chess.getPosition().col() - 1 == col) && getChessFromPoint(chess.getPosition().row() - 1, chess.getPosition().col()) == null)) {
                    //System.out.println("Horse move");
                    return true;
                }

                break;
            case "elephant":
                if (((chess.getPosition().row() + 2 == row && chess.getPosition().col() + 2 == col) && getChessFromPoint(chess.getPosition().row() + 1, chess.getPosition().col() + 1) == null)
                        || ((chess.getPosition().row() + 2 == row && chess.getPosition().col() - 2 == col) && getChessFromPoint(chess.getPosition().row() + 1, chess.getPosition().col() - 1) == null)
                        || ((chess.getPosition().row() - 2 == row && chess.getPosition().col() + 2 == col) && getChessFromPoint(chess.getPosition().row() - 1, chess.getPosition().col() + 1) == null)
                        || ((chess.getPosition().row() - 2 == row && chess.getPosition().col() - 2 == col) && getChessFromPoint(chess.getPosition().row() - 1, chess.getPosition().col() - 1) == null)) {
                    if (chess.getPlayer().equals("B")) {
                        if (row < 5) {
                            //System.out.println("Elephant move");
                            return true;
                        }
                    }
                    if (chess.getPlayer().equals("R")) {
                        if (row > 4) {
                            //System.out.println("Elephant move");
                            return true;
                        }
                    }
                }

                break;
            case "guard":
                if ((chess.getPosition().row() + 1 == row && chess.getPosition().col() + 1 == col) || (chess.getPosition().row() + 1 == row && chess.getPosition().col() - 1 == col)
                        || (chess.getPosition().row() - 1 == row && chess.getPosition().col() + 1 == col) || (chess.getPosition().row() - 1 == row && chess.getPosition().col() - 1 == col)) {
                    if (chess.getPlayer().equals("B")) {
                        if (col > 2 && col < 6 && row < 3) {
                            //System.out.println("Guard move");
                            return true;
                        }
                    }
                    if (chess.getPlayer().equals("R")) {
                        if (col > 2 && col < 6 && row > 6) {
                            //System.out.println("Guard move");
                            return true;
                        }
                    }
                }


                break;
            case "general":
                if ((chess.getPosition().row() + 1 == row && chess.getPosition().col() == col) || (chess.getPosition().row() - 1 == row && chess.getPosition().col() == col)
                        || (chess.getPosition().row() == row && chess.getPosition().col() + 1 == col) || (chess.getPosition().row() == row && chess.getPosition().col() - 1 == col)) {
                    if (chess.getPlayer().equals("B")) {
                        if (col > 2 && col < 6 && row < 3) {
                            //System.out.println("General move");
                            return true;
                        }
                    }
                    if (chess.getPlayer().equals("R")) {
                        if (col > 2 && col < 6 && row > 6) {
                            //System.out.println("General move");
                            return true;
                        }
                    }

                }else if(getChessFromPoint(row, col) != null &&chessCountBetweenTwoPosition(chess,row,col) == 0 && getChessFromPoint(row, col).getName().equals("general"))
                    {
                        return true;
                    }
                break;
            case "cannon":
                if (((chess.getPosition().row() == row && chess.getPosition().col() != col) || (chess.getPosition().row() != row && chess.getPosition().col() == col))) {
                    if (chessCountBetweenTwoPosition(chess, row, col) == 0 && getChessFromPoint(row, col) == null) {
                        //System.out.println("Cannon move");
                        return true;
                    }
                    if (chessCountBetweenTwoPosition(chess, row, col) == 1 && getChessFromPoint(row, col) != null) {
                        //System.out.println("Cannon eat");
                        return true;
                    }
                }

                break;
            case "pawn":
                if (chess.getPlayer().equals("B")) {
                    if (row < 5) {
                        if (chess.getPosition().row() + 1 == row && chess.getPosition().col() == col) {
                            return true;
                        }
                    } else {
                        if ((chess.getPosition().row() == row && chess.getPosition().col() + 1 == col)
                                || (chess.getPosition().row() == row && chess.getPosition().col() - 1 == col)
                                || (chess.getPosition().row() + 1 == row && chess.getPosition().col() == col)) {
                            return true;
                        }
                    }
                }
                if (chess.getPlayer().equals("R")) {
                    if (row > 4) {
                        if (chess.getPosition().row() - 1 == row && chess.getPosition().col() == col) {
                            return true;
                        }
                    } else {
                        if ((chess.getPosition().row() == row && chess.getPosition().col() + 1 == col)
                                || (chess.getPosition().row() == row && chess.getPosition().col() - 1 == col)
                                || (chess.getPosition().row() - 1 == row && chess.getPosition().col() == col)) {
                            return true;
                        }
                    }
                }
                //System.out.println("Pawn move");
                break;
        }

        return false;
    }

    public int chessCountBetweenTwoPosition(Chess chess, int row, int col) {
        int x = chess.getPosition().row();
        int y = chess.getPosition().col();
        int count = 0;

        if (x == row && y != col) {
            if (y > col) {
                for (int i = y - 1; i > col; i--) {
                    if (getChessFromPoint(x, i) != null) {
                        count += 1;
                    }
                }
            } else {
                for (int i = y + 1; i < col; i++) {
                    if (getChessFromPoint(x, i) != null) {
                        count += 1;
                    }
                }
            }
        } else if (y == col && x != row) {
            if (x > row) {
                for (int i = x - 1; i > row; i--) {
                    if (getChessFromPoint(i, y) != null) {
                        count += 1;
                    }
                }
            } else {
                for (int i = x + 1; i < row; i++) {
                    if (getChessFromPoint(i, y) != null) {
                        count += 1;
                    }
                }

            }
        }
        return count;
    }

}
