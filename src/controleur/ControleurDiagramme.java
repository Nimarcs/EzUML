package controleur;

import modele.Modele;
import modele.classe.ObjectClasse;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Controleur qui regarde les actions faites par la souris de l'utilisateur sur l'affichage du diagramme
 */
public class ControleurDiagramme implements MouseListener, MouseMotionListener {

    /**
     * constante qui represente le minimum de deplacement a faire avant de considerer que ce n'est plus un clic de selection mais un clic pour deplacer une classe
     */
    private static final int DEPLACEMENT_MIN = 5;

    /**
     * modele dont on veut controller les valeurs
     */
    private final Modele modele;

    /**
     * position a laquelle on a commencer a faire un clique gauche, permet d'eviter de selectionner si on ne deplace pas la souris
     */
    private int positionDebutMaintientX, positionDebutMaintientY;
    private int positionMaintientX, positionMaintientY;

    /**
     * dernier objectClasse sur lequel l'utilisateur a cliquer dessus
     */
    private ObjectClasse objectClasse;

    /**
     * Contructeur de ControleurDiagramme
     * @param m modele a modifier, ne doit pas etre null
     */
    public ControleurDiagramme(Modele m){
        assert m != null;
        modele=m;
    }

    /**
     * methode qui est appele lorsque l'on appuie sur la souris sur le diagramme
     *
     * @param e l'événement à traiter
     */
    @Override
    public void mousePressed(MouseEvent e) {
        //On memorise le début du maintient
        positionDebutMaintientX = e.getX();
        positionDebutMaintientY = e.getY();
        positionMaintientX = e.getX();
        positionMaintientY = e.getY();

        //on cherche l'objectClasse
        try {
            objectClasse = modele.getObjectClasseEnPosition(positionDebutMaintientX, positionDebutMaintientY);
        } catch (ClassNotFoundException ex) {
            objectClasse = null;
        }

        //if (objectClasse!=null) //modele.selectionnerUneClasse(objectClasse);


    }

    /**
     * methode appele lorsque l'on arrete d'appuyer sur la souris dans le diagramme
     *
     * @param e l'evenement à traiter
     */
    @Override
    public void mouseReleased(MouseEvent e) {

        // on cacule les vecteurs de deplacement a partir de la position initial
        int vecteurDeplacementX = e.getX()-positionDebutMaintientX;
        int vecteurDeplacementY = e.getY()-positionDebutMaintientY;

        //si on se deplace assez
        if (Math.abs(vecteurDeplacementX) > DEPLACEMENT_MIN || Math.abs(vecteurDeplacementY) > DEPLACEMENT_MIN) {
            //si on a trouve la classe
            if (objectClasse!=null) {
                //si la classe est selectionne
                if (!modele.getSelection().contains(objectClasse) && !e.isControlDown()) {
                    //le click va deselectionner
                    modele.deselectionner();
                } else {
                    //modele.selectionnerUneClasse(objectClasse);
                }
            }

        } else {
            if (objectClasse!=null) {
                if (!e.isControlDown()) modele.deselectionner();
                modele.selectionnerUneClasse(objectClasse);
            }
            else modele.deselectionner();
        }

    }

    /**
     * methode appele lorsque l'on bouge la souris dans le diagramme
     *
     * @param e l'événement à traiter
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        // on caclcule le vecteur de deplacement
        int vecteurDeplacementX = e.getX()-positionMaintientX;
        int vecteurDeplacementY = e.getY()-positionMaintientY;
        // On change la position de la selection
        if (!modele.getSelection().isEmpty() && objectClasse!=null && modele.getSelection().contains(objectClasse)){
            modele.deplacerClasseSelectionne(vecteurDeplacementX, vecteurDeplacementY);
        }
        else {
            //On change le decalage
            modele.deplacerDecalageX(vecteurDeplacementX);
            modele.deplacerDecalageY(vecteurDeplacementY);
            //sinon on deplace la selection de classe
        }
        // On reactualise la position du maitien avec celle de la souris à cet instant
        positionMaintientX = e.getX();
        positionMaintientY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

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

    /**
     * Invoked when the mouse button has been clicked (pressed
     * and released) on a component.
     *
     * @param e the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent e) {

    }
}
