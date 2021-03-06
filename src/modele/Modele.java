// PACKAGE
package modele;

// IMPORTS
import introspection.FacadeIntrospection;
import modele.classe.Attribut;
import modele.classe.Extendable;
import modele.classe.ObjectClasse;
import modele.classe.TypeClasse;
import vue.AffichageErreur;
import vue.VueDiagramme;
import java.awt.*;
import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Modele permet de mettre en commun les differents elements du logiciel
 * Contient ce qui est necessaire pour armoniser le diagramme, l'arborescence et les differents controleurs et vues
 */
public class Modele extends Sujet implements Serializable {


    //Attributs

    /**
     * Object qui contient les differents ObjectClasse chargee
     * Contient egalement l'arborescence
     */
    private CollectionObjectClasse collectionObjectClasse;

    /**
     * Booleen a vrai si on affiche les packages dans lesquels sont les classes sous forme textuelle, faux sinon
     */
    private boolean afficherPackage;

    /**
     * Liste des classes selectionne au moment courant
     * On ne peut selectionner que des classes
     */
    private transient List<ObjectClasse> selection;

    /**
     * Fichier charge couramment, sert a l'arboresance
     */
    private File dossierCourant;

    /**
     * Facade qui permet d'obtenir les ObjectClasse a partir des fichiers
     */
    private transient FacadeIntrospection facade;

    /**
     * Liste des fleches actuellement represente
     */
    private List<FlecheAssociation> associations;

    /**
     * Represente le decalage de la vision sur le diagramme par rapport au 0 0
     */
    private int decalageX, decalageY;

    /**
     * On stocke la vueDiagramme, pour etre capable de savoir quel ObjectClasse est à une position
     */
    private transient VueDiagramme vueDiagramme;

    //CONSTRUCTEURS

    /**
     * Contructeur de Modele
     * initialise les attributs de modeles
     */
    public Modele() {
        collectionObjectClasse = new CollectionObjectClasse();
        selection = new LinkedList<>();
        afficherPackage = true;
        vueDiagramme = null;
        dossierCourant = null; // pas de dossier chargée de base
        facade = new FacadeIntrospection();
        associations = new LinkedList<>();
    }

    //METHODES

    /**
     * Methode qui permet de selectionner une classe
     * Si la classe est deja selectionne, cela la desélectionne
     *
     * @param objectClasse ObjectClasse : a selectionner ou a deselectionner
     */
    public void selectionnerUneClasse(ObjectClasse objectClasse) {
        //on regarde si la classe est selectionner
        if (selection.contains(objectClasse)) {
            selection.remove(objectClasse);
            //si elle est selectionne, la deselectionne
        } else {
            //sinon la selectionne
            selection.add(objectClasse);
        }
        // on notifie les observateurs
        notifierObservateurs();
    }

    /**
     * Methode qui vide la selection
     */
    public void deselectionner() {
        selection.clear();
        notifierObservateurs();
    }


    /**
     * Methode qui permet d'alterner entre masquer ou afficher les packages sous forme textuelle
     */
    public void changerAffichagePackage() {
        afficherPackage = !afficherPackage;
        notifierObservateurs();
    }

    /**
     * Methode qui permet de reinitialiser le diagramme
     */
    public void reintialiserDiagramme() {
        collectionObjectClasse.reintialiserDonnee(); //on vide toutes les classes chargées en remplaçant toute celle deja charge
        deselectionner(); // on deselectionne tout
        chargerArborescenceProjet(dossierCourant); //on recharger le fichier charge en cours
    }

    /**
     * Methode qui permet de charger l'arborescence et les differents fichier le composant
     * Appelle la methode privee adapte et change le dossier projet
     *
     * @param f fichier a tester
     */
    public void chargerArborescenceProjet(File f) {
        //on apelle la methode privee qui charge tous les fichiers depuis le fichier donnee
        chargerArborescenceProjetRecursif(f);

        //on modifie quel est le dernier dossier charge du projet
        dossierCourant = f;
        notifierObservateurs();
    }

    /**
     * Methode qui va charger de maniere recursive tous les fichiers .class qui sont accessible depuis le fichier fournit
     * <p>
     * Si le fichier est un repertoire, on appele recursivement tous les fichiers fils qu'il contient
     * Sinon on essaie de le charger en tant que classe
     *
     * @param f fichier fourni
     */
    private void chargerArborescenceProjetRecursif(File f) {
        if (f == null) return; //si c'est null on ne fait rien

        //si c'est un fichier on tente de la charger
        if (f.isFile()) {
            chargerClasse(f);
        } else {
            //sinon on regarde si c'est un repertoire
            if (f.isDirectory()) {
                //on recupere les fils
                File[] fils = f.listFiles();

                if (fils == null)
                    throw new IllegalStateException("f est cense etre un repertoire"); //Erreur suppose impossible

                //on parcours les fils
                for (File enfant : fils) {
                    chargerArborescenceProjetRecursif(enfant);//on appelle recursivement sur tout les enfants
                }
            }

        }
    }

    /**
     * Methode qui permet de charger une classe en lui donnant un fichier
     *
     * @param f fichier a charger
     */
    private void chargerClasse(File f) {
        //si le fichier est pas null
        if (f != null) {

            //si le fichier est un .class
            if (f.isFile() && f.getName().contains(".class")) {

                //on recupere l'objectClasse
                ObjectClasse o;
                try {
                    o = facade.introspectionClasse(f);

                } catch (MalformedURLException e) {
                    AffichageErreur.getInstance().afficherErreur("Erreur lors du chargement de la classe");
                    return;
                }
                collectionObjectClasse.ajouterObjectClasse(o);
                //ajouterClasse(collectionObjectClasse.getObjectClasse(o.getNomObjectClasse()), 0 , 0);

            }
        }
    }


    /**
     * Methode qui permet d'ajouter un objectClasse chargee au diagramme
     *
     * @param objectClasse objectClasse a ajouter au diagramme
     * @param x            position sur l'axe des abscisses de la classe sur le diagramme lors de l'ajout
     * @param y            position sur l'axe des ordonnees de la classe sur le diagramme lors de l'ajout
     */
    public void ajouterClasse(ObjectClasse objectClasse, int x, int y) {
        assert objectClasse != null;
        assert collectionObjectClasse.verifierClasseCharge(objectClasse) : "La classe doit etre chargee";

        //on change les options de la classe en elle meme
        objectClasse.changerVisibilite(true);
        objectClasse.setPosition(x, y);

        //fleches dont on est la source
        //on parcourt les types des attributs
        for (Attribut a : objectClasse.getAttributs()) {

            //on verifie si le type correspond a une classe chargee

            ObjectClasse o = uniformisationNomObjectClasse(a);

            if (o != null) { // on verifie que la classe est chargée

                if (o.isVisible()) {
                    //elle est dans le diagramme
                    transformerEnFleche(objectClasse, a, o);
                }
            }
        }

        //fleches dont on est la destination
        //on parcourt les objectClasse charge qui sont visible
        for (ObjectClasse o : collectionObjectClasse.getClassesChargees().stream().filter(ObjectClasse::isVisible).collect(Collectors.toList())) {

            //on parcourt les attributs qui sont visible
            for (Attribut a : o.getAttributs().stream().filter(Attribut::isVisible).collect(Collectors.toList())) {
                ObjectClasse oc = uniformisationNomObjectClasse(a);
                //si l'attribut corresponds a l'object classe
                if (oc != null && oc.equals(objectClasse)) {
                    //on transforme en fleche
                    transformerEnFleche(o, a, objectClasse);
                }

            }

        }

        notifierObservateurs(); // on notifie les observers
    }

    /**
     * Methode qui par son attribut donne en parametre, retourne l'objectClasse correspondant
     * Pour cela, on doit formater le nom pour permettre de retrouver la bonne classe chargée (Fonctionne pour les listes, les tableaux et les types simples)
     *
     * @param a Attribut: dont on extrait l'objectClasse
     * @return ObjectClasse bien
     */
    public ObjectClasse uniformisationNomObjectClasse(Attribut a) {

        if (a.getTypeAttribut().contains("<") && a.getTypeAttribut().contains(">")) {
            // Si l'attribut est une collection, d'une ou plusieurs types
            String[] tabTmp = a.getTypeAttribut().split(Pattern.quote("<")); // tabTmp coupe la string en deux a partie du char <
            String res = tabTmp[tabTmp.length - 1];
            return collectionObjectClasse.getObjectClasse(res.substring(0, res.length() - 1));
        } else if (a.getTypeAttribut().matches("\\[L(.*)")) {
            // Un attribut tableau est de la form [L..., il faut donc retirer les deux premier caracteres
            return collectionObjectClasse.getObjectClasse((a.getTypeAttribut().substring(2, a.getTypeAttribut().length() - 1)));
        } else { // sinon, pour un cas simple, on recupére juste le nom du type de l'attribut en fin de string
            return collectionObjectClasse.getObjectClasse(a.getTypeAttribut());
        }
    }

    /**
     * Methode pour deplacer une selection de classe sur le diagramme
     *
     * @param x deplacement sur l'axe des abscisses
     * @param y deplacement sur l'axe des ordonnees
     */
    public void deplacerClasseSelectionne(int x, int y) {

        for (ObjectClasse objectClasse : selection) {//on parcours les objects classes selectionne

            if (collectionObjectClasse.verifierClasseCharge(objectClasse)) {//on verifie que la classe est chargee
                //On deplace la classe courante
                objectClasse.deplacer(x, y);
                notifierObservateurs();
            }
        }
    }

    /**
     * Methode qui permet de retirer du diagramme des classes chargees
     */
    public void retirerClasseSelectionne() {

        for (ObjectClasse objectClasse : selection) {//on parcours les objects classes selectionne

            if (collectionObjectClasse.verifierClasseCharge(objectClasse)) {//on verifie que la classe est chargee

                //on retire les fleches liees

                //on parcourt toutes les fleches
                for (FlecheAssociation f : associations) {
                    //si la fleche est liee
                    if (f.getDest().equals(objectClasse) || f.getSrc().equals(objectClasse)) {
                        //on la transforme en attribut (on "supprime" la fleche)
                        transformerEnAttribut(f);
                    }
                }

                //on retire du diagramme la classe courante
                objectClasse.changerVisibilite(false);
                notifierObservateurs();
            }
        }
    }


    /**
     * Methode qui permet de decharger les classes selectionnees
     */
    public void dechargerClasseSelectionne() {

        //on retire du diagramme les classe
        retirerClasseSelectionne();
        collectionObjectClasse.dechargerListeClasse(selection);

        //on vide la selection puisqu'on a decharge tout ce qui etais selectionne
        deselectionner();

        notifierObservateurs();
    }

    /**
     * Methode qui permet de transformer un attribut d'une classe en une fleche d'association
     * Si la destination de la fleche n'est pas trouve ou pas affiche, on ne fait rien
     *
     * @param objectClasseSrc  objectClasse d'ou viens l'attribut
     * @param objectClasseDest objectClasse ou qui est le type de l'attribut
     * @param attribut         attribut a transformer en fleche d'association
     */
    public void transformerEnFleche(ObjectClasse objectClasseSrc, Attribut attribut, ObjectClasse objectClasseDest) {
        //on verifie que tout est correct
        assert objectClasseSrc.getAttributs().contains(attribut) : "L'attribut doit etre un attribut de l'object classe";
        assert objectClasseSrc.isVisible() : "L'object classe doit etre affiche dans le diagramme";
        assert objectClasseDest.isVisible() : "L'object classe doit etre affiche dans le diagramme";
        assert attribut.isVisible() : "l'attribut ne doit pas etre masque";

        // on ajoute la nouvelle flèche assosciation et on change la visibilité de l'attribut en false
        associations.add(new FlecheAssociation(objectClasseSrc, objectClasseDest, attribut));
        attribut.changerVisibilite(false);
    }

    /**
     * Methode qui permet de transformer une fleche d'association en attribut
     * Si l'attribut n'est pas trouve on ne fait rien
     *
     * @param association fleche d'association a transformer
     */
    public void transformerEnAttribut(FlecheAssociation association) {
        ObjectClasse source = association.getSrc();
        ObjectClasse dest = association.getDest();

        boolean trouve = false;
        // pour tous les attributs
        for (Attribut a : source.getAttributs()) {
            ObjectClasse OCattribut = uniformisationNomObjectClasse(a); // on recherche l'objectClasse qui représente le type de l'attribut
            if (OCattribut != null) { //on verifie si c'est une classe chargée
                String nomObjectclasse = OCattribut.getNomObjectClasse();
                if (nomObjectclasse.equals(dest.getNomObjectClasse()) && a.getNomAttribut().equals(association.getNom().substring(2))) {
                    // on vérifie qu'on a bien trouvé le bonne attribut qu'on retransforme en attribut
                    a.changerVisibilite(true);
                    trouve = true;
                }
            }
        }
        // on supprime la flèche si on a trouvé l'attribut
        if (trouve) associations.remove(association);
    }

    /**
     * Methode qui fournit objectClasse a une position donnee
     *
     * @param x position donnee sur l'axe des abscisses
     * @param y position donnee sur l'axe des ordonnees
     * @return ObjectClasse trouve
     * @throws ClassNotFoundException renvoyé si l'objectClasse n'est pas trouvé
     */
    public ObjectClasse getObjectClasseEnPosition(int x, int y) throws ClassNotFoundException {

        //l'objectclasse trouve sera stocke dans cette variable
        ObjectClasse res = null;

        //L'affichage est inverse donc on inverse
        List<ObjectClasse> classesChargeeInversee = new ArrayList<>(collectionObjectClasse.getClassesChargees());
        Collections.reverse(classesChargeeInversee);

        //on prepare un parcours des objectClasses
        Iterator<ObjectClasse> ite = classesChargeeInversee.iterator();
        boolean trouve = false;

        //on parcourt les differents objectClasse
        while (!trouve && ite.hasNext()) {
            ObjectClasse o = ite.next();
            if (!o.isVisible()) continue;

            //on calcule la zone dans laquelle est l'objectClasse
            int minX = o.getX() + decalageX, maxX = minX + vueDiagramme.calculerLargeur(o);
            int minY = o.getY() + decalageY, maxY = minY + vueDiagramme.calculerHauteur(o);

            //on regarde si l'objectClasse est dans la zone
            if ((minX <= x && x <= maxX) && (minY <= y && y <= maxY)) {

                //on a trouve le resultat
                res = o;
                trouve = true;
            }
        }

        //si on a pas trouve on renvoie l'exception correspondante
        if (!trouve) throw new ClassNotFoundException();
        return res;
    }

    /**
     * recupere les dimentions du diagramme
     *
     * @return Rectangle qui correspond a la zone de capture
     */
    private Rectangle getBoundsDiagramme() {
        int xmin, xmax, ymin, ymax;
        Iterator<ObjectClasse> ite = collectionObjectClasse.getClassesChargees().stream().filter(ObjectClasse::isVisible).iterator();
        //valeur initiale
        if (ite.hasNext()) {
            ObjectClasse o = ite.next();
            xmax = o.getX() + vueDiagramme.calculerLargeur(o);
            xmin = o.getX();
            ymax = o.getY() + vueDiagramme.calculerHauteur(o);
            ymin = o.getY();
        } else {
            throw new IllegalStateException("Le diagramme ne doit pas etre vide");
        }
        //on cherche le max et min parmi le reste
        while (ite.hasNext()) {
            ObjectClasse o = ite.next();
            if (o.getX() + vueDiagramme.calculerLargeur(o) > xmax) xmax = o.getX() + vueDiagramme.calculerLargeur(o);
            if (o.getX() < xmin) xmin = o.getX();
            if (o.getY() + vueDiagramme.calculerHauteur(o) > ymax) ymax = o.getY() + vueDiagramme.calculerHauteur(o);
            if (o.getY() < ymin) ymin = o.getY();
        }
        Dimension dimLegende = vueDiagramme.getDimensionsLegende();
        return new Rectangle(xmin - 10, ymin - 10, Math.abs(xmax - xmin) + 20 + dimLegende.width, Math.abs(ymax - ymin) + 20 + dimLegende.height);
    }

    /**
     * Methode qui permet d'exporter le diagramme dans un fichier
     *
     * @param typeImage     type d'image dans lequel on veut enregistrer l'image
     * @param cheminFichier fichier dans lequel on veut enregistrer l'image
     */
    public void exporterEnImage(String typeImage, String cheminFichier) {
        //valeurs initiale
        Rectangle bounds = getBoundsDiagramme();
        BufferedImage bi = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();

        //on recupere les valeurs avant le changement
        int dX = decalageX;
        int dY = decalageY;
        decalageX = bounds.x * -1;
        decalageY = bounds.y * -1;
        deselectionner();
        vueDiagramme.reorganiserPourCapture(bounds);

        //on fait la capture
        vueDiagramme.paintComponent(g);

        //on remet les valeurs apres le changement
        vueDiagramme.reinitialiserApresCapture();
        decalageX = dX;
        decalageY = dY;

        //on supprime le graphics
        g.dispose();


        try {
            //On cree l'image
            ImageIO.write(bi, typeImage, new FileOutputStream(cheminFichier));
        } catch (IllegalArgumentException | IOException ignored) {
            AffichageErreur.getInstance().afficherErreur("Erreur lors de l'exportation en image " + typeImage);
            //erreur
        }
    }


    /**
     * Methode qui permet d'enregistrer le diagramme que l'on vient de charger
     *
     * @param cheminAbs chemin absolue où l'image sera enregistrer
     */
    public void enregistrement(String cheminAbs) {
        ObjectOutputStream oos = null;

        if (!cheminAbs.endsWith(".ezuml")) {
            cheminAbs = cheminAbs + ".ezuml";
        }

        try {
            final FileOutputStream fichier = new FileOutputStream(cheminAbs);
            oos = new ObjectOutputStream(fichier);
            oos.writeObject(this);
            oos.flush();
        } catch (final java.io.IOException e) {
            //e.printStackTrace();
            AffichageErreur.getInstance().afficherErreur("Erreur lors de la sauvegarde du diagramme");
            return;
        } finally {
            try {
                if (oos != null) {
                    oos.flush();
                    oos.close();
                }
            } catch (final IOException ex) {
                AffichageErreur.getInstance().afficherErreur("Erreur lors de la sauvegarde du diagramme");
                return;
            }

        }
        AffichageErreur.getInstance().afficherMessage("Fichier bien enregistrer à l'emplacement :\n" + cheminAbs);

    }

    /**
     * Methode qui permet de désérialiser un fichier que l'utilisateur à enregistrer
     *
     * @param cheminAbs chemin absolue où l'image sera désérialisé
     */
    public void deserilization(String cheminAbs) {
        ObjectInputStream ois = null;
        try { // on capture les exceptions si l'objectInputStream est mal chargée
            // on désérialise l'objet
            final FileInputStream fichier = new FileInputStream(cheminAbs);
            ois = new ObjectInputStream(fichier);
            final Modele p = (Modele) ois.readObject();

            // on récupère les informations qu'on range dans le modele
            this.collectionObjectClasse = p.getCollectionObjectClasse();
            this.afficherPackage = p.isAfficherPackage();
            this.dossierCourant = p.getDossierCourant();
            this.associations = p.getAssociations();
            // on notifie les observeurs
            notifierObservateurs();
        } catch (java.io.IOException | ClassNotFoundException e) {
            AffichageErreur.getInstance().afficherErreur("Erreur lors du chargement du diagramme");
            return;
        } finally {
            // a la fin, son vérifie si l'object ois se ferme bien
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (final IOException ex) { // s'il ne ferme pas bien, on affiche une erreur
                AffichageErreur.getInstance().afficherErreur("Erreur lors de la fermeture du chargement du diagramme");
                return;
            }
        }
    }

    /**
     * Calcule le nombre de flèches qui ont la meme src et la meme destination
     * Commence du début et parcours la liste association jusqu'à celle donnée en paramètre
     * Methode utile pour l'affichage
     *
     * @param src       ObjectClasse, classe source
     * @param dest      ObjectClasse, classe de destination
     * @param nameAssos String, nom de l'assosciation qui indique la fin de recherche
     * @return
     */
    public int nbOccurencesFleches(ObjectClasse src, ObjectClasse dest, String nameAssos) {
        int res = 0;
        ObjectClasse o;
        // on regarde si deja il y a existe une fleche d'heritage entre la classe source et la classe destination
        if (src.getType() == TypeClasse.CLASSE || src.getType() == TypeClasse.ABSTRACT) {
            o = ((Extendable) src).getObjectClasseExtends();
            if (o != null && o.equals(dest)) res++;
        } else if (dest.getType() == TypeClasse.CLASSE || dest.getType() == TypeClasse.ABSTRACT) {
            o = ((Extendable) dest).getObjectClasseExtends();
            if (o != null && o.equals(src)) res++;
            // on regarde aussi les flèches d'implémentation entre les deux classes
        } else if (src.getListeObjectClasseImplements().contains(dest)) res++;
        else if (dest.getListeObjectClasseImplements().contains(src)) res++;
        FlecheAssociation a = this.associations.get(0);
        int j = 0;
        // on parcours maintenant toutes les assosciations jusqu'à tomber sur celle donéne en parametre
        while (!a.getNom().equals(nameAssos) || !a.getSrc().equals(src) && !a.getDest().equals(dest)) {
            if (a.getSrc().equals(src) || a.getSrc().equals(dest)) res++;
            j++;
            a = this.associations.get(j);
        }
        // on retourne le nombre d'occurences de flèches entre source et destination
        return res;
    }


    //getters setters

    /**
     * getter des packages
     * Source des packages du projet
     * Contient toute les clees vers les classes chargee
     *
     * @return copie du package src
     */
    public Package getPackages() {
        return collectionObjectClasse.getPackages();
    }

    public CollectionObjectClasse getCollectionObjectClasse() {
        return collectionObjectClasse;
    }

    /**
     * getter des classes chargee
     *
     * @return collection d'ObjectClasse chargee
     */
    public Collection<ObjectClasse> getObjectClasses() {
        return collectionObjectClasse.getClassesChargees();
    }

    /**
     * getter d'un objectClasse a partir de son nom complet
     *
     * @param cheminComplet nom complet compose de son chemin en package et de son nom separer par un point
     * @return ObjectClasse correspondant ou null
     */
    public ObjectClasse getObjectClasse(String cheminComplet) {
        return collectionObjectClasse.getObjectClasse(cheminComplet);
    }

    /**
     * getter du dossier courant
     *
     * @return dernier repertoire/fichier charge
     */
    public File getDossierCourant() {
        return dossierCourant;
    }

    /**
     * getter de la selection
     *
     * @return selection courante
     */
    public List<ObjectClasse> getSelection() {
        return selection;
    }

    /**
     * getter de afficherPackage
     *
     * @return booleen vrai si on affiche les packages, faux sinon
     */
    public boolean isAfficherPackage() {
        return afficherPackage;
    }

    /**
     * getter du decalage de l'affichage par rapport au centre
     *
     * @return decalage sur X
     */
    public int getDecalageX() {
        return decalageX;
    }

    /**
     * getter du decalage de l'affichage par rapport au centre
     *
     * @return decalage sur Y
     */
    public int getDecalageY() {
        return decalageY;
    }

    /**
     * permet de decaler l'affichage sur X
     *
     * @param decalageX entier correspondant au decalage sur X
     */
    public void deplacerDecalageX(int decalageX) {
        this.decalageX += decalageX;
        notifierObservateurs();
    }

    /**
     * permet de decaler l'affichage sur Y
     *
     * @param decalageY entier correspondant au decalage sur Y
     */
    public void deplacerDecalageY(int decalageY) {
        this.decalageY += decalageY;
        notifierObservateurs();
    }

    /**
     * Setter de la Vue diagramme
     *
     * @param vueDiagramme vueDiagramme qui affiche le modele
     */
    public void setVueDiagramme(VueDiagramme vueDiagramme) {
        assert vueDiagramme != null;
        this.vueDiagramme = vueDiagramme;
    }

    /**
     * Setter selection
     *
     * @param selection
     */
    public void setSelection(List<ObjectClasse> selection) {
        this.selection = selection;
        notifierObservateurs();
    }

    public VueDiagramme getVueDiagramme() {
        return vueDiagramme;
    }

    /**
     * Getter des associations
     *
     * @return List<FlecheAssociation>
     */
    public List<FlecheAssociation> getAssociations() {
        return associations;
    }
}
