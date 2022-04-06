// PACKAGE
package modele.classe;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * Classe Attribut
 * <p>
 * Représente un attribut au sein d'une classe
 * Contient l'information concernant son statut, son type et son nom
 */
public class Attribut implements Serializable {

    // ATTRIBUTS

    /**
     * Attribut prive String contenant le nom de l'attribut
     */
    private String nomAttribut;

    /**
     * Attribut prive Statut qui contient le statut de l'attribut
     * Peut etre privee, public ....
     */
    private Statut statutAttribut;

    /**
     * Attribut prive String contenant le type d'objet qu'est l'attribut
     * Exemple: java.util.List
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
     *
     * @param nomAttribut    String: nom de l'attribut
     * @param statutAttribut Statut: statut de l'attribut (PUBLIC, PRIVATE ou PROTECTED)
     * @param typeAttribut   String: type de l'attribut
     * @param statique       boolean: indique si l'attribut est statique ou non
     * @param finale         boolean: indique si l'attribut est final ou non
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

    /**
     * Methode qui retourne le texte permettant l'affichage de l'attribut au sein de la zone d'affichage de diagrammes de classe
     * L'affiche est composé du symbole representant son statut (+, -, #) puis de son nom et pour finir du type de classe de l'attribut
     *
     * @return String, composé textuel d'affichage de l'attribut
     */
    public String afficher() {
        String res = "";
        if (this.statutAttribut == Statut.PUBLIC) res += "+ ";
        else if (this.statutAttribut == Statut.PRIVATE) res += "- ";
        else res += "# ";
        StringBuilder type;

        /*
        Cas où c'est une collection
         */
        if (this.typeAttribut.contains("<") && this.typeAttribut.contains(">")) {
            // tabTmp coupe la string en deux a partir du char <
            String[] tabTmp = this.typeAttribut.split(Pattern.quote("<"));
            // dans la partie de gauche de tabTmp (séparé par <) on fait comme pour tabChemin,
            // on recupere la string la plus a droite séparé par un point
            String[] tabChemin2 = tabTmp[0].split(Pattern.quote("."));


            // cas pour les collections avec 2 type et plus
            if (this.typeAttribut.contains(",")) {
                // on prend la partie droite de tabTmp et on coupe a partir des virgules
                String[] tabTypes = tabTmp[1].split(Pattern.quote(","));

                // on ajoute le symbole coupé
                type = new StringBuilder(tabChemin2[tabChemin2.length - 1] + "<");

                for (int i = 0; i < tabTypes.length; i++) {
                    // pour chaque string coupé à la virgule on recupere seulement la partie tout a droite apres le dernier point
                    String[] tabLigne = tabTypes[i].split(Pattern.quote("."));
                    type.append(tabLigne[tabLigne.length - 1]);
                    if (i != tabTypes.length - 1) type.append(",");
                }


                // cas pour le reste avec 1 seul type
            } else {
                String[] tabContenuTemp = tabTmp[1].split(Pattern.quote("."));
                type = new StringBuilder(tabChemin2[tabChemin2.length - 1] + "<" + tabContenuTemp[tabContenuTemp.length - 1]);
            }

            //Cas pour les tableaux
        } else if (this.typeAttribut.matches("\\[L(.*)")) {
            String[] tabContenu = this.typeAttribut.split(Pattern.quote("."));
            String val = tabContenu[tabContenu.length - 1];
            type = new StringBuilder(val.substring(0, val.length() - 1) + " []");

            // sinon, pour un cas simple, on recupere juste le nom du type de l'attribut en fin de string
        } else {
            String[] tabChemin = this.typeAttribut.split(Pattern.quote(".")); // tabChemin devise la string par le point
            type = new StringBuilder(tabChemin[tabChemin.length - 1]);
        }

        res += this.nomAttribut + ": " + type;
        return res;
    }

    // GETTERS && SETTERS

    /**
     * Getter sur le nom de l'attribut
     *
     * @return String
     */
    public String getNomAttribut() {
        return this.nomAttribut;
    }

    /**
     * Methode setter qui permet de changer la visibilite de l'attribut
     *
     * @param visibilite booleen: parametre indiquant la nouvelle visibilite de l'attribut
     */
    public void changerVisibilite(boolean visibilite) {
        this.visible = visibilite;
    }

    /**
     * Getter sur le statut de l'attribut
     *
     * @return Statut
     */
    public Statut getStatutAttribut() {
        return this.statutAttribut;
    }

    /**
     * Getter sur le type de l'attribut
     *
     * @return String
     */
    public String getTypeAttribut() {
        return this.typeAttribut;
    }

    /**
     * Getter sur l'etat statique ou non de l'attribut
     *
     * @return booleen
     */
    public boolean isStatique() {
        return this.statique;
    }

    /**
     * Getter sur l'etat final ou non de l'attribut
     *
     * @return booleen
     */
    public boolean isFinale() {
        return this.finale;
    }

    /**
     * Getter sur la visibilite de l'attribut au sein de la zone d'affichage de diagrammes de classe
     *
     * @return booleen
     */
    public boolean isVisible() {
        return this.visible;
    }

}