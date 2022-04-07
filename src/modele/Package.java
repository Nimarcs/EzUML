package modele;

import java.io.Serializable;
import java.util.*;

/**
 * Classe representant un package
 * Géneré par le modele avec les objects classe que l'on lui fournit
 */
public class Package implements Serializable {


    //ATTRIBUT

    /**
     * Classe contenue dans le package
     */
    private final Set<String> classesContenues;

    /**
     * Sous package contenu dans le package
     */
    private final Set<Package> sousPackages;

    /**
     * Nom du package
     */
    private final String nom;

    /**
     * Permet de savoir est ce que le package est cense etre ouvert dans l'arborescence ou non
     */
    private boolean ouvert;

    //CONSTRUCTEUR

    /**
     * Constructeur du Package
     * Construit un package vide
     *
     * @param pNom nom du package
     */
    public Package(String pNom) {
        nom = pNom;
        classesContenues = new HashSet<>();
        sousPackages = new HashSet<>();
    }

    /**
     * Cree une copie du package
     * Tous les sous package sont egalement copier
     * Les ObjectClasse reste les memes objects
     *
     * @param packageACopier package dont on veut construire une copie
     * @throws NullPointerException erreur renvoye si packageACopie vaut null
     */
    public Package(Package packageACopier) {
        if (packageACopier != null) { // si le package a copier n'est pas null
            nom = packageACopier.nom;
            classesContenues = new HashSet<>(packageACopier.classesContenues);
            sousPackages = new HashSet<>();
            for (Package p : packageACopier.sousPackages) {
                sousPackages.add(new Package(p));
            }
            ouvert = packageACopier.ouvert;
        } else { // si null, on retourne une exception
            throw new NullPointerException();
        }
    }

    //METHODES

    /**
     * methode qui permet d'ajouter un sous package au package
     *
     * @param p package fournit
     */
    public void ajouterSousPackage(Package p) {
        if (p != null) {
            sousPackages.add(p);
        }
    }

    /**
     * methode qui permet d'ajouter une classe au package
     *
     * @param s nom complet de l'objectClasse a ajouter
     */
    public void ajouterClasse(String s) {
        classesContenues.add(s);
    }

    /**
     * methode qui permet de retirer un sous package du package
     *
     * @param p package a supprimer
     */
    public void retirerSousPackage(Package p) {
        sousPackages.remove(p);
    }

    /**
     * methode qui permet de retirer une classe du package
     *
     * @param s nom complet de l'objectClasse a supprimer
     */
    public void retirerClasse(String s) {
        classesContenues.remove(s);
    }

    /**
     * methode qui permet de verifier l'egalite entre this et un objet fourni
     * Ils sont considerer egaux si ils ont le meme nom et contiennent les memes choses
     *
     * @param o objet founit
     * @return True si et seulement si les deux objets sont egaux
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Package aPackage = (Package) o;
        return aPackage.classesContenues.containsAll(classesContenues) && aPackage.sousPackages.containsAll(sousPackages) && Objects.equals(nom, aPackage.nom);
    }

    /**
     * methode qui retourne le hashcode de l'objet
     *
     * @return entier gal a un autre hashCode si et seulement si les deux sont egaux
     */
    @Override
    public int hashCode() {
        return Objects.hash(classesContenues, sousPackages, nom);
    }


    //GETTERS ET SETTERS

    /**
     * getter du nom du package
     *
     * @return nom du package
     */
    public String getNom() {
        return nom;
    }

    /**
     * getter des sous packages
     *
     * @return sous packages
     */
    public Set<Package> getSousPackages() {
        return sousPackages;
    }

    /**
     * getter des classes contenues dans le package
     *
     * @return classes contenues dans le package
     */
    public Set<String> getClassesContenues() {
        return classesContenues;
    }

    /**
     * permet d'inverser la position fermer ou ouverte du package
     */
    public void changerOuverture() {
        ouvert = !ouvert;
    }

    /**
     * getter de l'ouverture
     *
     * @return booleen ouvert, vrai si il si il est cense etre ouvert
     */
    public boolean isOuvert() {
        return ouvert;
    }

    /**
     * Affichage string d'un package
     *
     * @return String
     */
    @Override
    public String toString() {
        return "Package{" +
                "classesContenues=" + classesContenues +
                ", sousPackages=" + sousPackages.size() +
                ", nom='" + nom + '\'' +
                ", ouvert=" + ouvert +
                '}';
    }

}
