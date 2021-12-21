// PACKAGE
package modele.classe;

/**
 * Classe Attribut
 *
 * Représente un attribut au sein d'une classe
 * Contient l'information concernant son statut, son type et son nom
 */
public class Attribut {

    // ATTRIBUTS

    /**
     * Attribut prive String contenant le nom de l'attribut
     */
    private String nomAttribut;

    /**
     * Attribut prive Statut qui contient le statut de l'attribut
     */
    private Statut statutAttribut;

    /**
     * Attribut prive String contenant le type d'objet qu'est l'attribut
     */
    private String typeAttribut;

    /**
     * Attribut prive booleen qui permet de definir si l'attribut est statique ou non
     */
    private boolean statique;

    /**
     * Attribut prive booleen qui permet de definir si l'attribut est final ou non
     */
    private boolean finale;

    /**
     * Attribut prive boolean indiquant la visibilite de l'attribut dans la zone d'affichage du diagramme
     */
    private boolean visible;

    // CONSTRUCTEURS

    /**
     * Constructeur d'un objet Attribut, représentant un attribut au sein d'une classe
     * @param nomAttribut String: nom de l'attribut
     * @param statutAttribut Statut: statut de l'attribut (PUBLIC, PRIVATE ou PROTECTED)
     * @param typeAttribut String: type de l'attribut
     * @param statique boolean: indique si l'attribut est statique ou non
     * @param finale boolean: indique si l'attribut est final ou non
     */
    public Attribut(String nomAttribut, Statut statutAttribut, String typeAttribut, boolean statique, boolean finale) {
        this.nomAttribut = nomAttribut;
        this.statutAttribut = statutAttribut;
        this.typeAttribut = typeAttribut;
        this.statique = statique;
        this.finale = finale;
        this.visible = true;
    }

    // METHODES


    // GETTERS && SETTERS

    /**
     * Getter sur le nom de l'attribut
     * @return String
     */
    public String getNomAttribut() {
        return this.nomAttribut;
    }

    /**
     * Getter sur le statut de l'attribut
     * @return Statut
     */
    public Statut getStatutAttribut() {
        return this.statutAttribut;
    }

    /**
     * Getter sur le type de l'attribut
     * @return String
     */
    public String getTypeAttribut() {
        return this.typeAttribut;
    }

    /**
     * Getter sur l'etat statique ou non de l'attribut
     * @return booleen
     */
    public boolean isStatique() {
        return this.statique;
    }

    /**
     * Getter sur l'etat final ou non de l'attribut
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