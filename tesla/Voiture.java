package tesla;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.*;

import chrono.Chrono;
import utils.Utils;

class ConsoParKilo {
    public double poidsMin;
    public double poidsMax;
    public double coef;

    public ConsoParKilo(double poidsMin, double poidsMax, double coef) {
        this.poidsMin = poidsMin;
        this.poidsMax = poidsMax;
        this.coef = coef;
    }

    public Boolean between(double poids) {
        return poids >= poidsMin && poids <= poidsMax;
    }
}

public class Voiture extends Observable implements Observer {
    double batterie = 10000;
    double consoParKH = 5;

    double vitesse = 0;
    double consumed = 0;
    double consommationActuelle = 0;
    HashMap<String, Double> listeConsommation;
    Vector<ConsoParKilo> listeConsoParKilo;
    Vector<Utilisateur> listeUtilisateurs;

    double acceleration = 0;

    double cumulInstantannee = 0;

    Chrono chrono;

    Vector<Double> listeVitesse;
    int indiceVitesse = 0;

    public Voiture() throws Exception {
        chargerBatterie();
        chargerConsommations();
        chargerConsoParKilo();
        listeUtilisateurs = Utilisateur.getUtisateurs();
        this.chrono = new Chrono();
        chrono.addObserver(this);
        chargerListeVitesse();
        // chrono.start();
    }

    public Vector<Utilisateur> getListeUtilisateurs() {
        return listeUtilisateurs;
    }

    public void ajouterOlona(double poids) {
        this.listeUtilisateurs.add(new Utilisateur("Jean", 175, Utilisateur.conducteur.getConfigSiege(),
                Utilisateur.conducteur.getConfigVolant(), Utilisateur.conducteur.getConfigClim(), poids));
    }

    void chargerBatterie() {
        try {
            String fichier = "./data/batterie.csv";
            BufferedReader br = new BufferedReader(new FileReader(fichier));
            String ligne = br.readLine();
            this.batterie = Double.parseDouble(ligne);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chargerConsoParKilo() throws Exception {
        String fichier = "./data/consommationParKilo.csv";
        BufferedReader br = new BufferedReader(new FileReader(fichier));
        String line = "";
        listeConsoParKilo = new Vector<ConsoParKilo>();

        while ((line = br.readLine()) != null) {
            String split[] = line.split(";");
            try {
                listeConsoParKilo.add(new ConsoParKilo(Double.parseDouble(split[1]), Double.parseDouble(split[2]),
                        Double.parseDouble(split[3])));
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
    }

    public Double sommePoidsUtilisateur() {
        Double somme = 0.0;
        if (listeUtilisateurs != null) {
            for (Utilisateur u : listeUtilisateurs) {
                somme += u.getPoids();
            }
            return somme;
        }
        return 0.0;
    }

    public Chrono getChrono() {
        return chrono;
    }

    void chargerListeVitesse() throws Exception {
        listeVitesse = new Vector<Double>();
        String fichier = "./data/vitesses.csv";

        BufferedReader br = new BufferedReader(new FileReader(fichier));
        String line = "";

        while ((line = br.readLine()) != null) {
            try {
                Double vitesse = Double.parseDouble(line);
                listeVitesse.add(vitesse);
            } catch (Exception e) {
                continue;
            }
        }

    }

    public double getConsommationActuelle() {
        return consommationActuelle;
    }

    public void setConsommationActuelle(double consommationActuelle) {
        this.consommationActuelle = consommationActuelle;
        setChanged();
        notifyObservers();
    }

    public double getAcceleration() {
        return acceleration;
    }

    Double getConsommation(double vitesse) throws Exception {
        try {
            double coefPoids = 1;
            // double sommePoids = sommePoidsUtilisateur();

            // for (ConsoParKilo intervale : listeConsoParKilo) {
            // if (intervale.between(sommePoids)) {
            // coefPoids = intervale.coef;
            // }
            // }

            // return listeConsommation.get("" + vitesse) * coefPoids;

            // la consommation par vitesse
            double kwParKh = 0;
            double consomVitesse = kwParKh * vitesse; // La consommation de la vitesse

            // La consommation de l'accélération
            double kwParKhParSeconde = 1;
            double consomAcceleration = kwParKhParSeconde * acceleration;

            return (consomAcceleration + consomVitesse) * coefPoids;
        } catch (Exception e) {
            // TODO: handle exception
        }

        return 0.0;
    }

    public double getConsommationMoyenne() {
        return cumulInstantannee / chrono.getTotalSecondes();
    }

    public void accelerer(double accel) throws Exception {
        // if (accel > 0) {
        // indiceVitesse = indiceVitesse < listeVitesse.size() - 1 ? indiceVitesse + 1 :
        // indiceVitesse;
        // } else if (accel < 0) {
        // indiceVitesse = indiceVitesse > 0 ? indiceVitesse - 1 : indiceVitesse;

        // }
        // vitesse = listeVitesse.get(indiceVitesse);

        enregistrerConsoActuelle();

        acceleration += accel;
        consommationActuelle = getConsommation(vitesse);
        setChanged();
        notifyObservers();

        chrono.restart();
    }

    void chargerConsommations() throws Exception {
        this.listeConsommation = new HashMap<String, Double>();
        String fichier = "./data/consommation.csv";
        BufferedReader br = new BufferedReader(new FileReader(fichier));
        String ligne = "";
        while ((ligne = br.readLine()) != null) {
            String split[] = ligne.split(";");
            try {
                Double vitesse = Double.parseDouble(split[1]);
                Double conso = Double.parseDouble(split[2]);
                listeConsommation.put("" + vitesse, conso);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

    }

    void enregistrerConsoActuelle() throws Exception {
        long temps = chrono.getSeconds();
        double vitesse = this.vitesse;

        // Calcul de la consommation
        Double conso = this.consommationActuelle * temps;
        Double cumul = (consumed += conso);

        String fichier = "./data/consommationVita.csv";
        Vector<String[]> lines = Utils.csvGetLines(fichier, ";");
        lines.add(new String[] { "" + vitesse, "" + temps, "" + conso, "" + cumul, "" + getChargeRestante(),
                "" + getAutonomie() });
        Utils.csvWriteLines(lines, fichier);
    }

    public void setVitesse(double vitesse) {
        this.vitesse = vitesse;
        setChanged();
        notifyObservers();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o == chrono) {
            try {
                vitesse += acceleration;
                cumulInstantannee += getConsommation(vitesse);
            } catch (Exception e) {
                // TODO: handle exception
            }
            setChanged();
            notifyObservers();
        }
    }

    public double getCumulInstantannee() {
        return cumulInstantannee;
    }

    public double getVitesse() {
        return vitesse;
    }

    public double getConsommation() throws Exception {
        return getConsommation(this.vitesse);
    }

    public double getPourcentageBatterie() {
        return 100 - consumed * 100 / batterie;
    }

    public String getAutonomie() {
        // if (vitesse == 0) {
        // return "infinie";
        // }

        // try {
        // double reste = batterie - cumulInstantannee;
        // double tempsRestant = reste / getConsommation(vitesse);

        // // tempsRestant = tempsRestant / 3600;
        // double autonomie = vitesse * tempsRestant;

        // return "" + autonomie;
        // } catch (Exception e) {
        // // TODO: handle exception
        // }
        // return "sahay";
        try {
            double batterieRestant = batterie - cumulInstantannee;
            double consommation = getConsommation();
            double secondesRestant = batterieRestant / consommation;
            double heureRestant = secondesRestant / 3600;
            double autonomie = vitesse * heureRestant;

            return "" + autonomie;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "sahay";

    }

    public String getChargeRestante() {
        double reste = batterie - cumulInstantannee;
        return reste + " Wh";
    }

    public double getBatterie() {
        return batterie;
    }
}
