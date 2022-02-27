package controleur;

import modele.Modele;
import modele.classe.ObjectClasse;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Controleur de l'affichage du diagramme de classe
 */
public class ControleurDiagramme implements MouseListener {

    private Modele modele;

    private int positionDebutMaintientX, positionDebutMaintientY;

    private static final int DEPLACEMENT_MIN = 5;

    public ControleurDiagramme(Modele m){
        assert m != null;
        modele=m;
    }

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * Invoked when a mouse button has been pressed on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent e) {
        //On memorise le début du maintient
        positionDebutMaintientX = e.getX();
        positionDebutMaintientY = e.getY();
    }

    /**
     * Invoked when a mouse button has been released on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        
        int vecteurDeplacementX = e.getX()-positionDebutMaintientX;
        int vecteurDeplacementY = e.getY()-positionDebutMaintientY;

        ObjectClasse objectClasse = null;
        boolean trouve;

        //on cherche l'objectClasse
        try {
            objectClasse = modele.getObjectClasseEnPosition(positionDebutMaintientX, positionDebutMaintientY);
            trouve = true;
        } catch (ClassNotFoundException ex) {
            trouve = false;
        }



        System.out.println(vecteurDeplacementX);
        System.out.println(vecteurDeplacementY);
        if (Math.abs(vecteurDeplacementX) > DEPLACEMENT_MIN || Math.abs(vecteurDeplacementY) > DEPLACEMENT_MIN) {

            if (trouve) {

                if (!modele.getSelection().contains(objectClasse)) {

                    //si c'est pas un ctrl click
                    if (!e.isControlDown()) {

                        //le click va deselectionner
                        modele.deselectionner();
                    }

                    modele.selectionnerUneClasse(objectClasse);
                }
            } else {
                modele.deselectionner();
            }

            if (modele.getSelection().isEmpty()) {

                //On change le decalage
                modele.deplacerDecalageX(vecteurDeplacementX);
                modele.deplacerDecalageY(vecteurDeplacementY);

            } else {

                //on deplace la selection de classe
                modele.deplacerClasseSelectionne(vecteurDeplacementX, vecteurDeplacementY);

            }

        } else {

            //si c'est pas un ctrl click
            if (!e.isControlDown()) {

                //le click va deselectionner
                modele.deselectionner();
            }

            //on a clicker sur une classe
            if (trouve) {
                System.out.println(objectClasse.getX() + ", " + objectClasse.getY());

                //si c'est un click gauche
                if (e.getButton() == MouseEvent.BUTTON1) {
                    modele.selectionnerUneClasse(objectClasse);
                }
            }

        }


    }

    /**
     * Invoked when the mouse enters a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Invoked when the mouse exits a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent e) {

    }
}
