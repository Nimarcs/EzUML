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
        //pas utilise
    }

    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     *
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("Key released");
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
            default:
                //on fait rien
                System.out.println(e.getKeyCode());

        }

    }
}
