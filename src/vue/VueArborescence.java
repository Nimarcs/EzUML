package vue;

import java.util.Iterator;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import modele.*;
import modele.classe.ObjectClasse;

/**
 * Vue de l'arborescence
 * Elle permet de voir les fichiers .class que l'ont peut ajouter dans notre projet
 * 
 * 
 */

public class VueArborescence extends JScrollPane implements Observateur{
	
	
	
	/**
	 * Le constructeur instancie un arbre vide dans le JScrollPane
	 */
	public VueArborescence() {
		
	    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Racine");

		JTree base = new JTree(root);
		
		base.setRootVisible(false);
		add(base);		
		
		
	}
	
	
	
	
	
	/**
	 * La methode actualiser regarde les classes dans le modele et les affiches en vrac
	 */
	@Override
	public void actualiser(Sujet s) {
		remove(0);
		Modele m=(Modele)s;
		
	    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Racine");

		Iterator ite = m.getClasses().iterator();
		
		while(ite.hasNext()) {
			
			ObjectClasse obj = (ObjectClasse) ite.next();
			
			DefaultMutableTreeNode feuille = new DefaultMutableTreeNode(obj.getNomObjectClasse());
			root.add(feuille);
		}
		
		JTree base = new JTree(root);

		this.add(new JTree(root));		

	}

}
