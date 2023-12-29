package GUI;

import Logic.Board;
import Logic.Game;
import Logic.Move;
import Logic.Piece;
import Logic.Position;

import java.awt.*;
import javax.swing.JPanel;

public class GBoard extends JPanel
{
    private final GameFrame frame;

    private final Game game;
    private final Board board;
    private static int count = 0;

    // this Array starts from 1,1
    private final GSpot spots[][];

    private final Moving moving;

    private static Dimension spotSize;

    private Move lastMove; // it is used to make undo

    private static final Cursor hand = new Cursor(Cursor.HAND_CURSOR); //hand cursor

    private static final Cursor normal = new Cursor(Cursor.DEFAULT_CURSOR); /* default
                                                                                 cursor */

    public GBoard(Game g, GameFrame frame, Background background)
    {
        super(null);
        game = g;
        this.frame = frame;
        board = game.getBoard();
        setBounds((GameFrame.screenSize.width-frame.getBoardSize().width)/2, 0, frame.getBoardSize().width, frame.getBoardSize().height);
        this.setBackground(new Color(54, 50, 50));
        spotSize = new Dimension(this.getWidth()  / 8, this.getHeight() / 8);
        spots = new GSpot[9][9];
        drawSpots();
        validate();
        changeCursor();
        moving = new Moving(this.frame ,game, background,this);
        this.addMouseListener(moving);
        this.addMouseMotionListener(moving);
    }


    private void drawSpots()
    {
        Position position = new Position();

        for (int i = 1; i <= 8 ; i++)
        {
            position.row = i;
            for (int j = 1; j <= 8; j++)
            {
                position.column = j;
                spots[i][j] = new GSpot(board,position); //initializing a new spot
                add(spots[i][j]); // adding the spot to the board

                /* setting spot location on board */

                /* as the frame starts drawing from up to down we have to
                    invert i to start from down by subtracting from 8 */
                spots[i][j].setBounds(spotSize.width * (j-1),
                        spotSize.height * (8-i), spotSize.width, spotSize.height);

                //////////////////////////////////
            }
        }
    }

    public void Redo()
    {
        moving.hideValidMoves();
        if (game.Redo()) {
            hideCheck(game.getLastMove(2));
            updateMove(game.getLastMove(1), false);
            displayCheck(game.getLastMove(1)); /* displays the red check square if it was
                                                  check*/
            removeLastMovePositions();

            displayLastMovePositions(); /* display the last move positions after redo*/

            changeCursor(); // change cursor after undo as the players turns changed
        }
    }

    public void Undo()
    {
        moving.hideValidMoves();
        if(game.Undo())
        {
            hideCheck(lastMove);

            updateMove(lastMove, true); // update the spots after undo
            displayCheck(game.getLastMove(1)); /* displays the check red square if the
                                                move after undo was a check */

            displayLastMovePositions(); /* display the last move positions after undo
                                        (the move before the last move) */

            changeCursor(); // change cursor after undo as the players turns changed
        }
    }

    // update the piece first and last positions
    public void updateMove(Move m, boolean undo)
    {
        if (m != null)
        {
            Position first = m.getFirstPosition();
            Position last = m.getLastPosition();

            // draw the two spots again
            spots[first.row][first.column].addPieceImage(board);
            spots[last.row][last.column].addPieceImage(board);
            ///////////////////////////////

            if (m.isCastling())// if this move was a Castling
            {
                Position rookPos = m.getCastlingRookFirstPos();
                Position rookNewPos = m.getCastlingRookNewPos();

                // draw the two rook move spots again
                spots[rookPos.row][rookPos.column].addPieceImage(board);
                spots[rookNewPos.row][rookNewPos.column].addPieceImage(board);
                /////////////////////////////////////
            }

            //Enpassant
            else if (!undo)// if this function wasn't called in "Undo" function
            {
                Move previousMove = game.getLastMove(2);
                if (previousMove != null && previousMove.isEnemyHasEnPassant() &&

                        //if the pawn is in the en passant position(a pawn was captured)
                        Position.equals(previousMove.getEnemyEnPassantPos(),
                                m.getLastPosition()))
                {
                    //removing the pawn captured in enpassant
                    getSpot(previousMove.getLastPosition()).addPieceImage(board);
                }
            }
            else if (undo) // if this function was called in "Undo" function
            {
                Move previousMove = game.getLastMove(1);/* it is the previous move
                                                            before deleting the last
                                                            move in the Game.Undo
                                                            function */

                if (previousMove != null && previousMove.isEnemyHasEnPassant() &&

                        //if the pawn is in the en passant position(a pawn was captured)
                        Position.equals(previousMove.getEnemyEnPassantPos(),
                                m.getLastPosition()))
                {
                    //get the captured pawn back
                    getSpot(previousMove.getLastPosition()).addPieceImage(board);
                }
            }
            frame.changeTurn(m.getCapturedPiece());
            frame.repaint();
        }
        lastMove = game.getLastMove(1); // Storing the last move
    }

    // it makes the pieces cursor of the player who have to play, hand and the other normal
    public void changeCursor()
    {
        Position pos = new Position();
        for(int i = 1 ; i <= 8; i++)
        {
            pos.row = i;
            for (int j = 1; j <= 8; j++)
            {
                pos.column = j;
                // if the spot has a piece
                if (board.getSpot(pos).hasPiece())
                {
                    // if the piece color is the same player color
                    if (hasSamePlayerColor(pos))
                    {
                        spots[i][j].getPiece().setCursor(hand); // make it hand
                    }
                    else
                    {
                        spots[i][j].getPiece().setCursor(normal); //make it normal
                    }
                }
            }
        }
    }

    // displays the last move Golden squares
    public void displayLastMovePositions()
    {
        java.awt.Color c = new java.awt.Color(255,215,0,140); // Gold
        Move m = game.getLastMove(1); // the last move on the moves array
        if (m != null)
        /* "m" may be null if (only one move is or no moves are)
            in the array and we made an undo */
        {
            Position first = m.getFirstPosition();
            Position last = m.getLastPosition();

            // display the Golden squares
            spots[first.row][first.column].drawSquare(c, normal);
            spots[last.row][last.column].drawSquare(c, normal);
            //////////////////////////////
        }
    }

    // hides the last move Golden squares
    public void removeLastMovePositions()
    {
        Move previousMove = game.getLastMove(2); /* 2 means the second value
                                                    from the end */
        if (previousMove != null)
        // "previousMove" may be null if the array dosn't have at least 2 values
        {
            Position first = previousMove.getFirstPosition();
            Position last = previousMove.getLastPosition();

            // delete golden rectangles
            spots[first.row][first.column].drawPieceOnly();
            spots[last.row][last.column].drawPieceOnly();
            ///////////////////////////
        }
    }
    // displaying valid moves and the moving piece green square
    public void displayValidMoves (Position piecePosition, Move m)
    {
        if (isValidPosition(piecePosition) &&
                moving.getValidPosition() != null)
        {
            for (int i = 0; i < moving.getValidPositionLength(); i++)
            {
                // if the array value is valid as it may be (0,0)
                if (isValidPosition(moving.getValidPosition()[i]))
                {
                    //if this valid position is an En Passant move
                    if (m != null && m.isEnemyHasEnPassant()
                            && Piece.isPawn(board.getSpot(piecePosition).getPiece())
                            && Position.equals(moving.getValidPosition()[i],
                            m.getEnemyEnPassantPos()))
                    {
                        getSpot(moving.getValidPosition()[i])
                                .drawValidMove(true);
                    }
                    else
                    {
                        // displaying valid moves
                        getSpot(moving.getValidPosition()[i])
                                .drawValidMove(false);
                    }
                }
            }
            java.awt.Color green = new java.awt.Color(0,120,0,60);

            /* if there is a check and the checked king was the selected piece
            to avoid hiding the check red square*/
            if ((m == null) || !(m.isEnemyInCheck()) ||
                    (m.isEnemyInCheck() && !Position.equals(piecePosition, m.getCheckedEnemyKing())))
            {
                // displaying green square of the moving piece if wasn't a checked king
                spots[piecePosition.row][piecePosition.column].drawSquare(green, hand);
            }
        }
    }

    // hides valid moves and the moving piece green square
    public void hideValidMoves(Position piecePosition, Move m)
    {
        if (isValidPosition(piecePosition) &&
                moving.getValidPosition() != null)
        {
            for (int i = 0; i < moving.getValidPositionLength(); i++)
            {
                if (isValidPosition(moving.getValidPosition()[i]))
                {
                    spots[moving.getValidPosition()[i].row][moving.getValidPosition()[i].column]
                            .drawPieceOnly(); // Hide valid moves (green circles)
                    if (board.getSpot(moving.getValidPosition()[i]).hasPiece())
                    {
                        spots[moving.getValidPosition()[i].row][moving.getValidPosition()[i].column]
                                .getPiece().setCursor(normal);
                    }
                }
            }

            /* if there is a check and the checked king was the selected piece
            to avoid hiding the check red square*/
            if ((m == null) || !(m.isEnemyInCheck()) ||
                    (m.isEnemyInCheck() &&
                            !Position.equals(piecePosition, m.getCheckedEnemyKing())))
            {
                // deleting green square of the moving piece if wasn't a checked king
                spots[piecePosition.row][piecePosition.column].drawPieceOnly();
            }
        }
    }

    // displays a green or a red square and the moving piece green square
    public void displayValidPointed(Piece movingPiece ,Position pos, Move m)
    {
        if (moving.getValidPosition() != null)
        {
            for (int i = 0; i < moving.getValidPositionLength(); i++)
            {
                // if we found the mouse position in the array
                if (isValidPosition(moving.getValidPosition()[i]) &&
                        Position.equals(pos, moving.getValidPosition()[i]))
                {
                    java.awt.Color c;
                    if (board.getSpot(pos).hasPiece())
                    {
                        c = new java.awt.Color(180,0,0,120); //Red
                    }
                    else
                    {
                        //if this valid position is an En Passant move
                        if (m != null && m.isEnemyHasEnPassant() &&
                                Piece.isPawn(movingPiece)
                                && Position.equals(pos, m.getEnemyEnPassantPos()))
                        {
                            c = new java.awt.Color(180,0,0,120); //Red
                        }
                        else
                        {
                            c = new java.awt.Color(0,80,0,80); //Green
                        }
                    }
                    getSpot(pos).drawSquare(c, hand);
                    break;
                }
            }
        }
    }

    // hides a green or a red square if mouse pull away from a pointed spot
    public void hideValidPointed(Piece movingPiece ,Position pos, Move m)
    {
        if (moving.getValidPosition() != null)
        {
            for (int i = 0; i < moving.getValidPositionLength(); i++)
            {
                // if we found the mouse position in the array
                if (isValidPosition(moving.getValidPosition()[i]) &&
                        Position.equals(pos, moving.getValidPosition()[i]))
                {
                    boolean enPassant = false;
                    // displays the valid position color again

                    //if this valid position is an En Passant move
                    if (m != null && m.isEnemyHasEnPassant() &&
                            Piece.isPawn(movingPiece)
                            && Position.equals(pos, m.getEnemyEnPassantPos()))
                    {
                        enPassant = true;
                        getSpot(pos).drawValidMove(enPassant);
                    }
                    else
                    {
                        getSpot(pos).drawValidMove(enPassant);
                    }
                    break;
                }
            }
        }
    }

    //Displays check red square
    public void displayCheck(Move m)
    {
        if (m != null && m.isEnemyInCheck())
        {
            Position checkedKing = m.getCheckedEnemyKing();
            java.awt.Color red = new Color(240,0,0,120);
            getSpot(checkedKing).drawSquare(red, hand);
        }
    }

    public void hideCheck(Move m)
    {
        if (m != null && m.isEnemyInCheck())
        {
            Position checkedKing = m.getCheckedEnemyKing();
            getSpot(checkedKing).drawPieceOnly();
        }
    }

    // Checkers
    public boolean isValidPosition(Position pos)
    {
        if (pos != null)
        {
            return (pos.row > 0 && pos.row < 9 && pos.column > 0 && pos.column < 9);
        }
        else
        {
            return false;
        }
    }

    public boolean hasSamePlayerColor(Position pos)
    {
        if (!isValidPosition(pos) || !board.getSpot(pos).hasPiece())
        {
            return false;
        }
        else
        {
            return board.getSpot(pos).getPiece().getColor().equals(
                    game.getCrntPlayerClr());
        }
    }
    /////////////////////////////////////////////////////////

    //Getters
    public GSpot getSpot(Position pos)
    {
        return spots[pos.row][pos.column];
    }

    public static Dimension getSpotSize()
    {
        return spotSize;
    }
    /////////////
}

