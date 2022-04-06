package principal;

import controleur.ControleurArborescence;
import controleur.ControleurClavier;
import controleur.ControleurDiagramme;
import controleur.ControleurMenu;
import modele.Modele;
import vue.AffichageErreur;
import vue.VueArborescence;
import vue.VueDiagramme;

import javax.swing.*;
import javax.swing.tree.TreeSelectionModel;
import java.awt.*;

public class Principale {

    /**
     * Constante de la taille par default du logiciel
     */
    private static final int LARGEUR_FEN=750, HAUTEUR_FEN=500, TAILLE_MENU=50, TAILLE_ARBORESCENCE=150;

    /**
     * Programme principal
     */
    public static void main(String[] args) {
        //Modele
        Modele modele = new Modele();


        //JFrame
        JFrame frame = new JFrame("EzUML");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //defini la taille minimum de la fenetre

        //on instancie l'affichage d'erreur
        AffichageErreur.instancier(frame);

        //Controleur
        ControleurMenu controleurMenu= new ControleurMenu(modele, frame);
        ControleurClavier controleurClavier = new ControleurClavier(modele, controleurMenu);
        ControleurDiagramme controleurDiagramme = new ControleurDiagramme(modele);
        ControleurArborescence controleurArborescence = new ControleurArborescence(modele, frame);
        //on ajoute le controleur
        frame.addKeyListener(controleurClavier);
        frame.setFocusable(true);
        frame.requestFocus();

        //ContentPane
        JPanel contentPane = (JPanel) frame.getContentPane();
        contentPane.setPreferredSize(new Dimension(LARGEUR_FEN,  HAUTEUR_FEN));
        contentPane.setLayout(new BorderLayout());


        //Menu
        Font fontMenu = new Font("Arial", Font.BOLD, 16);

        JMenuBar menuBar = new JMenuBar();
        JMenu fichier = new JMenu("Fichier");
        fichier.setFont(fontMenu);
        JMenu edit = new JMenu("Edition");
        edit.setFont(fontMenu);
        JMenuItem rechargerUniquementDernierChargement = new JMenuItem("Recharger dernier chargement");
        rechargerUniquementDernierChargement.addActionListener(controleurMenu);
        rechargerUniquementDernierChargement.setFont(fontMenu);
        JMenuItem chargerFileClasse = new JMenuItem("Charger fichiers .class");
        chargerFileClasse.addActionListener(controleurMenu);
        chargerFileClasse.setFont(fontMenu);
        JMenuItem chargerDossier = new JMenuItem("Charger dossier");
        chargerDossier.addActionListener(controleurMenu);
        chargerDossier.setFont(fontMenu);
        JMenuItem retirerSelectionDiagramme = new JMenuItem("Retirer selection du diagramme");
        retirerSelectionDiagramme.addActionListener(controleurMenu);
        retirerSelectionDiagramme.setFont(fontMenu);
        JMenuItem save = new JMenuItem("Sauvegarder .ezuml");
        save.addActionListener(controleurMenu);
        save.setFont(fontMenu);
        JMenuItem charger = new JMenuItem("Charger .ezuml");
        charger.addActionListener(controleurMenu);
        charger.setFont(fontMenu);
        JMenuItem exporter = new JMenuItem("Exporter");
        exporter.addActionListener(controleurMenu);
        exporter.setFont(fontMenu);

        menuBar.add(fichier);
        menuBar.add(edit);
        fichier.add(chargerFileClasse);
        fichier.add(chargerDossier);
        fichier.add(charger);
        fichier.add(save);
        fichier.add(exporter);
        edit.add(retirerSelectionDiagramme);
        edit.add(rechargerUniquementDernierChargement);

        contentPane.add(menuBar, BorderLayout.NORTH);

        //PanelPrincipal
        JSplitPane panelPrincipal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        panelPrincipal.setOneTouchExpandable(true);

        contentPane.add(panelPrincipal, BorderLayout.CENTER);

        //VueArborescence
        VueArborescence vueArborescence = new VueArborescence(modele);
        panelPrincipal.setDividerLocation(TAILLE_ARBORESCENCE);
        controleurArborescence.setArbre(vueArborescence.getBase());
        vueArborescence.getBase().getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
        vueArborescence.getBase().addMouseListener(controleurArborescence);
        vueArborescence.addMouseListener(controleurArborescence);

        modele.ajouterObservateur(vueArborescence);
        panelPrincipal.setLeftComponent(vueArborescence);

        //VueDiagramme
        VueDiagramme vueDiagramme = new VueDiagramme(modele);

        modele.ajouterObservateur(vueDiagramme);
        panelPrincipal.setRightComponent(vueDiagramme);
        modele.setVueDiagramme(vueDiagramme);
        vueDiagramme.addMouseListener(controleurDiagramme);
        vueDiagramme.addMouseMotionListener(controleurDiagramme);

        //affichage
        frame.setVisible(true);
        frame.pack();
    }

}
