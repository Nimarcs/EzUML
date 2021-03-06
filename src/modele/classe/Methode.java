// PACKAGE
package modele.classe;

// IMPORTS

import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Classe Methode
 * <p>
 * Represente une methode au sein d'une classe
 * Contient l'information concernant son nom, son statut, son type de retour et ses parametres
 */
public class Methode implements Serializable {

    // ATTRIBUTS

    /**
     * Attribut prive String contenant le nom de la methode
     * packagen-1.packagen.NomClasse.nomMethode
     */
    private String nomMethode;

    /**
     * Attribut prive Statut definissant le statut de la methode
     */
    private Statut statutMethode;

    /**
     * Attribut prive String contenant le type de retour, null si c'est un constructeur
     */
    private String typeRetour;

    /**
     * Attribut prive liste de String contenant la liste des types de parametres
     * chemin avec les packages des types
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
     *
     * @param nomMethode      String: nom de la methode
     * @param statutMethode   Statut: statut de la methode
     * @param typeRetour      String: le type de retour de la methode, si null, c'est un constructeur
     * @param listeParametres List<String>: liste des parametres de la methode
     * @param abstrait        booleen: indique si la methode est abstraite ou non
     * @param statique        booleen: indique si la methode est statique ou non
     * @param finale          booleen: indique si la methode est finale ou non
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
     * L'affiche est compos?? du symbole representant son statut (+, -, #) puis de son nom, de ses parametres et pour finir de son type de retour si non null/void
     *
     * @return String, compos?? textuel d'affichage de la methode
     */
    public String afficher() {
        StringBuilder res = new StringBuilder();

        //status
        if (this.statutMethode == Statut.PUBLIC) res.append("+ ");
        else if (this.statutMethode == Statut.PRIVATE) res.append("- ");
        else res.append("# ");

        //on recupere uniquement le nom de la methode
        String[] tabNom = nomMethode.split(Pattern.quote("."));
        res.append(tabNom[tabNom.length - 1]).append(" (");

        //on met les parametres dans les parantheses
        if (this.listeParametres != null) {
            for (int i = 0; i < this.listeParametres.size(); i++) {
                res.append(retournerType(this.listeParametres.get(i)));
                //on ajoute pas de virgule pour le dernier element
                if (i != this.listeParametres.size() - 1) res.append(", ");
            }
        }
        res.append(")");

        //on rajoute le type de retour simplifie (sans les package)
        if (this.typeRetour != null && !this.typeRetour.equals("void")) {
            res.append(": ").append(retournerType(this.typeRetour));
        }
        return res.toString();
    }

    /**
     * Methode privee qui formalise un type selon ses sp??cificit??s
     *
     * @param s : String qu'on doit formater
     * @return String formate pour l'affichage
     */
    private String retournerType(String s) {
        //on initialise
        StringBuilder type;

        // tabChemin devise la string par le point
        String[] tabChemin = s.split(Pattern.quote("."));
        String valType = s;

        //cas ou c'est une liste
        if (valType.matches("class(.*)")) valType = valType.substring(6);
        if (valType.contains("<") && valType.contains(">")) {

            // tabTmp coupe la string en deux a partie du char <
            String[] tabTmp = valType.split(Pattern.quote("<"));
            // dans la partie de gauche de tabTmp (s??par?? par <) on fait comme pour tabChemin,
            // on recupere la string la plus a droite s??par?? par un point
            String[] tabChemin2 = tabTmp[0].split(Pattern.quote("."));

            // cas pour les collections avec 2 type et plus
            if (valType.contains(",")) {

                // on prend la partie droite de tabTmp et on coupe a partir des virgules
                String[] tabTypes = tabTmp[1].split(Pattern.quote(","));
                // on ajoute le symbole coup??
                type = new StringBuilder(tabChemin2[tabChemin2.length - 1] + "<");
                for (int i = 0; i < tabTypes.length; i++) {
                    // pour chaque string coup?? ?? la virgule on recupere seulement la partie tout a droite apres le dernier point
                    String[] tabLigne = tabTypes[i].split(Pattern.quote("."));
                    type.append(tabLigne[tabLigne.length - 1]);
                    //on ne met pas la virgule pour le dernier
                    if (i != tabTypes.length - 1) type.append(",");
                }

                // cas pour le reste avec 1 seul type
            } else {
                String[] tabContenuTemp = tabTmp[1].split(Pattern.quote("."));
                type = new StringBuilder(tabChemin2[tabChemin2.length - 1] + "<" + tabContenuTemp[tabContenuTemp.length - 1]);
            }

            //cas ou c'est un tableau
        } else if (valType.matches("\\[L(.*)")) {
            String[] tabContenu = valType.split(Pattern.quote("."));
            String val = tabContenu[tabContenu.length - 1];
            type = new StringBuilder(val.substring(0, val.length() - 1) + " []");

            // sinon, pour un cas simple, on recup??re juste le nom du type de l'attribut en fin de string
        } else {
            type = new StringBuilder(tabChemin[tabChemin.length - 1]);
        }
        return type.toString();
    }

    // GETTERS && SETTERS

    /**
     * Methode setter qui permet de changer la visibilite de la methode
     *
     * @param visibilite booleen: parametre indiquant la nouvelle visibilite de la methode
     */
    public void changerVisibilite(boolean visibilite) {
        this.visible = visibilite;
    }

    /**
     * Getter sur le nom de la methode
     *
     * @return String
     */
    public String getNomMethode() {
        return this.nomMethode;
    }

    /**
     * Getter sur le statut de la methode
     *
     * @return Statut
     */
    public Statut getStatutMethode() {
        return this.statutMethode;
    }

    /**
     * Getter sur le type de retour de la methode
     * si null, c'est un constructeur
     *
     * @return String
     */
    public String getTypeRetour() {
        return this.typeRetour;
    }

    /**
     * Getter sur la liste des parametres
     *
     * @return List<String>
     */
    public List<String> getListeParametres() {
        return this.listeParametres;
    }

    /**
     * Getter sur l'etat abstrait de la methode ou non
     *
     * @return booleen
     */
    public boolean isAbstrait() {
        return this.abstrait;
    }

    /**
     * Getter sur l'etat statique de la methode ou non
     *
     * @return booleen
     */
    public boolean isStatique() {
        return this.statique;
    }

    /**
     * Getter sur l'etat final de la methode ou non
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