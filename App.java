import affichage.Fenetre;
import affichage2.Fenetre2;
import tesla.Plan;
import tesla.Utilisateur;
import tesla.Voiture;

public class App {
    public static void main(String args[]) {
        try {
            Voiture v = new Voiture();
            Utilisateur utilisateur = new Utilisateur(1);
            Utilisateur.conducteur = utilisateur;

            Plan p = new Plan(utilisateur);
            // Fenetre f = new Fenetre(p, v, v.getChrono());
            Fenetre2 f2 = new Fenetre2(v, v.getChrono());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}