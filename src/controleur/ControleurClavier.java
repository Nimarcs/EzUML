package controleur;

import modele.Modele;
import modele.classe.ObjectClasse;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Controleur qui permet de faire les actions avec les touches du clavier
 */
public class ControleurClavier implements KeyListener {

    /**
     * modele dont on veut controler les valeurs
     */
    private final Modele modele;

    /**
     * Contructeur de ControleurClavier
     * @param m modele a modifier, ne doit pas etre null
     */
    public ControleurClavier(Modele m){
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
        if (e.getKeyCode() == KeyEvent.VK_DELETE){
            modele.dechargerClasseSelectionne();
        }
        for (ObjectClasse o: modele.getObjectClasses()
             ) {
            modele.ajouterClasse(o, 0, 0);
        }
    }
}
