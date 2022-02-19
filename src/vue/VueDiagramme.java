package vue;

import modele.Modele;
import modele.Sujet;
import modele.classe.*;

import javax.swing.*;
import java.awt.*;

public class VueDiagramme extends JPanel implements Observateur { //extends JPanel temporaire

	/**
	 * Attribut prive modele
	 */
    private Modele modele;

	/**
	 * Attribut prive FontMetrics qui permet de connaitre la taille en pixel
	 */
    private FontMetrics metrics;

	/**
	 * Variable statique privee entiere qui definit la taille de la police
	 */
    private final static int SIZE = 10;

	/**
	 * Variable statique privée entiere qui definit l'ecart entre les lignes
	 */
    private final static int ECART = 2;

    public VueDiagramme(Modele m) {
        this.modele = m;
    }

	/**
	 * Methode qui dessine dans le JPanel
	 * @param g
	 */
    public void paintComponent(Graphics g) {

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // On définit les font définies
        Font normal = new Font(Font.MONOSPACED, Font.PLAIN, SIZE);
        Font abstrait = new Font(Font.MONOSPACED, Font.ITALIC, SIZE);

        // On définit le font du Graphics
        g.setFont(normal);

		// On définit le metrics qui permet de calculer la taille en pixel
        metrics = g.getFontMetrics();

		// On execute pour tous les objectClasse
        for (ObjectClasse oc : modele.getObjectClasses()) {

            // On définit la hauteur et la largeur de la classe oc
            int hauteur = (oc.getAttributs().size() + oc.getMethodes().size() + 2) * SIZE /** Le nombre de ligne*/ + (oc.getAttributs().size() + oc.getMethodes().size() + 5) * ECART; /** L'ecart entre les lignes */
            int largeur = this.largeurRectangleClasse(oc) + ECART * 2;

            // On dessine le rectangle de la classe avec la couleur de fond
            if (modele.getSelection().contains(oc)) {
                g.setColor(new Color(0x7AD0FF));
            } else {
                g.setColor(new Color(0xFFDB7A));
            }
            g.fillRect(oc.getX(), oc.getY(), largeur, hauteur);

            // On dessine la bordure du rectangle avec une couleur noire
            g.setColor(Color.BLACK);
            g.drawRect(oc.getX(), oc.getY(), largeur, hauteur);

            // On dessine les bordures interne du rectangle pour definir le titre, les attributs et les constructeurs/methodes
            g.drawRect(oc.getX(), oc.getY(), largeur, 2 * SIZE + 3 * ECART);
            g.drawRect(oc.getX(), oc.getY(), largeur, 2 * SIZE + 3 * ECART + oc.getAttributs().size() * (SIZE + ECART));

            // On définit la hauteur courante (faut prevoir que pour ecrire, il faut prendre le point en bas a gauche et non en haut)
            int hauteurCourante = oc.getY() + SIZE + ECART;

			// On définit le titre de la classe
            String type = "<< " + oc.getType().toString() + " >>";
            g.drawString(type, oc.getX() + (largeur - metrics.stringWidth(type)) / 2, hauteurCourante);
            hauteurCourante += SIZE + ECART;
            g.drawString(oc.getNomObjectClasse(), oc.getX() + (largeur - metrics.stringWidth(oc.getNomObjectClasse())) / 2, hauteurCourante);
            hauteurCourante += SIZE + ECART;

			// On passe aux attributs
            for (Attribut a : oc.getAttributs()) {
                g.drawString(a.afficher(), oc.getX() + ECART, hauteurCourante);
                hauteurCourante += SIZE + ECART;
            }

			// On passe aux methodes
            for (Methode m : oc.getMethodes()) {
                if (m.isAbstrait()) g.setFont(abstrait);
                g.drawString(m.afficher(), oc.getX() + ECART, hauteurCourante);
                if (m.isAbstrait()) g.setFont(normal);
                hauteurCourante += SIZE + ECART;
            }

        }

    }

	/**
	 * Methode privée qui calcule la plus grande largeur qu'on doit prendre pour dessiner le rectangle
	 * @param oc ObjectClasse
	 * @return int
	 */
    private int largeurRectangleClasse(ObjectClasse oc) {
        int taille = 0;
        if (metrics.stringWidth("<< " + oc.getType().toString() + " >>") > taille)
            taille = metrics.stringWidth("<< " + oc.getType().toString() + " >>");
        if (metrics.stringWidth(oc.getNomObjectClasse()) > taille)
            taille = metrics.stringWidth(oc.getNomObjectClasse());
        for (Attribut a : oc.getAttributs()) {
            if (metrics.stringWidth(a.afficher()) > taille) taille = metrics.stringWidth(a.afficher());
        }
        for (Methode m : oc.getMethodes()) {
            if (metrics.stringWidth(m.afficher()) > taille) taille = metrics.stringWidth(m.afficher());
        }
        return taille;
    }

    @Override
    public void actualiser(Sujet sujet) {
        this.modele = (Modele)sujet;
        this.repaint();
    }

}
