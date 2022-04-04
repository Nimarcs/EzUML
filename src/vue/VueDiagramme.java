package vue;

import modele.FlecheAssociation;
import modele.Modele;
import modele.Sujet;
import modele.classe.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class VueDiagramme extends JPanel implements Observateur,Serializable { //extends JPanel temporaire

	/**
	 * Attribut prive modele
	 */
    private Modele modele;

    /**
     * Attribut qui permet de restaurer la taille correcte après l'avoir changee pour la capture
     */
    private Rectangle bounds;

	/**
	 * Attribut prive FontMetrics qui permet de connaitre la taille en pixel
	 */
    private FontMetrics metrics;

	/**
	 * Variable statique privee entiere qui definit la taille de la police
	 */
    private final static int SIZE = 13;

	/**
	 * Variable statique privée entiere qui definit l'ecart entre les lignes
	 */
    private final static int ECART = 2;

    /**
     * Font Normal, police d'ecriture pour le cas normal
     */
    Font NORMAL = new Font(Font.MONOSPACED, Font.PLAIN, SIZE);

    /**
     * Font abstrait, police d'ecriture lorsqu'une methode ou un attribut est abstrait, ecrit en italique
     */
    Font ABSTRAIT = new Font(Font.MONOSPACED, Font.ITALIC, SIZE);

    /**
     * Font statique, police d'ecriture lorsqu'une methode ou un attribut est statique, ecrit en gras
     */
    Font STATIQUE = new Font(Font.MONOSPACED, Font.BOLD, SIZE);

    private final static int FLECHE_HERITAGE = 1;
    private final static int FLECHE_IMPLEMENTS = 2;
    private final static int FLECHE_ASSOSCIATION = 3;
    private final int ECART_VISUELLE_X = 100;
    private final int ECART_VISUELLE_Y = 40;

    private transient BufferedImage tabInfo;


    public VueDiagramme(Modele m) {
        this.modele = m;
        try {
            tabInfo = ImageIO.read(new File("ressources/TableauInfo.png"));
        } catch (IOException e) {
            e.printStackTrace();
            tabInfo = null;
        }
    }

	/**
	 * Methode qui dessine dans le JPanel
	 * @param g
	 */
    public void paintComponent(Graphics g) {

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // On définit le font du Graphics
        g.setFont(NORMAL);

		// On définit le metrics qui permet de calculer la taille en pixel
        metrics = g.getFontMetrics();

        // On execute tous les objectClasse pour afficher les fleches d'heritage
        for (ObjectClasse oc : modele.getObjectClasses()) {
            if(oc.isVisible()) {

                /*
                On affiche les flèches d'heritages
                 */
                if (oc.getType()==TypeClasse.CLASSE || oc.getType()==TypeClasse.ABSTRACT) {
                    Extendable e = (Extendable) oc;
                    if (e.getObjectClasseExtends()!=null) {
                        if (modele.getObjectClasses().contains(e.getObjectClasseExtends())) {
                            drawArrow(g, oc, e.getObjectClasseExtends(), FLECHE_HERITAGE, null);
                        }
                    }
                }

                /*
                On affiche les flèches d'implémentations
                 */
                if (!oc.getListeObjectClasseImplements().isEmpty()) {
                    for (Interface i:oc.getListeObjectClasseImplements()) {
                        drawArrow(g, oc, i, FLECHE_IMPLEMENTS, null);
                    }
                }
            }
        }

        // Affichage de toutes les fleches d'associations
        for (FlecheAssociation f:modele.getAssociations()) {
            drawArrow(g, f.getSrc(), f.getDest(), FLECHE_ASSOSCIATION, f.getNom());
        }

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
                g.drawString(oc.getNomSimple(), posX + (largeur - metrics.stringWidth(oc.getNomSimple())) / 2, hauteurCourante);
                hauteurCourante += SIZE + ECART;

                String ligne = "";

                // On passe aux attributs
                for (Attribut a : oc.getAttributs()) {
                    if (a.isVisible()) {
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
                }

                // On passe aux methodes
                for (Methode m : oc.getMethodes()) {
                    if (m.isVisible()) {
                        if (m.isAbstrait()) g.setFont(ABSTRAIT);
                        else g.setFont(NORMAL);
                        g.drawString(m.afficher(), posX + ECART, hauteurCourante);
                        hauteurCourante += SIZE + ECART;
                    }
                }
            }
        }

        // Affichage de toutes les fleches d'associations
        for (FlecheAssociation f:modele.getAssociations()) {
            drawArrow(g, f.getSrc(), f.getDest(), FLECHE_ASSOSCIATION, f.getNom());
        }

        // Pour finir, on affiche le rectangle d'information.
        g.drawImage(tabInfo, getWidth()-tabInfo.getWidth()/2, getHeight()-tabInfo.getHeight()/2, tabInfo.getWidth()/2, tabInfo.getHeight()/2, null);
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

    void drawArrow(Graphics g, ObjectClasse src, ObjectClasse destination, int choixFleche, String message) {

        ObjectClasse dest = modele.getObjectClasse(destination.getNomObjectClasse());

        if (dest!=null) {
            int srcX, srcY, destX, destY;

            if (!src.getNomObjectClasse().equals(dest.getNomObjectClasse())) {

                int milieuDestX = dest.getX() + calculerLargeur(dest) / 2;
                int milieuDestY = dest.getY() + calculerHauteur(dest)/2;

                if (src.getX() - ECART_VISUELLE_X <= milieuDestX
                        && milieuDestX <= src.getX() + calculerLargeur(src) + ECART_VISUELLE_X
                        && milieuDestY <= src.getY() - ECART_VISUELLE_Y
                        || milieuDestY >= src.getY() + calculerHauteur(src) + ECART_VISUELLE_Y) {
                    if (dest.getY() <= src.getY()) {
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
                    if (dest.getX() <= src.getX()) {
                        srcX = src.getX();
                        srcY = src.getY() + calculerHauteur(src) / 2;
                        destX = dest.getX() + calculerLargeur(dest);
                        destY = dest.getY() + calculerHauteur(dest) / 2;
                    } else {
                        srcX = src.getX() + calculerLargeur(src);
                        srcY = src.getY() + calculerHauteur(src) / 2;
                        destX = dest.getX();
                        destY = dest.getY() + calculerHauteur(dest) / 2;
                    }
                }
            } else {
                srcX = src.getX() + calculerLargeur(src) / 3;
                srcY = 50+src.getY() + calculerHauteur(src);
                destX = src.getX() + calculerLargeur(src) / 3 * 2;
                destY = 50+src.getY() + calculerHauteur(src);
                System.out.println(srcX +" "+srcY+" "+destX+" "+destY);
            }

            int dx = destX - srcX, dy = destY - srcY;
            double D = Math.sqrt(dx * dx + dy * dy);
            int d = 12, h = 6;
            double xm = D - d, xn = xm, ym = h, yn = -h, x;
            double sin = dy / D, cos = dx / D;
            x = xm * cos - ym * sin + srcX;
            ym = xm * sin + ym * cos + srcY;
            xm = x;
            x = xn * cos - yn * sin + srcX;
            yn = xn * sin + yn * cos + srcY;
            xn = x;

            int decX = modele.getDecalageX();
            int decY = modele.getDecalageY();
            int[] xpoints = {destX + decX, (int) xm + decX, (int) xn + decX};
            int[] ypoints = {destY + decY, (int) ym + decY, (int) yn + decY};

            switch (choixFleche) {
                case FLECHE_HERITAGE :
                    g.setColor(Color.BLACK);
                    g.drawLine(srcX + decX, srcY + decY, destX + decX, destY + decY);
                    g.setColor(Color.WHITE);
                    g.fillPolygon(xpoints, ypoints, 3);
                    g.setColor(Color.BLACK);
                    g.drawPolygon(xpoints, ypoints, 3);
                    break;

                case FLECHE_IMPLEMENTS :
                    Graphics2D gg = ((Graphics2D) g);
                    Stroke s = gg.getStroke();
                    gg.setColor(Color.BLACK);
                    gg.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_MITER, 1.0f, new float[]{6f, 6f}, 2.0f));
                    gg.drawLine(srcX + decX, srcY + decY, destX + decX, destY + decY);
                    gg.setStroke(s);
                    g.setColor(Color.WHITE);
                    g.fillPolygon(xpoints, ypoints, 3);
                    g.setColor(Color.BLACK);
                    g.drawPolygon(xpoints, ypoints, 3);
                    break;

                case FLECHE_ASSOSCIATION :
                    System.out.println(src.getNomObjectClasse()+" "+dest.getNomObjectClasse());
                    g.setColor(Color.BLACK);
                    //g.drawLine(srcX + decX, srcY + decY, destX + decX, destY + decY);
                    dessinerFlecheArc(srcX, srcY, destX, destY, decX, decY, g);
                    g.drawLine(destX+decX, destY +decY, xpoints[2], ypoints[2]);
                    g.drawString(message, srcX+decX+(destX-srcX)/2, srcY+decY+(destY-srcY)/2);
                    break;

                default :
                    throw new Error("Impossible, un choix de fleche doit etre fait parmis celle existante");
            }
        }

    }

    private void dessinerFlecheArc(int srcX, int srcY, int destX, int destY, int decX, int decY, Graphics g) {
        double x1 = srcX, y1 = srcY;
        double x2 = destX, y2 = destY;
        double hh = 100;

        double x3 = (x1+x2)/2, y3 = (y1+y2)/2; // (x3,y3) middle of (x1,y1) and (x2,y2)
        double p = Math.hypot(x3-x1,y3-y1); // distance between (x1,y1) and (x3,y3) solve (x3-x1)²+(y3-y1)²=p²
        double r = (p*p+hh*hh)/(2*hh); // on right triangle, solve p²+(r-h)²=r²

        double a = (y1-y2) / (x1-x2), b = (x1*y2 - x2*y1) / (x1-x2); // y=ax+b for (x1,y1) and (x2,y2)
        double c = -1/a, dd = y3-c*x3; // line y=cx+d is perpendicular with line y=ax+b

        // y0=c.x0+d so distance between (x0,y0) and (x3,y3) solve (x3-x0)²+(y3-(c.x0+d))² = (r-h)²
        double x0 = resolve(x3, -1, y3-dd, -c, r-hh), y0 = c*x0+dd;

        double alpha1 = Math.atan2(y1-y0,x1-x0), alpha2 = Math.atan2(y2-y0,x2-x0);

        int x = (int) Math.round(x0 - r), y = (int) Math.round(y0 - r), width = (int) Math.round(2*r), height = width;
        // angles are negative because Swing origin is on top-left corner with descending ordinates
        int startAngle = (int) -Math.round(Math.toDegrees(alpha1)), arcAngle = (int) -Math.round(Math.toDegrees(alpha2 - alpha1));

        g.drawArc(x+decX, y+decY, width, height, startAngle, arcAngle);
    }

    private static double resolve(double a, double b, double c, double d, double e) {
        return (-Math.sqrt(2*a*b*c*d - a*a*d*d - b*b*c*c + b*b*e*e + d*d*e*e) - (a*b+c*d)) / (b*b+d*d);
    }

    /**
     * Calcule la hauteur de la case d'un objectClasse lors de l'affichage
     * @param oc objectClasse fournit
     * @return taille en pixel
     */
    public int calculerHauteur(ObjectClasse oc){
        return (oc.getNbAttributsVisible() + oc.getNbMethodesVisible() + 2) * SIZE /* Le nombre de ligne*/ + (oc.getNbAttributsVisible() + oc.getNbMethodesVisible() + 5) * ECART; /* L'ecart entre les lignes */
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

    public void reorganiserPourCapture(Rectangle boundsCapture){
        bounds = getBounds();
        setBounds(boundsCapture.x, boundsCapture.y ,boundsCapture.width, boundsCapture.height);
    }

    public void reinitialiserApresCapture(){
        setBounds(bounds);
    }

}
