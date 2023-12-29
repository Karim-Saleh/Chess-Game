package Logic;

public class King extends Piece {

    private static final int MAX_NUM_OF_POSSIBLE_MOVES = 8;

    public King(Color c)
    {
        super(MAX_NUM_OF_POSSIBLE_MOVES, c);
    }

    public King(Piece piece)
    {
        super(piece);
    }

    protected boolean correctMove(Position newPos)
    {
        Position test = position.subtract(newPos);
        return (Math.abs(test.row) == 0 && Math.abs(test.column) == 1) ||
               (Math.abs(test.row) == 1 && Math.abs(test.column) == 0) ||
               (Math.abs(test.row) == 1 && Math.abs(test.column) == 1);
    }

    public boolean isValidMove(Position newPos, Board board)
    {
        return ((correctMove(newPos) && oppositeColor(board.getSpot(newPos).getPiece()))
                || (isCastling(newPos, board)));
    }
}
