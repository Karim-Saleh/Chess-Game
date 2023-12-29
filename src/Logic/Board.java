package Logic;

public class Board
{
    private final Spot[][] spots;
    private Move lastMove;

    public Board(Board board)
    {
        spots = new Spot[9][9];
        for(int i = 1; i <= 8; i++)
        {
            for(int j = 1; j <= 8; j++)
            {
                spots[i][j] = new Spot(board.spots[i][j]);
                /*spots[i][j] = Spot.spotCache.getSpot(board.spots[i][j]);*/
            }
        }
    }

    public Board()
    {
        // indexing starts from spots[1][1]
        spots = new Spot[9][9];
        setValuesOfSpots();
        updatePiecesValidMoves();
    }

    private void setValuesOfSpots()
    {
        //initializing spots
        for(int i = 1; i < 9; i++)
        {
            for(int j = 1; j < 9; j++)
            {
                /*spots[i][j] = new Spot();*/
                spots[i][j] = Spot.spotCache.getSpot();
            }
        }
        // initializing Pieces
        int colorIndex = -1;
        for(int row = 1; row < 9; row += 7)
        {
            Color color = Color.getColor(++colorIndex);
            spots[row][1].setPiece(new Rook(color), new Position(1,row));
            spots[row][2].setPiece(new Knight(color), new Position(2,row));
            spots[row][3].setPiece(new Bishop(color), new Position(3,row));
            spots[row][4].setPiece(new Queen(color), new Position(4,row));
            spots[row][5].setPiece(new King(color), new Position(5,row));
            spots[row][6].setPiece(new Bishop(color), new Position(6,row));
            spots[row][7].setPiece(new Knight(color), new Position(7,row));
            spots[row][8].setPiece(new Rook(color), new Position(8,row));
        }
        for(int row = 7; row > 1; row-=5)
        {
            Color color = Color.getColor(colorIndex--);
            for(int column = 1; column < 9; column++)
            {
                spots[row][column].setPiece(new Pawn(color), new Position(column,row));
            }
        }
        ///////////////
    }

    private void updatePiecesValidMoves() {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (spots[i][j].hasPiece()  && !Piece.isKing(spots[i][j].getPiece())) {
                    spots[i][j].getPiece().generateValidMoves(this);
                }
            }
        }
        //This is done so the validation of not castling into check is right
        getSpot(Piece.getWhiteKing()).getPiece().generateValidMoves(this);
        getSpot(Piece.getBlackKing()).getPiece().generateValidMoves(this);
    }

    public boolean movePiece(Color currentPlayerColor, Position startingPosition,
                             Position endingPosition, boolean promotion, Piece promotedPiece)
    {
        if(isValidPosition(startingPosition) && isValidPosition(endingPosition) &&
                getSpot(startingPosition).hasPiece() &&
                currentPlayerColor.equals(getSpot(startingPosition).getPiece().getColor()))
        {
            Piece movingPiece = getSpot(startingPosition).getPiece();
            Position[] validMoves = movingPiece.getValidMoves();
            if (Position.exist(endingPosition, validMoves))
            {
                /* storing the last move in lastMove object to use it in the
                Game class to be stored in the arrayList */
                Piece capturedPiece = getSpot(endingPosition).getPiece();
                lastMove = new Move(startingPosition, endingPosition,
                        movingPiece, capturedPiece, movingPiece.isFirstMove());

                if (enemyHasEnPassant(startingPosition, endingPosition))
                {
                    lastMove.setEnemyHasEnPassant(true);
                    lastMove.setEnemyEnPassantPos(determineEnPassantPos(startingPosition));
                }
                ///////////////////////////////////////

                //Castling
                if (isCastling(movingPiece, endingPosition))
                {
                    Castling(startingPosition, endingPosition);
                }
                /////////

                //Enpassant
                else if (isEnPassant(startingPosition, endingPosition))
                {
                    lastMove.setEnPassant(true);
                    enPassant(startingPosition, endingPosition);
                }
                ///////////

                updateMove(startingPosition, endingPosition);
                if(promotion)
                {
                    getSpot(endingPosition).setPiece(promotedPiece, endingPosition);
                }
                updatePiecesValidMoves();
                ////////

                getSpot(endingPosition).getPiece().setFirstMove(false);
                return true;
            }
        }
        return false;
    }

    public void Castling(Position kingPos, Position castlingPos)
    {
        Position rookPos, rookNewPos;
        Direction kingMoveDirection = getSpot(kingPos).getPiece().
                moveDirection(castlingPos);

        switch(kingMoveDirection.ordinal())
        {
            case 2: //King is moving East
                rookPos = new Position(8, kingPos.row);
                rookNewPos = new Position(6, kingPos.row);
                break;

            default: //King is moving West
                rookPos = new Position(1, kingPos.row);
                rookNewPos = new Position(4, kingPos.row);
                break;
        }
        updateMove(rookPos, rookNewPos); //Moving rook
        getSpot(rookNewPos).getPiece().setFirstMove(false); /* it isn't its first
                                                                move anymore */

        // setting values of lastMove
        lastMove.setCastling(true);
        lastMove.setCastlingRookFirstPos(rookPos);
        lastMove.setCastlingRookNewPos(rookNewPos);
        //////////////////////////////
    }

    public void promotion(Piece piece, Position pawnPos)
    {
        getSpot(pawnPos).setPiece(piece, pawnPos);
        lastMove.setPromotedPiece(piece);
        piece.setFirstMove(false);
        updatePiecesValidMoves();
        if(isEnemyInCheck(piece.getColor()))
        {
            lastMove.setEnemyInCheck(true);
            lastMove.setCheckedEnemyKing(Piece.getEnemyKing(piece.getColor()));
        }
        deCheck(piece.getColor());
    }

    //removing the captured pawn of the En Passant Move
    private void enPassant(Position pawnPos, Position newPos)
    {
        Position capturedPawn;
        switch(getSpot(pawnPos).getPiece().getColor().ordinal())
        {
            case 0: //White
                capturedPawn = new Position (newPos.column, newPos.row - 1);
                break;

            default: //Black
                capturedPawn = new Position (newPos.column, newPos.row + 1);
                break;
        }
        lastMove.setCapturedPiece(getSpot(capturedPawn).getPiece());
        getSpot(capturedPawn).setPiece(null, null);
    }

    private void updateMove(Position firstPos, Position newPos)
    {
        Piece movingPiece = getSpot(firstPos).getPiece();
        getSpot(firstPos).setPiece(null, null);
        getSpot(newPos).setPiece(movingPiece, newPos);
    }

    public void Undo(Move thisMove, Move previousMove)
    {
        //Updating the move spots (first Position and last Position)
        getSpot(thisMove.getFirstPosition()).setPiece(thisMove.getMovingPiece(),
                thisMove.getFirstPosition());

        if(!thisMove.isEnPassant()) {
            getSpot(thisMove.getLastPosition()).setPiece(thisMove.getCapturedPiece(),
                    thisMove.getLastPosition());
        }
        else
        {
            getSpot(thisMove.getLastPosition()).setPiece(null, null);
        }
        ///////////////////////////

        thisMove.getMovingPiece().setFirstMove(thisMove.isFirstMove());

        if (thisMove.isCastling()) //if this move was castling
        {
            //get the rook back to its first Position
            updateMove(thisMove.getCastlingRookNewPos(), thisMove.getCastlingRookFirstPos());

            //it is sure that the rook before the castling had never moved
            getSpot(thisMove.getCastlingRookFirstPos()).getPiece().setFirstMove(true);
        }
        //if the player after previous move can play enpassant
        else if (previousMove != null && previousMove.isEnemyHasEnPassant())
        {
            //assuming that the last move was enpassant
            getSpot(previousMove.getLastPosition()).setPiece
                    (previousMove.getMovingPiece(), previousMove.getLastPosition());
            previousMove.getMovingPiece().setFirstMove(true); /*we need to set the pawn
                                                             firstMove true before updating
                                                             validMoves to make enPassant
                                                             can be done again*/
        }
        ///////

        updatePiecesValidMoves();

        //set the pawn first move false again after updating valid moves
        if (previousMove != null && previousMove.isEnemyHasEnPassant())
        {
            previousMove.getMovingPiece().setFirstMove(false);
        }
        lastMove = previousMove;
    }

    private boolean pawnCheck(boolean first, int i, int j, Color playerColor)
    {
        return(first && Piece.isPawn(spots[i][j].getPiece()) &&
                spots[i][j].getPiece().getColor().equals(playerColor));
    }

    private boolean horizVertCheck(int start, int end, int pinned, Color playerColor, boolean horizontal)
    {
        Position position = new Position();
        boolean first = true;
        for(int i = start; i > end; i--)
        {
            position.column = horizontal ? Math.abs(i) : pinned;
            position.row = horizontal ? pinned : Math.abs(i);

            if(spots[position.row][position.column].hasPiece())
            {
                Piece checkingPiece = spots[position.row][position.column].getPiece();

                if( checkingPiece.getColor().equals(playerColor) &&
                        ((Piece.isQueen(checkingPiece) || Piece.isRook(checkingPiece)) ||
                        ( first && Piece.isKing(checkingPiece))) )
                {
                    return true;
                }
                else
                {
                    break;
                }
            }
            first = false;
        }
        return false;
    }

    private boolean diagonalCheck(int startI, int startJ, int endI, int endJ,
                                  Color playerColor, int color)
    {
        boolean first = true;
        for(int i = startI, j = startJ; i > endI && j > endJ; i--, j--)
        {
            if(spots[Math.abs(i)][Math.abs(j)].hasPiece())
            {
                Piece checkingPiece = spots[Math.abs(i)][Math.abs(j)].getPiece();

                if(checkingPiece.getColor().equals(playerColor) && ((Piece.isQueen(checkingPiece)
                        || Piece.isBishop(checkingPiece)) ||
                        (pawnCheck(first, Math.abs(i), Math.abs(j), playerColor)) &&
                                playerColor.ordinal() == color || ( first &&
                        Piece.isKing(checkingPiece)) &&
                        checkingPiece.getColor().equals(playerColor)))
                {
                    return true;
                }
                else
                {
                    break;
                }
            }
            first = false;
        }
        return false;
    }

    private boolean knightCheck(int x, int y, Color playerColor)
    {
        if(isValidPosition(new Position(x, y)) && spots[y][x].hasPiece() &&
        Piece.isKnight(spots[y][x].getPiece()) && spots[y][x].getPiece().getColor().equals(playerColor))
        {
            return true;
        }
            return false;
    }

    public boolean isEnemyInCheck(Color currentPlayerColor)
    {
        Position enemyKing = currentPlayerColor.ordinal() == 0 ?
                Piece.getBlackKing() : Piece.getWhiteKing();
        return
                (
        horizVertCheck(enemyKing.column - 1, 0,
                        enemyKing.row, currentPlayerColor, true) ||

        (horizVertCheck(-1 * (enemyKing.column + 1), -9,
                        enemyKing.row, currentPlayerColor, true)) ||

        (horizVertCheck(enemyKing.row - 1, 0,
                        enemyKing.column, currentPlayerColor, false)) ||

        (horizVertCheck(-1 * (enemyKing.row + 1), -9,
                        enemyKing.column, currentPlayerColor, false)) ||

        (diagonalCheck(enemyKing.row - 1, enemyKing.column - 1,
                                    0, 0, currentPlayerColor, 0)) ||

        diagonalCheck(enemyKing.row - 1, -1 * (enemyKing.column + 1),
                                      0, -9, currentPlayerColor, 0) ||

        diagonalCheck(-1 * (enemyKing.row + 1), -1 * (enemyKing.column + 1),
                                    -9, -9, currentPlayerColor, 1) ||

                diagonalCheck(-1 * (enemyKing.row + 1), enemyKing.column - 1,
                                        -9, 0, currentPlayerColor, 1) ||

        knightCheck(enemyKing.column + 1, enemyKing.row + 2, currentPlayerColor) ||

        knightCheck(enemyKing.column + 1, enemyKing.row - 2, currentPlayerColor) ||

        knightCheck(enemyKing.column - 1, enemyKing.row + 2, currentPlayerColor) ||

        knightCheck(enemyKing.column - 1, enemyKing.row - 2, currentPlayerColor) ||

        knightCheck(enemyKing.column + 2, enemyKing.row + 1, currentPlayerColor) ||

        knightCheck(enemyKing.column + 2, enemyKing.row - 1, currentPlayerColor) ||

        knightCheck(enemyKing.column - 2, enemyKing.row + 1, currentPlayerColor) ||

        knightCheck(enemyKing.column - 2, enemyKing.row - 1, currentPlayerColor)
                );
    }

    public void deCheck(Color currentPlayerColor)
    {
        Board copy = new Board(this);
        Position checkedKing = Piece.getEnemyKing(currentPlayerColor);
        Color enemy = copy.spots[checkedKing.row][checkedKing.column].getPiece().getColor();
        for(int i = 1; i < 9; i++)
        {
            for(int j = 1; j < 9; j++)
            {
                if(copy.spots[i][j].hasPiece() &&
                        enemy.equals(copy.spots[i][j].getPiece().getColor()))
                {
                    for (Position valid : spots[i][j].getPiece().getValidMoves())
                    {
                        boolean erase = false;
                        if(isValidPosition(valid)) {
                            copy.movePiece(enemy, new Position(j, i), valid, false, null);
                            if (copy.isEnemyInCheck(currentPlayerColor)) {
                                erase = true;
                            }
                            copy.Undo(copy.lastMove, lastMove);
                            if(erase)
                            {
                                valid.column = 0;
                                valid.row = 0;
                            }
                        }
                    }
                }
            }
        }
    }

    private Position determineEnPassantPos(Position pawnPos)
    {
        Position enPassantPos;
        switch(getSpot(pawnPos).getPiece().getColor().ordinal())
        {
            case 0: //White
                enPassantPos = new Position (pawnPos.column, pawnPos.row + 1);
                break;

            default: //Black
                enPassantPos = new Position (pawnPos.column, pawnPos.row - 1);
                break;
        }
        return enPassantPos;
    }

    // Checkers
    private boolean isValidPosition(Position position)
    {
        return position.column >= 1 && position.column < 9
                && position.row >= 1 && position.row < 9;
    }

    public boolean isCastling(Piece kingChecker, Position castlingPos)
    {
        return ( Piece.isKing(kingChecker) ) &&
                (Math.abs(kingChecker.getPosition().subtract(
                        castlingPos).column) == 2);
    }

    //if a pawn moved to an En Passant position
    private boolean isEnPassant(Position pawnPos, Position newPos)
    {
        Position difference = newPos.subtract(pawnPos);
        difference.column = Math.abs(difference.column); difference.row = Math.abs(difference.row);
        return (Piece.isPawn(getSpot(pawnPos).getPiece()) &&
                !getSpot(newPos).hasPiece()&& difference.column == 1 &&
                difference.row == 1);
    }

    // return if the enemy has an En Passant move to be stored in the moves(ArrayList)
    private boolean enemyHasEnPassant(Position pawnPos, Position newPos)
    {
        Position difference = pawnPos.subtract(newPos);
        // if the moved piece was pawn and if it moved 2 spots vertically
        if (Piece.isPawn(getSpot(pawnPos).getPiece()) && Math.abs(difference.row) == 2)
        {
            Position rightSpot = new Position (newPos.column + 1, newPos.row);
            Position leftSpot = new Position (newPos.column - 1, newPos.row);
            //if the new spot has a pawn next to it (in the right spot or left)
            return (isValidPosition(rightSpot) &&
                    Piece.isPawn(getSpot(rightSpot).getPiece())) ||
                    (isValidPosition(leftSpot) &&
                            Piece.isPawn(getSpot(leftSpot).getPiece()));
        }
        return false;
    }
    ///////////

    //Getters
    public Move getLastMove()
    {
        return lastMove;
    }

    public Spot getSpot(Position position)
    {
        return spots[position.row][position.column];
    }

    //////////
}