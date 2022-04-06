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
import java.awt.image.BufferedImage;
import java.util.Objects;

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
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Principale.class.getResource("icon.png")));
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
        JMenuItem rechargerUniquementDernierChargement = new JMenuItem(ControleurMenu.RECHARGER_DERNIER_CHARGEMENT_TXT);
        rechargerUniquementDernierChargement.addActionListener(controleurMenu);
        rechargerUniquementDernierChargement.setFont(fontMenu);
        JMenuItem chargerFileClasse = new JMenuItem(ControleurMenu.CHARGER_FICHIERS_CLASS_TXT);
        chargerFileClasse.addActionListener(controleurMenu);
        chargerFileClasse.setFont(fontMenu);
        JMenuItem chargerDossier = new JMenuItem(ControleurMenu.CHARGER_DOSSIER_TXT);
        chargerDossier.addActionListener(controleurMenu);
        chargerDossier.setFont(fontMenu);
        JMenuItem retirerSelectionDiagramme = new JMenuItem(ControleurMenu.RETIRER_SELECTION_DU_DIAGRAMME_TXT);
        retirerSelectionDiagramme.addActionListener(controleurMenu);
        retirerSelectionDiagramme.setFont(fontMenu);
        JMenuItem save = new JMenuItem(ControleurMenu.SAUVEGARDER_EZUML_TXT);
        save.addActionListener(controleurMenu);
        save.setFont(fontMenu);
        JMenuItem charger = new JMenuItem(ControleurMenu.CHARGER_EZUML_TXT);
        charger.addActionListener(controleurMenu);
        charger.setFont(fontMenu);
        JMenuItem exporter = new JMenuItem(ControleurMenu.EXPORTER_TXT);
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
