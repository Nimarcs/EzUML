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
        ControleurClavier controleurClavier = new ControleurClavier(modele);
        ControleurMenu controleurMenu= new ControleurMenu(modele, frame);
        ControleurDiagramme controleurDiagramme = new ControleurDiagramme(modele);
        ControleurArborescence controleurArborescence = new ControleurArborescence(modele);
        //on ajoute le controleur
        frame.addKeyListener(controleurClavier);

        //ContentPane
        JPanel contentPane = (JPanel) frame.getContentPane();
        contentPane.setPreferredSize(new Dimension(LARGEUR_FEN,  HAUTEUR_FEN));
        contentPane.setLayout(new BorderLayout());

        //Menu
        JPanel menu = new JPanel();
        menu.setLayout(new GridLayout(1, 5));
        menu.setPreferredSize(new Dimension(LARGEUR_FEN,TAILLE_MENU));

        //ajout d'un bouton charger
        JButton boutonCharger = new JButton ("Charger un .class");
        menu.add(boutonCharger);
        boutonCharger.addActionListener(controleurMenu);
        //ajout d'un bouton nouveau
        JButton boutonNouveau = new JButton ("Nouveau");
        menu.add(boutonNouveau);
        boutonNouveau.addActionListener(controleurMenu);
        //ajout d'un bouton Retirer Selection du diagramme
        JButton boutonRetirerDuDiag = new JButton ("Retirer Selection du diagramme");
        menu.add(boutonRetirerDuDiag);
        boutonRetirerDuDiag.addActionListener(controleurMenu);
        //ajout d'un bouton pour sauvegarder un diagramme
        JButton boutonSauver = new JButton("Sauvegarder");
        menu.add(boutonSauver);
        boutonSauver.addActionListener(controleurMenu);
        //ajout d'un bouton pour charger unn diagramme sauvegarder
        JButton boutonChargerDiag = new JButton("Charger diagramme");
        menu.add(boutonChargerDiag);
        boutonChargerDiag.addActionListener(controleurMenu);
        //ajout d'un bouton Exporter en image
        JButton boutonExporterImage = new JButton ("Exporter en image");
        menu.add(boutonExporterImage);
        boutonExporterImage.addActionListener(controleurMenu);

        contentPane.add(menu, BorderLayout.NORTH);

        //PanelPrincipal
        JSplitPane panelPrincipal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        panelPrincipal.setOneTouchExpandable(true);

        contentPane.add(panelPrincipal, BorderLayout.CENTER);

        //VueArborescence
        VueArborescence vueArborescence = new VueArborescence();
        panelPrincipal.setDividerLocation(TAILLE_ARBORESCENCE);
        vueArborescence.getBase().getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);;
        vueArborescence.getBase().addTreeSelectionListener(controleurArborescence);

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
