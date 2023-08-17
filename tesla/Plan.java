package tesla;

import java.util.Observable;
import java.util.Observer;

import utils.Coord;

public class Plan extends Observable implements Observer {
    int positionSiege = 2;
    int positionVolant;
    Utilisateur utilisateur;

    Coord positions[];
    Coord coordVolant[];

    public Plan(Utilisateur u) {
        this.utilisateur = u;
        u.addObserver(this);
        positionSiege = u.getConfigSiege();
        positionVolant = u.getConfigVolant();

        positions = new Coord[5];
        positions[0] = new Coord(50, 450);
        positions[1] = new Coord(150, 450);
        positions[2] = new Coord(250, 450);
        positions[3] = new Coord(350, 450);
        positions[4] = new Coord(450, 450);

        coordVolant = new Coord[3];
        coordVolant[0] = new Coord(0, 0);
        coordVolant[1] = new Coord(0, 0);
        coordVolant[2] = new Coord(0, 0);
    }

    public Coord[] getCoordVolant() {
        return coordVolant;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == utilisateur) {
            positionSiege = utilisateur.getConfigSiege();
            positionVolant = utilisateur.getConfigVolant();
            setChanged();
            notifyObservers();
        }
    }

    public Coord[] getPositions() {
        return positions;
    }

    public int getPositionVolant() {
        return positionVolant;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur u) {
        this.utilisateur = u;
        u.addObserver(this);
        positionSiege = u.getConfigSiege();
        positionVolant = u.getConfigVolant();

        setChanged();
        notifyObservers();
    }

    public int getPositionSiege() {
        return positionSiege;
    }
}
