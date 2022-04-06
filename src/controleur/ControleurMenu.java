package controleur;

import modele.ImageSaveFilter;
import modele.ImageSaveFilterBuilder;
import modele.Modele;
import modele.classe.ObjectClasse;
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

/**
 * Controleur qui regarde les actions faites sur la barre du menu en haut de notre interface graphique
 */
public class ControleurMenu implements ActionListener {

    private Modele modele;

    private JFrame oldFrame;

    private File dernierRepOuvert;

    public static final String CHARGER_FICHIERS_CLASS_TXT = "Charger fichiers .class - \u1D9C\u1D57\u02B3\u02E1 \u02E2\u02B0\u1DA6\u1DA0\u1D57 \u1DA0";
    public static final String CHARGER_DOSSIER_TXT = "Charger dossier - \u1D9C\u1D57\u02B3\u02E1 \u02E2\u02B0\u1DA6\u1DA0\u1D57 \u1D48";
    public static final String RETIRER_SELECTION_DU_DIAGRAMME_TXT = "Retirer selection du diagramme - \u02E2\u1D58\u1D56\u1D56\u02B3";
    public static final String SAUVEGARDER_EZUML_TXT = "Sauvegarder .ezuml - \u1D9C\u1D57\u02B3\u02E1 \u02E2";
    public static final String CHARGER_EZUML_TXT = "Charger .ezuml - \u1D9C\u1D57\u02B3\u02E1 \u1D52";
    public static final String EXPORTER_TXT = "Exporter - \u1D9C\u1D57\u02B3\u02E1 \u1D49";
    public static final String RECHARGER_DERNIER_CHARGEMENT_TXT = "Recharger dernier chargement - \u1da0\u2075";

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
     * @param e contient le nom du bouton qui a ete appuyer
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
            case CHARGER_FICHIERS_CLASS_TXT:

                chargerFichiers();

                break;
            /*
                Permet de commencer un nouveau diagramme
             */
            case RECHARGER_DERNIER_CHARGEMENT_TXT:
                modele.reintialiserDiagramme();
                break;
            /*
                Permet de retirer les classe selectionne du diagramme
             */
            case RETIRER_SELECTION_DU_DIAGRAMME_TXT :
                modele.retirerClasseSelectionne();
                break;
            /*
                Permet d'enregistrer la classe modele
             */
            case SAUVEGARDER_EZUML_TXT:
                sauvegarde();

                break;
                /*
                    Permet de charger une classe modele Sauvegarder
                 */
            case CHARGER_EZUML_TXT:
                charger();

                break;
            /*
                Permet d'exporter en image le diagramme
             */
            case EXPORTER_TXT :
                exporterEnImage();

                break;
                /*
                Permet de charger des .class dans un repertoire choisi par l'utilisateur
                 */
            case CHARGER_DOSSIER_TXT:

                chargerDossier();

                break;

            default:
                throw new IllegalStateException("Bouton non traite");
        }
        //permet a la fenetre de regagner le focus une fois fini
        oldFrame.requestFocus();

    }

    /**
     * Methode qui lance le menu pour choisir un repertoire depuis lequel on veut charger les classes et les charge
     */
    public void chargerDossier() {
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
    }

    /**
     * ouvre une fenetre d'exploration window
     * Si l'utilisateur choisit un fichier .class on lance la méthode "chargerArborescenceProjet" qui permet de faire l'introspection
     */
    public void chargerFichiers() {
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
    }

    /**
     * Methode qui lance le menu pour choisir la sauvegarde et la charger
     */
    public void charger() {
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
    }

    /**
     * Methode qui lance le menu pour l'exportation et la sauvegarde en elle meme
     */
    public void exporterEnImage() {
        //on regarde est ce qu'il y a des classes dans le diagramme
        int nbClasseAfficher = (int) modele.getObjectClasses().stream().filter(ObjectClasse::isVisible).count();
        if (nbClasseAfficher == 0) {
            AffichageErreur.getInstance().afficherMessage("Ajoutez des classes dans le diagramme avant de l'exporter");
            return;
        }

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
            else{
                extension= "png"; // si il n'y pas d'extension
                cheminFichier+=".png";//on la rajoute aussi au fichier
            }

            //pour une raison inconnue les exportations en jpg et bmp ne marche pas
            modele.exporterEnImage(extension,cheminFichier );
            dernierRepOuvert=fc3.getSelectedFile().getParentFile();


            AffichageErreur.getInstance().afficherMessage("Fichier bien enregistrer a l'emplacement :\n"+cheminFichier);

        }
    }

    /**
     * Methode qui lance le menu pour la sauvegarde et la sauvegarde en elle meme
     */
    public void sauvegarde() {
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
    }

}
