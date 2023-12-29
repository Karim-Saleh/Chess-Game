package Logic;

public class Spot implements Cloneable
{
    private Piece piece = null;
    private boolean hasPiece = false; // checks if the spot has a piece or not
    private boolean hasSet = false;

    public Spot(){
    }

    public Spot(Spot spot)
    {
        this.piece = Piece.getPieceCopy(spot.piece);
        this.hasPiece = spot.hasPiece;
    }

    public Spot(Position position, Piece piece) // constructor of Spots
    {
        this.setPiece(piece, position);
    }

    protected Object clone()
    {
        Object clone = null;
        try{
            clone = super.clone();
        } catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }

    public void setPiece(Piece piece, Position position) // takes the parameter (piece) and assigns it to the piece variable
    {
        this.piece = piece;
        setHasPiece(piece);
        if (piece != null)
        {
            piece.setPosition(position.column, position.row);
        }
    }

    public Piece getPiece() // returns the value of the piece
    {
        return piece;
    }

    public static abstract class spotCache
    {
        private static Spot spot = new Spot();
        public static Spot getSpot()
        {
            return (Spot)spot.clone();
        }
        public static Spot getSpot(Spot spot)
        {
            return (Spot)spot.clone();
        }
    }

    public boolean hasPiece() // returns the value of the boolean hasPiece
    {
        return hasPiece;
    }

    private void setHasPiece(Piece piece)
    {
        hasPiece = piece != null;
    }


}