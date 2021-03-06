package controleur;

import modele.Modele;
import modele.Package;
import modele.classe.ObjectClasse;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

/**
 * Controleur qui permet de gerer les actions de l'arborescence
 */
public class ControleurArborescence implements MouseListener, TreeSelectionListener {

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
     * permet de reprendre le focus sur le JFrame principal et pas sur celui des pop-up
     */
    private final JFrame oldFrame;

    /**
     * Contructeur de ControleurClavier
     *
     * @param m        modele a modifier, ne doit pas etre null
     * @param oldFrame JFrame du diagramme
     */
    public ControleurArborescence(Modele m, JFrame oldFrame) {
        this.oldFrame = oldFrame;
        this.arbre = null;
        assert m != null;
        modele = m;
    }

    /**
     * methode appele lorsque l'on appuie sur l'arborescence qui se trouve a gauche
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        assert arbre != null;
        //on recupere le path
        TreePath treePath = arbre.getPathForRow(arbre.getClosestRowForLocation(e.getX(), e.getY()));
        if (treePath == null) return;

        //on recupere la node
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treePath.getLastPathComponent();
        if (node == null) return;

        Object o = node.getUserObject();

        //on regarde si c'est une feuille donc un objectClasse
        if (node.isLeaf()) {

            //si c'est un double click sur une node qui contient un string (donc le chemin vers un objectclasse)
            if (o instanceof String && e.getClickCount() >= 2) {

                //on l'ajoute si elle est chargee
                String cheminComplet = (String) o;
                ObjectClasse objectClasse = modele.getObjectClasse(cheminComplet);
                if (objectClasse != null) {
                    if (objectClasse.isVisible()) {
                        List<ObjectClasse> tmp = modele.getSelection();
                        modele.deselectionner();
                        modele.selectionnerUneClasse(objectClasse);
                        modele.retirerClasseSelectionne();
                        modele.setSelection(tmp);
                    } else {
                        modele.ajouterClasse(objectClasse, -modele.getDecalageX(), -modele.getDecalageY());
                    }
                }
            }
        } else {

            //sinon c'est un package
            if (o instanceof Package) {

                Package p = (Package) o;
                this.modele.getCollectionObjectClasse().changerOuverture(p);
            }

        }
        oldFrame.requestFocus();
    }

    /**
     * methode qui permet de modifier l'arbre
     *
     * @param arbre
     */
    public void setArbre(JTree arbre) {
        this.arbre = arbre;
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


    /**
     * Called whenever the value of the selection changes.
     *
     * @param e the event that characterizes the change.
     */
    @Override
    public void valueChanged(TreeSelectionEvent e) {

    }

}
