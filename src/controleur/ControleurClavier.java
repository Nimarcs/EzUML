package controleur;

import modele.Modele;
import modele.classe.ObjectClasse;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controleur qui permet de faire les actions avec les touches du clavier
 */
public class ControleurClavier implements KeyListener {

    /**
     * modele dont on veut controler les valeurs
     */
    private final Modele modele;

    /**
     * permet de faire les raccourcis du menu
     */
    private final ControleurMenu controleurMenu;

    /**
     * constante utilise pour le deplacement avec les flèches
     */
    private static final int DEPLACEMENT = 1, DEPLACEMENT_MULTIPLICATEUR = 10;

    /**
     * Contructeur de ControleurClavier
     * @param m modele a modifier, ne doit pas etre null
     * @param controleurMenu controleur du menu
     */
    public ControleurClavier(Modele m, ControleurMenu controleurMenu){
        this.controleurMenu = controleurMenu;
        assert m != null;
        modele=m;
    }

    /**
     * Invoked when a key has been typed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key typed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {
        //pas utilise
    }

    /**
     * Invoked when a key has been pressed.
     * See the class description for {@link KeyEvent} for a definition of
     * a key pressed event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        //on deplace si c'est une des flèches
        switch (e.getKeyCode()){
            case KeyEvent.VK_LEFT:
                modele.deplacerDecalageX(getDeplacement(e.isShiftDown()));
                break;
            case KeyEvent.VK_RIGHT:
                modele.deplacerDecalageX(-getDeplacement(e.isShiftDown()));
                break;
            case KeyEvent.VK_UP:
                modele.deplacerDecalageY(getDeplacement(e.isShiftDown()));
                break;
            case KeyEvent.VK_DOWN:
                modele.deplacerDecalageY(-getDeplacement(e.isShiftDown()));
                break;
        }
    }

    /**
     * on calcule la vitesse de deplacement
     * @param isShiftDown booleen vrai si le bouton shift est presse, faux sinon
     * @return vitesse de deplacement
     */
    private int getDeplacement(boolean isShiftDown) {
        int deplacement = DEPLACEMENT;
        if (isShiftDown) deplacement *= DEPLACEMENT_MULTIPLICATEUR;
        return deplacement;
    }

    /**
     * methode appele lorsque l'on relache n'importe quel touche,
     * cela permet de faire les raccourcis clavier
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_DELETE://suppr
                modele.retirerClasseSelectionne();
                break;
            case KeyEvent.VK_A://ctrl A
                if (e.isControlDown()){
                    List<ObjectClasse> objectClasseAfficheList = modele.getObjectClasses().stream().filter(ObjectClasse::isVisible).collect(Collectors.toList());
                    modele.setSelection(objectClasseAfficheList);
                }
                break;
            case KeyEvent.VK_S://ctrl S
                if (e.isControlDown()){
                    //lance le menu de sauvegarde
                    controleurMenu.sauvegarde();
                }
                break;
            case KeyEvent.VK_E://ctrl E
                if (e.isControlDown()){
                    //lance le menu de sauvegarde
                    controleurMenu.exporterEnImage();
                }
                break;
            case KeyEvent.VK_O://ctrl O
                if (e.isControlDown()){
                    //lance le menu de sauvegarde
                    controleurMenu.charger();
                }
                break;
            case KeyEvent.VK_D://ctrl shift D
                if (e.isControlDown() && e.isShiftDown()){
                    //lance le menu de sauvegarde
                    controleurMenu.chargerDossier();
                }
                break;
            case KeyEvent.VK_F://ctrl shift F
                if (e.isControlDown() && e.isShiftDown()){
                    //lance le menu de sauvegarde
                    controleurMenu.chargerFichiers();
                }
                break;
            case KeyEvent.VK_F5://F5
                modele.reintialiserDiagramme();
                break;
        }

    }
}
