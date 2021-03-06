// PACKAGE
package modele.classe;

// IMPORTS

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Classe ObjectClasse
 * <p>
 * Represente une classe java
 * Contient une liste d'attributs et de methode mais aussi sa position dans la zone d'affichage des diagrammes des classes
 */
public abstract class ObjectClasse implements Serializable {

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
     * Attribut prive entier contenant les coordonnees x et y de la position du coin haut gauche de la classe dans la zone graphique d'affichage des classes
     */
    protected int x, y;

    /**
     * Attribut prive liste d'attributs contenant tous les attributs de la classe
     */
    protected List<Attribut> attributs;

    /**
     * Attribut prive liste de methodes contenant toutes les methodes et constructeurs de la classe
     */
    protected List<Methode> methodes;

    /**
     * Attribut prive liste d'interface contenant toutes les interfaces qui implemente cette classe
     */
    protected List<Interface> listeObjectClasseImplements;

    /**
     * Attribut prive booleen qui indique la visibilite de l'object classe dans la zone d'affichage
     */
    protected boolean visible;

    // CONSTRUCTEURS

    /**
     * Constructeur d'un ObjectClasse, representant une classe java
     *
     * @param nomObjectClasse     String: nom de la classe
     * @param packageObjectClasse String: nom du package de la classe
     * @param x                   int: position x de la classe (coin en haut a gauche du rectangle a afficher)
     * @param y                   int: position y de la classe (coin en haut a gauche du rectangle a afficher)
     */
    public ObjectClasse(String nomObjectClasse, String packageObjectClasse, int x, int y) {
        this.nomObjectClasse = nomObjectClasse;
        this.packageObjectClasse = packageObjectClasse;
        this.x = x;
        this.y = y;
        this.attributs = new ArrayList<>();
        this.methodes = new ArrayList<>();
        this.listeObjectClasseImplements = new ArrayList<>();
        this.visible = false;
    }

    // METHODES

    /**
     * Methode pour ajouter une interface qui implemente cette classe
     *
     * @param inter Interface: interface qu'on doit ajouter dans la liste d'implementation
     */
    public void ajouterImplements(Interface inter) {
        this.listeObjectClasseImplements.add(inter);
    }

    /**
     * Methode pour supprimer une interface de la liste d'interface qui implemente cette classe
     *
     * @param inter Interface: interface qu'on doit supprimer
     */
    public void supprimerImplements(Interface inter) {
        this.listeObjectClasseImplements.remove(inter);
    }

    /**
     * Mehode pour ajouter un attribut dans une classe
     *
     * @param att Attribut: attribut qu'on doit ajouter a la liste d'attributs
     */
    public void ajouterAttribut(Attribut att) {
        this.attributs.add(att);
    }

    /**
     * Methode pour supprimer un attribut dans une classe
     *
     * @param att Attribut: attribut qu'on doit enlever dans la liste d'attributs
     */
    public void supprimerAttribut(Attribut att) {
        this.attributs.remove(att);
    }

    /**
     * Methode pour ajouter une methode dans la liste des methodes de cette classe
     *
     * @param meth Methode: methode qu'on doit ajouter
     */
    public void ajouterMethode(Methode meth) {
        this.methodes.add(meth);
    }

    /**
     * Methode pour supprimer une methode de la liste des methodes de la classe
     *
     * @param meth Methode: methode qu'on doit supprimer
     */
    public void supprimerMethode(Methode meth) {
        this.methodes.remove(meth);
    }

    /**
     * Methode pour deplacer la classe selon deux vecteurs
     *
     * @param vecteurX int: vecteur sur l'axe X
     * @param vecteurY int: vecteur sur l'axe Y
     */
    public void deplacer(int vecteurX, int vecteurY) {
        this.x += vecteurX;
        this.y += vecteurY;
    }

    /**
     * Methode pour changer la visibilite de l'object classe
     *
     * @param visibilite boolean: indique la nouvelle visibilite ou non de l'objet
     */
    public void changerVisibilite(boolean visibilite) {
        this.visible = visibilite;
    }

    /**
     * Methode equals pour comparer deux ObjectClasse entre eux
     *
     * @param o Object: object qu'on compare avec celle qui appelle cette methode
     * @return booleen: true si les deux objets sont egaux, sinon false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectClasse that = (ObjectClasse) o;
        return nomObjectClasse.equals(that.nomObjectClasse) && packageObjectClasse.equals(that.packageObjectClasse);
    }

    /**
     * Methode hashCode
     * @return int: retourne la valeur de l'objet hasher
     */
    @Override
    public int hashCode() {
        return Objects.hash(nomObjectClasse, packageObjectClasse);
    }

    // GETTERS && SETTERS

    /**
     * Getter sur le nom de la classe
     *
     * @return String
     */
    public String getNomObjectClasse() {
        return nomObjectClasse;
    }

    /**
     * Getter sur le nom du package de la classe
     *
     * @return String
     */
    public String getPackageObjectClasse() {
        return packageObjectClasse;
    }

    /**
     * Getter sur la position X (point en haut a gauche) de la classe dans la zone d'affichage
     *
     * @return int
     */
    public int getX() {
        return x;
    }

    /**
     * Getter sur la position Y (point en haut a gauche) de la classe dans la zone d'affichage
     *
     * @return int
     */
    public int getY() {
        return y;
    }

    /**
     * Getter sur la liste des attributs de la classe
     *
     * @return List<Attribut>
     */
    public List<Attribut> getAttributs() {
        return attributs;
    }

    /**
     * Getter sur la liste des methodes de la classe
     *
     * @return List<Methode>
     */
    public List<Methode> getMethodes() {
        return methodes;
    }

    /**
     * Getter sur la liste des interfaces qui implementent la classe
     *
     * @return List<Interface>
     */
    public List<Interface> getListeObjectClasseImplements() {
        return listeObjectClasseImplements;
    }

    /**
     * Getter sur la visibilite de la classe
     *
     * @return boolean
     */
    public boolean isVisible() {
        return this.visible;
    }

    /**
     * Getter abstrait sur le type de classe que ce sera
     *
     * @return TypeClasse
     */
    public abstract TypeClasse getType();

    /**
     * setter de la position
     * Deplace l'ObjectClasse aux coordonne fournie
     *
     * @param x position sur x
     * @param y position sur y
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter qui renvoie le nom simplifier de l'objectClasse
     *
     * @return nom sans les packages de l'objectClasse
     */
    public String getNomSimple() {
        String tab[] = nomObjectClasse.split(Pattern.quote("."));
        return tab[tab.length - 1];
    }

    /**
     * Getter qui renvoie le nombre d'attributs visible
     *
     * @return int
     */
    public int getNbAttributsVisible() {
        int nb = 0;
        for (Attribut a : this.attributs) {
            if (a.isVisible()) nb++;
        }
        return nb;
    }

    /**
     * Getter qui renvoie le nombre de methodes visibles
     *
     * @return int
     */
    public int getNbMethodesVisible() {
        int nb = 0;
        for (Methode m : this.methodes) {
            if (m.isVisible()) nb++;
        }
        return nb;
    }

}
