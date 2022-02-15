package modele;

import introspection.FacadeIntrospection;
import modele.classe.ObjectClasse;

import java.io.File;
import java.net.MalformedURLException;
import java.util.*;

/**
 * Modele permet de mettre en commun les differents elements du logiciel
 * Contient ce qui est necessaire pour armoniser le diagramme, l'arborescence et les differents controleurs et vues
 * @author Marcus RICHIER
 */
public class Modele extends Sujet {


    //Attributs


    /**
     * Booleen a vrai si et seulement si la touche controle du clavier est en cours d'appui
     * Permet d'effectuer des combinaisons de touche comme ctrl+clic pour ajouter des elements a la selection
     */
    private boolean ctrlActive;

    /**
     * Source des packages du projet
     * Ensemble des classes chargees dans le programme sont dans les different sous packages et sous package
     * Les classes elles meme contiennent l'information sachant si elles sont visibles
     */
    private Package src;

    /**
     * Booleen a vrai si on affiche les packages dans lesquels sont les classes sous forme textuelle, faux sinon
     */
    private boolean afficherPackage;

    /**
     * Liste des classes selectionne au moment courant
     * On ne peut selectionner que des classes
     */
    private List<ObjectClasse> selection;

    /**
     * Fichier charge couramment, sert a l'arboresance
     */
    private File dossierCourant;

    /**
     * Facade qui permet d'obtenir les ObjectClasse a partir des fichiers
     */
    private FacadeIntrospection facade;


    //Contructeurs


    /**
     * Contructeur de Modele
     * initialise les attributs de modeles
     */
    public Modele(){
        src = new Package("src");
        selection = new LinkedList<>();
        afficherPackage = true;
        ctrlActive = false;
        dossierCourant = null; // pas de dossier chargee de base
    }


    //Methodes

    /**
     * methode qui permet de selectionner une classe
     * Si la classe est deja selectionne, la deselectionne
     * @param objectClasse ObjectClasse a selectionner ou deselectionner
     */
    public void selectionnerUneClasse(ObjectClasse objectClasse){
        //on regarde si la classe est selectionner
        if (selection.contains(objectClasse)){
            selection.remove(objectClasse);
            //si elle est selectionne, la deselectionne
        } else {
            //sinon la selectionne
            selection.add(objectClasse);
        }
        notifierObservateurs();
    }

    /**
     * Methode qui vide la selection
     */
    public void deselectionner(){
       selection.clear();
       notifierObservateurs();
    }


    /**
     * methode qui permet d'alterner entre masquer ou afficher les packages sous forme textuelle
     */
    public void changerAffichagePackage(){
        afficherPackage = !afficherPackage;
        notifierObservateurs();
    }

    /**
     * methode qui permet de reinitialiser le diagramme
     */
    public void reintialiserDiagramme(){
        src = new Package("src"); //on vide toute les classes chargee en remplaçant toute celle deja charge
        deselectionner(); // on deselectionne tout
        chargerArborescenceProjet(dossierCourant); //on recharger le fichier charge en cours
    }

    /**
     * Methode qui permet de charger une classe en lui donnant un fichier
     * @param f fichier a charger
     */
    private void chargerClasse(File f){
        //si le fichier est pas null
        if (f != null){

            //si le fichier est un .class
            if(f.isFile() && f.getName().contains(".class")) {

                //on recupere l'objectClasse
                ObjectClasse o = null;
                try {
                    o = facade.introspectionClasse(f);
                } catch (MalformedURLException e) {
                    throw new IllegalStateException("Erreur pas encore traitée");
                }

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
                pCourant.ajouterClasse(o);
            }
        }
    }

    /**
     * Methode qui permet de charger l'arborescence et les differents fichier le composant
     * Appelle la methode privee adapte et change le dossier projet
     * @param f fichier a tester
     */
    public void chargerArborescenceProjet(File f){
        //on apelle la methode privee qui charge tous les fichiers depuis le fichier donnee
        chargerArborescenceProjetRecursif(f);

        //on modifie quel est le dernier dossier charge du projet
        dossierCourant = f;
        notifierObservateurs();
    }

    /**
     * Methode qui va charger de manière recursive tous les fichiers .class qui sont accessible depuis le fichier fournit
     *
     * Si le fichier est un repertoire, on appele recursivement tous les fichiers fils qu'il contient
     * Sinon on essaie de le charger en tant que classe
     *
     * @param f fichier fourni
     */
    private void chargerArborescenceProjetRecursif(File f){
        if (f == null) return; //si c'est null on ne fait rien

        //si c'est un fichier on tente de la charger
        if (f.isFile()){
            chargerClasse(f);
        } else {
            //sinon on regarde si c'est un repertoire
            if (f.isDirectory()){
                //on recupere les fils
                File[] fils = f.listFiles();

                if (fils == null) throw new IllegalStateException("f est censé etre un repertoire"); //Erreur suppose impossible

                //on parcours les fils
                for (File enfant:fils) {
                    chargerArborescenceProjet(enfant);//on appelle recursivement sur tout les enfants
                }
            }

        }
    }

    /**
     * Methode qui permet de verifier si un ObjectClasse est charge
     * Utilise les packages pour chercher une classe au bon endroit
     * @param objectClasse objectClasse dont on veut verifier le chargement
     * @return package qui contient l'objectClasse s'il existe un objectClasse charge positionne correctement, null sinon
     */
    private Package trouvePackageDeClasse(ObjectClasse objectClasse){
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
        if(pCourant.getClassesContenues().contains(objectClasse)){
            //si c'est le cas on renvoie le package qui la contient
            return pCourant;
        }
        //sinon on retourne null
        return null;
    }

    /**
     * methode qui permet de verifier si une classe est chargee
     * @param objectClasse object classe dont on veut verifier si elle est chargee
     * @return booleen vrai si il existe un objectClasse charge positionne correctement, faux sinon
     */
    private boolean verifierClasseCharge(ObjectClasse objectClasse){
        return trouvePackageDeClasse(objectClasse) != null;
    }

    /**
     * Methode qui permet d'ajouter un objectClasse chargee au diagramme
     * @param objectClasse objectClasse a ajouter au diagramme
     * @param x position sur l'axe des abscisses de la classe sur le diagramme lors de l'ajout
     * @param y position sur l'axe des ordonnees de la classe sur le diagramme lors de l'ajout
     */
    public void ajouterClasse(ObjectClasse objectClasse, int x, int y ){

        if(verifierClasseCharge(objectClasse)){ //on verifie que la classe est chargee
            objectClasse.changerVisibilite(true);
            objectClasse.setPosition(x,y);
            notifierObservateurs();
        }

    }

    /**
     * Methode pour deplacer une selection de classe sur le diagramme
     * @param x deplacement sur l'axe des abscisses
     * @param y deplacement sur l'axe des ordonnees
     */
    public void deplacerClasseSelectionne(int x, int y){

        for (ObjectClasse objectClasse: selection) {//on parcours les objects classes selectionne

            if (verifierClasseCharge(objectClasse)) {//on verifie que la classe est chargee
                //On deplace la classe courante
                objectClasse.deplacer(x, y);
                notifierObservateurs();
            }
        }
    }

    /**
     * Methode qui permet de retirer du diagramme des classes chargees
     */
    public void retirerClasseSelectionne(){

        for (ObjectClasse objectClasse: selection) {//on parcours les objects classes selectionne

            if (verifierClasseCharge(objectClasse)) {//on verifie que la classe est chargee
                //on retire du diagramme la classe courante
                objectClasse.changerVisibilite(false);
                notifierObservateurs();
            }
        }
    }

    public void dechargerClasse(ObjectClasse objectClasse){
        //ObjectClasse est deja decharge puisqu'il n'existe pas
        if (objectClasse == null) return;

        //on decharge la classe
        trouvePackageDeClasse(objectClasse).getClassesContenues().remove(objectClasse);
        notifierObservateurs();

    }


    //getters setters

    /**
     * getter de ctrlActive
     * @return vrai si ctrl est active, faux sinon
     */
    public boolean isCtrlActive() {
        return ctrlActive;
    }

    /**
     * getter de src
     * Source des packages du projet
     * Contient toute les classe chargee
     * @return copie du package src
     */
    public Package getSrc() {
        return new Package(src);
    }

    /**
     * getter du dossier courant
     * @return dernier repertoire/fichier charge
     */
    public File getDossierCourant() {
        return dossierCourant;
    }

    /**
     * getter de la selection
     * @return selection courante
     */
    public List<ObjectClasse> getSelection() {
        return selection;
    }

    /**
     * setter de ctrlActive
     * @param ctrlActive nouvelle valeur de ctrlActive
     */
    public void setCtrlActive(boolean ctrlActive) {
        this.ctrlActive = ctrlActive;
    }

    /**
     * getter de afficherPackage
     * @return booleen vrai si on affiche les packages, faux sinon
     */
    public boolean isAfficherPackage() {
        return afficherPackage;
    }
}