package modele;

import modele.classe.Attribut;
import modele.classe.ObjectClasse;
import modele.classe.Statut;

import java.io.Serializable;
import java.util.regex.Pattern;

/**
 * FlecheAssociation qui permet de representer une association
 */
public class FlecheAssociation implements Serializable {

    /**
     * Source de la fleche
     * ne peut pas etre null
     */
    private ObjectClasse src;

    /**
     * Ce qui est associe a la source de la fleche
     * ne peut pas etre null
     */
    private ObjectClasse dest;

    /**
     * Nom de l'attribut
     */
    private String nom;

    /**
     * String contenant les cardinalités de sources et de dest
     */
    private String cardSrc, cardDest;

    /**
     * Constructeur de fleche
     * @param src object source de la fleche
     * @param dest object avec lequel la fleche associe la source
     * @param attribut attribut que represente la fleche
     * @throws NullPointerException renvoye si src ou dest est null
     */
    public FlecheAssociation(ObjectClasse src, ObjectClasse dest, Attribut attribut){
        assert src != null && dest != null && attribut != null:"FlecheAssociation doit avoir une source et une destination";
        assert src.getAttributs().contains(attribut): "L'attribut doit etre un attribut de la source";
        this.src = src;
        this.dest = dest;
        String res;
        if (attribut.getStatutAttribut() == Statut.PUBLIC) res = "+ ";
        else if (attribut.getStatutAttribut() == Statut.PRIVATE) res = "- ";
        else res = "# ";
        this.nom = res + attribut.getNomAttribut();
        if (attribut.getNomAttribut().contains("<")&&attribut.getNomAttribut().contains(">")||attribut.getNomAttribut().contains("[]")) {
            cardSrc = "*";
            cardDest = "*";
        } else{
            cardSrc = "*";
            cardDest = "0..1";
        }

    }

    //getter setter

    /**
     * setter de la destination
     * @param dest objectClasse associe, ne peut pas etre null
     */
    public void setDest(ObjectClasse dest) {
        assert  (dest != null):"FlecheAssociation doit avoir une source et une destination";
        this.dest = dest;
    }

    /**
     * setter de la source
     * @param src objectClasse qui associe, ne peut pas etre null
     */
    public void setSrc(ObjectClasse src) {
        assert  (dest != null):"FlecheAssociation doit avoir une source et une destination";
        this.src = src;
    }

    /**
     * getter de la destination
     * @return objectClasse associe
     */
    public ObjectClasse getDest() {
        return dest;
    }

    /**
     * getter de la source
     * @return objectClasse qui associe
     */
    public ObjectClasse getSrc() {
        return src;
    }

    /**
     * getter du nom
     * @return nom de l'association
     */
    public String getNom() {
        return nom;
    }

    /**
     * Getter de la cardinalité de la classe source
     * @return String
     */
    public String getCardSrc() {
        return cardSrc;
    }

    /**
     * Getter de la cardinalité de la classe destination
     * @return String
     */
    public String getCardDest() {
        return cardDest;
    }

}
