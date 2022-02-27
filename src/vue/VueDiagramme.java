package vue;

import modele.Modele;
import modele.Sujet;
import modele.classe.*;

import javax.swing.*;
import java.awt.*;
import java.util.Locale;

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
        Font NORMAL = new Font(Font.MONOSPACED, Font.PLAIN, SIZE);
        Font ABSTRAIT = new Font(Font.MONOSPACED, Font.ITALIC, SIZE);
        Font STATIQUE = new Font(Font.MONOSPACED, Font.BOLD, SIZE);


        // On définit le font du Graphics
        g.setFont(NORMAL);

		// On définit le metrics qui permet de calculer la taille en pixel
        metrics = g.getFontMetrics();

		// On execute pour tous les objectClasse
        for (ObjectClasse oc : modele.getObjectClasses()) {

            if (oc.isVisible()) { // on verifie si la classe es visible

                // On définit la hauteur et la largeur de la classe oc
                int hauteur = calculerHauteur(oc);
                int largeur = calculerLargeur(oc);

                // On dessine le rectangle de la classe avec la couleur de fond
                if (modele.getSelection().contains(oc)) {
                    g.setColor(new Color(0x7AD0FF));
                } else {
                    g.setColor(new Color(0xFFDB7A));
                }

                //on defini des variables pour prendre en compte le decalage
                int posX = oc.getX() + modele.getDecalageX();
                int posY = oc.getY() + modele.getDecalageY();

                g.fillRect(posX, posY, largeur, hauteur);

                // On dessine la bordure du rectangle avec une couleur noire
                g.setColor(Color.BLACK);
                g.drawRect(posX, posY, largeur, hauteur);

                // On dessine les bordures interne du rectangle pour definir le titre, les attributs et les constructeurs/methodes
                g.drawRect(posX, posY, largeur, 2 * SIZE + 3 * ECART);
                g.drawRect(posX, posY, largeur, 2 * SIZE + 3 * ECART + oc.getAttributs().size() * (SIZE + ECART));

                // On définit la hauteur courante (faut prevoir que pour ecrire, il faut prendre le point en bas a gauche et non en haut)
                int hauteurCourante = posY + SIZE + ECART;

                // On définit le titre de la classe
                String type = "<< " + oc.getType().toString() + " >>";
                g.drawString(type, posX + (largeur - metrics.stringWidth(type)) / 2, hauteurCourante);
                hauteurCourante += SIZE + ECART;
                g.drawString(oc.getNomObjectClasse(), posX + (largeur - metrics.stringWidth(oc.getNomObjectClasse())) / 2, hauteurCourante);
                hauteurCourante += SIZE + ECART;

                String ligne = "";

                // On passe aux attributs
                for (Attribut a : oc.getAttributs()) {
                    ligne = a.afficher();
                    if (a.isFinale() && a.isStatique()) {
                        ligne = ligne.toUpperCase();
                        g.setFont(STATIQUE);
                    }
                    if (a.isFinale()) ligne = ligne.toUpperCase();
                    else if (a.isStatique()) g.setFont(STATIQUE);
                    else g.setFont(NORMAL);
                    g.drawString(ligne, posX + ECART, hauteurCourante);
                    hauteurCourante += SIZE + ECART;
                }

                // On passe aux methodes
                for (Methode m : oc.getMethodes()) {
                    if (m.isAbstrait()) g.setFont(ABSTRAIT);
                    else g.setFont(NORMAL);
                    g.drawString(m.afficher(), posX + ECART, hauteurCourante);
                    hauteurCourante += SIZE + ECART;
                }
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

    void drawArrow(Graphics g, ObjectClasse src, ObjectClasse destination, int choixFleche) {

        ObjectClasse dest = modele.getObjectClasse(destination.getNomComplet());

        int srcX, srcY, destX, destY;
        int milieuDestX = dest.getX() + calculerLargeur(dest)/2;

        if (src.getX() <= milieuDestX && milieuDestX >= src.getX() + calculerLargeur(src)) {
            if (dest.getY() >= src.getY()) {
                srcX = src.getX() + calculerLargeur(src) / 2;
                srcY = src.getY();
                destX = dest.getX() + calculerLargeur(dest) / 2;
                destY = dest.getY() + calculerHauteur(dest);
            } else {
                srcX = src.getX() + calculerLargeur(src) / 2;
                srcY = src.getY() + calculerHauteur(src);
                destX = dest.getX() + calculerLargeur(dest) / 2;
                destY = dest.getY();
            }
        } else {
            if (dest.getX() >= src.getX()) {
                srcX = src.getX();
                srcY = src.getY() + calculerHauteur(src)/2;
                destX = dest.getX() + calculerLargeur(dest);
                destY = dest.getY() + calculerHauteur(dest)/2;
            } else {
                srcX = src.getX() + calculerLargeur(src);
                srcY = src.getY() + calculerHauteur(src)/2;
                destX = dest.getX();
                destY = dest.getY() + calculerHauteur(dest)/2;
            }
        }

        int dx = destX - srcX, dy = destY - srcY;
        double D = Math.sqrt(dx * dx + dy * dy);
        int d= 20, h=10;
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;
        x = xm * cos - ym * sin + srcX;
        ym = xm * sin + ym * cos + srcY;
        xm = x;
        x = xn * cos - yn * sin + srcX;
        yn = xn * sin + yn * cos + srcY;
        xn = x;
        int[] xpoints = {destX, (int) xm, (int) xn};
        int[] ypoints = {destY, (int) ym, (int) yn};
        g.drawLine(srcX, srcY, destX, destY);
        g.fillPolygon(xpoints, ypoints, 3);

    }

    /**
     * Calcule la hauteur de la case d'un objectClasse lors de l'affichage
     * @param oc objectClasse fournit
     * @return taille en pixel
     */
    public int calculerHauteur(ObjectClasse oc){
        return (oc.getAttributs().size() + oc.getMethodes().size() + 2) * SIZE /* Le nombre de ligne*/ + (oc.getAttributs().size() + oc.getMethodes().size() + 5) * ECART; /* L'ecart entre les lignes */
    }

    /**
     * Calcule la largeur de la case d'un objectClasse lors de l'affichage
     * @param oc objectClasse fournit
     * @return taille en pixel
     */
    public int calculerLargeur(ObjectClasse oc){
        return this.largeurRectangleClasse(oc) + ECART * 2;
    }

    @Override
    public void actualiser(Sujet sujet) {
        this.modele = (Modele)sujet;
        this.repaint();
    }

}
