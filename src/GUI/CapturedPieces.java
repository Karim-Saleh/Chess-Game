package GUI;

import Logic.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class CapturedPieces extends JPanel
{
    private static final LinkedStack<JLabel> pieces = new LinkedStack<>();
    private final int pieceLength = 70, constant = 19;
    private static int movesSize = 0;
    private int x = 0, y = 0, count = 0, yConst;
    private final SpringLayout layout = new SpringLayout();

    public CapturedPieces()
    {
        setLayout(null);
        setOpaque(false);
    }
    private static void addPiece(CapturedPieces player, Piece piece)
    {
        if(piece != null)
        {
            JLabel pieceImage;
            String location = "Images/" + piece.getColor() + piece.getClass().getSimpleName()
                    + ".png";
            try
            {
                BufferedImage image = ImageIO.read(new File(location));
                Image scaledImage = image.getScaledInstance(player.pieceLength,
                        player.pieceLength, Image.SCALE_SMOOTH);
                pieceImage = new JLabel(new ImageIcon(scaledImage));
                player.add(pieceImage, BorderLayout.WEST);
                pieces.push(pieceImage);
                player.count++;
                if(player.count == 1)
                {
                    player.x = -1 * (player.pieceLength + player.constant);
                    player.y = (piece.getColor().ordinal() == 0) ? 0 :
                            2 * (player.pieceLength + player.constant);
                    player.yConst = piece.getColor().ordinal() == 0 ? 1 : -1;
                }
                player.x = (player.count == 6 || player.count == 11) ? 0 :
                        player.x + player.pieceLength + player.constant;
                player.y += (player.count == 6 || player.count == 11) ?
                        player.yConst * (player.pieceLength + player.constant) : 0;
                pieceImage.setBounds(player.x, player.y, player.pieceLength,
                        player.pieceLength);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            player.validate(); player.repaint();
        }
    }

    private boolean isComponentFound(Component c)
    {
        Component[] components = this.getComponents();
        for (Component component : components) {
            if (c == component) {
                return true;
            }
        }
        return false;
    }

    private static void removePiece(CapturedPieces player)
    {
        if(pieces.Top() != null) {
            player.remove(pieces.Top());
            player.repaint();
            player.validate();
            pieces.pop();
            player.count--;
            if(player.count == 0)
            {
                player.x = -1 * (player.pieceLength + player.constant);
                player.y = (player.yConst == 1) ? 0 :
                        2 * (player.pieceLength + player.constant);
            }
            player.x = (player.count == 5 || player.count == 10) ?
                    4 * (player.pieceLength + player.constant) :
                    player.x - player.pieceLength - player.constant;
            player.y -= (player.count == 5 || player.count == 10) ?
                    player.yConst * (player.pieceLength + player.constant) : 0;
        }
    }

    public static void alterPieces(CapturedPieces player1, CapturedPieces player2,
                                   int moves, Piece piece)
    {
        if(piece != null)
        {
            if(moves > movesSize)
            {
                CapturedPieces player = piece.getColor().ordinal() == 0 ? player2 : player1;
                    addPiece(player, piece);
                movesSize = moves;
            }
            else if (moves < movesSize)
            {
                CapturedPieces player = player1.isComponentFound(pieces.Top()) ? player1 : player2;
                removePiece(player);
                movesSize = moves;
            }
        }
    }

    public int getPieceHeight()
    {
        return 2*(pieceLength + constant) + pieceLength;
    }
}
