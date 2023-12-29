package Logic;

public abstract class Piece {

    protected final Position position;
    protected Position[] validMoves;
    private final Color color;
    protected boolean firstMove = true; //For castling
    protected static Position whiteKing = new Position(5, 1), blackKing = new Position(5, 8);

    // Methods

    public Piece(int maxNumOfPossibleMoves, Color c)
    {
        position = new Position();
        validMoves = new Position[maxNumOfPossibleMoves];
        color = c;
    }

    public Piece(Piece piece) //copy constructor for check
    {
        position = new Position(piece.position);
        validMoves = new Position[piece.getValidMoves().length];
        int i = 0;
        for(Position validMove : piece.validMoves)
        {
            validMoves[i] = new Position(validMove);
            i++;
        }
        color = piece.getColor();
        firstMove = piece.firstMove;
    }

    // abstract methods
    protected abstract boolean correctMove(Position new_position);

    public abstract boolean isValidMove(Position newPos, Board board);
    // -------------------------

    //Initializing the validMoves array
    protected void initValidMovesArr()
    {
        for (int i = 0; i < validMoves.length; i++) {
            validMoves[i] = new Position();
        }
    }

    // Stores the valid moves Positions of a piece in its validMoves array
    public void generateValidMoves(Board board)
    {
        int validMovesCounter = 0;
        Position new_pos = new Position();
        initValidMovesArr();
        for (int y = 1; y < 9; y++)
        {
            new_pos.row = y;
            for (int x = 1; x < 9; x++)
            {
                new_pos.column = x;
                if (isValidMove(new_pos, board))
                {
                    validMoves[validMovesCounter] = new Position(new_pos);
                    validMovesCounter++;
                }
            }
        }
    }

    //Determines the move direction
    public Direction moveDirection(Position newPos) {
        Position test;
        test = position.subtract(newPos);
        Direction direction;
        if (test.column > 0 && test.row > 0 && Math.abs(test.column) == Math.abs(test.row))
            direction = Direction.North_East;

        else if (test.column < 0 && test.row > 0 && Math.abs(test.column) == Math.abs(test.row))
            direction = Direction.North_West;

        else if (test.column > 0 && test.row < 0 && Math.abs(test.column) == Math.abs(test.row))
            direction = Direction.South_East;

        else if (test.column < 0 && test.row < 0 && Math.abs(test.column) == Math.abs(test.row))
            direction = Direction.South_West;

        else if (test.row > 0 && test.column == 0)
            direction = Direction.North;

        else if (test.row < 0 && test.column == 0)
            direction = Direction.South;

        else if (test.column > 0 && test.row == 0)
            direction = Direction.East;

        else if (test.column < 0 && test.row == 0)
            direction = Direction.West;

        else
            direction = Direction.None;
        return direction;
    }

    //Checkers

    //Checks if the path that a piece will cross it is clean or have another piece
    protected boolean cleanPath(Position destination, Board board) {
        /*Direction direction = moveDirection(destination);*/
        Position subResult = position.subtract(destination);
        int rowIncrementer = subResult.row != 0 ? subResult.row / Math.abs(subResult.row) : 0,
                columnIncrementer = subResult.column != 0 ? subResult.column / Math.abs(subResult.column) : 0;
        for(int i = rowIncrementer, j = columnIncrementer;
            (Math.abs(i) < Math.abs(subResult.row) || subResult.row == 0) &&
            (Math.abs(j) < Math.abs(subResult.column) || subResult.column == 0);
        i += rowIncrementer, j += columnIncrementer)
        {
            if (board.getSpot(new Position(position.column + j, position.row + i)).hasPiece())
                return false;
        }
        return true;
    }

    // makes sure it is castling
    public boolean isCastling(Position kingNewPos, Board board)
    {
        if (isKing(this) && firstMove &&
                Math.abs(position.subtract(kingNewPos).column) == 2)
        {
            Position rookPos;
            int directionSign;
            Direction kingMoveDirection = moveDirection(kingNewPos);
            switch(kingMoveDirection.ordinal())
            {
                case 2: //O-O castling
                    rookPos = new Position(8 ,kingNewPos.row);
                    directionSign = 1;
                    break;

                case 3: //O-O-O castling
                    rookPos = new Position(1,kingNewPos.row);
                    directionSign = -1;
                    break;

                default:
                    return false;
            }
            return (isRook(board.getSpot(new Position(rookPos.column, rookPos.row)).getPiece()) &&
                    board.getSpot(new Position(rookPos.column, rookPos.row)).getPiece().isFirstMove() &&
                    cleanPath(rookPos, board) &&
                    !checkInCastling(board, directionSign));
        }
        return false;
    }

    // it makes sure that the king is not in check when castling
    public boolean checkInCastling(Board board, int directionSign)
    {
        final int pathSpotsNumber = 3;
        Position kingPos = this.getColor().ordinal() == 0 ? whiteKing : blackKing;
        final Position kingPositionReference = new Position(kingPos);
        for(int i = 0; i < pathSpotsNumber; i++, kingPos.column += directionSign)
        {
            if (board.isEnemyInCheck(this.getColor().ordinal() == 0 ? Color.Black : Color.White))
            {
                kingPos.column = kingPositionReference.column;
                return true;
            }
        }
        kingPos.column = kingPositionReference.column;
        return false;
    }

    //Checks if a spot has a piece with the opposite player's color
    protected boolean oppositeColor(Piece piece)
    {
        if (piece == null)
            return true;
        else
            return !color.equals(piece.getColor());
    }

    //Checks if a spot doesn't have a piece
    protected boolean noPiece(Position newPos, Board board)
    {
        return !board.getSpot(newPos).hasPiece();
    }

    //Checks if a piece is a Rook
    public static boolean isRook(Piece piece)
    {
        return (piece instanceof Rook);
    }

    // Checks if a piece is King
    public static boolean isKing(Piece piece)
    {
        return (piece instanceof King);
    }

    // Checks if a piece is Pawn
    public static boolean isPawn(Piece piece)
    {
        return (piece instanceof Pawn);
    }

    public static boolean isKnight(Piece piece)
    {
        return (piece instanceof Knight);
    }

    public static boolean isQueen(Piece piece)
    {
        return (piece instanceof Queen);
    }

    public static boolean isBishop(Piece piece)
    {
        return (piece instanceof Bishop);
    }
    //////////////////

    // Getters
    public Position getPosition() {
        return position;
    }

    public boolean isFirstMove()
    {
        return this.firstMove;
    }

    public Color getColor()
    {
        return color;
    }

    public Position[] getValidMoves()
    {
        return validMoves;
    }

    public static Position getWhiteKing()
    {
        return whiteKing;
    }

    public static Position getBlackKing()
    {
        return blackKing;
    }

    public static Position getEnemyKing(Color c)
    {
        return c.ordinal() == 0 ? blackKing : whiteKing;
    }

    public static Piece getPieceCopy(Piece piece)
    {
        if(isKnight(piece))
            return new Knight(piece);
        else if (isQueen(piece))
            return new Queen(piece);
        else if (isPawn(piece))
            return new Pawn(piece);
        else if (isBishop(piece))
            return new Bishop(piece);
        else if (isRook(piece))
            return new Rook(piece);
        else if (isKing(piece))
            return new King(piece);
        else
            return null;
    }

    ////////////

    //Setters
    public void setFirstMove(boolean b)
    {
        this.firstMove = b;
    }

    public void setPosition(int x, int y) {
        position.column = x;
        position.row = y;
        if(isKing(this))
        {
            if(color.ordinal() == 0)
            {
                whiteKing = new Position(position);
            }
            else
            {
                blackKing = new Position(position);
            }
        }
    }

    ////////////
}