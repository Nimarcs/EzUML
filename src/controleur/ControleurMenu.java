package controleur;

import modele.Modele;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

public class ControleurMenu implements ActionListener {

    private Modele modele;


    public ControleurMenu(Modele m){
        modele=m;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String bAppuyer = e.getActionCommand();


        /*
        * On rentre dans la condition lorsque l'utilisateur appuie sur le bouton ouvrir
        * Cela a pour effet d'ouvrir une fenetre d'exploration window
        * Si l'utilisateur choisit un fichier .class on lance la méthode "chargerArborescenceProjet" qui permet de faire l'introspection
         */
        if(bAppuyer.equals("Ouvrir")){

            JFrame frame = new JFrame();
            FileDialog fd = new FileDialog(frame, "Choix d'un fichier .class", FileDialog.LOAD);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            fd.setDirectory("C:\\");
            fd.setVisible(true);

            if( fd.getDirectory()==null) {
                System.out.println("aucun fichier séléctionner");
            } else {
                File f = new File(fd.getDirectory()+fd.getFile());
               modele.chargerArborescenceProjet(f);

            }
        }

    }
}
