package Logic;

public class Rook extends Piece {
    private static final int MAX_NUM_OF_POSSIBLE_MOVES = 14;
    public Rook(Color c)
    {
        super(MAX_NUM_OF_POSSIBLE_MOVES, c);
    }

    public Rook(Piece piece)
    {
        super(piece);
    }

    protected boolean correctMove(Position newPos) {
        Position test = position.subtract(newPos);
        return (test.column == 0 && test.row != 0) || (test.column != 0 && test.row == 0);
    }

    public boolean isValidMove(Position newPos, Board board) {
        // Rook
        return correctMove(newPos) && oppositeColor(board.getSpot(newPos).getPiece()) &&
                cleanPath(newPos, board);
    }
}