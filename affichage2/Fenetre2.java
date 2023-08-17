package affichage2;

import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import chrono.Chrono;

import java.awt.event.*;

import tesla.Voiture;

public class Fenetre2 extends JFrame implements Observer {

    Voiture voiture;
    Chrono chrono;

    JLabel consommationAff;
    JLabel consommationMoyenneAff;
    JLabel vitessAff;
    JLabel autonomieAff;
    JLabel accelerationAff;
    JLabel batterieAff;
    JLabel consommationTotale;
    JLabel chronoAff;

    JButton btAccelerer;
    JButton btRalentir;
    JButton btArreter;

    public Fenetre2(Voiture v, Chrono c) throws Exception {
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        voiture = v;
        chrono = c;
        v.addObserver(this);
        c.addObserver(this);

        batterieAff = new JLabel("Batterie : " + voiture.getBatterie() + " W");
        consommationAff = new JLabel("Consommation : " + voiture.getConsommation() + " W");
        consommationMoyenneAff = new JLabel("Consommation moyenne : " + voiture.getConsommationMoyenne() + " W");
        vitessAff = new JLabel("Vitesse : " + voiture.getVitesse() + " km/h");
        accelerationAff = new JLabel("Accélération : " + voiture.getAcceleration() + "km/h/s");
        autonomieAff = new JLabel("Autonomie : " + voiture.getAutonomie() + " km");
        consommationTotale = new JLabel("Consommation Totale : " + voiture.getCumulInstantannee() + " W");

        chronoAff = new JLabel("Temps passé : " + chrono.getSeconds());

        btAccelerer = new JButton("Accelerer");
        btRalentir = new JButton("Freiner");
        btArreter = new JButton("Arreter");

        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        add(batterieAff);
        add(consommationAff);
        add(consommationMoyenneAff);
        add(vitessAff);
        add(accelerationAff);
        add(autonomieAff);
        add(consommationTotale);
        add(chronoAff);
        add(btAccelerer);
        add(btRalentir);
        add(btArreter);

        // Listeners
        btAccelerer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    voiture.accelerer(5);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btRalentir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    voiture.accelerer(-5);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        btArreter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chrono.toggle();
            }
        });

        setVisible(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == voiture) {
            try {
                batterieAff.setText("Batterie : " + voiture.getBatterie() + " W");
                consommationAff.setText("Consommation : " + voiture.getConsommation() + " W");
                consommationMoyenneAff.setText(
                        "Consommation moyenne : " + voiture.getConsommationMoyenne() + " W");
                vitessAff.setText("Vitesse : " + voiture.getVitesse() + " km/h");
                accelerationAff.setText("Accélération : " + voiture.getAcceleration() + "km/h/s");
                autonomieAff.setText("Autonomie : " + voiture.getAutonomie() + " km");
                consommationTotale.setText("Consommation Totale : " + voiture.getCumulInstantannee() + " W");
            } catch (Exception e) {

            }
        }

        if (o == chrono) {
            chronoAff.setText("Temps passé : " + chrono.getSeconds());
        }
    }
}
