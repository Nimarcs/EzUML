// PACKAGE
package modele.classe;

/**
 * Classe Enumeration
 *      herite de ObjectClasse
 *
 * Represente un ObjectClasse de type enum
 */
public class Enumeration extends ObjectClasse {


    // ATTRIBUTS

    // CONSTRUCTEURS

    /**
     * Constructeur d'un ObjectClasse, representant une classe java
     *
     * @param nomObjectClasse             String: nom de la classe
     * @param packageObjectClasse         String: nom du package de la classe
     * @param x                           int: position x de la classe (coin en haut a gauche du rectangle a afficher)
     * @param y                           int: position y de la classe (coin en haut a gauche du rectangle a afficher)
     */
    public Enumeration(String nomObjectClasse, String packageObjectClasse, int x, int y) {
        super(nomObjectClasse, packageObjectClasse, x, y);
    }

    // METHODES

    // GETTERS

    /**
     * Getter abstrait sur le type de classe qu'est la classe
     * @return TypeClasse -> ENUM
     */
    @Override
    public TypeClasse getType() {
        return TypeClasse.ENUM;
    }

}