package Logic;

public class Move
{
    private final Position firstPosition;
    private final Position lastPosition;
    private final Piece movingPiece;
    private Piece capturedPiece;
    private final boolean firstMove;
    private boolean castling = false;
    private Position castlingRookFirstPos;
    private Position castlingRookNewPos;
    private boolean enemyInCheck = false;
    private Position checkedEnemyKing;
    private boolean enPassant = false;
    private boolean enemyHasEnPassant = false;
    private Position enemyEnPassantPos;
    private boolean promotion = false;
    private Piece promotedPiece = null;

    public Move(Position first, Position last, Piece movPiece, Piece capPiece,
                boolean firstMove)
    {
        this.firstPosition = first;
        this.lastPosition = last;
        this.movingPiece = movPiece;
        this.capturedPiece = capPiece;
        this.firstMove = firstMove;

    }

    // Setters
    public void setCapturedPiece(Piece capturedPiece) {
        this.capturedPiece = capturedPiece;
    }

    public void setCastling(boolean b)
    {
        castling = b;
    }

    public void setCastlingRookFirstPos(Position position)
    {
        castlingRookFirstPos = new Position(position);
    }

    public void setCastlingRookNewPos(Position position)
    {
        castlingRookNewPos = new Position(position);
    }

    public void setEnemyInCheck(boolean b)
    {
        this.enemyInCheck = b;
    }

    public void setCheckedEnemyKing(Position kingPos)
    {
        this.checkedEnemyKing = kingPos;
    }

    public void setEnPassant(boolean enPassant) {
        this.enPassant = enPassant;
    }

    public void setEnemyHasEnPassant(boolean b)
    {
        this.enemyHasEnPassant = b;
    }

    public void setEnemyEnPassantPos(Position pawnPos)
    {
        this.enemyEnPassantPos = pawnPos;
    }

    public void setPromotedPiece(Piece piece)
    {
        promotion = true;
        promotedPiece = piece;
    }

//////////

    //Gettres
    public Position getFirstPosition()
    {
        return firstPosition;
    }

    public Piece getMovingPiece()
    {
        return movingPiece;
    }

    public Position getLastPosition()
    {
        return lastPosition;
    }

    public Piece getCapturedPiece()
    {
        return capturedPiece;
    }

    public boolean isFirstMove()
    {
        return firstMove;
    }

    public boolean isCastling()
    {
        return castling;
    }

    public Position getCastlingRookFirstPos()
    {
        return castlingRookFirstPos;
    }

    public Position getCastlingRookNewPos()
    {
        return castlingRookNewPos;
    }

    public boolean isEnemyInCheck()
    {
        return enemyInCheck;
    }

    public Position getCheckedEnemyKing()
    {
        return checkedEnemyKing;
    }

    public boolean isEnPassant() {
        return enPassant;
    }

    public boolean isEnemyHasEnPassant()
    {
        return enemyHasEnPassant;
    }

    public Position getEnemyEnPassantPos()
    {
        return enemyEnPassantPos;
    }

    public boolean isPromotion()
    {
        return promotion;
    }

    public Piece getPromotedPiece()
    {
        return promotedPiece;
    }
    ///////////
}