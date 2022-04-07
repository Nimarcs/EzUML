package vue;

import java.util.Iterator;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import modele.*;
import modele.Package;

/**
 * Vue de l'arborescence
 * Elle permet de voir les fichiers .class que l'ont peut ajouter dans notre projet
 */
public class VueArborescence extends JScrollPane implements Observateur {

    // ATTRIBUTS

    /**
     * Attribut JTree qui sert de base à la vue arborescence
     */
    private final JTree base;

    /**
     * Attribut noeuds du Jtree
     */
    private final DefaultMutableTreeNode rootArbre;

    /**
     * Le constructeur instancie un arbre vide dans le JScrollPane
     */
    public VueArborescence(Modele modele) {
        rootArbre = new DefaultMutableTreeNode("Racine");
        rootArbre.add(new DefaultMutableTreeNode(new Package("src")));//purement esthetique
        base = new JTree(rootArbre);
        base.setToggleClickCount(1);
        base.setCellRenderer(new CustomRenderArbo(modele));
        base.setRootVisible(false);
        this.setViewportView(base);
    }

    /**
     * Cette méthode est appelée lorsque le sujet est modifiée et que l'observateur doit en être informée
     *
     * @param s
     */
    @Override
    public void actualiser(Sujet s) {

        Modele m = (Modele) s;

        Package p = m.getPackages();
        //initialistaion de la racine principale
        DefaultMutableTreeNode rootPackage = new DefaultMutableTreeNode(p);

        //appelle la methode ajouterNoeud, qui permet de generer l'arborescence en entier
        ajouterNoeud(rootPackage, p);

        //creer l'arbre a partir du noeud root contenant l'arborescence en entier
        rootArbre.removeAllChildren();
        rootArbre.add(rootPackage);

        //on actualise la vue
        DefaultTreeModel model = (DefaultTreeModel) (base.getModel());
        model.reload();

        //on ouvre les packages qui étais deja ouvert
        ouvrirPackage();
    }

    /**
     * methode qui permet d'ouvrir les packages qui sont ouvert selon le stockage
     */
    private void ouvrirPackage() {
        if (base.getRowCount() > 0) base.expandRow(0);
        for (int i = 0; i < base.getRowCount(); i++) { //on parcours toute les colonnes
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) base.getPathForRow(i).getLastPathComponent();
            if (!node.isLeaf()) {
                Package p = (Package) node.getUserObject();
                if (p.isOuvert()) {
                    base.expandRow(i);
                }
            }
        }
    }

    /**
     * methode qui permet de generer chaque noeud de l'arbre de maniere recursive
     *
     * @param racine DefaultMutableTreeNode
     * @param p      Package
     */
    public void ajouterNoeud(DefaultMutableTreeNode racine, Package p) {

        //iterateur des classes contenues dans le package p
        Iterator<String> iteclasses = p.getClassesContenues().iterator();

        //ajout de chaque fichier .class dans le noeud racine
        while (iteclasses.hasNext()) {
            String classe = iteclasses.next();
            racine.add(new DefaultMutableTreeNode(classe));
        }

        //iterateur des sous-packages contenus dans le package p
        Iterator<Package> itePackages = p.getSousPackages().iterator();

        //ajout de chaque sous-package dans le noeud racine
        while (itePackages.hasNext()) {
            Package pack = itePackages.next();
            DefaultMutableTreeNode racinePackage = new DefaultMutableTreeNode(pack);

            //appel recurisf de la methode ajouterNoeud, qui ajoutera tout les noeuds necessaires de ce sous-package
            ajouterNoeud(racinePackage, pack);
            //ajoute le noeud complet de ce sous-package sur cette racine
            racine.add(racinePackage);
        }

    }

    /**
     * Getter base du JTree
     *
     * @return JTre
     */
    public JTree getBase() {
        return base;
    }
}
