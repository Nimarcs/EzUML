package modele;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
     * Ensemble des classes chargee dans le programme
     * Les classes elle meme contiennent l'information sachant si elles sont visibles
     */
    private Set<ObjectClasse> classes;

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
    private File dossierProjet;

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
        classes = new HashSet<>();
        selection = new LinkedList<>();
        afficherPackage = true;
        ctrlActive = false;
        dossierProjet = null; // pas de dossier chargee de base
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
     * Methode qui permet de deplacer la ou les classes selectionnee de x et y
     * @param x deplacement sur l'axe des abscisses
     * @param y deplacement sur l'axe des ordonnees
     */
    public void deplacerSelection(int x, int y){
        throw new IllegalStateException("PAS FAIT");
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
        classes.clear(); //on vide toute les classes chargee
        deselectionner(); // on deselectionne tout
        chargerArborescenceProjet(dossierProjet); //on recharger le fichier charge en cours
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
                ObjectClasse o = facade.instropectionClasse(f);

                //on l'ajoute dans notre collection de classe charge
                classes.add(o);
            }
        }
    }

    /**
     * methode qui permet de charger l'arborescence et les different fichier la composant
     * @param f fichier a tester
     */
    public void chargerArborescenceProjet(File f){
        throw new IllegalStateException("PAS FAIT");
        /*
        notifierObservateurs();
        dossierProjet = f;

         */
    }


    //getters setters


    public boolean isCtrlActive() {
        return ctrlActive;
    }

    public Set<ObjectClasse> getClasses() {
        return classes;
    }

    public File getDossierProjet() {
        return dossierProjet;
    }

    public List<ObjectClasse> getSelection() {
        return selection;
    }

    public void setCtrlActive(boolean ctrlActive) {
        this.ctrlActive = ctrlActive;
    }
}