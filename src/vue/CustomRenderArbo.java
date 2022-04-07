package vue;

import modele.Modele;
import modele.Package;
import modele.classe.ObjectClasse;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.Objects;

/**
 * Classe CustomRenderArbo, permet d'afficher chaque element de l'arborescence de maniere correcte
 */
public class CustomRenderArbo extends DefaultTreeCellRenderer {

    // ATTRIBUTS

    /**
     * Attribut Icon pour le point vert
     */
    private static final ImageIcon VERT = new ImageIcon(CustomRenderArbo.class.getResource("/ressources/pointvert.png"));

    /**
     * Attribut Icon pour le point rouge
     */
    private static final ImageIcon ROUGE = new ImageIcon(CustomRenderArbo.class.getResource("/ressources/pointrouge.png"));

    /**
     * Attribut label du noeud
     */
    private final JLabel label;

    /**
     * Attribut contenant le modele qui contient toutes les données
     */
    private final Modele modele;

    // CONSTRUCTEURS

    /**
     * Constructeur CustomRenderArbo
     *
     * @param m Modele
     */
    public CustomRenderArbo(Modele m) {
        this.label = new JLabel();
        this.modele = m;
    }

    /**
     * Getter qui retourne le composant avec du contenus d'une celleule d'un arbre
     *
     * @param tree     JTree
     * @param value    Object
     * @param sel      boolean
     * @param expanded boolean
     * @param leaf     boolean
     * @param row      int
     * @param hasFocus boolean
     * @return Component
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        Object objet = ((DefaultMutableTreeNode) value).getUserObject();

        /*
         * le custom render va regarder si l'objet est un package ou pas
         * puis si l'objet est visible ou pas et afficher chaque element de maniere correcte
         *
         */

        //affichage du package dans l'arbre, il s'agit d'un simple label avec le nom du package ou un placeholder si le .class n'a pas de package
        if (objet instanceof Package) {
            label.setIcon(null);
            if (Objects.equals(((Package) objet).getNom(), "")) {
                label.setText("PackageSansNom");
            } else {
                label.setText(((Package) objet).getNom());

            }

        }

        //affichage de la classe, avec une pastille verte si il est affiche ou rouge sinon
        if (objet instanceof String) {
            String cheminComplet = (String) objet;
            ObjectClasse objectClasse = modele.getObjectClasse(cheminComplet);
            //si la classe existe pas on l'affiche pas
            if (objectClasse != null) {

                //on recupere le nom
                label.setText(objectClasse.getNomSimple());

                if (objectClasse.isVisible()) label.setIcon(VERT);
                else label.setIcon(ROUGE);
            }
        }

        return label;

    }


}
