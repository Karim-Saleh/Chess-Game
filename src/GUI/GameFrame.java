package GUI;

import Logic.Game;
import Logic.Piece;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GameFrame extends JFrame {
    private static final int width = 1080, height = 1080; //You can change the size here
    private final Game game;
    public static final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize()
            ,size = new Dimension(width * screenSize.width/1920, height * screenSize.height/1080);
    private final Dimension boardSize;
    private final GBoard GBoard;
    private final Background backGround;
    private final PlayerInfo white, black;
    private JButton undo, redo;
    public GameFrame(Game g)
    {
        //Frame
        super("Chess");
        setUndecorated(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ////////////

        //Background
        backGround = new Background();
        setContentPane(backGround);
        pack();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //////////////

        //Board
        boardSize = new Dimension(backGround.getWidth(), backGround.getHeight());
        GBoard = new GBoard(g, this, backGround);
        backGround.add(GBoard);
        /////////

    //Players

        //White
        final Dimension whiteSize = new Dimension((int)(screenSize.getWidth() -
                boardSize.width)/2, boardSize.height);
        final int whiteX = GBoard.getX() + boardSize.width, whiteY = 0;
        white = new PlayerInfo("Player 1", whiteX, whiteY, whiteSize, true);
        backGround.add(white);
        ////////

        //Black
        final Dimension blackSize = new Dimension(((int)screenSize.getWidth() -
                boardSize.width)/2, boardSize.height);
        final int blackX = 0, blackY = 0;
        black = new PlayerInfo("Player 2", blackX, blackY, blackSize, false);
        backGround.add(black);
        ////////

    ///////////
        validate();
        white.autoSetPlayerInfo();
        black.autoSetPlayerInfo();
        this.game = g;

        //Undo & Redo
        createButtons();
        /////////////////
        setVisible(true);
    }

    private void createButtons()
    {
        undo = new JButton(); redo = new JButton();
        final int length = 70;
        BufferedImage undoImage, redoImage;
        undoImage = initializeButton("Images/Undo.png", length, 30, undo);
        redoImage = initializeButton("Images/Redo.png", length, undo.getX()+ length + 40, redo);
        setButtonMouse(undo, undoImage, true, length);
        setButtonMouse(redo, redoImage, false, length);
    }

    private BufferedImage initializeButton(String location, int length, int x,
                                           JButton btn)
    {
        final Cursor hand = new Cursor(Cursor.HAND_CURSOR);
        BufferedImage bf = null;
        btn.setLayout(new GridBagLayout());
        try
        {
            bf = ImageIO.read(new File(location));
            Image im = bf.getScaledInstance(length, length, Image.SCALE_SMOOTH);
            JLabel btnLabel = new JLabel(new ImageIcon(im));
            btnLabel.setOpaque(false);
            btn.add(btnLabel);
            btn.setBounds(x, GameFrame.height - length-10, length, length);
            backGround.add(btn);
            backGround.setComponentZOrder(btn, 0);
            btn.setCursor(hand);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("Can't load image");
        }
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setBorder(null);
        return bf;
    }

    private void setButtonMouse(JButton btn, BufferedImage bf, boolean undo, int length)
    {
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(undo)
                    GBoard.Undo();
                else
                    GBoard.Redo();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                btn.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                final int constant = 5;
                JLabel tmp = (JLabel) btn.getComponentAt(0, 0);
                tmp.removeAll();
                Image im = bf.getScaledInstance(length + constant, length + constant, Image.SCALE_SMOOTH);
                tmp.setIcon(new ImageIcon(im));
                btn.setSize(length + constant, length + constant);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                JLabel tmp = (JLabel) btn.getComponentAt(0, 0);
                tmp.removeAll();
                Image im = bf.getScaledInstance(length, length, Image.SCALE_SMOOTH);
                tmp.setIcon(new ImageIcon(im));
                btn.setSize(length, length);
            }
        });
    }

    public Dimension getBoardSize()
    {
        return boardSize;
    }
    /////////

    public void changeTurn(Piece capturedPiece)
    {
        PlayerInfo.addCapturedPieces(white, black, game.getMoves().getSize(), capturedPiece);
    }
}
