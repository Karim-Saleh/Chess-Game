package GUI;

import Logic.Piece;

import javax.swing.*;
import java.awt.*;

public class PlayerInfo extends JPanel
{
    private final PlayerState playerState;
    private final CapturedPieces capturedPieces;
    //private final ModifiedScrollPane scrollPane;
    private final boolean active;
    public PlayerInfo(String playerName, int x, int y, Dimension size, boolean white)
    {
        active = white;
        //player state
        setLayout(null);
        setBounds(x, y, size.width, size.height);
        setOpaque(false);
        ////////////////

        //Player name
        playerState = new PlayerState(playerName, white) ;
        add(playerState);
        ///////////////

        //Captured Pieces
        //Scrollbar
        capturedPieces = new CapturedPieces();
        /*scrollPane = new ModifiedScrollPane(capturedPieces,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane);*/
        add(capturedPieces);
        ///////////
        /////////////////

        repaint();
    }
    public void autoSetPlayerInfo()
    {
        playerState.autoSetBounds();
        //Player States
        int PSHeight = playerState.getNameHeight();
        int PSWidth = playerState.getWidth();
        int PSX = (getWidth() - PSWidth) / 2;
        int PSY = active ? GameFrame.screenSize.height - PSHeight - 30 : 30;
        playerState.setBounds(PSX, PSY, PSWidth, PSHeight);
        ///////////////
/*

        //Scroll Panel
        int SPHeight = capturedPieces.getPieceHeight() +
                scrollPane.getHorizontalScrollBar().getHeight();
        int SPWidth = getWidth();
        int SPX = 0;
        int SPY = active ? PSY - 100 : PSY + PSHeight + 100 - SPHeight;
        scrollPane.setBounds(SPX, SPY, SPWidth, SPHeight);
        ///////////////
*/

        int SPHeight = capturedPieces.getPieceHeight();
        int SPWidth = getWidth();
        int SPX = 0;
        int SPY = active ? PSY - SPHeight - 50 : PSY + PSHeight + 50;
        capturedPieces.setBounds(SPX, SPY, SPWidth, SPHeight);
    }
    private void changeTurn()
    {
        playerState.changeTurn();
    }

    public static void addCapturedPieces(PlayerInfo white, PlayerInfo black,
                                         int moves, Piece piece)
    {
        white.changeTurn();
        black.changeTurn();
        CapturedPieces.alterPieces(white.capturedPieces, black.capturedPieces, moves, piece);
/*
        //making the bar at the end
        JScrollBar whiteHorizontal = white.scrollPane.getHorizontalScrollBar();
        JScrollBar blackHorizontal = black.scrollPane.getHorizontalScrollBar();
        AdjustmentListener rightScroller = new AdjustmentListener() {

            @Override
            public void adjustmentValueChanged(AdjustmentEvent e) {
                Adjustable adjustable = e.getAdjustable();
                adjustable.setValue(adjustable.getMaximum());
                blackHorizontal.removeAdjustmentListener(this);
                whiteHorizontal.removeAdjustmentListener(this);
            }
        };
        whiteHorizontal.addAdjustmentListener(rightScroller);
        blackHorizontal.addAdjustmentListener(rightScroller);
        ///////////////////////////////
        */
    }
}