package controleur;

import modele.Modele;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ControleurMenu implements ActionListener {

    private Modele modele;

    private JFrame oldFrame;
    /**
     * Contructeur de ControleurDiagramme
     * @param m modele a modifier, ne doit pas etre null
     */
    public ControleurMenu(Modele m, JFrame frame){
        assert m != null;
        assert frame != null;
        modele=m;
        oldFrame = frame;
    }


    /**
     * methode qui est appelé à chaque fois que l'utilisateur appuie sur un bouton de la barre de menu
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String bAppuyer = e.getActionCommand();

        switch (bAppuyer) {


            /*
             * On rentre dans la condition lorsque l'utilisateur appuie sur le bouton charger
             * Cela a pour effet d'ouvrir une fenetre d'exploration window
             * Si l'utilisateur choisit un fichier .class on lance la méthode "chargerArborescenceProjet" qui permet de faire l'introspection
             */
            case "Charger un .class":

                JFrame frame = new JFrame();
                FileDialog fd = new FileDialog(frame, "Choix d'un fichier .class", FileDialog.LOAD);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                fd.setDirectory("C:");
                fd.setFile("*.class");
                fd.setMultipleMode(true);
                fd.setVisible(true);


                if (fd.getDirectory() != null) {
                    File[] f = fd.getFiles();
                    for (File fichier : f) {
                        System.out.println(fichier.getAbsolutePath());
                        File fich = new File(fichier.getAbsolutePath());
                        modele.chargerArborescenceProjet(fich);

                    }
                }
                //permet a la fenetre de regagner le focus une fois la popup finie
                oldFrame.requestFocus();
                break;
            /*
                Permet de commencer un nouveau diagramme
             */
            case "Nouveau":
                modele.reintialiserDiagramme();
                break;
            /*
                Permet de retirer les classe selectionne du diagramme
             */
            case "Retirer Selection du diagramme" :
                modele.retirerClasseSelectionne();
                break;
                /*
                Permet d'enrigistrer la classe modele
             */
            case "Sauvegarder":
                modele.enregistrement();
                break;
            default:
                throw new IllegalStateException("Bouton non traite");
        }

    }

}


