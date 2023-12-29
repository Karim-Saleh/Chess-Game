package GUI;

import java.awt.*;
import javax.swing.JPanel;

public class Circle extends JPanel {
    private final int x, y, diameter;
    private final Color c;

    public Circle(Dimension spotSize, Color c)
    {
        diameter = (int)Math.min(Math.ceil(28.0 * (double)spotSize.width) / 100,
                Math.ceil(28.0 * (double)spotSize.height) / 100);
        //Setting its position
        this.x = (GBoard.getSpotSize().width - this.diameter) / 2;
        this.y = (GBoard.getSpotSize().height - this.diameter) / 2;
        //////////////////////
        this.c = c;
        this.setOpaque(false); //Making the panel transparent
    }

    public Circle(int diameter, int containerHeight,Color c)
    {
        this.diameter = diameter;
        x = 0;
        y = (containerHeight - diameter) / 2;
        this.c = c;
        this.setOpaque(false); //Making the panel transparent
    }

    @Override
    // This function is executed automatically like the main function
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g; //Graphics2D more efficient than Graphics
        g2d.setColor(c); //Setting its color
        g2d.fillOval(x, y, diameter, diameter);
    }
}