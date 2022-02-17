package modele;

import introspection.FacadeIntrospection;
import modele.classe.Attribut;
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
     * Object qui contient les differents ObjectClasse chargee
     * Contient egalement l'arborescence
     */
    private CollectionObjectClasse collectionObjectClasse;

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

    /**
     * Liste des fleches actuellement represente
     */
    private List<FlecheAssociation> associations;


    //Contructeurs


    /**
     * Contructeur de Modele
     * initialise les attributs de modeles
     */
    public Modele(){
        collectionObjectClasse = new CollectionObjectClasse();
        selection = new LinkedList<>();
        afficherPackage = true;
        ctrlActive = false;
        dossierCourant = null; // pas de dossier chargee de base
        associations = new LinkedList<>();
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
        collectionObjectClasse.reintialiserDonnee(); //on vide toute les classes chargee en remplaçant toute celle deja charge
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
                    throw new IllegalStateException("Erreur pas encore traitee");
                }

                collectionObjectClasse.ajouterObjectClasse(o);

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
     * Methode qui va charger de maniere recursive tous les fichiers .class qui sont accessible depuis le fichier fournit
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

                if (fils == null) throw new IllegalStateException("f est cense etre un repertoire"); //Erreur suppose impossible

                //on parcours les fils
                for (File enfant:fils) {
                    chargerArborescenceProjetRecursif(enfant);//on appelle recursivement sur tout les enfants
                }
            }

        }
    }



    /**
     * Methode qui permet d'ajouter un objectClasse chargee au diagramme
     * @param objectClasse objectClasse a ajouter au diagramme
     * @param x position sur l'axe des abscisses de la classe sur le diagramme lors de l'ajout
     * @param y position sur l'axe des ordonnees de la classe sur le diagramme lors de l'ajout
     */
    public void ajouterClasse(ObjectClasse objectClasse, int x, int y ){

        assert collectionObjectClasse.verifierClasseCharge(objectClasse) : "La classe doit etre chargee";
        //on verifie que la classe est chargee

        //on change les options de la classe en elle meme
        objectClasse.changerVisibilite(true);
        objectClasse.setPosition(x,y);

        //on change les options liees aux associations

        //fleches dont on est la source
        //on parcours les types des attributs
        for (Attribut a:objectClasse.getAttributs()) {

            //on verifie si le type correspond a une classe chargee
            ObjectClasse o = collectionObjectClasse.getObjectClasse( a.getTypeAttribut());
            if (o != null){
                //la classe est chargee

                if (o.isVisible()){
                    //elle est dans le diagramme
                    transformerEnFleche(objectClasse, a);
                }
            }
        }

        //a faire fleches dont on est la destination

        notifierObservateurs();
    }

    /**
     * Methode pour deplacer une selection de classe sur le diagramme
     * @param x deplacement sur l'axe des abscisses
     * @param y deplacement sur l'axe des ordonnees
     */
    public void deplacerClasseSelectionne(int x, int y){

        for (ObjectClasse objectClasse: selection) {//on parcours les objects classes selectionne

            if (collectionObjectClasse.verifierClasseCharge(objectClasse)) {//on verifie que la classe est chargee
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

            if (collectionObjectClasse.verifierClasseCharge(objectClasse)) {//on verifie que la classe est chargee

                //on retire les fleches liees

                //on parcourt toutes les fleches
                for (FlecheAssociation f: associations) {
                    //si la fleche est liee
                    if (f.getDest().equals(objectClasse) || f.getSrc().equals(objectClasse)){
                        //on la transforme en attribut (on "supprime" la fleche)
                        transformerEnAttribut(f);
                    }
                }

                //on retire du diagramme la classe courante
                objectClasse.changerVisibilite(false);
                notifierObservateurs();
            }
        }
    }


    /**
     * Methode qui permet de decharger les classes selectionnees
     */
    public void dechargerClasseSelectionne(){

        //on retire du diagramme les classe
        retirerClasseSelectionne();

        collectionObjectClasse.dechargerListeClasse(selection);

        //on vide la selection puisqu'on a decharge tout ce qui etais selectionne
        deselectionner();

        notifierObservateurs();
    }

    /**
     * Methode qui permet de transformer un attribut d'une classe en une fleche d'association
     * Si la destination de la fleche n'est pas trouve ou pas affiche, on ne fait rien
     * @param objectClasse objectClasse d'ou viens l'attribut
     * @param attribut attribut a transformer en fleche d'association
     */
    public void transformerEnFleche(ObjectClasse objectClasse, Attribut attribut){

        //on verifie que tout est correct
        assert objectClasse.getAttributs().contains(attribut) : "L'attribut doit etre un attribut de l'object classe";
        assert objectClasse.isVisible() : "L'object classe doit etre affiche dans le diagramme";
        assert attribut.isVisible() : "l'attribut ne doit pas etre masque";

        ObjectClasse dest = collectionObjectClasse.getObjectClasse(attribut.getTypeAttribut());
        if (dest != null && dest.isVisible()) {
            associations.add(new FlecheAssociation(objectClasse, dest, attribut));
            attribut.changerVisibilite(false);
        }
    }

    /**
     * Methode qui permet de transformer une fleche d'association en attribut
     * Si l'attribut n'est pas trouve on ne fait rien
     * @param association fleche d'association a transformer
     */
    public void transformerEnAttribut(FlecheAssociation association){
        ObjectClasse source = association.getSrc();
        ObjectClasse dest = association.getDest();

        boolean trouve  =false;
        for (Attribut a: source.getAttributs()) {
            if (a.getTypeAttribut().equals(dest.getNomObjectClasse()) && a.getNomAttribut().equals(association.getNom())){
                a.changerVisibilite(true);
                trouve = true;
            }
        }
        if (trouve) associations.remove(association);
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
     * getter des packages
     * Source des packages du projet
     * Contient toute les clees vers les classes chargee
     * @return copie du package src
     */
    public Package getPackages() {
        return collectionObjectClasse.getPackages();
    }

    /**
     * getter des classes chargee
     * @return collection d'ObjectClasse chargee
     */
    public Collection<ObjectClasse> getObjectClasses(){
        return collectionObjectClasse.getClassesChargees();
    }

    /**
     * getter d'un objectClasse a partir de son nom complet
     * @param cheminComplet nom complet compose de son chemin en package et de son nom separer par un point
     * @return ObjectClasse correspondant ou null
     */
    public ObjectClasse getObjectClasse(String cheminComplet){
        return collectionObjectClasse.getObjectClasse(cheminComplet);
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