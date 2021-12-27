// PACKAGE
package modele.classe;

// IMPORTS
import java.util.List;

/**
 * Classe ObjectClasse
 *
 * Represente une classe java
 * Contient une liste d'attributs et de methode mais aussi sa position dans la zone d'affichage des diagrammes des classes
 */
public abstract class ObjectClasse {

    // ATTRIBUTS

    /**
     * Attribut protege String contenant le nom de la classe
     */
    protected String nomObjectClasse;

    /**
     * Attribut protege String contenant le nom du package de la classe
     */
    protected String packageObjectClasse;

    /**
     * Attribut prive entier contenant les coordonnees x et y de la position de la classe dans la zone graphique d'affichage des classes
     */
    protected int x, y;

    /**
     * Attribut prive liste d'attributs contenant tous les attributs de la classe
     */
    protected List<Attribut> attributs;

    /**
     * Attribut prive liste de méthodes contenant toutes les methodes et constructeurs de la classe
     */
    protected List<Methode> methodes;

    /**
     * Attribut prive liste d'interface contenant toutes les interfaces qui implemente cette classe
     */
    protected List<Interface> listeObjectClasseImplements;

    // CONSTRUCTEURS


    // METHODES

    /**
     * Methode pour ajouter une interface qui implemente cette classe
     * @param inter Interface: interface qu'on doit ajouter dans la liste d'implementation
     */
    public void ajouterImplements(Interface inter) {
        this.listeObjectClasseImplements.add(inter);
    }

    /**
     * Methode pour supprimer une interface de la liste d'interface qui implemente cette classe
     * @param inter Interface: interface qu'on doit supprimer
     */
    public void supprimerImplements(Interface inter) {
        this.listeObjectClasseImplements.remove(inter);
    }

    /**
     * Mehode pour ajouter un attribut dans une classe
     * @param att Attribut: attribut qu'on doit ajouter a la liste d'attributs
     */
    public void ajouterAttribut(Attribut att) {
        this.attributs.add(att);
    }

    /**
     * Methode pour supprimer un attribut dans une classe
     * @param att Attribut: attribut qu'on doit enlever dans la liste d'attributs
     */
    public void supprimerAttribut(Attribut att) {
        this.attributs.remove(att);
    }

    /**
     * Methode pour ajouter une methode dans la liste des methodes de cette classe
     * @param meth Methode: methode qu'on doit ajouter
     */
    public void ajouterMethode(Methode meth) {
        this.methodes.add(meth);
    }

    /**
     * Methode pour supprimer une methode de la liste des methodes de la classe
     * @param meth Methode: methode qu'on doit supprimer
     */
    public void supprimerMethode(Methode meth) {
        this.methodes.remove(meth);
    }


    // GETTERS && SETTERS

    /**
     * Getter sur le nom de la classe
     * @return String
     */
    public String getNomObjectClasse() {
        return nomObjectClasse;
    }

    /**
     * Getter sur le nom du package de la classe
     * @return String
     */
    public String getPackageObjectClasse() {
        return packageObjectClasse;
    }

    /**
     * Getter sur la position X (point en haut a gauche) de la classe dans la zone d'affichage
     * @return int
     */
    public int getX() {
        return x;
    }

    /**
     * Getter sur la position Y (point en haut a gauche) de la classe dans la zone d'affichage
     * @return int
     */
    public int getY() {
        return y;
    }

    /**
     * Getter sur la liste des attributs de la classe
     * @return List<Attribut>
     */
    public List<Attribut> getAttributs() {
        return attributs;
    }

    /**
     * Getter sur la liste des methodes de la classe
     * @return List<Methode>
     */
    public List<Methode> getMethodes() {
        return methodes;
    }

    /**
     * Getter sur la liste des interfaces qui implementent la classe
     * @return List<Interface>
     */
    public List<Interface> getListeObjectClasseImplements() {
        return listeObjectClasseImplements;
    }

}
