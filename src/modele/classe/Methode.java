// PACKAGE
package modele.classe;

// IMPORTS
import java.util.List;

/**
 * Classe Methode
 *
 * Represente une methode au sein d'une classe
 * Contient l'information concernant son nom, son statut, son type de retour et ses parametres
 */
public class Methode {

    // ATTRIBUTS

    /**
     * Attribut prive String contenant le nom de la methode
     */
    private String nomMethode;

    /**
     * Attribut prive Statut definissant le statut de la methode
     */
    private Statut statutMethode;

    /**
     * Attribut prive String contenant le type de retour, null si le retour est void
     */
    private String typeRetour;

    /**
     * Attribut prive liste de String contenant la liste des types de parametres
     */
    private List<String> listeParametres;

    /**
     * Attribut prive booleen indiquant si la methode est abstraite ou non
     */
    private boolean abstrait;

    /**
     * Attribut prive booleen indiquant si la methode est statique ou non
     */
    private boolean statique;

    /**
     * Attribut prive booleen indiquant si la methode est finale ou non
     */
    private boolean finale;

    /**
     * Attribut prive booleen indiquant la visibilite de la methode dans la zone d'affichage du diagramme
     */
    private boolean visible;

    // CONSTRUCTEURS


    // METHODES


    // GETTERS && SETTERS


}