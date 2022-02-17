package controleur;

import modele.Modele;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControleurMenu implements ActionListener {

    private Modele modele;


    public ControleurMenu(Modele m){
        modele=m;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
