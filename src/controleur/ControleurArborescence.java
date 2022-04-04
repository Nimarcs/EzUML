package controleur;

import modele.Modele;
import modele.classe.ObjectClasse;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

/**
 * Controleur qui permet de faire les actions avec les touches du clavier
 */
public class ControleurArborescence implements MouseListener {

    /**
     * modele dont on veut controler les valeurs
     */
    private final Modele modele;

    /**
     * arbre qui doit etre set a l'initialisation
     * Arbre sur lequel on écoute les clics
     */
    private JTree arbre;

    /**
     * Contructeur de ControleurClavier
     * @param m modele a modifier, ne doit pas etre null
     */
    public ControleurArborescence(Modele m){
        this.arbre = null;
        assert m != null;
        modele=m;
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        assert arbre != null;
        //on recupere le path
        TreePath treePath = arbre.getPathForLocation(e.getX(), e.getY());
        if (treePath == null) return;

        //on recupere la node
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
        if (node == null) return;

        System.out.println("notnull");
        //on regarde si c'est une feuille donc un objectClasse
        if (node.isLeaf()){
            System.out.print("leaf");
            System.out.println(node.getUserObject().getClass());

            Object o = node.getUserObject();
            if (o instanceof String && e.getClickCount() >= 2) {
                String cheminComplet = (String) o;
                ObjectClasse objectClasse = modele.getObjectClasse(cheminComplet);
                if (objectClasse != null) modele.ajouterClasse(objectClasse, 0, 0);
                System.out.println(objectClasse);
                for (ObjectClasse objectClasse1 :  modele.getCollectionObjectClasse().getClassesChargees() ) {
                    System.out.print(objectClasse1 + " : ");
                    System.out.println(objectClasse1.isVisible());
                }
            }
        }

    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {

    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void setArbre(JTree arbre) {
        this.arbre = arbre;
    }
}
