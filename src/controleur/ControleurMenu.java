package controleur;

import modele.ImageSaveFilter;
import modele.ImageSaveFilterBuilder;
import modele.Modele;
import vue.AffichageErreur;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ControleurMenu implements ActionListener {

    private Modele modele;

    private JFrame oldFrame;

    private File dernierRepOuvert;
    /**
     * Contructeur de ControleurDiagramme
     * @param m modele a modifier, ne doit pas etre null
     */
    public ControleurMenu(Modele m, JFrame frame){
        assert m != null;
        assert frame != null;
        modele=m;
        oldFrame = frame;
        dernierRepOuvert=FileSystemView.getFileSystemView().getHomeDirectory();
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
            case "Charger fichiers .class":

                JFileChooser fc = new JFileChooser(dernierRepOuvert);
                //fc.setFileFilter(new EzumlSaveFilter());
                fc.setDialogTitle("Ouvrir un .class");
                fc.setApproveButtonText("Ouvrir");
                fc.setMultiSelectionEnabled(true);


                fc.setAcceptAllFileFilterUsed(false);

                FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only .class files", "class");
                fc.addChoosableFileFilter(restrict);



                int returnValue = fc.showOpenDialog(null);

                if(returnValue==JFileChooser.APPROVE_OPTION){

                    File files[] = fc.getSelectedFiles();
                    for (File fichier : files) {
                        File fich = new File(fichier.getAbsolutePath());
                        modele.chargerArborescenceProjet(fich);

                    }
                    dernierRepOuvert=files[0].getParentFile();
                }

                //permet a la fenetre de regagner le focus une fois la popup finie
                oldFrame.requestFocus();
                break;
            /*
                Permet de commencer un nouveau diagramme
             */
            case "Recharger dernier chargement":
                modele.reintialiserDiagramme();
                break;
            /*
                Permet de retirer les classe selectionne du diagramme
             */
            case "Retirer selection du diagramme" :
                modele.retirerClasseSelectionne();
                break;
            /*
                Permet d'enregistrer la classe modele
             */
            case "Sauvegarder .ezuml":
                if(modele.getObjectClasses().isEmpty()){
                    AffichageErreur.getInstance().afficherMessage("Charger un diagramme avant de l'enregistrer");

                } else {
                    JFileChooser fc1 = new JFileChooser(dernierRepOuvert);
                    //fc.setFileFilter(new EzumlSaveFilter());
                    fc1.setDialogTitle("Sauvegarder votre fichier");
                    fc1.setApproveButtonText("Sauvegarder");
                    fc1.setAcceptAllFileFilterUsed(false);

                    FileNameExtensionFilter filtreEzuml = new FileNameExtensionFilter("Only .ezuml files", "ezuml");
                    fc1.addChoosableFileFilter(filtreEzuml);


                    int returnValue1 = fc1.showOpenDialog(null);

                    if(returnValue1==JFileChooser.APPROVE_OPTION){
                        modele.enregistrement(fc1.getSelectedFile().getAbsolutePath());

                        dernierRepOuvert=fc1.getSelectedFile().getParentFile();

                    }
                }

                oldFrame.requestFocus();
                break;
                /*
                    Permet de charger une classe modele Sauvegarder
                 */
            case "Charger .ezuml":
                JFileChooser fc2 = new JFileChooser(dernierRepOuvert);
                //fc.setFileFilter(new EzumlSaveFilter());
                fc2.setDialogTitle("Ouvrir votre fichier");
                fc2.setApproveButtonText("Ouvrir");
                fc2.setAcceptAllFileFilterUsed(false);

                FileNameExtensionFilter filtreEzumlO = new FileNameExtensionFilter("Only .ezuml files", "ezuml");
                fc2.addChoosableFileFilter(filtreEzumlO);


                int returnValue2 = fc2.showOpenDialog(null);

                if(returnValue2==JFileChooser.APPROVE_OPTION) {
                    modele.deserilization(fc2.getSelectedFile().getAbsolutePath());
                    dernierRepOuvert=fc2.getSelectedFile().getParentFile();

                }
                oldFrame.requestFocus();

                break;
            /*
                Permet d'exporter en image le diagramme
             */
            case "Exporter" :
                //menu de choix
                JFileChooser fc3 = new JFileChooser(dernierRepOuvert);
                fc3.setApproveButtonText("Sauvegarder");



                //on recupere les filters
                for (ImageSaveFilter imgFilter: ImageSaveFilterBuilder.getFilters()) {
                    fc3.addChoosableFileFilter(imgFilter);
                }
                fc3.setDialogTitle("Choisissez ou exporter votre image");
                int returnValue3 = fc3.showOpenDialog(null);

                if(returnValue3==JFileChooser.APPROVE_OPTION){
                    String cheminFichier = fc3.getSelectedFile().getAbsolutePath();
                    String extension;

                    //Si le fileFilter est un des notre et qu'il manque l'extension on la rajoute
                    try {
                        ImageSaveFilter fileFilterChoisi = (ImageSaveFilter) fc3.getFileFilter();

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
                    dernierRepOuvert=fc3.getSelectedFile().getParentFile();


                    AffichageErreur.getInstance().afficherMessage("Fichier bien enregistrer a l'emplacement :\n"+cheminFichier);

                }


                oldFrame.requestFocus();
                break;
                /*
                Permet de charger des .class dans un repertoire choisi par l'utilisateur
                 */
            case "Charger dossier":

                JFileChooser fc4 = new JFileChooser(dernierRepOuvert);
                fc4.setDialogTitle("Ouvrir un repertoire");
                fc4.setApproveButtonText("Ouvrir un repertoire");
                fc4.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int returnValue4 = fc4.showOpenDialog(null);

                if(returnValue4==JFileChooser.APPROVE_OPTION) {
                    String f = fc4.getSelectedFile().getAbsolutePath()+File.separator;
                    File rep = new File(f);


                    //partie qui donne tout les fichiers que contient un repertoire meme dans des sous-repertoire
                    List<String> result = new ArrayList<>();
                    try (Stream<Path> walk = Files.walk(Paths.get(rep.getAbsolutePath()))) {
                        result = walk.filter(Files::isRegularFile)
                                .map(x -> x.toString()).collect(Collectors.toList());

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }


                    File fich =null;
                    //On regarde dans la liste result pour voir si il contient des .class
                    for (int i = 0; i < result.size(); i++) {
                        if (result.get(i).endsWith(".class") == true) {
                            fich = new File(result.get(i));
                            modele.chargerArborescenceProjet(fich);
                        }
                    }

                    if(fich==null){
                        AffichageErreur.getInstance().afficherMessage("Le repertoire que vous avez choisi ne contient pas de .class");
                    } else {
                        dernierRepOuvert=fich.getParentFile();

                    }
                }

                //permet a la fenetre de regagner le focus une fois la popup finie
                oldFrame.requestFocus();
                break;

            default:
                throw new IllegalStateException("Bouton non traite");
        }

    }

}
