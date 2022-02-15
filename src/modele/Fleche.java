package modele;

import modele.classe.ObjectClasse;

/**
 * Fleche qui permet de representer une association
 */
public class Fleche {

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
     * Constructeur de flèche
     * @param src object source de la flèche
     * @param dest object avec lequel la flèche associe la source
     * @throws NullPointerException renvoye si src ou dest est null
     */
    public Fleche(ObjectClasse src, ObjectClasse dest){
        if (src == null || dest == null) throw new NullPointerException("Fleche doit avoir une source et une destination");
        this.src = src;
        this.dest = dest;
    }

    //getter setter

    /**
     * setter de la destination
     * @param dest objectClasse associé, ne peut pas être null
     */
    public void setDest(ObjectClasse dest) {
        if (dest == null) throw new NullPointerException("Fleche doit avoir une source et une destination");
        this.dest = dest;
    }

    /**
     * setter de la source
     * @param src objectClasse qui associe, ne peut pas être null
     */
    public void setSrc(ObjectClasse src) {
        if (src == null) throw new NullPointerException("Fleche doit avoir une source et une destination");
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
}
