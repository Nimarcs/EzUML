// PACKAGE
package modele.classe;

import java.io.Serializable;

/**
 * Classe abstraite Extendable
 *
 * Represente les ObjectClasse qui herite d'un autre ObjectClasse
 */
public abstract class Extendable extends ObjectClasse implements Serializable {

    // ATTRIBUTS
    /**
     * Attribut protegee qui correspond a la classe qui extends cette classe
     */
    protected Extendable objectClasseExtends;

    // CONSTRUCTEURS

    /**
     * Constructeur d'un ObjectClasse, representant une classe java dans le diagramme
     *
     * @param nomObjectClasse     String: nom de la classe
     * @param packageObjectClasse String: nom du package de la classe
     * @param x                   int: position x de la classe (coin en haut a gauche du rectangle a afficher)
     * @param y                   int: position y de la classe (coin en haut a gauche du rectangle a afficher)
     */
    public Extendable(String nomObjectClasse, String packageObjectClasse, int x, int y) {
        super(nomObjectClasse, packageObjectClasse, x, y);
        this.objectClasseExtends = null;
    }

    // METHODES

    /**
     * Methode pour changer la classe duquel herite cette classe
     * si null, cela signifie qu'elle n'herite d'aucune classe
     *
     * @param ext Extendable: classe qu'herite cette classe
     */
    public void changerExtends(Extendable ext) {
        this.objectClasseExtends = ext;
    }

    // GETTERS

    /**
     * Getter de classe qu'hérite cette classe
     *
     * @return Extends
     */
    public Extendable getObjectClasseExtends() {
        return objectClasseExtends;
    }

}
