// PACKAGE
package modele.classe;

// IMPORTS
import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Classe Methode
 *
 * Represente une methode au sein d'une classe
 * Contient l'information concernant son nom, son statut, son type de retour et ses parametres
 */
public class Methode implements Serializable {

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

    /**
     * Constructeur de l'objet Methode, qui correspond a une methode ou un constructeur d'une classe
     * @param nomMethode String: nom de la methode
     * @param statutMethode Statut: statut de la methode
     * @param typeRetour String: le type de retour de la methode, si null, c'est un constructeur
     * @param listeParametres List<String>: liste des parametres de la methode
     * @param abstrait booleen: indique si la methode est abstraite ou non
     * @param statique booleen: indique si la methode est statique ou non
     * @param finale booleen: indique si la methode est finale ou non
     */
    public Methode(String nomMethode, Statut statutMethode, String typeRetour, List<String> listeParametres, boolean abstrait, boolean statique, boolean finale) {
        this.nomMethode = nomMethode;
        this.statutMethode = statutMethode;
        this.typeRetour = typeRetour;
        this.listeParametres = listeParametres;
        this.abstrait = abstrait;
        this.statique = statique;
        this.finale = finale;
        this.visible = true;
    }

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
        String[] tabNom = nomMethode.split(Pattern.quote("."));
        res += tabNom[tabNom.length-1] + " (";
        if (this.listeParametres!=null) {
            for (int i = 0; i < this.listeParametres.size(); i++) {
                res += retournerType(this.listeParametres.get(i));
                if (i != this.listeParametres.size() - 1) res += ", ";
            }
        }
        res += ")";
        if (this.typeRetour != null && !this.typeRetour.equals("void")) {
            res += ": " + retournerType(this.typeRetour);
        }
        return res;
    }

    /**
     * Methode privee qui formalise un type selon ses spécificités
     * @param s : String qu'on doit formater
     * @return String formate pour l'affichage
     */
    private String retournerType(String s) {
        String type;
        String[] tabChemin = s.split(Pattern.quote(".")); // tabChemin devise la string par le point
        if (s.contains("<")&&s.contains(">")) {
            String tabTmp[] = s.split(Pattern.quote("<")); // tabTmp coupe la string en deux a partie du char <
            String tabChemin2[] = tabTmp[0].split(Pattern.quote(".")); // dans la partie de gauche de tabTmp (séparé par <) on fait comme pour tabChemin, on recupere la string la plus a droite séparé par un point
            if (s.contains(",")) { // cas pour les collections avec 2 type et plus
                String tabTypes[] = tabTmp[1].split(Pattern.quote(",")); // on prend la partie droite de tabTmp et on coupe a partir des virgules
                type = tabChemin2[tabChemin2.length-1] + "<"; // on ajoute le symbole coupé
                for (int i=0; i<tabTypes.length; i++) {
                    String tabLigne[] = tabTypes[i].split(Pattern.quote(".")); // pour chaque string coupé à la virgule on recupere seulement la partie tout a droite apres le dernier point
                    type+=tabLigne[tabLigne.length-1];
                    if (i!=tabTypes.length-1) type+=",";
                }
            } else { // cas pour le reste avec 1 seul type
                String tabContenuTemp[] = tabTmp[1].split(Pattern.quote("."));
                type = tabChemin2[tabChemin2.length-1] + "<" + tabContenuTemp[tabContenuTemp.length-1];
            }
        } else {
            if (s.matches("\\[L(.*)")) {
                String tabContenu[] = s.split(Pattern.quote("."));
                String val = tabContenu[tabContenu.length - 1];
                type = val.substring(0, val.length() - 1) + " []";
            } else { // sinon, pour un cas simple, on recupére juste le nom du type de l'attribut en fin de string
                type = tabChemin[tabChemin.length - 1];
            }
        }
        return type;
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