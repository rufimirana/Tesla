package affichage;

import java.awt.Color;
import java.awt.*;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import utils.Coord;

public class Panneau extends JPanel {

    Fenetre f;

    public Panneau(Fenetre j) {
        f = j;
    }

    @Override
    public void paint(Graphics g) {
        // TODO Auto-generated method stub
        super.paint(g);
        drawPositionsSiege(g);
        drawSiege(g);
    }

    public void drawPositionsSiege(Graphics g) {
        int taille = f.getPlan().getPositions().length;
        if (taille < 2)
            return;
        Coord coord1 = f.getPlan().getPositions()[0];
        Coord coord2 = f.getPlan().getPositions()[taille - 1];

        g.setColor(Color.BLACK);
        g.drawLine((int) coord1.x, (int) coord1.y, (int) coord2.x, (int) coord2.y);

        for (Coord cor : f.getPlan().getPositions()) {
            g.fillOval((int) cor.x - 5, (int) cor.y - 5, 10, 10);
        }

    }

    public void drawSiege(Graphics g) {
        Image image = new ImageIcon("./seat.png").getImage();
        int index = f.getPlan().getPositionSiege() - 1;

        Coord cor = f.getPlan().getPositions()[index];

        g.drawImage(image, (int) cor.x - 125, (int) cor.y - 280, null);
    }
}
