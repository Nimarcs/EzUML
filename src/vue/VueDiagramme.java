// PACKAGE
package vue;

// IMPORTS
import modele.FlecheAssociation;
import modele.Modele;
import modele.Sujet;
import modele.classe.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * Classe VueDiagramme, hérite de JPanel, implémente Observeur et Serializable
 * A pour raison d'être d'afficher les classes affichées avec la bonne syntaxe
 */
public class VueDiagramme extends JPanel implements Observateur, Serializable {

    // ATTRIBUTS

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

    /**
     * Variable statique entière qui indique que c'est une flèche d'héritage qu'on souhaite afficher
     */
    private final static int FLECHE_HERITAGE = 1;

    /**
     * Variable statique entière qui indique qu'on souhaite dessiner une flèche d'implémentation
     */
    private final static int FLECHE_IMPLEMENTS = 2;

    /**
     * Variable statique entière qui indique qu'on souhaite l'affichage d'une flèche association
     */
    private final static int FLECHE_ASSOSCIATION = 3;

    /**
     * Variable entière qui permet de définir l'écart en plus à gauche et à droite sur l'abscisse pour une classe
     * Permet de délimiter la zone de changement de côté lorsqu'on déplace une classe
     */
    private final int ECART_VISUELLE_X = 100;

    /**
     * Variable entière qui permet de définir l'écart en plus à gauche et à droite sur l'ordonnée pour une classe
     * Permet de délimiter la zone de changement de côté lorsqu'on déplace une classe
     */
    private final int ECART_VISUELLE_Y = 40;

    /**
     * Attribut privé transient (non enregistré lors de la serialization) pour l'image de lexique
     */
    private transient BufferedImage tabInfo;

    /**
     * Variable statique entière définissant le nombre de pixel pour le décalage du texte d'une flèche d'assosciation lorsqu'il y en a plusieurs
     */
    private final static int DECALAGE_FLECHE = 25;

    /**
     * Conbstructeur VueDiagramme, basé selon le modèle
     *
     * @param m Modele, modele dont on tire les données
     */
    public VueDiagramme(Modele m) {
        this.modele = m;
        // On charge l'image du tableau d'information
        try {
            System.out.println();
            tabInfo = ImageIO.read(Objects.requireNonNull(getClass().getResource("/ressources/TableauInfo.png")));
        } catch (IOException e) {
            e.printStackTrace();
            tabInfo = null;
        }
    }

    /**
     * Methode qui dessine dans le JPanel
     *
     * @param g
     */
    public void paintComponent(Graphics g) {

        // On dessine tous le fond en blanc
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // On définit le font du Graphics
        g.setFont(NORMAL);

        // On définit le metrics qui permet de calculer la taille en pixel
        metrics = g.getFontMetrics();

        // On execute tous les objectClasse pour afficher les fleches d'heritage
        for (ObjectClasse oc : modele.getObjectClasses()) {

            // On vérifie si la classe est visible
            if (oc.isVisible()) {

                //On affiche les flèches d'heritages, si la classe oc est une CLASSE ou une CLASSE ABSTRAITE
                if (oc.getType() == TypeClasse.CLASSE || oc.getType() == TypeClasse.ABSTRACT) {
                    Extendable e = (Extendable) oc;
                    // On verifie que la classe oc a bien une classe parent
                    if (e.getObjectClasseExtends() != null) {
                        // On verifie que la classe parent est bien chargée
                        if (modele.getObjectClasses().contains(e.getObjectClasseExtends())) {
                            drawArrow(g, oc, e.getObjectClasseExtends(), FLECHE_HERITAGE, null);
                        }
                    }
                }

                //On affiche les flèches d'implémentations
                if (!oc.getListeObjectClasseImplements().isEmpty()) {
                    for (Interface i : oc.getListeObjectClasseImplements()) {
                        drawArrow(g, oc, i, FLECHE_IMPLEMENTS, null);
                    }
                }
            }
        }

        // Affichage de toutes les fleches d'associations
        for (FlecheAssociation f : modele.getAssociations()) {
            drawArrow(g, f.getSrc(), f.getDest(), FLECHE_ASSOSCIATION, f);
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

                // on calcule le nombres d'attributs visible
                int nbAttr = 0;
                for (Attribut a : oc.getAttributs()) {
                    if (a.isVisible()) nbAttr++;
                }

                // On dessine les bordures interne du rectangle pour definir le titre, les attributs et les constructeurs/methodes
                g.drawRect(posX, posY, largeur, 2 * SIZE + 3 * ECART);
                g.drawRect(posX, posY, largeur, 2 * SIZE + 3 * ECART + nbAttr * (SIZE + ECART));

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
                    // On verifie que l'attribut est visible
                    if (a.isVisible()) {
                        ligne = a.afficher();
                        // On regarde si l'attribut est statique et final
                        if (a.isFinale() && a.isStatique()) {
                            ligne = ligne.toUpperCase();
                            g.setFont(STATIQUE);
                        }
                        if (a.isFinale()) ligne = ligne.toUpperCase(); // si l'attribut est juste final
                        else if (a.isStatique()) g.setFont(STATIQUE); // si l'attribut est juste statique
                        else g.setFont(NORMAL); // cas normal
                        // on dessine la fleche
                        g.drawString(ligne, posX + ECART, hauteurCourante);
                        hauteurCourante += SIZE + ECART;
                    }
                }

                // On passe aux methodes
                for (Methode m : oc.getMethodes()) {
                    // On verifie que la méthode est visible
                    if (m.isVisible()) {
                        if (m.isAbstrait()) g.setFont(ABSTRAIT); // si la methode est abstraite
                        else g.setFont(NORMAL); // cas normal
                        g.drawString(m.afficher(), posX + ECART, hauteurCourante);
                        hauteurCourante += SIZE + ECART;
                    }
                }
            }
        }

        // Pour finir, on affiche le rectangle d'information.
        g.drawImage(tabInfo, getWidth() - tabInfo.getWidth() / 2, getHeight() - tabInfo.getHeight() / 2, tabInfo.getWidth() / 2, tabInfo.getHeight() / 2, null);
    }

    /**
     * Methode privée qui calcule la plus grande largeur qu'on doit prendre pour dessiner le rectangle
     *
     * @param oc ObjectClasse, dont
     * @return int
     */
    private int largeurRectangleClasse(ObjectClasse oc) {
        // on effectue plusieurs tests afin de trouver la largeur la plus grande
        int taille = 0;
        // on teste les deux lignes de l'entete de l'objet classe
        if (metrics.stringWidth("<< " + oc.getType().toString() + " >>") > taille)
            taille = metrics.stringWidth("<< " + oc.getType().toString() + " >>");
        if (metrics.stringWidth(oc.getNomObjectClasse()) > taille)
            taille = metrics.stringWidth(oc.getNomObjectClasse());
        // on teste chaque affichage d'attributs et de methode, pour voir si une ligne est plus grande que la taille actuelle trouvée
        for (Attribut a : oc.getAttributs()) {
            if (metrics.stringWidth(a.afficher()) > taille) taille = metrics.stringWidth(a.afficher());
        }
        for (Methode m : oc.getMethodes()) {
            if (metrics.stringWidth(m.afficher()) > taille) taille = metrics.stringWidth(m.afficher());
        }
        return taille;
    }

    /**
     * Methode qui dessine une flèche
     *
     * @param g           Graphics, zone où on dessine
     * @param src         ObjectClasse, classe de départ de la flèche
     * @param destination ObjectClasse, classe d'arrivée de la flèche
     * @param choixFleche int: indique quel type de flèches on doit afficher, voir les variables statiques définit plus haut
     * @param f   FlecheAssociation
     */
    void drawArrow(Graphics g, ObjectClasse src, ObjectClasse destination, int choixFleche, FlecheAssociation f) {

        // On recupere l'objectClasse de destination dans ceux chargée, au cas où celui qu'on aurait ne soit pas a jour
        ObjectClasse dest = modele.getObjectClasse(destination.getNomObjectClasse());
        // on recupère le décalage x et y
        int decX = modele.getDecalageX();
        int decY = modele.getDecalageY();
        String cote;

        // si la classe de destination n'est pas nulle et visible, et que la classe src soit aussi visible
        if (dest != null && src.isVisible() && dest.isVisible()) {
            int srcX, srcY, destX, destY;

            // cas où la classe src et dest sont différents
            if (!src.getNomObjectClasse().equals(dest.getNomObjectClasse())) {

                // on recupere le milieu de la classe de destination en x et y
                int milieuDestX = dest.getX() + calculerLargeur(dest) / 2;
                int milieuDestY = dest.getY() + calculerHauteur(dest) / 2;

                // On regarde où se situe la classe src par rapport à la classe dest
                if (src.getX() - ECART_VISUELLE_X <= milieuDestX
                        && milieuDestX <= src.getX() + calculerLargeur(src) + ECART_VISUELLE_X
                        && milieuDestY <= src.getY() - ECART_VISUELLE_Y
                        || milieuDestY >= src.getY() + calculerHauteur(src) + ECART_VISUELLE_Y) {
                    if (dest.getY() <= src.getY()) { // la classe src est en haut
                        cote = "Haut";
                        srcX = src.getX() + calculerLargeur(src) / 2;
                        srcY = src.getY();
                        destX = dest.getX() + calculerLargeur(dest) / 2;
                        destY = dest.getY() + calculerHauteur(dest);
                    } else { // la classe src est en bas
                        cote = "Bas";
                        srcX = src.getX() + calculerLargeur(src) / 2;
                        srcY = src.getY() + calculerHauteur(src);
                        destX = dest.getX() + calculerLargeur(dest) / 2;
                        destY = dest.getY();
                    }
                } else {
                    if (dest.getX() <= src.getX()) { // la classe src est à gauche
                        cote = "Gauche";
                        srcX = src.getX();
                        srcY = src.getY() + calculerHauteur(src) / 2;
                        destX = dest.getX() + calculerLargeur(dest);
                        destY = dest.getY() + calculerHauteur(dest) / 2;
                    } else { // la classe src est à droite
                        cote = "Droite";
                        srcX = src.getX() + calculerLargeur(src);
                        srcY = src.getY() + calculerHauteur(src) / 2;
                        destX = dest.getX();
                        destY = dest.getY() + calculerHauteur(dest) / 2;
                    }
                }

            } else { // cas où la classe src et dest sont identiques, flèche qui autopointe
                cote = "null";
                int largeur = calculerLargeur(src);
                // on dessine deux traits et on indique les points x et y pour dessiner le troisième et dernier trait avec la fleche
                g.setColor(Color.BLACK);
                g.drawLine(src.getX() + +decX + largeur / 3, src.getY() + decY, src.getX() + +decX + largeur / 3, src.getY() + decY - largeur / 3);
                g.drawLine(src.getX() + +decX + largeur / 3, src.getY() + decY - largeur / 3, src.getX() + +decX + largeur / 3 + largeur / 3, src.getY() + decY - largeur / 3);
                srcX = src.getX() + largeur / 3 + largeur / 3;
                srcY = src.getY() - largeur / 3;
                destX = src.getX() + largeur / 3 + largeur / 3;
                destY = src.getY();
            }

            // on calcule le nombre de fleches entre la classe src et la classe dest afin de pouvoir créer un décalage dans l'affichage
            int nbOcc = 0;
            if (choixFleche == FLECHE_ASSOSCIATION) {
                nbOcc = modele.nbOccurencesFleches(src, dest, f.getNom());
                if (cote.equals("Droite") || cote.equals("Gauche")) {
                    srcY += DECALAGE_FLECHE * nbOcc;
                    destY += DECALAGE_FLECHE * nbOcc;
                } else if (cote.equals("Bas") || cote.equals("Haut")) {
                    srcX += DECALAGE_FLECHE * nbOcc;
                    destX += DECALAGE_FLECHE * nbOcc;
                }
            }

            // On calcule les points afin de définir la tete de la flèche
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
            int[] xpoints = {destX + decX, (int) xm + decX, (int) xn + decX};
            int[] ypoints = {destY + decY, (int) ym + decY, (int) yn + decY};

            // Selon le choix de flèche, l'affichage du trait et de la tête de la fleche diffère
            switch (choixFleche) {

                case FLECHE_HERITAGE: // cas d'une fleche d'heritage
                    // on dessine un trait noir plein
                    g.setColor(Color.BLACK);
                    g.drawLine(srcX + decX, srcY + decY, destX + decX, destY + decY);
                    // on dessine un triangle blanc pour représenter la fleche
                    g.setColor(Color.WHITE);
                    g.fillPolygon(xpoints, ypoints, 3);
                    // on dessine les contours du triangle en trait noir plein
                    g.setColor(Color.BLACK);
                    g.drawPolygon(xpoints, ypoints, 3);
                    break;

                case FLECHE_IMPLEMENTS: // cas d'une fleche d'implementation
                    Graphics2D gg = ((Graphics2D) g);
                    Stroke s = gg.getStroke();
                    // on dessine un trait noir en tiret
                    gg.setColor(Color.BLACK);
                    gg.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_MITER, 1.0f, new float[]{6f, 6f}, 2.0f));
                    gg.drawLine(srcX + decX, srcY + decY, destX + decX, destY + decY);
                    gg.setStroke(s);
                    // on dessine un triangle blanc pour représenter la fleche
                    g.setColor(Color.WHITE);
                    g.fillPolygon(xpoints, ypoints, 3);
                    // on dessine les contours du triangle en trait noir plein
                    g.setColor(Color.BLACK);
                    g.drawPolygon(xpoints, ypoints, 3);
                    break;

                case FLECHE_ASSOSCIATION: // cas d'une fleche d'assosciation
                    // on dessine le trait noir en continu ainsi que les deux cotés de la flèche
                    g.setColor(Color.BLACK);
                    g.drawLine(srcX + decX, srcY + decY, destX + decX, destY + decY);
                    g.drawLine(destX + decX, destY + decY, xpoints[1], ypoints[1]);
                    g.drawLine(destX + decX, destY + decY, xpoints[2], ypoints[2]);
                    // selon le coté où se trouve la classe src par rapport à la classe dest, on dispose différement la string du nom de l'assosciation
                    if (cote.equals("Haut") || cote.equals("Bas")) {
                        // si c'est en haut ou en bas, on insere le nbOccurence de fleche afin que plusieurs nom d'assosciation ne se superpose pas
                        g.drawString(f.getNom(), srcX + decX + (destX - srcX) / 2, srcY + decY + (destY - srcY) / 2 + nbOcc * DECALAGE_FLECHE);
                    } else {
                        // si c'est a droite ou a gauche, on l'insere normalement
                        g.drawString(f.getNom(), srcX + decX + (destX - srcX) / 2, srcY + decY + (destY - srcY) / 2);
                    }
                    // on dessine les cardinalités
                    g.drawString(f.getCardSrc(), srcX + decX + (destX - srcX) / 8, srcY + decY + (destY - srcY) / 8);
                    g.drawString(f.getCardDest(), (int) (srcX + decX + (destX - srcX) * 0.875), (int) (srcY + decY + (destY - srcY) * 0.875));
                    break;

                default: // cas d'eeurs si aucune flèches ne correspond
                    throw new Error("Impossible, un choix de fleche doit etre fait parmis celle existante");
            }
        }

    }

    /**
     * Methode privée qui résout une opération mathématique nécessaire au calcul des points formant le triangle d'une flèche
     *
     * @param a double
     * @param b double
     * @param c double
     * @param d double
     * @param e double
     * @return double
     */
    private static double resolve(double a, double b, double c, double d, double e) {
        return (-Math.sqrt(2 * a * b * c * d - a * a * d * d - b * b * c * c + b * b * e * e + d * d * e * e) - (a * b + c * d)) / (b * b + d * d);
    }

    /**
     * Calcule la hauteur de la case d'un objectClasse lors de l'affichage
     *
     * @param oc objectClasse fournit
     * @return taille en pixel
     */
    public int calculerHauteur(ObjectClasse oc) {
        return (oc.getNbAttributsVisible() + oc.getNbMethodesVisible() + 2) * SIZE /* Le nombre de ligne*/ + (oc.getNbAttributsVisible() + oc.getNbMethodesVisible() + 5) * ECART; /* L'ecart entre les lignes */
    }

    /**
     * Calcule la largeur de la case d'un objectClasse lors de l'affichage
     *
     * @param oc objectClasse fournit
     * @return taille en pixel
     */
    public int calculerLargeur(ObjectClasse oc) {
        return this.largeurRectangleClasse(oc) + ECART * 2;
    }

    /**
     * Actualise la vue lorsque le sujet change, et repaint le panel en conséquence
     *
     * @param sujet Sujet
     */
    @Override
    public void actualiser(Sujet sujet) {
        this.modele = (Modele) sujet;
        this.repaint();
    }

    /**
     * Change les bordures d'affichage pour capture lors de l'exportation
     *
     * @param boundsCapture Rectangle: bordures qu'on doit appliquer
     */
    public void reorganiserPourCapture(Rectangle boundsCapture) {
        bounds = getBounds();
        setBounds(boundsCapture.x, boundsCapture.y, boundsCapture.width, boundsCapture.height);
    }

    /**
     * Remet des bordures d'affichage du panel a ceux intiaux après l'export
     */
    public void reinitialiserApresCapture() {
        setBounds(bounds);
    }

    /**
     * getter de la dimension de tabInfo
     *
     * @return dimension de tabInfo
     */
    public Dimension getDimensionsLegende() {
        if (tabInfo == null) throw new NullPointerException("tabInfo doit etre initialisee");
        return new Dimension(tabInfo.getWidth() / 2, tabInfo.getHeight() / 2);
    }


}
