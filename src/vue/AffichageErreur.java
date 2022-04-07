package vue;

import javax.swing.*;

/**
 * Classe qui va permettre l'affichage d'erreur sous la forme de popup
 * <p>
 * Singleton adapte pour etre sur d'avoir acces a la frame de l'application
 */
public class AffichageErreur {

    /**
     * Instance de AffichageErreur
     */
    private static AffichageErreur affichageErreur;

    /**
     * Frame de l'application principale
     * ne peut pas etre null
     */
    private final JFrame oldFrame;

    /**
     * Constructeur de AffichageErreur
     * Appelee par la methode instancier
     *
     * @param oldF Frame de l'application principale
     */
    private AffichageErreur(JFrame oldF) {
        oldFrame = oldF;
    }

    /**
     * Methode a appeler au debut du programme
     * permet d'instancier l'affichage d'erreur qui pourra etre appelee depuis n'importe ou ensuite
     *
     * @param jFrame Frame du programme principal
     */
    public synchronized static void instancier(JFrame jFrame) {
        if (affichageErreur != null) throw new IllegalStateException("Affichage erreur deja instancie");
        if (jFrame == null)
            throw new NullPointerException("La JFrame doit etre non null et correspondre a votre fenetre principale");
        affichageErreur = new AffichageErreur(jFrame);
    }

    /**
     * Methode qui permet de recuperer l'instance
     *
     * @return Instance de AffichageErreur
     */
    public synchronized static AffichageErreur getInstance() {
        if (affichageErreur == null)
            throw new IllegalStateException("Vous devez instancier l'affichage de l'erreur prealablement");
        return affichageErreur;
    }

    /**
     * Methode qui permet d'afficher un message d'erreur
     *
     * @param message message d'erreur a afficher
     */
    public void afficherErreur(String message) {
        JOptionPane.showMessageDialog(oldFrame, message, "Erreur", JOptionPane.ERROR_MESSAGE);
        oldFrame.requestFocus();
    }


    /**
     * Methode qui permet d'afficher un message
     *
     * @param message message d'erreur a afficher
     */
    public void afficherMessage(String message) {
        JOptionPane.showMessageDialog(oldFrame, message, "Message Systeme", JOptionPane.INFORMATION_MESSAGE);
        oldFrame.requestFocus();
    }
}
