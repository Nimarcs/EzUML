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
     * Attribut prive String contenant le type de retour, null si c'est un cosntructeur
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

    /**
     * Methode qui retourne le texte permettant l'affichage de la methode au sein de la zone d'affichage de diagrammes de classe
     * L'affiche est composé du symbole representant son statut (+, -, #) puis de son nom, de ses parametres et pour finir de son type de retour si non null/void
     * @return String, composé textuel d'affichage de la methode
     */
    public String afficher() {
        String res = "";
        if (this.statutMethode == Statut.PUBLIC) res += "+ ";
        else if (this.statutMethode == Statut.PRIVATE) res += "- ";
        else res += "# ";
        res += this.nomMethode + "(";
        for (int i = 0; i < this.listeParametres.size(); i++) {
            res += this.listeParametres.get(i);
            if (i != this.listeParametres.size() - 1) res += ", ";
        }
        res += ")";
        if (this.typeRetour != null || !this.typeRetour.equals("void")) res += " : " + this.typeRetour;
        return res;
    }

    /**
     * Methode setter qui permet de changer la visibilite de la methode
     * @param visibilite booleen: parametre indiquant la nouvelle visibilite de la methode
     */
    public void changerVisibilite(boolean visibilite) {
        this.visible = visibilite;
    }

    // GETTERS && SETTERS

    /**
     * Getter sur le nom de la methode
     * @return String
     */
    public String getNomMethode() {
        return this.nomMethode;
    }

    /**
     * Getter sur le statut de la methode
     * @return Statut
     */
    public Statut getStatutMethode() {
        return this.statutMethode;
    }

    /**
     * Getter sur le type de retour de la methode
     * si null, c'est un constructeur
     * @return String
     */
    public String getTypeRetour() {
        return this.typeRetour;
    }

    /**
     * Getter sur la liste des parametres
     * @return List<String>
     */
    public List<String> getListeParametres() {
        return this.listeParametres;
    }

    /**
     * Getter sur l'etat abstrait de la methode ou non
     * @return booleen
     */
    public boolean isAbstrait() {
        return this.abstrait;
    }

    /**
     * Getter sur l'etat statique de la methode ou non
     * @return booleen
     */
    public boolean isStatique() {
        return this.statique;
    }

    /**
     * Getter sur l'etat final de la methode ou non
     * @return booleen
     */
    public boolean isFinale() {
        return this.finale;
    }

    /**
     * Getter sur la visibilite de l'attribut au sein de la zone d'affichage de diagrammes de classe
     * @return booleen
     */
    public boolean isVisible() {
        return this.visible;
    }

}