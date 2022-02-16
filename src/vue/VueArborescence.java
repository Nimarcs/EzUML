package vue;

import java.awt.Component;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;


import modele.*;
import modele.Package;
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
    	 * le custom render va regarder si l'objet est un package ou pas
    	 * puis si l'objet est visible ou pas et afficher chaque �l�ment de mani�re correcte
    	 * 
    	 */
    	
    	if(objet instanceof Package) {
    		label.setIcon(null);
    		label.setText(((Package) objet).getNom());

    	}
    	
    	if(objet instanceof ObjectClasse) {
    		label.setText(((ObjectClasse) objet).getNomObjectClasse());
    		
    		if(((ObjectClasse) objet).isVisible()) {
    			label.setIcon(vert);
    		} else {
    			label.setIcon(rouge);
    		}
    	} 
    	    	
    	return label;    	
    	
    	}
    
    
    }
	
	
	
	/**
	 * La methode actualiser regarde les classes dans le modele et les affiches de maniere ordonnee avec le bon code couleur
	 */
	@Override
	public void actualiser(Sujet s) {
		remove(0);
		Modele m=(Modele)s;
		
		Package p = m.getPackages();
		
	    DefaultMutableTreeNode root = new DefaultMutableTreeNode(p);

		Set<ObjectClasse> classes = new HashSet<>();
		for (String nomComplet: p.getClassesContenues()) {
			ObjectClasse o = m.getObjectClasse(nomComplet);
			if (o != null) classes.add(o);
		}

	    root = creerBranche(root, p.getSousPackages(), classes, m);
	    

		JTree base = new JTree(root);
		
		base.setCellRenderer(new CustomRenderArbo());
		
	

		this.add(new JTree(root));		

	}
	
	/**
	 * Methode recursive, qui permet d'afficher chaque branche de maniere correcte
	 * @param noeud racine sur laquelle on va greffer les branches
	 * @param souspackages tout les packages de la racine
	 * @param classes tout les objets class de la racine
	 * @return
	 */
	public DefaultMutableTreeNode creerBranche(DefaultMutableTreeNode noeud, Set<Package> souspackages, Set<ObjectClasse> classes, Modele m) {
		
		Iterator<ObjectClasse> iteC = classes.iterator();
		
		while(iteC.hasNext()) {
	    	DefaultMutableTreeNode sousclasses = new DefaultMutableTreeNode(iteC.next());
	    	noeud.add(sousclasses);
		}
		
		
	    Iterator<Package> iteP = souspackages.iterator();

	    while(iteP.hasNext()){
			Package p =  iteP.next();
	    	DefaultMutableTreeNode souspackage = new DefaultMutableTreeNode(p);


			Set<ObjectClasse> classesContenue = new HashSet<>();
			for (String nomComplet:  p.getClassesContenues()) {
				ObjectClasse o = m.getObjectClasse(nomComplet);
				if (o != null) classes.add(o);
			}

	    	DefaultMutableTreeNode souspackagerempli = creerBranche(souspackage, iteP.next().getSousPackages(), classesContenue, m);
	    	noeud.add(souspackagerempli);
	    	
	    }		
		
		return noeud;
		
	}

}
