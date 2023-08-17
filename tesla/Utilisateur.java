package tesla;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.Time;
import java.util.List;
import java.util.Observable;
import java.util.Vector;

import utils.Utils;

public class Utilisateur extends Observable {
    public static Utilisateur conducteur;

    int id;
    String nom;
    double taille;
    int configVolant;
    int configSiege;
    double configClim;
    double poids;
    static Time heureActuelle;

    public Utilisateur(int id) throws Exception {
        this.id = id;
        String fichier = "./data/utilisateur.csv";
        String line = "";
        String csvSplitBy = ";";
        heureActuelle = getHeureActuelle();

        BufferedReader br = new BufferedReader(new FileReader(fichier));
        while ((line = br.readLine()) != null) {
            String[] split = line.split(csvSplitBy);
            try {
                if (Integer.parseInt(split[0]) == id) {
                    this.nom = split[1];
                    this.taille = Double.parseDouble(split[2]);
                    this.poids = Double.parseDouble(split[6]);

                    // siege
                    getSiegeFromBase(split[3]);
                    // volant
                    getVolantFromBase(split[4]);
                    // clim
                    getClimFromBase(split[5]);

                }
            } catch (Exception e) {
            }
        }
        // si id = 1, donc, je set le conducteur à cet utilisateur
        if (id == 1) {
            conducteur = this;
        }
    }

    public static int lastId() {
        try {
            String fichier = "./data/utilisateur.csv";
            BufferedReader br = new BufferedReader(new FileReader(fichier));
            String ligne = "";
            int last = 0;
            while ((ligne = br.readLine()) != null) {
                try {
                    String split[] = ligne.split(";");
                    last = Integer.parseInt(split[0]);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            return last;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return 0;

    }

    public Utilisateur(String nom, double taille, int configSiege, int configVolant, double configClim, double poids) {
        this.nom = nom;
        this.configVolant = configVolant;
        this.configSiege = configSiege;
        this.configClim = configClim;
        this.poids = poids;
    }

    public double getPoids() {
        return poids;
    }

    public String getListeTemperatureHoraire() throws Exception {
        String fichier = "./data/utilisateur.csv";
        BufferedReader br = new BufferedReader(new FileReader(fichier));
        String line = "";

        while ((line = br.readLine()) != null) {
            try {
                String split[] = line.split(";");
                if (Integer.parseInt(split[0]) == id) {
                    return split[5];
                }
            } catch (Exception e) {
            }
        }
        return "";
    }

    public void saveTemperatureHoraires(String tempHoraire) throws Exception {
        if (tempHoraire.trim().length() == 0) {
            tempHoraire = "0";
        }
        String file = "./data/utilisateur.csv";
        Vector<String[]> lignes = Utils.csvGetLines(file, ";");
        int i = 0;
        for (String[] singleLine : lignes) {
            try {
                if (Integer.parseInt(singleLine[0]) == id) {
                    lignes.get(i)[5] = tempHoraire;
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            i++;
        }

        Utils.csvWriteLines(lignes, file);
        getClimFromBase(tempHoraire);
    }

    void getClimFromBase(String supposeListeTemps) {
        try {
            String[] listeTranche = supposeListeTemps.split(",");
            for (String tranche : listeTranche) {
                try {
                    String split[] = tranche.split(":");
                    String tenaTranche = split[0];
                    String valeur = split[1];

                    String[] splitTranche = tenaTranche.split("-");
                    // System.out.println(splitTranche[0]);
                    Time heure1 = parseTemps(splitTranche[0]);
                    Time heure2 = parseTemps(splitTranche[1]);

                    if (heureActuelle.after(heure1) && heureActuelle.before(heure2)) {
                        this.configClim = Double.parseDouble(valeur);
                        setChanged();
                        notifyObservers();
                        return;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
        } catch (Exception e) {

        }
        // s'il n'y en a pas, je m'en tiens à la config par défaut
        try {
            // if id > 1 je copie les données du conducteur
            if (id > 1) {
                this.configClim = conducteur.getConfigClim();
                return;
            }

            String fichier = "./data/config_clim_defaut.csv";
            BufferedReader br = new BufferedReader(new FileReader(fichier));
            String ligne = "";
            String separator = ";";

            while ((ligne = br.readLine()) != null) {
                String split[] = ligne.split(separator);

                try {
                    Time heure1 = parseTemps(split[1]);
                    Time heure2 = parseTemps(split[2]);
                    System.out.println(heure2);

                    if (heureActuelle.after(heure1) && heureActuelle.before(heure2)) {
                        this.configClim = Integer.parseInt(split[3]);
                        setChanged();
                        notifyObservers();
                    }

                } catch (Exception ex2) {
                    continue;
                }
            }

        } catch (Exception ex) {
            // TODO: handle exception
        }
    }

    public static Time parseTemps(String supposeTemps) {

        String[] split = supposeTemps.split("a");
        int heure = 0;
        int minute = 0;
        int seconde = 0;
        try {
            heure = Integer.parseInt(split[0]);
            minute = Integer.parseInt(split[1]);
            seconde = Integer.parseInt(split[2]);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return new Time(heure, minute, seconde);
    }

    public static Time getHeureActuelle() {
        // C'est ça ou je remplace par l'heure système
        try {
            String fichier = "./data/heureActuelle.csv";
            BufferedReader br = new BufferedReader(new FileReader(fichier));
            String ligne = "";
            while ((ligne = br.readLine()) != null) {
                String[] splitLera = ligne.split(":");
                return new Time(Integer.parseInt(splitLera[0]), Integer.parseInt(splitLera[1]),
                        Integer.parseInt(splitLera[2]));
            }
        } catch (Exception e) {

        }
        return new Time(0, 0, 0);
    }

    void getSiegeFromBase(String supposeSiege) throws Exception {
        try {
            this.configSiege = Integer.parseInt(supposeSiege);
            if (configSiege <= 0) {
                throw new Exception("tsy nombre ty");
            }
        } catch (Exception e) {
            // si id > 1 je copie les information du conducteur
            if (id > 1) {
                this.configSiege = conducteur.getConfigSiege();
                return;
            }

            String fichier = "./data/config_siege_defaut.csv";
            BufferedReader br = new BufferedReader(new FileReader(fichier));
            String line = "";
            String separator = ";";
            while ((line = br.readLine()) != null) {
                try {
                    String[] splitted = line.split(separator);
                    double tailleMin = Integer.parseInt(splitted[1]);
                    double tailleMax = Integer.parseInt(splitted[2]);

                    if (this.taille >= tailleMin && this.taille <= tailleMax) { // C'est le bon
                        this.configSiege = Integer.parseInt(splitted[3]);
                    }
                } catch (Exception ex) {
                    continue;
                }
            }
        }
    }

    void getVolantFromBase(String supposeVolant) throws Exception {
        try {
            this.configVolant = Integer.parseInt(supposeVolant);
            if (configVolant <= 0) {
                throw new Exception("Tsy mba izy ty");
            }
        } catch (Exception e) {
            // si id > 1 je copie les information du conducteur
            if (id > 1) {
                this.configVolant = conducteur.getConfigVolant();
                return;
            }

            String fichier = "./data/config_volant_defaut.csv";
            BufferedReader br = new BufferedReader(new FileReader(fichier));
            String ligne = "";
            String separator = ";";
            while ((ligne = br.readLine()) != null) {
                try {
                    String splitted[] = ligne.split(separator);
                    double tailleMin = Integer.parseInt(splitted[1]);
                    double tailleMax = Integer.parseInt(splitted[2]);

                    if (this.taille >= tailleMin && this.taille <= tailleMax) { // C'est le bon
                        this.configVolant = Integer.parseInt(splitted[3]);
                    }
                } catch (Exception ex) {
                    continue;
                }
            }
        }
    }

    public String getNom() {
        return nom;
    }

    public void setConfigSiege(int position) throws Exception {
        String fichier = "./data/utilisateur.csv";
        Vector<String[]> lignes = Utils.csvGetLines(fichier, ";");

        int i = 0;
        for (String[] uneLigne : lignes) {
            try {
                if (Integer.parseInt(uneLigne[0]) == id) {
                    lignes.get(i)[3] = "" + position;
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            i++;
        }

        if (id == 0) {
            int nouvelId = lastId() + 1;
            lignes.add(new String[] { "" + nouvelId, nom, "" + taille, "" + configSiege, "" + position,
                    "" + configClim, "" + poids });
            this.id = nouvelId;
        }

        Utils.csvWriteLines(lignes, fichier);

        this.configSiege = position;
        setChanged();
        notifyObservers();
    }

    public void setConfigVolant(int position) throws Exception {
        String fichier = "./data/utilisateur.csv";
        Vector<String[]> lignes = Utils.csvGetLines(fichier, ";");

        int i = 0;
        for (String[] uneLigne : lignes) {
            try {
                if (Integer.parseInt(uneLigne[0]) == id) {
                    lignes.get(i)[4] = "" + position;
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
            i++;
        }

        Utils.csvWriteLines(lignes, fichier);

        this.configVolant = position;
        setChanged();
        notifyObservers();
    }

    public double getConfigClim() {
        return this.configClim;
    }

    public int getConfigSiege() {
        return configSiege;
    }

    public int getConfigVolant() {
        return configVolant;
    }

    public double getTaille() {
        return taille;
    }

    public static Vector<Utilisateur> getUtisateurs() throws Exception {
        Vector<Utilisateur> retour = new Vector<Utilisateur>();

        String fichier = "./data/utilisateur.csv";
        BufferedReader br = new BufferedReader(new FileReader(fichier));
        String ligne = "";
        String separator = ";";
        while ((ligne = br.readLine()) != null) {
            String split[] = ligne.split(separator);

            try {
                retour.add(new Utilisateur(Integer.parseInt(split[0])));
            } catch (Exception e) {
                continue;
            }
        }

        return retour;
    }

    @Override
    public String toString() {
        return this.nom;
    }

}
