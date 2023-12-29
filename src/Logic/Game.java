package Logic;

import GUI.GameFrame;

public class Game {
    private final Player player1;
    private final Player player2;
    private Player currentPlayer;
    private final Board board;
    private final LinkedStack<Move> moves;

    public static void main(String[] args) {
        System.setProperty("prism.allowhidpi", "false");
        Game g = new Game();
        GameFrame f = new GameFrame(g);
    }

    public Game(){
        player1 = new Player(Color.White);
        player2 = new Player(Color.Black);
        currentPlayer = player1;
        board = new Board();
        moves = new LinkedStack<>();
    }

    public boolean move(Position startingPosition, Position endingPosition) {
        if(board.movePiece(currentPlayer.getColor(),startingPosition, endingPosition, false, null))
        {
            if(board.isEnemyInCheck(currentPlayer.getColor())) {
            board.getLastMove().setEnemyInCheck(true);
            board.getLastMove().setCheckedEnemyKing(Piece.getEnemyKing(currentPlayer.getColor()));
            }
            changeTurn();
            moves.push(board.getLastMove());
            if(!isPromotion()) {
                board.deCheck(moves.Top().getMovingPiece().getColor());
            }
            return true;
        }
        return false;
    }
    public Color getCrntPlayerClr()
    {
        return currentPlayer.getColor();
    }
    public boolean Undo()
    {
        if (moves.getSize() > 0)
        {
            board.Undo(moves.Top(), moves.previous());
            changeTurn();
            moves.pop();
            if(moves.getSize() > 0)
                board.deCheck(moves.Top().getMovingPiece().getColor());
            return true;
        }
        return false;
    }

    public boolean Redo()
    {
        if(moves.rePush())
        {
            board.movePiece(currentPlayer.getColor(), moves.Top().getFirstPosition(),
                    moves.Top().getLastPosition(), moves.Top().isPromotion(),
                    moves.Top().getPromotedPiece());
            changeTurn();
            board.deCheck(moves.Top().getMovingPiece().getColor());
            return true;
        }
        return false;
    }

    public Move getLastMove(int move)
    {
        return switch (move) {
            case 1 -> (moves.getSize() > 0 ? moves.Top() : null);
            case 2 -> (moves.getSize() > 1 ? moves.previous() : null);
            default -> null;
        };
    }


    public void changeTurn()
    {
        currentPlayer = currentPlayer == player1 ? player2 : player1;
    }

    private boolean isPromotion()
    {
        return (moves.Top().getLastPosition().row == 8 || moves.Top().getLastPosition().row == 1)
                && Piece.isPawn(moves.Top().getMovingPiece());
    }

    //Getters
    public Board getBoard() {
        return board;
    }

    public LinkedStack<Move> getMoves()
    {
        return moves;
    }
}