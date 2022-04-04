package vue;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Objects;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;


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


	private final JTree base;

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
	
	



    @Override
    public void actualiser(Sujet s) {
       
        Modele m =(Modele)s;
        
        Package p = m.getPackages();
        
        //initialistaion de la racine principale
        DefaultMutableTreeNode rootPackage = new DefaultMutableTreeNode(p);
        
        //appelle la methode ajouterNoeud, qui permet de generer l'arborescence en entier
        ajouterNoeud(rootPackage,p);

        //creer l'arbre a partir du noeud root contenant l'arborescence en entier
		rootArbre.removeAllChildren();
		rootArbre.add(rootPackage);

		//on actualise la vue
		DefaultTreeModel model = (DefaultTreeModel) (base.getModel());
		model.reload();

		//on ouvre src pour qu'on puisse voir les packages
		if (base.getRowCount() > 0) base.expandRow(0);
	}

    /**
     * m�thode qui permet de generer chaque noeud de l'arbre de maniere recursive
     * @param racine 
     * @param p
     */
    public void ajouterNoeud(DefaultMutableTreeNode racine, Package p) {
        
        //iterateur des classes contenues dans le package p
        Iterator<String> iteclasses = p.getClassesContenues().iterator();
        
        //ajout de chaque fichier .class dans le noeud racine
        while(iteclasses.hasNext()) {
            String classe = iteclasses.next();
            racine.add(new DefaultMutableTreeNode(classe));
        }
        
        //iterateur des sous-packages contenus dans le package p
        Iterator<Package> itePackages = p.getSousPackages().iterator();
        
        //ajout de chaque sous-package dans le noeud racine
        while(itePackages.hasNext()) {
            Package pack = itePackages.next();
            DefaultMutableTreeNode racinePackage = new DefaultMutableTreeNode(pack);
            
            //appel recurisf de la methode ajouterNoeud, qui ajoutera tout les noeuds necessaires de ce sous-package
            ajouterNoeud(racinePackage,pack);
            //ajoute le noeud complet de ce sous-package sur cette racine
            racine.add(racinePackage);           
        }
        
    }



	
	
//	/**
//	 * La methode actualiser regarde les classes dans le modele et les affiches de maniere ordonnee avec le bon code couleur
//	 */
//	@Override
//	public void actualiser(Sujet s) {
//		remove(0);
//		Modele m=(Modele)s;
//		
//		
//		Package p = m.getPackages();
//		
//	    DefaultMutableTreeNode root = new DefaultMutableTreeNode(p);
//
////	     System.out.println("ok");
//
//		Set<ObjectClasse> classes = new HashSet<>();
//	    System.out.println("ok1");
//	    
//	    
//	    
//	    System.out.println(p.getNom()); 
//
//	    System.out.println(p.getSousPackages()); 
//	    
//	    for(Package pack : p.getSousPackages()) {
//	        System.out.println(pack);
//
//	        System.out.println(pack.getNom());
//	        for (String nomComplet: pack.getClassesContenues()) {
//
//	            System.out.println(nomComplet.substring(1));
//	        }
//	    }
//	    
//	    
//	    
//	    System.out.println(p.getClassesContenues()); 
//	    
//		for (String nomComplet: p.getClassesContenues()) {
//
//			ObjectClasse o = m.getObjectClasse(nomComplet);
//
//			
//
//			if (o != null) classes.add(o);
//		}
//
//		
//		root = creerBranche(root, p.getSousPackages(), p.getClassesContenues(), m);
//		
//	    
//
//		JTree base = new JTree(root);
//		
//		base.setCellRenderer(new CustomRenderArbo());
//		
//	
//
//		this.setViewportView(base);
//
//	}
//	
//	/**
//	 * Methode recursive, qui permet d'afficher chaque branche de maniere correcte
//	 * @param noeud racine sur laquelle on va greffer les branches
//	 * @param souspackages tout les packages de la racine
//	 * @param classes tout les objets class de la racine
//	 * @return
//	 */
//	public DefaultMutableTreeNode creerBranche(DefaultMutableTreeNode noeud, Set<Package> souspackages, Set<String> classes, Modele m) {
//		
//		Iterator<String> iteC = classes.iterator();
//		
//
//		
//	    Iterator<Package> iteP = souspackages.iterator();
//
//	    while(iteP.hasNext()){
//	        
//	        Package p =  iteP.next();
//	    	System.out.println(p);
//	        DefaultMutableTreeNode souspackage = new DefaultMutableTreeNode(p);
//
//
//
//			
//			System.out.println(p.getClassesContenues());
//			
//	        System.out.println("okdsds312-3");
//
//			
//			System.out.println(p.getSousPackages());
//			
//			System.out.println("ok312-3");
//			
//			DefaultMutableTreeNode souspackagerempli = creerBranche(souspackage, p.getSousPackages(), p.getClassesContenues(), m);
//			noeud.add(souspackagerempli);
//			
//	    	
//	    }		
//	       while(iteC.hasNext()) {
//	            System.out.println("passe classe");
//	            String classe = iteC.next().substring(1);
//	            System.out.println(classe);
//	            DefaultMutableTreeNode sousclasses = new DefaultMutableTreeNode(classe);
//	            noeud.add(sousclasses);
//	        }
//	        
//		
//		return noeud;
//		
//	}


	public JTree getBase() {
		return base;
	}
}
