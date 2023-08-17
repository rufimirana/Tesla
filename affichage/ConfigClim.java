package affichage;

import java.sql.Time;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.event.*;
import tesla.Plan;
import tesla.Utilisateur;
import utils.Utils;

class TemperatureHoraire {
    public Time heureDebut;
    public Time heureFin;
    public Double temperature;

    public TemperatureHoraire(Time heureDebut, Time heureFin, Double temperature) {
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.temperature = temperature;
    }
}

class InputTempHoraire extends JPanel {
    TemperatureHoraire value;
    JTextField inputHeure1;
    JTextField inputHeure2;
    JTextField inputValeur;
    JFrame conteneur;
    Boolean nesorina = false;

    JButton enlever;

    public InputTempHoraire(TemperatureHoraire value, JFrame conteneur) {
        super();
        this.conteneur = conteneur;
        this.value = value;

        // this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setLayout(new FlowLayout());

        inputHeure1 = new JTextField("" + value.heureDebut, 10);
        inputHeure2 = new JTextField("" + value.heureFin, 10);
        inputValeur = new JTextField("" + value.temperature, 5);
        enlever = new JButton("enlever");
        this.add(new JLabel("Heure 1 : "));
        this.add(inputHeure1);
        this.add(new JLabel("Heure 2 : "));
        this.add(inputHeure2);
        this.add(new JLabel("Valeur : "));
        this.add(inputValeur);
        this.add(enlever);
        JPanel lui = this;

        enlever.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                conteneur.remove(lui);
                conteneur.revalidate();
                nesorina = true;
            }
        });
    }

}

public class ConfigClim extends JFrame implements Observer {
    Plan plan;
    Vector<TemperatureHoraire> listeTemperature;

    JPanel panelAjouterTemp;
    JTextField inputHeure1;
    JTextField inputHeure2;
    JTextField nouvelleValeur;
    JButton envoyer;
    Vector<InputTempHoraire> listeInput;

    public ConfigClim(Plan p) throws Exception {
        this.plan = p;
        listeInput = new Vector<InputTempHoraire>();
        setListeTemperature();

        p.addObserver(this);
        setSize(800, 600);

        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        for (TemperatureHoraire tempHoraire : listeTemperature) {
            InputTempHoraire nouvelInput = new InputTempHoraire(tempHoraire, this);
            this.getContentPane().add(nouvelInput);
            listeInput.add(nouvelInput);
        }

        add(new JLabel("Ajouter une nouvelle valeur"));
        panelAjouterTemp = new JPanel(new FlowLayout());
        inputHeure1 = new JTextField(10);
        inputHeure2 = new JTextField(10);
        nouvelleValeur = new JTextField(5);
        envoyer = new JButton("envoyer");
        panelAjouterTemp.add(new JLabel("Heure 1 : "));
        panelAjouterTemp.add(inputHeure1);
        panelAjouterTemp.add(new JLabel("Heure 2 : "));
        panelAjouterTemp.add(inputHeure2);
        panelAjouterTemp.add(new JLabel("Valeur : "));
        panelAjouterTemp.add(nouvelleValeur);
        panelAjouterTemp.add(envoyer);

        add(panelAjouterTemp);

        setVisible(true);
        JFrame fenetre = this;

        envoyer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String valeurAEnvoyer = "";
                    String prefix = "";
                    for (InputTempHoraire singleInput : listeInput) {
                        if (!singleInput.nesorina) {
                            String heure1 = singleInput.inputHeure1.getText().replace(':', 'a');
                            String heure2 = singleInput.inputHeure2.getText().replace(':', 'a');
                            String temperature = singleInput.inputValeur.getText();
                            valeurAEnvoyer += prefix + heure1 + "-" + heure2 + ":" + temperature;
                            prefix = ",";
                        }
                    }
                    String heure1 = inputHeure1.getText().replace(':', 'a');
                    String heure2 = inputHeure2.getText().replace(':', 'a');
                    String temperature = nouvelleValeur.getText();
                    valeurAEnvoyer += prefix + heure1 + "-" + heure2 + ":" + temperature;
                    plan.getUtilisateur().saveTemperatureHoraires(valeurAEnvoyer);
                    fenetre.setVisible(false);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
    }

    void setListeTemperature() throws Exception {
        this.listeTemperature = new Vector<TemperatureHoraire>();
        try {
            String temperatureStr = plan.getUtilisateur().getListeTemperatureHoraire();
            String temperatureSplit[] = temperatureStr.split(",");
            for (String tempHoraire : temperatureSplit) {
                try {
                    String split[] = tempHoraire.split(":");
                    Double valeur = Double.parseDouble(split[1]);
                    String creneauHoraire = split[0];
                    String splitCreneauHoraire[] = creneauHoraire.split("-");
                    Time heure1 = Utils.parseTemps(splitCreneauHoraire[0]);
                    Time heure2 = Utils.parseTemps(splitCreneauHoraire[1]);

                    listeTemperature.add(new TemperatureHoraire(heure1, heure2, valeur));
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == plan) {

        }
    }

}
