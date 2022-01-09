package vue;

import java.awt.Component;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;


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
	 * Classe CustomRenderArbo, permet d'afficher chaque element de l'arborescence de maniere correcte
	 *
	 */
    public class CustomRenderArbo extends DefaultTreeCellRenderer {
    	
    	private final ImageIcon vert;
    	private final ImageIcon rouge;
    	private final JLabel label;
    	
    	
    	public CustomRenderArbo() {
    		this.vert = new ImageIcon(VueArborescence.class.getResource("imagesfeuilles/pointvert.png"));    		

    		this.rouge = new ImageIcon(VueArborescence.class.getResource("imagesfeuilles/pointrouge.png"));    		
    	
    		this.label = new JLabel();
    	}
    	
    	 public Component getTreeCellRendererComponent(JTree tree, Object value,boolean sel,boolean expanded,boolean leaf, int row,boolean hasFocus) {
    	
    	Object objet = ((DefaultMutableTreeNode) value).getUserObject();
    		 
    	/**
    	 * ceci est un prototype de comment fonctionnerait le custom render
    	 * il a pour but d'afficher une pastille rouge a cote des objets non affiche et une pastille
    	 * verte a cote de ceux qui sont affiches
    	 * 
    	 */
    	
//    	if(objet instanceof ObjetClass) {
//    		label.setIcon(rouge);
//    	} else {
//    		label.setIcon(vert);
//    		label.setText(value.toString());
//    	}
//    	
//    	if(!leaf) {
//    		label.setIcon(null);
//    	}
    	
    	return label;    	
    	
    	
    }
    
    
    }
	
	
	
	/**
	 * La methode actualiser regarde les classes dans le modele et les affiches en vrac
	 */
	@Override
	public void actualiser(Sujet s) {
		remove(0);
		Modele m=(Modele)s;
		
	    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Racine");

//		Iterator ite = m.getClasses().iterator();
//		
//		while(ite.hasNext()) {
//			
//			ObjectClasse obj = (ObjectClasse) ite.next();
//			
//			DefaultMutableTreeNode feuille = new DefaultMutableTreeNode(obj.getNomObjectClasse());
//			root.add(feuille);
//		}
	    
	    

	    
	    

		JTree base = new JTree(root);
		
		base.setCellRenderer(new CustomRenderArbo());
		
	

		this.add(new JTree(root));		

	}

}
