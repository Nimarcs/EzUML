package modele;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.*;

/**
 * Filtre utilise pour les images
 */
public class ImageSaveFilter extends FileFilter {

    /**
     * Liste des extentions acceptee
     * Ne peut ni etre null, ni etre vide
     */
    private final List<String> nomsExtentionAcceptee;

    /**
     * Constructeur de ImageSaveFilter
     *
     * @param nomsExtentionAcceptee recupere un array avec tous les differentes extentions possible du type acceptee
     */
    public ImageSaveFilter(String[] nomsExtentionAcceptee) {
        if (nomsExtentionAcceptee == null)
            throw new NullPointerException("nomsExtentionAcceptee ne doit pas etre null");
        //initialise la liste des extentions acceptee
        this.nomsExtentionAcceptee = Arrays.asList(nomsExtentionAcceptee);
        if (this.nomsExtentionAcceptee.isEmpty())
            throw new IllegalArgumentException("nomsExtentionAcceptee ne doit pas etre vide");
    }

    /**
     * Methode qui verifie si un fichier est acceptee
     *
     * @param f fichier a verifier
     * @return booleen a vrai si et seulement si le fichier est accepter par le filtre
     */
    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return false;
        }

        String s = f.getName();
        //on recupere juste son extention sans le point
        s = s.substring(s.lastIndexOf('.') + 1);

        return nomsExtentionAcceptee.contains(s);
    }

    /**
     * methode qui affiche a quoi le filtre correspond
     *
     * @return string avec la liste des differentes extentions possibles comme : "*.png *.PNG "
     */
    @Override
    public String getDescription() {
        StringBuilder stringBuilder = new StringBuilder();

        //fait le String avec la liste des differentes extentions
        for (String s : nomsExtentionAcceptee) {
            stringBuilder.append("*.").append(s).append(' ');
        }
        return stringBuilder.toString();
    }

    /**
     * methode qui permet de recuperer les differentes extentions possible pour ce filtre
     *
     * @return extention possible
     */
    public List<String> getExtentions() {
        return nomsExtentionAcceptee;
    }

}
