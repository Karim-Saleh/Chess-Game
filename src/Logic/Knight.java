package Logic;

public class Knight extends Piece {

    private static final int MAX_NUM_OF_POSSIBLE_MOVES = 8;

    public Knight(Color c)
    {
        super(MAX_NUM_OF_POSSIBLE_MOVES, c);
    }

    public Knight(Piece piece)
    {
        super(piece);
    }

    protected boolean correctMove(Position newPos)
    {
        Position test = position.subtract(newPos);
        return (Math.abs(test.column) == 1 && Math.abs(test.row) == 2) ||
               (Math.abs(test.column) == 2 && Math.abs(test.row) == 1);
    }

    public boolean isValidMove(Position newPos, Board board)
    {
        return correctMove(newPos) && oppositeColor(board.getSpot(newPos).getPiece());
    }
}