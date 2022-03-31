package controleur;

import modele.ImageSaveFilter;
import modele.ImageSaveFilterBuilder;
import modele.Modele;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
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
                Permet d'enregistrer la classe modele
             */
            case "Sauvegarder":
                modele.enregistrement();
                break;
                /*
                    Permet de charger une classe modele Sauvegarder
                 */
            case "Charger diagramme":
                modele.deserilization();
            break;
            /*
                Permet d'exporter en image le diagramme
             */
            case "Exporter en image" :
                //menu de choix
                JFileChooser fc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                fc.setApproveButtonText("Sauvegarder");



                //on recupere les filters
                for (ImageSaveFilter imgFilter: ImageSaveFilterBuilder.getFilters()) {
                    fc.addChoosableFileFilter(imgFilter);
                }
                fc.setDialogTitle("Choisissez ou exporter votre image");
                int returnValue = fc.showOpenDialog(null);

                if(returnValue==JFileChooser.APPROVE_OPTION){
                    String cheminFichier = fc.getSelectedFile().getAbsolutePath();
                    String extension;

                    //Si le fileFilter est un des notre et qu'il manque l'extension on la rajoute
                    try {
                        ImageSaveFilter fileFilterChoisi = (ImageSaveFilter) fc.getFileFilter();

                        extension = cheminFichier.substring(cheminFichier.lastIndexOf('.') + 1);
                        if (!fileFilterChoisi.getExtentions().contains(extension)) {
                            cheminFichier = cheminFichier + "." + fileFilterChoisi.getExtentions().get(0);
                        }
                    } catch (ClassCastException ignored){}

                    //on recupere l'extension
                    int indexExtention = cheminFichier.lastIndexOf('.')+1;
                    if (indexExtention > 0)
                        extension= cheminFichier.substring(indexExtention);
                    else
                        extension= "png"; // si il n'y pas d'extension

                    //pour une raison inconnue les exportations en jpg et bmp ne marche pas
                    modele.exporterEnImage(extension,cheminFichier );
                }

                break;
            default:
                throw new IllegalStateException("Bouton non traite");
        }

    }

}
