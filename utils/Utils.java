package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Time;
import java.util.Vector;

public class Utils {
    public static Vector<String[]> csvGetLines(String fichier, String separator) throws Exception {
        Vector<String[]> retour = new Vector<String[]>();
        BufferedReader br = new BufferedReader(new FileReader(fichier));
        String ligne = "";
        while ((ligne = br.readLine()) != null) {
            retour.add(ligne.split(separator));
        }
        br.close();
        return retour;
    }

    public static void csvWriteLines(Vector<String[]> lines, String fichier) throws Exception {
        Vector<String> lineJoints = new Vector<String>();

        for (String[] lineNonJoint : lines) {
            lineJoints.add(String.join(";", lineNonJoint));
        }

        String texteAEcrire = String.join("\n", lineJoints);

        BufferedWriter bw = new BufferedWriter(new FileWriter(fichier));
        bw.write(texteAEcrire);
        bw.close();
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
}
