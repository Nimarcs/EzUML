package controleur;

import modele.Modele;
import modele.classe.ObjectClasse;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Controleur qui permet de faire les actions avec les touches du clavier
 */
public class ControleurArborescence implements TreeSelectionListener {

    /**
     * modele dont on veut controler les valeurs
     */
    private final Modele modele;

    /**
     * Contructeur de ControleurClavier
     * @param m modele a modifier, ne doit pas etre null
     */
    public ControleurArborescence(Modele m){
        assert m != null;
        modele=m;
    }

    /**
     * Called whenever the value of the selection changes.
     *
     * @param e the event that characterizes the change.
     */
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        System.out.println("event");

        DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
        if (node == null) return;
        System.out.println("notnull");
        if (node.isLeaf()){
            System.out.println("leaf");
            try {
                System.out.println(node.getUserObject().getClass());
                Object o = node.getUserObject();
                if (o instanceof String) {
                    String cheminComplet = (String) o;
                    modele.ajouterClasse(modele.getObjectClasse(cheminComplet), 0, 0);
                }
            }catch (ClassCastException exception){
                exception.printStackTrace();
            }
        }

    }
}
