package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class RoundedCircle extends JPanel {
    private final int x, y;
    private final Ellipse2D e;
    private final Color c = new Color(180,0,0,120);
    private final int width, height;
    public RoundedCircle()
    {
        width = (int)Math.ceil(24.0 * (double) GBoard.getSpotSize().width / 100.0);
        height = (int)Math.ceil(23.80952381 * (double) GBoard.getSpotSize().height / 100.0);
        //Setting its position
        this.x = (GBoard.getSpotSize().width - this.width) / 2;
        this.y = (GBoard.getSpotSize().height - this.height) / 2;
        //////////////////////
        e = new Ellipse2D.Double(x, y, this.width, this.height);//Initializing the circle
        this.setOpaque(false); //Making the panel transparent
    }

    @Override
    // This function is executed automatically like the main function
    public void paintComponent(Graphics g)
    {
        Graphics2D g2d = (Graphics2D) g; //Graphics2D more efficient than Graphics
        g2d.setColor(c); //Setting its color
        float value = (float) Math.ceil(550.0 * (double)Math.min(width, height) / 100.0);
        g2d.setStroke(new BasicStroke(value));
        g2d.draw(e); //Drawing the square
    }
}
