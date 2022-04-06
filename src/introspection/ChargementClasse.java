package introspection;

import vue.AffichageErreur;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.regex.Pattern;


/**
 * ChargementClasse permet de charger un objet classe a partir du chemin absolue d'un File
 *
 */
public class ChargementClasse {


    /**
     * methode qui charge une class a partir d'un File donne
     * Pour faire un classLoder il faut le chemin absolue jusqu'au repertoire du package si il y en a un
     * exemple :
     * chemin absolue :
     * C:\\Users\\guill\\eclipse-workspace\\ReadFichier\\bin\\lireFichier\\lectureTXT.class
     * le chemin pour construire le classLoader doit etre :
     * C:\\Users\\guill\\eclipse-workspace\\ReadFichier\\bin\\
     *
     * avec lireFichier comme package le reste est utilisé pour instancier la classe
     * exemple : lireFichier.lectureTXT
     *
     *
     * probleme, il peut avoir un packages dans en packages dans un packages...
     * et on connait pas le nombre de packages qu'il y a dans le File, la
     * methode teste donc tout d'abord pour aucun package
     * Si le classLoader ne revele aucune erreur, on renvoie la Class
     * sinon  on catch l'execption et on refait la methode pour
     * tester notre File avec un package puis avec 2 package puis ainsi de suite jusqu'a ce que le FIle n'est plus de repertoire ce qui revelera une exception
     *
     * @param f fichier ou l'on veut sa class
     * @param p nombre de fois que la méthode chargerClass est appelé
     * @return Class
     * @throws MalformedURLException
     * @throws ClassNotFoundException
     */
    public Class chargerClass(File f, int p) throws MalformedURLException {
        Class cls = null;

        // on regarde que elle le directory separator du systeme
        String a= File.separator;

        // condition qui regarde que le nombre de repertoire n'est pas superieur a p
        if (p <= compterBackQuote(f.getAbsolutePath(), a )) {
            //tableau qui contient le chemin d'acces juste avant les packages et le nom du fichier avec ses packages
            String[] s = new String[2];
            String cheminAbsolue = f.getAbsolutePath();

            int i = cheminAbsolue.length() - 1;
            int repertoirePackage = 0;


            // boucle qui regarde si on trouve \ ou /(en fonction de a ) et qui s'arrete quand on a trouve le bon
            // nombre qui est celui de p
            while ((i > 0) && (repertoirePackage != p)) {
                // on regarde si le caractere courant est \ ou /
                if (a.charAt(0) == cheminAbsolue.charAt(i)) {
                    repertoirePackage++;
                }
                i--;
            }

            // separe le string en 2 pour pouvoir faire le ClassLoader
            if (i != 0) {
                // la ligne suivante est celle qui va etre un nouveau File sans le fichier et sans les packages associe a lui
                s[0] = cheminAbsolue.substring(0, i + 2);
                // le reste du string est donne pour rechercher
                s[1] = cheminAbsolue.substring(i + 2);
                // on enleve le .class
                s[1] = s[1].split(Pattern.quote(".")+"class")[0];
                // on remplace les \ ou / par des point
                s[1] = s[1].replace(a, ".");

            }
            try {

                // creation du classLoader

                //On le creer a partir de s[0] qui est le chemin absolue du fichier sans celui-ci et sans les packages associes au fichier
                File fl = new File(s[0]);
                URLClassLoader loader = new URLClassLoader(new URL[] { fl.toURL() }, getClass().getClassLoader());

                // la methode forname renvoie la classe associe a s[1] et grace au loader fait ci-dessus
                try {
                    cls = Class.forName(s[1], false, loader);
                } catch( ClassFormatError e ){
                    AffichageErreur.getInstance().afficherErreur("On ne peut pas agir sur votre fichier a l'emplacement :\n"+f.getAbsolutePath());

                    return null;
                }

            } catch (NoClassDefFoundError | ClassNotFoundException e) {
                // execption levee quand le loader n'est pas bon
                // cad le nouveau File n'est pas bon ou le fichier et inexistant
                p = p + 1;
                cls = chargerClass(f, p);
            }
        } else {
            // erreur lorsque l'on a fait toute les possibilités de package
            AffichageErreur.getInstance().afficherErreur("On ne peut pas agir sur votre fichier a l'emplacement :\n"+f.getAbsolutePath());
        }

        return cls;
    }

    /**
     * methode qui compte le nombre de \ ou de / dans notre le string que l'on lui donne
     *
     * @param text
     * @return int nombre de a contenu dans le String
     */
    private int compterBackQuote(String text, String a) {
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == a.charAt(0)) {
                count++;
            }
        }
        return count;
    }
}
