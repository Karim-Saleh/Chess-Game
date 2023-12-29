package GUI;

import Logic.Piece;
import Logic.Position;
import Logic.Board;

import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
    //old one
    private static final Color LIGHT_COLOR = new Color(215, 190, 148);
    private static final Color DARK_COLOR = new Color(151, 93, 7);

//blue
    private static final Color LIGHT_COLOR = new Color(204,229,255);
    private static final Color DARK_COLOR = new Color(28,142,237);
*/
public class GSpot extends JPanel
{
    private final Position position;
    private Position coordinates;
    private static final Color lightColor = new Color(238, 209, 187);
    private static final Color darkColor =  new Color(121, 71, 35);
    private JLabel pieceImage, rowNumber, columnNumber;
    private final int pieceWidth, pieceHeight;
    public final Dimension pieceSize;
    private Image spotImage;

    //Methods
    public GSpot(final Board board, Position position)
    {
        super(null);
        this.position = new Position(position);
        pieceWidth = (int)Math.ceil(103.0 *  (double) GBoard.getSpotSize().width / 100.0);
        pieceHeight = (int)Math.ceil(103.0 * (double) GBoard.getSpotSize().height / 100.0);
        pieceSize = new Dimension(pieceWidth, pieceHeight);
        assignSpotColor();
        loadSpotImage();
        addPieceImage(board);
        validate();
    }

    private String assignSpotColor()
    {
        String color;
        if (position.row % 2 == 0)
        {
            color = position.column % 2 == 0 ? "dark" : "light";
        }
        else
        {
            color = position.column % 2 == 0 ? "light" : "dark";
        }
        return color;
    }

    private void loadSpotImage()
    {
        try
        {
            final BufferedImage image = ImageIO.read(new File("Images/" +
                    assignSpotColor() + "Spot" + ".png"));
            spotImage = image.getScaledInstance(GBoard.getSpotSize().width,
                    GBoard.getSpotSize().height, Image.SCALE_SMOOTH);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(spotImage, 0, 0, null);
    }

    private void assignNumber()
    {
        int fontSize = (int)Math.ceil(Math.min(24.0 * (double) GBoard.getSpotSize().width / 100.0,
                25.0 * (double) GBoard.getSpotSize().height / 100.0));
        int width = (int)(Math.ceil(57.6 * (double)fontSize / 100.0));
        if(position.column == 8)
        {
            int height = (int)Math.ceil(83.33333333 * (double) fontSize / 100.0);
            rowNumber = new JLabel(String.valueOf(position.row));
            add(rowNumber);
            rowNumber.setFont(new Font("SansSerif", Font.BOLD, fontSize));
            rowNumber.setBounds(GBoard.getSpotSize().width - width, 1, width+12, height);
            rowNumber.setForeground(position.row % 2 == 0 ? lightColor : darkColor);
            setComponentZOrder(rowNumber, 0);
        }
        if (position.row == 1)
        {
            columnNumber = new JLabel(String.valueOf((char)('a' + position.column - 1)));
            columnNumber.setFont(new Font("SansSerif", Font.BOLD, fontSize));
            add(columnNumber);
            columnNumber.setForeground(position.column % 2 == 0 ? darkColor : lightColor);
            if(position.column == 7)
            {
                int height = (int)(Math.ceil(126.6666667 * fontSize / 100));
                columnNumber.setBounds(1, GBoard.getSpotSize().height - height -1, width+10, height);
            }
            else
            {
                int height = (int)Math.ceil(83.33333333 * (double) fontSize / 100.0);
                columnNumber.setBounds(1, GBoard.getSpotSize().height - height - 1, width, height);
            }
            setComponentZOrder(columnNumber, 0);
        }
    }

    public void addPieceImage(final Board board)
    {
        if (board.getSpot(position).hasPiece())
        {
            removeAll(); // remove all components in the spot
            Piece piece = board.getSpot(position).getPiece();
            String imageName = piece.getColor().name() +
                    piece.getClass().getSimpleName(); //determine piece picture name
            try
            {
                final BufferedImage image = ImageIO.read(new File("Images/" + imageName + ".png"));
                Image scaledImage = image.getScaledInstance(pieceSize.width, pieceSize.height, Image.SCALE_SMOOTH);
                pieceImage = new JLabel(new ImageIcon(scaledImage));
                add(pieceImage);
                if(Piece.isPawn(board.getSpot(position).getPiece())) {
                    coordinates = new Position(0, -7);
                }
                else if (Piece.isKnight(board.getSpot(position).getPiece()))
                {
                    coordinates = new Position(-4, -6);
                }
                else if (Piece.isRook(board.getSpot(position).getPiece()))
                {
                    switch (board.getSpot(position).getPiece().getColor().ordinal())
                    {
                        case 0:
                            coordinates = new Position(-4, -5);
                            break;
                        default:
                            coordinates = new Position(-4, -6);

                    }
                }
                else if (Piece.isQueen(board.getSpot(position).getPiece()))
                {
                    switch (board.getSpot(position).getPiece().getColor().ordinal())
                    {
                        case 0:
                            coordinates = new Position(-1, -1);
                            break;
                        default:
                            coordinates = new Position(-1, -4);

                    }
                }
                else if (Piece.isKing(board.getSpot(position).getPiece()))
                {
                    switch (board.getSpot(position).getPiece().getColor().ordinal())
                    {
                        case 0:
                            coordinates = new Position(-2, -5);
                            break;
                        default:
                            coordinates = new Position(-2, -7);
                    }
                }
                else
                {
                    coordinates = new Position(-2, -4);
                }
                coordinates.column = (int)((double)coordinates.column * (double) GBoard.getSpotSize().width / 125.0);
                coordinates.row = (int)((double)coordinates.row * (double) GBoard.getSpotSize().height / 126.0);
                pieceImage.setBounds(coordinates.column, coordinates.row, pieceWidth, pieceHeight);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            pieceImage = null; //set the piece null if there is a piece
            removeAll();
        }
        assignNumber();
        repaint();
        validate();
    }

    //Displaying a valid move position from the valid moves array
    public void drawValidMove(boolean enPassant)
    {
        this.removeAll(); //remove all components first
        assignNumber();
        if (pieceImage == null && !enPassant) // if spot hadn't had a piece
        {
            Color c = new Color(0,80,0,120);// green
            Circle g = new Circle(GBoard.getSpotSize(), c);// Green circle
            g.setSize(GBoard.getSpotSize().width, GBoard.getSpotSize().height); //Setting green circle panel size
            this.add(g);// adding the green circle
        }
        else if (enPassant)
        {
            Color c = new Color(120,0,0,120); //red
            Circle circle = new Circle(GBoard.getSpotSize(), c);// red circle
            circle.setSize(GBoard.getSpotSize().width, GBoard.getSpotSize().height); //Setting red circle panel size
            this.add(circle); //adding it to spot
        }
        else //if spot had had a piece
        {
            RoundedCircle r = new RoundedCircle(); //Red Triangles
            r.setSize(GBoard.getSpotSize().width, GBoard.getSpotSize().height); //Setting red triangles panel size
            this.add(r); //adding it to spot
            this.add(pieceImage); //Adding the piece
            pieceImage.setBounds(coordinates.column, coordinates.row, pieceWidth, pieceHeight);
        }
        repaint();
    }

    /*Displaying a colored Square*/
    public void drawSquare(Color c, Cursor cur)
    {
        java.awt.Color gold = new java.awt.Color(255,215,0,140),
                red1 = new java.awt.Color(240,0,0,120),
                red2 = new java.awt.Color(180,0,0,120);
        this.removeAll(); //Removing everything in spot
        Square g = new Square(GBoard.getSpotSize().width, GBoard.getSpotSize().height, c); /*making a new
                                                                        square*/

        g.setCursor(cur); //Setting its cursor
        this.add(g); //Adding the square to spot
        g.setBounds(0, 0, GBoard.getSpotSize().width, GBoard.getSpotSize().height);
        if (pieceImage != null) //if the spot had a piece
        {
            this.add(pieceImage); //Adding the piece again
            pieceImage.setBounds(coordinates.column, coordinates.row, pieceWidth, pieceHeight);
            setComponentZOrder(pieceImage, 0); /* Making the piece in front of
                                                the square */

            pieceImage.setCursor(cur); //Setting its cursor
        }
        assignNumber();
        if(c.equals(gold))
        {
            if(rowNumber != null)
                rowNumber.setForeground(darkColor);
            if(columnNumber != null)
                columnNumber.setForeground(darkColor);
        }
        else if(c.equals(red1) || c.equals(red2))
        {
            if(rowNumber != null)
                rowNumber.setForeground(lightColor);
            if(columnNumber != null)
                columnNumber.setForeground(lightColor);
        }
        repaint();
    }

    //Erasing everything and draw piece if there is a piece
    public void drawPieceOnly()
    {
        this.removeAll();
        if (pieceImage != null) {
            this.add(pieceImage);
            pieceImage.setBounds(coordinates.column, coordinates.row, pieceWidth, pieceHeight);
        }
        assignNumber();
        repaint();
    }

    //Setters
    public void setPiece(JLabel label)
    {
        pieceImage = label;
        if (pieceImage != null){
            add(pieceImage);
            pieceImage.setBounds(coordinates.column, coordinates.row, pieceWidth, pieceHeight);
        }
        assignNumber();
        repaint();
    }
    ////////////

    //Getters
    public JLabel getPiece()
    {
        return pieceImage;
    }
    /////////
}