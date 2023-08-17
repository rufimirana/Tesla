package affichage;

import javax.swing.*;

import chrono.Chrono;

import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import tesla.Plan;
import tesla.Utilisateur;
import tesla.Voiture;
import utils.Coord;

public class Fenetre extends JFrame implements Observer {
    Plan plan;
    Utilisateur utilisateur;
    Vector<Utilisateur> listeUtilisateur;
    Voiture voiture;

    Panneau panneau;
    JPanel panneauNord;
    JPanel panneauSud;
    JPanel panneauEst;
    JPanel panneauOuest;

    JLabel nomAff;
    JLabel accelerationAff;
    JLabel tailleAff;
    JLabel temperatureAff;
    JComboBox utilisateursAff;

    JLabel vitesseAff;
    JLabel consommationAff;
    // JLabel pourcentageAff;
    JLabel autonomieAff;
    JLabel cumulAff;
    JLabel chargeRestanteAff;
    JLabel batterieAff;
    JButton btAccelerer;
    JButton btRalentir;
    JButton btPauseLecture;
    JTextField poidsNouveau;
    JButton btNouveau;
    JLabel consoMoyenneAff;

    JComboBox siegeCombo;
    JComboBox volantCombo;
    JButton btOptionClim;

    JLabel seconds;
    Chrono chrono;

    public Fenetre(Plan plan, Voiture voiture, Chrono chrono) throws Exception {
        this.chrono = chrono;
        seconds = new JLabel("0");
        chrono.addObserver(this);
        listeUtilisateur = voiture.getListeUtilisateurs();
        this.voiture = voiture;
        voiture.addObserver(this);
        this.plan = plan;
        plan.addObserver(this);
        this.utilisateur = plan.getUtilisateur();
        this.setSize(1400, 600);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        panneau = new Panneau(this);

        setLayout(new BorderLayout());
        panneauNord = new JPanel(new FlowLayout());
        panneauSud = new JPanel(new FlowLayout());
        panneauEst = new JPanel();
        panneauEst.setLayout(new BoxLayout(panneauEst, BoxLayout.Y_AXIS));
        panneauOuest = new JPanel();
        panneauOuest.setLayout(new BoxLayout(panneauOuest, BoxLayout.Y_AXIS));

        // centre
        this.add(panneau, BorderLayout.CENTER);

        // nord
        this.add(panneauNord, BorderLayout.NORTH);
        nomAff = new JLabel("Utilisateur : " + utilisateur.getNom());
        tailleAff = new JLabel("Taille : " + utilisateur.getTaille());
        panneauNord.add(nomAff);
        panneauNord.add(tailleAff);
        utilisateursAff = new JComboBox(listeUtilisateur);
        panneauNord.add(utilisateursAff);

        // sud
        this.add(panneauSud, BorderLayout.SOUTH);
        temperatureAff = new JLabel("Clim : " + utilisateur.getConfigClim() + "°C");
        panneauSud.add(temperatureAff);
        panneauSud.add(new JLabel("Chrono"));
        panneauSud.add(seconds);

        // est
        this.add(panneauEst, BorderLayout.EAST);
        vitesseAff = new JLabel("Vitesse : " + voiture.getVitesse() + " KH");
        consommationAff = new JLabel("Consommation : " + voiture.getConsommation() + "W");
        cumulAff = new JLabel("Cumul : " + voiture.getCumulInstantannee());
        autonomieAff = new JLabel("Autonomie : " + voiture.getAutonomie() + " km");
        // pourcentageAff = new JLabel("Batterie : " + voiture.getPourcentageBatterie()
        // + "%");
        chargeRestanteAff = new JLabel("Charge restante : " + voiture.getChargeRestante());
        batterieAff = new JLabel("Batterie : " + voiture.getBatterie() + " Wh");
        poidsNouveau = new JTextField(10);
        btNouveau = new JButton("Insérer");
        btPauseLecture = new JButton("Pause / Reprendre");
        accelerationAff = new JLabel("Acceleration : " + voiture.getAcceleration());
        consoMoyenneAff = new JLabel("Consommation Moyenne : " + voiture.getConsommationMoyenne());
        panneauEst.add(batterieAff);
        panneauEst.add(vitesseAff);
        panneauEst.add(accelerationAff);
        panneauEst.add(consommationAff);
        panneauEst.add(cumulAff);
        panneauEst.add(autonomieAff);
        panneauEst.add(chargeRestanteAff);
        panneauEst.add(consoMoyenneAff);
        panneauEst.add(new JLabel("Insérer une nouvelle personne"));
        panneauEst.add(poidsNouveau);
        panneauEst.add(btNouveau);
        panneauEst.add(btPauseLecture);

        // panneauEst.add(pourcentageAff);
        btAccelerer = new JButton("Accelerer");
        btRalentir = new JButton("ralentir");
        panneauEst.add(btAccelerer);
        panneauEst.add(btRalentir);

        // ouest
        this.add(panneauOuest, BorderLayout.WEST);
        panneauOuest.add(new JLabel("Choisir les options de siège"));
        siegeCombo = new JComboBox();
        int i = 0;
        for (Coord c : plan.getPositions()) {
            siegeCombo.addItem(++i);
        }
        siegeCombo.setSelectedIndex(plan.getPositionSiege() - 1);
        panneauOuest.add(siegeCombo);
        panneauOuest.add(new JLabel("Choisier les options de volant"));
        volantCombo = new JComboBox();
        i = 0;
        for (Coord c : plan.getCoordVolant()) {
            volantCombo.addItem(++i);
        }
        panneauOuest.add(volantCombo);
        setPageOuest();
        btOptionClim = new JButton("Options de clim");
        panneauOuest.add(btOptionClim);

        // Action Listeners
        btPauseLecture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chrono.toggle();
            }
        });

        btOptionClim.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ConfigClim nouvelleFenetre = new ConfigClim(plan);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        siegeCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    utilisateur.setConfigSiege(siegeCombo.getSelectedIndex() + 1);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        volantCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    utilisateur.setConfigVolant(volantCombo.getSelectedIndex() + 1);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

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

        btNouveau.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    voiture.ajouterOlona(Double.parseDouble(poidsNouveau.getText()));
                } catch (Exception ex) {
                    // TODO: handle exception
                }
            }
        });

        utilisateursAff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(utilisateursAff.getSelectedItem());
                utilisateur = (Utilisateur) utilisateursAff.getSelectedItem();
                plan.setUtilisateur(utilisateur);
                System.out.println(utilisateur.getConfigSiege());

                nomAff.setText("Utilisateur : " + utilisateur.getNom());
                tailleAff.setText("Taille : " + utilisateur.getTaille());
                panneau.repaint();
            }
        });

        setVisible(true);
    }

    void setPageOuest() {
        volantCombo.setSelectedIndex(plan.getPositionVolant() - 1);
        siegeCombo.setSelectedIndex(plan.getPositionSiege() - 1);
        temperatureAff.setText("" + this.plan.getUtilisateur().getConfigClim() + "°C");
    }

    public Plan getPlan() {
        return plan;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == voiture) {
            try {
                vitesseAff.setText("Vitesse : " + voiture.getVitesse() + " KH");
                consommationAff.setText("Consommation : " + voiture.getConsommation() + "W");
                cumulAff.setText("Cumul : " + voiture.getCumulInstantannee());
                autonomieAff.setText("Autonomie : " + voiture.getAutonomie() + " km");
                chargeRestanteAff.setText("Charge restante : " + voiture.getChargeRestante());
                autonomieAff.setText("Acceleration : " + voiture.getAcceleration());
                consoMoyenneAff.setText("Consommation Moyenne : " + voiture.getConsommationMoyenne());
            } catch (Exception e) {
                e.printStackTrace();
            }
            // pourcentageAff.setText("Batterie : " + voiture.getPourcentageBatterie() +
            // "%");
        }
        if (o == plan) {
            panneau.repaint();
            setPageOuest();
        }
        if (o == chrono) {
            this.seconds.setText("" + chrono.getSeconds());
        }
    }
}
