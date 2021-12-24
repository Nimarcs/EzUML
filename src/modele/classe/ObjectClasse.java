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


    // GETTERS && SETTERS

    public String getNomObjectClasse() {
        return nomObjectClasse;
    }

    public String getPackageObjectClasse() {
        return packageObjectClasse;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<Attribut> getAttributs() {
        return attributs;
    }

    public List<Methode> getMethodes() {
        return methodes;
    }

    public List<Interface> getListeObjectClasseImplements() {
        return listeObjectClasseImplements;
    }

}
