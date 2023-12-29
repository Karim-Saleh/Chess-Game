package GUI;

import javax.swing.*;
import java.awt.*;

public class PlayerState extends JPanel
{
    private JLabel name;
    private final JPanel namePanel;
    private final int fontSize = 50;
    private final int diameter = 32;
    private Circle statue;
    //private final Color darkRed = new Color(84, 11, 14);
    private final Color green = new Color(98, 153, 36);
    private final Color gray = new Color(70, 72, 73);
    private boolean active;
    public PlayerState(String playerName, boolean white)
    {
        active = white;

        setLayout(null);
        setOpaque(false);

        //Player name
        namePanel = new JPanel();
        namePanel.setOpaque(false);
        this.name = new JLabel(playerName);
        this.name.setFont(new Font("SansSerif", Font.BOLD, fontSize));
        this.name.setForeground(Color.black);
        namePanel.add(this.name);
        add(namePanel);
        //////////////

        repaint();
    }
    public void autoSetBounds()
    {
        namePanel.setBounds(diameter + 20, 0, name.getWidth(),name.getHeight() + 5);
        //statue Circle
        statue = new Circle(diameter, name.getHeight(), active ? green : gray);
        statue.setBounds(0,6,diameter*2,namePanel.getHeight());
        add(statue);
        ///////////////
    }
    public void changeTurn()
    {
        active = !active;
        remove(statue);
        statue = new Circle(diameter, name.getHeight(), active ? green : gray);
        statue.setBounds(0,6,diameter*2,namePanel.getHeight());
        add(statue);
    }
    public int getNameHeight()
    {
        return namePanel.getHeight();
    }
    public int getWidth()
    {
        return namePanel.getWidth() + namePanel.getX();
    }
}
