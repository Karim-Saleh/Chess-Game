package GUI;

import Logic.Board;
import Logic.Game;
import Logic.Piece;
import Logic.Position;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.JLabel;

public class Moving implements MouseListener, MouseMotionListener {

    private final Game game;
    private final GameFrame frame;
    private final Board board;
    private final GBoard gBoard;

    private Position Drag; /* position of the spot that piece dragged
                                        from when you choose (Drag and Drop) method*/

    private Position firstSelectedSpot; /* position of the moving piece
                                                         when you choose
                                                         click-to-move method*/

    private Position secondSelectedSpot; /* position of the moving
                                                        piece final destination when
                                                        you choose click-to-move
                                                        method */
    private Position lastSelectedSpot;

    private JLabel pieceImage; // The moving piece

    private Position mouseSpotPos; // Position of the mouse inside a spot

    private Position []validPosition; //The valid moves of the moving piece

    private int validLength; // "validPosition" array length

    private boolean moved = false; // Check if a piece dragged on the board

    private final Promotion promotionWindow; // The pop up promotion window

    private static Position promotionPosition; //last promotion piece position

    private Position lastPointedPosition; /* last spot position that
                                                    mouse pointed at */

    private static final Cursor hand = new Cursor(Cursor.HAND_CURSOR); //hand cursor

    private static final Cursor normal = new Cursor(Cursor.DEFAULT_CURSOR); /* default
                                                                             cursor */

    private final Background background;

    private boolean displayed = false;

    /*************************************************************************/

    /*Methods*/

    public Moving(GameFrame frame , Game g, Background background, GBoard gBoard)
    {
        game = g;
        this.frame = frame;
        board = game.getBoard();
        this.gBoard = gBoard;
        this.background = background;
        lastPointedPosition = new Position();
        promotionWindow = new Promotion(frame, board, gBoard);
    }

    public void selection(Point p) {
        firstSelectedSpot = determineSpot(p);
        if (firstSelectedSpot != null && gBoard.hasSamePlayerColor(firstSelectedSpot)) {
            displayValidMoves(firstSelectedSpot);
        } else {
            firstSelectedSpot = null;
        }
    }

    public void Clicking(MouseEvent me) {
        if (firstSelectedSpot != null) {
            secondSelectedSpot = determineSpot(me.getPoint());
            if (!Position.equals(firstSelectedSpot, secondSelectedSpot)) {
                secondSelectedSpot = null;
                gBoard.hideValidMoves(firstSelectedSpot, game.getLastMove(1));
                gBoard.displayLastMovePositions();
                selection(me.getPoint());
            }
        }

        if (secondSelectedSpot != null) {
            firstSelectedSpot = null;
            secondSelectedSpot = null;
        } else if (firstSelectedSpot == null) {
            selection(me.getPoint());
        }

    }

    public boolean move(Position piecePosition, Position destination) {
        if (piecePosition != null && destination != null && game.move(piecePosition, destination)) {
            gBoard.removeLastMovePositions();
            gBoard.hideCheck(game.getLastMove(2));
            gBoard.updateMove(game.getLastMove(1), false);
            gBoard.displayLastMovePositions();
            promotion(destination);
            gBoard.displayCheck(game.getLastMove(1));
            gBoard.changeCursor();
            frame.repaint();
            frame.validate();
            return true;
        } else {
            return false;
        }
    }

    public void dragPiece(MouseEvent me) {
        Drag = determineSpot(me.getPoint());
        gBoard.hideValidMoves(firstSelectedSpot, game.getLastMove(1));
        if (move(firstSelectedSpot, Drag)) {
            Drag = null;
            firstSelectedSpot = null;
        } else {
            gBoard.displayLastMovePositions();
            if (Drag != null && gBoard.hasSamePlayerColor(Drag)) {
                displayValidMoves(Drag);
            }
        }

        if (Drag != null && gBoard.hasSamePlayerColor(Drag)) {
            mouseSpotPos = getMouseSpotPosition(me.getPoint(), Drag);
            pieceImage.setCursor(normal);
        } else {
            Drag = null;
        }

    }

    public void dropPiece(Point p) {
        if (Drag != null) {
            Position Drop = determineSpotPosition(p);
            gBoard.hideValidMoves(Drag, game.getLastMove(1));
            gBoard.getSpot(Drag).getPiece().setCursor(hand);
            if (!move(Drag, Drop)) {
                gBoard.displayCheck(game.getLastMove(1));
                gBoard.displayLastMovePositions();
            }

            background.remove(pieceImage);
            frame.repaint();
            frame.validate();
        }

    }

    public void movePieceOnBoard(Point p) {
        gBoard.hideValidMoves(firstSelectedSpot, game.getLastMove(1));
        if (!moved) {
            gBoard.displayLastMovePositions();
        }

        if (Drag != null) {
            if (!moved) {
                displayValidMoves(Drag);
                background.add(pieceImage);
                background.setComponentZOrder(pieceImage, 0);
            }

            pieceImage.setLocation(p.x - mouseSpotPos.column + gBoard.getX(), p.y - mouseSpotPos.row + gBoard.getY());
            pointing(board.getSpot(Drag).getPiece(), p);
        }

        frame.repaint();
        frame.validate();
        firstSelectedSpot = null;
        moved = true;
    }
    
    private void displayValidMoves(Position pos)
    {
        if(pos != null && board.getSpot(pos).hasPiece()) {
            //initialize the valid position array to display it on the board
            validPosition = board.getSpot(pos).getPiece().getValidMoves();
            validLength = validPosition.length;
            gBoard.displayValidMoves(pos, game.getLastMove(1)); //display the valid positions
        }
    }

    //displays a colored square if the mouse pointed to a valid position
    private void pointing(Piece movingPiece,Point p)
    {
        Position pos = determineSpotPosition(p);
        if (pos != null) // if the position was valid
        {

            if (!Position.equals(lastPointedPosition,pos))
                /*if the mouse pointed to another spot as "lastPointedPosition"
                wasn't updated to the new position yet to avoid drawing the same
                spot many times*/
            {
                // display the new valid position colored rectangle
                gBoard.displayValidPointed(movingPiece ,pos,game.getLastMove(1));

                // hide the previous valid position colored rectangle
                gBoard.hideValidPointed(movingPiece ,lastPointedPosition, game.getLastMove(1));
            }
            lastPointedPosition = pos; // Updating "lastPointedPosition"
        }
    }
    public Position determineSpotPosition(Point p)
    {
        Position pos = new Position();
        pos.column = (p.x/ gBoard.getSpotSize().width) + 1; /* frame x-axis starts from left to
                                        right as we do with spots */

        pos.row = 8 - (p.y/ gBoard.getSpotSize().height); /* frame y-axis is inverted as it starts
                                         from up to down so we invert it by
                                         subtracting from 8(number of board rows) */

        if (gBoard.isValidPosition(pos))
        //if position was only a spot position (from 1 to 8)
        {
            return pos;
        }
        else
        {
            return null;
        }
    }

    public void hideValidMoves()
    {
        if(displayed) {
            gBoard.hideValidMoves(firstSelectedSpot, game.getLastMove(1));
            displayed = false;
        }
        firstSelectedSpot = null;
    }

    // return spot position and set the c value to the spot piece
    private Position determineSpot(Point p)
    {
        Position pos = determineSpotPosition(p);
        if (pos != null) //if position was valid
        {
            pieceImage = gBoard.getSpot(pos).getPiece();
            return pos;
        }
        else
        {
            return null;
        }
    }

    /* It returns the position of the mouse inside a spot to know where exactly
     the mouse pointed on the piece to move the piece from its same point */
    public Position getMouseSpotPosition(Point p, Position spotPos)
    {
        Position pos = new Position();
        pos.column = Math.abs( (p.x) - (gBoard.getSpot(spotPos).getX()) ) - pieceImage.getX();
        pos.row = Math.abs( (p.y) - (gBoard.getSpot(spotPos).getY()) ) - pieceImage.getY();
        return pos;
    }

    public void promotion(Position destination)
    {
        if (promotionWindow.isPromotion(destination))
        {
            promotionWindow.showPromotionWindow(destination);
            if (!promotionWindow.isPromoted())
            {
                gBoard.Undo();
                game.getMoves().clean();
            }
            promotionWindow.setPromoted(false);
        }
    }

    //setters
    public static void setPromotionPosition(Position Pos) {
        promotionPosition = Pos;
    }
    ///////////

    /**Getters**/
    public Position[] getValidPosition()
    {
        return validPosition;
    }
    public int getValidPositionLength()
    {
        return validLength;
    }
    public static Position getPromotionPosition()
    {
        return promotionPosition;
    }
    /****************************************************************************/

    /*Events*/

    @Override
    public void mouseClicked(MouseEvent me)
    {
        if (me.getButton() == 1) {
            Clicking(me);
        }
    }
    @Override
    public void mousePressed(MouseEvent me)
    {
        if (me.getButton() == 1) // left button is clicked
        {
            dragPiece (me);
        }
    }
    @Override
    public void mouseReleased(MouseEvent me)
    {
        if(me.getButton() == 1) {
            dropPiece(me.getPoint());
        }
        Drag = null; pieceImage = null; mouseSpotPos = null; moved = false;
    }

    @Override
    public void mouseEntered(MouseEvent me){}

    @Override
    public void mouseExited(MouseEvent me){}

    @Override
    public void mouseDragged(MouseEvent me)
    {
        movePieceOnBoard(me.getPoint());
    }

    @Override
    public void mouseMoved(MouseEvent me)
    {
        if (firstSelectedSpot != null) {
            pointing(board.getSpot(firstSelectedSpot).getPiece(), me.getPoint());
        }
    }
}
