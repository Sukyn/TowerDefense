package MVC.Views;

import MVC.Controller;
import MVC.Model;

import javax.swing.*;
import java.awt.*;

public class CharacterInfoView extends JPanel {

    public static final int HEIGHT = GameView.HEIGHT/7;
    public static final int WIDTH = GameView.WIDTH + CellInfoView.WIDTH;
    Model model;
    JButton bouttonRecolte;

    /** constructor */
    public CharacterInfoView(Model m, Controller c) {
        /* assigns model value from parameter */
        this.model = m;
        /* set window default size*/
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        bouttonRecolte = new JButton("Récolte");
        add(bouttonRecolte);
        //bouttonRecolte.addActionListener(c);

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                revalidate();
                repaint();
            }
        }).start();
    }

    /** Method paint
     * draw all the content in the window
     * @param g, Graphics
     */
    public void paintComponent(Graphics g) {
        g.setColor(Color.GRAY);
        g.fillRect(0, 0, WIDTH, HEIGHT);
    }
}
