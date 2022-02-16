package modele;

import modele.classe.ObjectClasse;

import java.util.*;

public class CollectionObjectClasse {

    /**
     * Source des packages du projet
     * les clee vers les differents objectClasse chargee sont dans les different sous packages et sous package
     */
    private Package src;

    /**
     * Map qui stocke les classes chargee
     * la clee correspond au chemin des packages et le nom de la classe separe par des points
     * Les classes elles meme contiennent l'information sachant si elles sont visibles
     *
     * Exemple : java.lang.Integer => ObjectClasse correspondant
     */
    private Map<String, ObjectClasse> classesChargees;

    /**
     * Constructeur de CollectionObjectClasse
     */
    public CollectionObjectClasse(){
        this.src = new Package("src");
        this.classesChargees = new HashMap<>();
    }

    /**
     * methode qui permet de reinitialiser les donnees
     */
    public void reintialiserDonnee(){
        src = new Package("src"); //on vide toute les classes chargee en remplaçant toute celle deja charge
        this.classesChargees = new HashMap<>();
    }

    /**
     * Methode qui permet d'ajouter un objectClasse dans la collection
     * @param o objectClasse a ajouter
     */
    public void ajouterObjectClasse(ObjectClasse o){
        assert o != null;

        //Classe chargee
        classesChargees.put(o.getNomComplet(), o);

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
        pCourant.ajouterClasse(o.getNomComplet());
    }

    /**
     * Methode qui permet d'obtenir le package dans lequel la clee du package est contenue
     * Utilise les packages pour chercher une classe au bon endroit
     * @param objectClasse objectClasse dont on veut obtenir le package
     * @return copie du package qui contient l'objectClasse s'il existe un objectClasse charge positionne correctement, null sinon
     */
    public Package trouvePackageDeClasse(ObjectClasse objectClasse){
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
        if(pCourant.getClassesContenues().contains(objectClasse.getNomComplet())){
            //si c'est le cas on renvoie le package qui la contient
            return new Package(pCourant);
        }
        //sinon on retourne null
        return null;
    }

    /**
     * methode qui permet de verifier si une classe est chargee
     * @param objectClasse object classe dont on veut verifier si elle est chargee
     * @return booleen vrai si il existe un objectClasse charge positionne correctement, faux sinon
     */
    public boolean verifierClasseCharge(ObjectClasse objectClasse){
        return classesChargees.containsValue(objectClasse);
    }

    /**
     * Methode qui permet de décharger les classes fournies
     */
    public void dechargerListeClasse(List<ObjectClasse> lo){

        for (ObjectClasse objectClasse:lo) {

            //on decharge la classe

            //on la retire des packages
            Package p = trouvePackageDeClasse(objectClasse);
            if (p != null) {
                p.getClassesContenues().remove(objectClasse.getNomComplet());
            }

            //on la retire de la map
            classesChargees.remove(objectClasse.getNomComplet());
        }
    }

    /**
     * getter d'un objectClasse
     * @param cheminComplet chemin des packages et le nom de la classe separe par des points, clee de le map
     * @return ObjectClasse correspondant ou null
     */
    public ObjectClasse getObjectClasse(String cheminComplet){
        return classesChargees.getOrDefault(cheminComplet, null);
    }

    public Package getPackages() {
        return new Package(src);
    }

    public Collection<ObjectClasse> getClassesChargees(){
        return classesChargees.values();
    }


}
