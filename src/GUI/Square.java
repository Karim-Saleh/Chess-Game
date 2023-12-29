package GUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class Square extends JPanel{
    private final Color c;

    public Square(int spotWidth, int spotHeight, Color c)
    {
        this.c = c;
        this.setOpaque(false); //Making label transparent
    }
    @Override
    // This function is executed automatically like the main function
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g; //Graphics2D more efficient than Graphics
        g2d.setColor(c); //Setting its color
        g2d.fill3DRect(0, 0, GBoard.getSpotSize().width, GBoard.getSpotSize().height, true);
    }
}
