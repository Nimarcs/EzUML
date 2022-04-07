package modele;

import modele.classe.ObjectClasse;
import vue.AffichageErreur;

import java.io.Serializable;
import java.util.*;

/**
 * Classe CollectionObjectClasse
 * <p>
 * Représente une collection des objets classes qui sont chargées
 */
public class CollectionObjectClasse implements Serializable {

    /**
     * Source des packages du projet
     * les clee vers les differents objectClasse chargee sont dans les different sous packages et sous package
     */
    private Package src;

    /**
     * Map qui stocke les classes chargee
     * la clee correspond au chemin des packages et le nom de la classe separe par des points
     * Les classes elles meme contiennent l'information sachant si elles sont visibles
     * <p>
     * Exemple : java.lang.Integer => ObjectClasse correspondant
     */
    private Map<String, ObjectClasse> classesChargees;

    /**
     * Constructeur de CollectionObjectClasse
     * <p>
     * création du package src qui contiendra tout les autres packages
     */
    public CollectionObjectClasse() {
        this.src = new Package("src");
        this.classesChargees = new HashMap<>();
    }

    /**
     * methode qui permet de reinitialiser les donnees
     */
    public void reintialiserDonnee() {
        src = new Package("src");
        //on vide toute les classes chargee en remplaçant toute celle deja charge
        this.classesChargees = new HashMap<>();
    }

    /**
     * Methode qui permet d'ajouter un objectClasse dans la collection
     *
     * @param o objectClasse a ajouter
     */
    public void ajouterObjectClasse(ObjectClasse o) {
        assert o != null;

        //Classe chargee
        classesChargees.put(o.getNomObjectClasse(), o);

        //Package
        //on verifie le package auquel l'objectClasse appartient
        String[] nomPackages = o.getPackageObjectClasse().split("\\.");

        //on part de la racine
        Package pCourant = src;

        //pour chaque package cite dans l'objectClasse
        for (String nomPackage : nomPackages) {

            //on regarde si un des sous-packages a un nom correspondant
            boolean trouve = false;
            Iterator<Package> iterator = pCourant.getSousPackages().iterator();
            Package p;

            while (!trouve && iterator.hasNext()) {
                p = iterator.next();

                //si on en trouve un
                if (p.getNom().equals(nomPackage)) {
                    //on arrete de chercher
                    trouve = true;
                    //on deplace notre pCourant au package trouve
                    pCourant = p;
                }
            }

            //si on n'en trouve aucun on le creer et continue
            if (!trouve) {
                Package aPackage = new Package(nomPackage);
                pCourant.ajouterSousPackage(aPackage);
                //on deplace notre pCourant au package creer
                pCourant = aPackage;
            }
        }

        //on l'ajoute dans notre collection de classe charge dans le package correct
        pCourant.ajouterClasse(o.getNomObjectClasse());
    }

    /**
     * Methode qui permet d'obtenir le package dans lequel la clee du package est contenue
     * Fournit le package reel donc permet de le modifier
     * Utilise les packages pour chercher une classe au bon endroit
     *
     * @param objectClasse objectClasse dont on veut obtenir le package
     * @return package qui contient l'objectClasse s'il existe un objectClasse charge positionne correctement, null sinon
     */
    private Package trouvePackageDeClasseLocal(ObjectClasse objectClasse) {
        //on verifie le package auquel l'objectClasse appartient
        String[] nomPackages = objectClasse.getPackageObjectClasse().split("\\.");

        //on part de la racine
        Package pCourant = src;

        //pour chaque package cite dans l'objectClasse
        for (String nomPackage : nomPackages) {
            //on regarde si un des sous-packages a un nom correspondant
            boolean trouve = false;
            Iterator<Package> iterator = pCourant.getSousPackages().iterator();
            Package p;

            while (!trouve && iterator.hasNext()) {
                p = iterator.next();

                //si on en trouve un
                if (p.getNom().equals(nomPackage)) {
                    //on arrete de chercher
                    trouve = true;
                    //on deplace notre pCourant au package trouve
                    pCourant = p;
                }
            }

            //si on n'en trouve aucun la classe n'est pas charge
            if (!trouve) {
                return null;
            }
        }
        //on verifie que le package contient bien la classe charge
        if (pCourant.getClassesContenues().contains(objectClasse.getNomObjectClasse())) {
            //si c'est le cas on renvoie le package qui la contient
            return pCourant;
        }
        //sinon on retourne null
        return null;
    }

    /**
     * Methode qui permet d'obtenir le package dans lequel la clee du package est contenue
     * Fournit le package reel donc permet de le modifier
     * Utilise les packages pour chercher une classe au bon endroit
     *
     * @param objectClasse objectClasse dont on veut obtenir le package
     * @return package qui contient l'objectClasse s'il existe un objectClasse charge positionne correctement, null sinon
     */
    private Package trouvePackageDeClasseLocalAvecPackage(ObjectClasse objectClasse, Package p) {
        //on verifie le package auquel l'objectClasse appartient
        String[] nomPackages = objectClasse.getNomObjectClasse().split("\\.");

        //on part de la racine
        Package pCourant = src;
        //pour chaque package cite dans l'objectClasse
        for (String nomPackage : nomPackages) {
            for (Package sousPackage : pCourant.getSousPackages()) {
                if (sousPackage.getNom().equals(p.getNom())) return sousPackage;
                if (sousPackage.getNom().equals(nomPackage)) {
                    pCourant = sousPackage;
                }
            }
        }
        return null;
    }

    /**
     * Methode qui permet d'obtenir le package dans lequel la clee du package est contenue
     * Utilise les packages pour chercher une classe au bon endroit
     *
     * @param objectClasse objectClasse dont on veut obtenir le package
     * @return package qui contient l'objectClasse s'il existe un objectClasse charge positionne correctement, null sinon
     */
    private Package trouvePackageDeClasse(ObjectClasse objectClasse) {
        Package p = trouvePackageDeClasseLocal(objectClasse);
        if (p == null) return null;
        else return new Package(p);
    }


    /**
     * Methode qui permet de verifier si une classe est chargee
     *
     * @param objectClasse object classe dont on veut verifier si elle est chargee
     * @return booleen vrai si il existe un objectClasse charge positionne correctement, faux sinon
     */
    public boolean verifierClasseCharge(ObjectClasse objectClasse) {
        return classesChargees.containsValue(objectClasse);
    }

    /**
     * Methode qui permet de changer l'ouverture du Package en paramètre
     *
     * @param p
     */
    public void changerOuverture(Package p) {
        //on veut avoir le package originel associé à p
        Package pOriginel = trouverPackageOriginel(src, p);
        if (pOriginel == null)
            AffichageErreur.getInstance().afficherErreur("le package originel n'a pas été retrouvé ");

        else
            // methode de package qui ouvert graphiquement le package
            pOriginel.changerOuverture();
    }

    /**
     * Methode qui permet de trouver le package Originel
     *
     * @param src
     * @param p
     * @return
     */
    private Package trouverPackageOriginel(Package src, Package p) {
        //Iterateur de toutes les classes contenues dans src
        Iterator<String> ite = src.getClassesContenues().iterator();
        if (ite.hasNext())
            // methode qui permet d'obtenir le package dans lequel la clee du package est contenue
            return trouvePackageDeClasseLocalAvecPackage(classesChargees.get(ite.next()), p);
        else {
            //boucle qui regarde les sous-packages et qui relance la methode pour trouver le package parent
            for (Package fils : src.getSousPackages()) {
                Package aPackage = trouverPackageOriginel(fils, p);
                if (aPackage != null) return aPackage;
            }
        }
        return null;
    }


    /**
     * Methode qui permet de decharger les classes fournies
     *
     * @param lo
     */
    public void dechargerListeClasse(List<ObjectClasse> lo) {

        for (ObjectClasse objectClasse : lo) {
            //on decharge la classe

            //on la retire des packages
            Package p = trouvePackageDeClasseLocal(objectClasse);
            if (p != null) {
                p.getClassesContenues().remove(objectClasse.getNomObjectClasse());
            }

            //on la retire de la map
            classesChargees.remove(objectClasse.getNomObjectClasse());
        }
        //on nettoie les packages dont on a plus besoin
        nettoyerPackage(src);
    }

    /**
     * permet de retirer tous les packages vide
     *
     * @param p package a partir duquel commencer la recherche
     */
    private void nettoyerPackage(Package p) {
        assert p != null;
        //boucle qui regarde les sous packages de p
        for (Package sousPackage : p.getSousPackages()) {
            nettoyerPackage(sousPackage);
            // retire un sous package lorsque le sous package ne contient plus de Classe et lorsqu'il ne contient plus de package
            if (sousPackage.getClassesContenues().isEmpty() && sousPackage.getSousPackages().isEmpty()) {
                p.getSousPackages().remove(sousPackage);
            }
        }
    }

    /**
     * getteur d'un objectClasse
     *
     * @param cheminComplet chemin des packages et le nom de la classe separe par des points, clee de le map
     * @return ObjectClasse correspondant ou null
     */
    public ObjectClasse getObjectClasse(String cheminComplet) {
        return classesChargees.getOrDefault(cheminComplet, null);
    }

    /**
     * getteur d'un package
     *
     * @return nouveau Package src
     */
    public Package getPackages() {
        return new Package(src);
    }

    /**
     * getteur des ObjectClasses qui sont charges
     *
     * @return ObjectClasse contenue dans l'attribut classesChargees
     */
    public Collection<ObjectClasse> getClassesChargees() {
        return classesChargees.values();
    }


}
