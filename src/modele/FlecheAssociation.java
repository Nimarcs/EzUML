package modele;

import modele.classe.Attribut;
import modele.classe.ObjectClasse;

/**
 * FlecheAssociation qui permet de representer une association
 */
public class FlecheAssociation {

    /**
     * Source de la flèche
     * ne peut pas être null
     */
    private ObjectClasse src;

    /**
     * Ce qui est associé à la source de la flèche
     * ne peut pas être null
     */
    private ObjectClasse dest;

    /**
     * Nom de l'attribut
     */
    private String nom;

    /**
     * Constructeur de flèche
     * @param src object source de la flèche
     * @param dest object avec lequel la flèche associe la source
     * @param attribut attribut que represente la flèche
     * @throws NullPointerException renvoye si src ou dest est null
     */
    public FlecheAssociation(ObjectClasse src, ObjectClasse dest, Attribut attribut){
        assert src != null && dest != null && attribut != null:"FlecheAssociation doit avoir une source et une destination";
        assert src.getAttributs().contains(attribut): "L'attribut doit être un attribut de la source";
        this.src = src;
        this.dest = dest;
        this.nom = attribut.getNomAttribut();
    }

    //getter setter

    /**
     * setter de la destination
     * @param dest objectClasse associé, ne peut pas être null
     */
    public void setDest(ObjectClasse dest) {
        assert  (dest != null):"FlecheAssociation doit avoir une source et une destination";
        this.dest = dest;
    }

    /**
     * setter de la source
     * @param src objectClasse qui associe, ne peut pas être null
     */
    public void setSrc(ObjectClasse src) {
        assert  (dest != null):"FlecheAssociation doit avoir une source et une destination";
        this.src = src;
    }

    /**
     * getter de la destination
     * @return objectClasse associé
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
}
