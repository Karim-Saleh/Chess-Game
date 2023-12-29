package Logic;

public class Pawn extends Piece
{
    private static final int MAX_NUM_OF_POSSIBLE_MOVES = 4;

    public Pawn(Color c)
    {
        super(MAX_NUM_OF_POSSIBLE_MOVES, c);
    }

    public Pawn(Piece piece)
    {
        super(piece);
    }

    @Override
    protected boolean correctMove(Position newPos)
    {
        Position test = position.subtract(newPos);
        return ((Math.abs(test.column) == 1 && Math.abs(test.row) == 1) ||
               (Math.abs(test.column) == 0 && Math.abs(test.row) == 1) ||
                (Math.abs(test.column) == 0 && Math.abs(test.row) == 2 && firstMove()));
    }

    @Override
    public boolean isValidMove(Position newPos, Board board)
    {
        Direction direction = moveDirection(newPos);

        //Moving diagonally
        if ((direction.ordinal() == 4 || direction.ordinal() == 5 ||
                direction.ordinal() == 6 || direction.ordinal() == 7))
        {
            if (noPiece(newPos, board)) //Not has piece (May be en passant)
            {
                return correctMove(newPos) && isValidDirection(newPos) &&
                        isEnPassant(newPos, board);
            }
            else //Has piece (can be captured)
            {
                return correctMove(newPos) && isValidDirection(newPos) &&
                        oppositeColor(board.getSpot(newPos).getPiece());
            }
        }

        //Moving vertically
        else if ((direction.ordinal() == 0 || direction.ordinal() == 1) &&
                noPiece(newPos, board) )
        {
            return correctMove(newPos) && isValidDirection(newPos) &&
                    cleanPath(newPos, board);
        }

        return false;
    }

    //return true if the pawn was at its starting position
    private boolean firstMove()
    {
        return ((getColor().equals(Color.White) && position.row == 2)) ||
                (getColor().equals(Color.Black) && position.row == 7);
    }


    private boolean isValidDirection(Position new_pos)
    {
        Direction direction = moveDirection(new_pos);

        // White pawn Moves (North or North East or North West)
        if (this.getColor().ordinal() == 0 && ( direction.ordinal() == 0 ||
                direction.ordinal() == 4 || direction.ordinal() == 5 ) )
            return true;

            // Black pawn Moves (South or South East or South West)
        else if (this.getColor().ordinal() == 1 && ( direction.ordinal() == 1 ||
                direction.ordinal() == 6 || direction.ordinal() == 7) )
            return true;

        else
            return false;
    }

    private boolean isEnPassant(Position newPos, Board board)
    {
        Piece capturedPawn;
        switch(getColor().ordinal())
        {
            case 0: //White
                capturedPawn = board.getSpot(new Position(newPos.column, newPos.row - 1)).getPiece();
                if (newPos.row == 6 && isPawn(capturedPawn) && capturedPawn.isFirstMove())
                    return true;

                break;

            default: //Black
                capturedPawn = board.getSpot(new Position(newPos.column, newPos.row + 1)).getPiece();
                if (newPos.row == 3 && isPawn(capturedPawn) && capturedPawn.isFirstMove())
                    return true;

                break;
        }
        return false;
    }
}