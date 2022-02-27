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
	

    
    int i = 0;

	
	/**
	 * Le constructeur instancie un arbre vide dans le JScrollPane
	 */
	public VueArborescence() {
		
	    
	    DefaultMutableTreeNode root = new DefaultMutableTreeNode("Racine");

		JTree base = new JTree(root);
		
		base.setCellRenderer(new CustomRenderArbo());
		
		base.setRootVisible(true);
		
		this.setViewportView(base);
		
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
    	 * puis si l'objet est visible ou pas et afficher chaque element de maniere correcte
    	 * 
    	 */

    	
    	if(objet instanceof Package) {
    		label.setIcon(null);
    		if(((Package) objet).getNom()=="") {
    		    label.setText("PackageSansNom");
    		} else {
    	          label.setText(((Package) objet).getNom());

    		}

    	}
    	
    	if(objet instanceof String) {
     
            System.out.println((String) objet);
    		label.setText((String) objet);
   			label.setIcon(vert);

    

    	} 
    	    	
    	return label;    	
    	
    	}
    
    
    }


    @Override
    public void actualiser(Sujet s) {
       
        Modele m =(Modele)s;
        
        Package p = m.getPackages();
        
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(p);
        ajouterNoeud(root,p);
        System.out.println(i);

        
        JTree base = new JTree(root);
        
        base.setCellRenderer(new CustomRenderArbo());
        
        base.setRootVisible(true);
        
        this.setViewportView(base);        
    }

    
    public void ajouterNoeud(DefaultMutableTreeNode racine, Package p) {
        Iterator<String> iteclasses = p.getClassesContenues().iterator();
        i++;
        while(iteclasses.hasNext()) {
            String classe = iteclasses.next();
            System.out.println(classe);
            racine.add(new DefaultMutableTreeNode(classe));
        }
        
        Iterator<Package> itePackages = p.getSousPackages().iterator();
        
        while(itePackages.hasNext()) {
            Package pack = itePackages.next();
            DefaultMutableTreeNode racinePackage = new DefaultMutableTreeNode(pack);
            ajouterNoeud(racinePackage,pack);
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

}
