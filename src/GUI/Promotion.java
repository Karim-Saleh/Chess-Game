package GUI;

import Logic.*;
import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Promotion extends JDialog implements ActionListener {

    private final JFrame frame;
    private final Board board;
    private final GBoard GBoard;
    private final JButton pieces[]; // Pieces in the promotion window
    private boolean promoted = false; // a sign of promotion
    private final int buttonLength;

    Promotion(JFrame frame, Board board, GBoard GBoard)
    {
        super(frame, "Promotion",
                ModalityType.APPLICATION_MODAL); /* first argument means that this JDialog
                                            is related to the frame (if the frame is
                                            closed the JDialog is closed). the second
                                            is the title. the third means that the
                                            JDialog blocks the frame until it have been
                                            closed*/

        this.pieces = new JButton[4];
        this.frame = frame;
        this.board = board;
        this.GBoard = GBoard;
        buttonLength = (int)Math.min(15.0 * (double) GBoard.getWidth() / 100.0,
                15.0 * (double) GBoard.getHeight() / 100.0);
        initializePromotionWindow();
    }

    // Methods
    private void initializePromotionWindow()
    {
        this.setLayout(new GridLayout(1,4));
        this.setResizable(false);
        for (int i = 0; i < 4; i++)
        {
            pieces[i] = new JButton();
            pieces[i].setLayout(new GridBagLayout());
        }
    }
    // adding the pieces pictures to the buttons and buttons to the JDialog
    private void buildPromotionWindow(Position newPosition)
    {
        String pieceName = "Queen";
        for (int i = 0; i< 4; i++)
        {
            if (i == 1)
                pieceName = "Rook";
            else if (i == 2)
                pieceName = "Bishop";
            else if (i == 3)
                pieceName = "Knight";
            pieces[i].removeAll(); /*removing the previous piece on the button
                                    to add a new one */

            String imageName = board.getSpot(newPosition).getPiece().getColor().
                    name() + pieceName; // Setting the piece picture name
            try
            {
                BufferedImage image =
                        ImageIO.read(new File("Images/" + imageName + ".png"));

                Image scaledImage =
                        image.getScaledInstance(buttonLength, buttonLength, Image.SCALE_SMOOTH);

                JLabel pic = new JLabel(new ImageIcon(scaledImage));
                pic.setSize(buttonLength,buttonLength);
                pic.setCursor(new Cursor(Cursor.HAND_CURSOR));
                pieces[i].add(pic); //Adding the piece picture to the button
                this.add(pieces[i]); // Adding the button to the window
                pieces[i].setPreferredSize(new Dimension(buttonLength, buttonLength));
            }
            catch (IOException e)
            {
                e.printStackTrace();
                System.out.println("Error! Couldn't load piece image.");
            }
            pieces[i].addActionListener(this); //Adding the event to the button
        }
        pack();
    }

    public void promoting (Piece promotedPiece)
    {
        /* updating pawn to the new piece on board(not gui) */
        board.promotion(promotedPiece ,Moving.getPromotionPosition());

        GBoard.getSpot(Moving.getPromotionPosition()).
                addPieceImage(board); /* updating pawn to the new piece on gui*/

        /* displaying this move positions (Gold spots) */
        GBoard.getSpot(Moving.getPromotionPosition()).drawSquare(new
                Color(255,215,0,140), new Cursor(Cursor.DEFAULT_CURSOR));

        promoted = true; /* this means the player chose a piece and didn't close
                            the promotion window */


        this.dispose(); // Closing the promotion Window
    }

    public void showPromotionWindow(Position newPosition)
    {
        buildPromotionWindow(newPosition);
        this.setLocationRelativeTo(frame); /* setting the dialog location at the
                                            middle of the frame */

        Moving.setPromotionPosition(newPosition);
        this.setVisible(true); //showing the window
    }
    /////////////////////////

    // Checkers
    private boolean isPawn(Position newPosition)
    {
        return ("Pawn".equals(board.getSpot(newPosition).getPiece().getClass().getSimpleName()));
    }

    public boolean isPromotion(Position newPosition)
    {
        if (newPosition == null || !board.getSpot(newPosition).hasPiece())
        {
            return false;
        }
        else
        {
            return (isPawn(newPosition) &&
                    (newPosition.row == 1 || newPosition.row == 8));
        }
    }
    //////////////////////////////

    //Getters
    public boolean isPromoted() {
        return promoted;
    }
    //////////

    // Setters
    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }
    /////////

/***********************************************************************************/

    /*Events*/
    /***********/

    // Buttons events for the pieces on the dialog
    @Override
    public void actionPerformed(ActionEvent ae)
    {

        if (ae.getSource() == pieces[0]) // if the piece was a queen
        {
            promoting(new Queen(board.getSpot(Moving.getPromotionPosition()).getPiece().getColor()));
        }
        else if (ae.getSource() == pieces[1]) // if the piece was a rook
        {
            promoting(new Rook(board.getSpot(Moving.getPromotionPosition()).getPiece().getColor()));
        }
        else if (ae.getSource() == pieces[2]) // if the piece was a bishop
        {
            promoting(new Bishop(board.getSpot(Moving.getPromotionPosition()).getPiece().getColor()));
        }
        else if (ae.getSource() == pieces[3]) // if the piece was a knight
        {
            promoting(new Knight(board.getSpot(Moving.getPromotionPosition()).getPiece().getColor()));
        }
    }
}
