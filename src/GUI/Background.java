package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Background extends JPanel
{
    private Image scaledImage;
    private final int width = GameFrame.size.width - (GameFrame.size.width % 8),
    height = GameFrame.size.height - (GameFrame.size.height % 8);

    public Background()
    {
        setPreferredSize(new Dimension(width, height));
        setLayout(null);
        try {
            final BufferedImage image = ImageIO.read(new File("Images/background.png"));
            scaledImage = image.getScaledInstance(GameFrame.screenSize.width,
                    GameFrame.screenSize.height, Image.SCALE_SMOOTH);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawImage(scaledImage, 0, 0, null);
    }

    private void displayButtons()
    {

    }
}
