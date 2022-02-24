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


    /**
     * methode qui est appelé à chaque fois que l'utilisateur appuie sur un bouton de la barre de menu
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String bAppuyer = e.getActionCommand();


        /**
        * On rentre dans la condition lorsque l'utilisateur appuie sur le bouton ouvrir
        * Cela a pour effet d'ouvrir une fenetre d'exploration window
        * Si l'utilisateur choisit un fichier .class on lance la méthode "chargerArborescenceProjet" qui permet de faire l'introspection
         */
        if(bAppuyer.equals("Ouvrir")){

            JFrame frame = new JFrame();
            FileDialog fd = new FileDialog(frame, "Choix d'un fichier .class", FileDialog.LOAD);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            fd.setDirectory("C:");
            fd.setFile("*.class");
            fd.setMultipleMode(true);
            fd.setVisible(true);



            if( fd.getDirectory()!=null) {
                File[] f = fd.getFiles();
                for (File fichier : f) {
                    System.out.println(fichier.getAbsolutePath());
                    File fich = new File(fichier.getAbsolutePath());
                    modele.chargerArborescenceProjet(fich);

                }
            }
        }
    }

}


