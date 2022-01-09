package modele;

import modele.classe.ObjectClasse;

import java.util.*;

/**
 * Classe representant un package
 * genere par le modele avec les objects classe que l'on lui fourni
 * @author Marcus RICHIER
 */
public class Package{


    //attributs


    /**
     * Classe contenue dans le package
     */
    private final Set<ObjectClasse> classesContenues;

    /**
     * Sous package contenu dans le package
     */
    private final Set<Package> sousPackages;

    /**
     * Nom du package
     */
    private final String nom;


    //Constructeur


    /**
     * Constructeur du Package
     * Construit un package vide
     * @param pNom nom du package
     */
    public Package(String pNom){
        nom =pNom;
        classesContenues = new HashSet<>();
        sousPackages = new HashSet<>();
    }

    /**
     * Cree une copie du package
     * Tous les sous package sont egalement copier
     * Les ObjectClasse reste les memes objects
     * @param packageACopier package dont on veut construire une copie
     * @throws NullPointerException erreur renvoye si packageACopie vaut null
     */
    public Package(Package packageACopier){
        if (packageACopier != null) {
            nom = packageACopier.nom;
            classesContenues = new HashSet<>(packageACopier.classesContenues);
            sousPackages = new HashSet<>();
            for (Package p: packageACopier.sousPackages) {
                sousPackages.add(new Package(p));
            }
        } else {
            throw new NullPointerException();
        }
    }


    //methode


    /**
     * methode qui permet d'ajouter un sous package au package
     * @param p package fournit
     */
    public void ajouterSousPackage(Package p){
        if (p != null){
            sousPackages.add(p);
        }
    }

    /**
     * methode qui permet d'ajouter une classe au package
     * @param c ObjectClasse fournit
     */
    public void ajouterClasse(ObjectClasse c){
        if (c != null){
            classesContenues.add(c);
        }
    }

    /**
     * methode qui permet de retirer un sous package du package
     * @param p package a supprimer
     */
    public void retirerSousPackage(Package p){
        sousPackages.remove(p);
    }

    /**
     * methode qui permet de retirer une classe du package
     * @param c ObjectClasse a supprimer
     */
    public void retirerClasse(ObjectClasse c){
        classesContenues.remove(c);
    }

    /**
     * methode qui permet de verifier l'egalite entre this et un objet fourni
     * Ils sont considerer egaux si ils ont le meme nom et contiennent les memes choses
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
     * @return entier gal a un autre hashCode si et seulement si les deux sont egaux
     */
    @Override
    public int hashCode() {
        return Objects.hash(classesContenues, sousPackages, nom);
    }


    //getter et setter

    /**
     * getter du nom du package
     * @return nom du package
     */
    public String getNom() {
        return nom;
    }

    /**
     * getter des sous packages
     * @return sous packages
     */
    public Set<Package> getSousPackages() {
        return sousPackages;
    }

    /**
     * getter des classes contenues dans le package
     * @return classes contenues dans le package
     */
    public Set<ObjectClasse> getClassesContenues() {
        return classesContenues;
    }


}