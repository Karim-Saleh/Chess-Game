package Logic;

public class Bishop extends Piece {

    private static final int MAX_NUM_OF_POSSIBLE_MOVES = 13;

    public Bishop(Color c)
    {
        super(MAX_NUM_OF_POSSIBLE_MOVES, c);
    }

    public Bishop(Piece piece)
    {
        super(piece);
    }

    @Override
    protected boolean correctMove(Position newPos)
    {
        Position test = position.subtract(newPos);
        return (Math.abs(test.column) == Math.abs(test.row) && test.column != 0);
    }

    @Override
    public boolean isValidMove(Position newPos, Board board)
    {
        return correctMove(newPos) && oppositeColor(board.getSpot(newPos).getPiece()) &&
                cleanPath(newPos, board);
    }
}