package Logic;

public class Queen extends Piece {

    private static final int MAX_NUM_OF_POSSIBLE_MOVES = 27;

    public Queen(Color c)
    {
        super(MAX_NUM_OF_POSSIBLE_MOVES, c);
    }

    public Queen(Piece piece)
    {
        super(piece);
    }

    protected boolean correctMove(Position newPos)
    {
        Position test = position.subtract(newPos);
        return ((Math.abs(test.column) == Math.abs(test.row) && test.column != 0))
                || ((test.column == 0 && test.row != 0) || (test.column != 0 && test.row == 0));
    }

    public boolean isValidMove(Position newPos, Board board)
    {
        // Queen

        return correctMove(newPos) && oppositeColor(board.getSpot(newPos).getPiece()) &&
                cleanPath(newPos, board);
    }
}