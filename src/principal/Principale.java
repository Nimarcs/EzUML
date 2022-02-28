package principal;

import controleur.ControleurClavier;
import controleur.ControleurDiagramme;
import controleur.ControleurMenu;
import modele.Modele;
import vue.VueArborescence;
import vue.VueDiagramme;

import javax.swing.*;
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

        //Controleur
        ControleurClavier controleurClavier = new ControleurClavier(modele);
        ControleurMenu controleurMenu= new ControleurMenu(modele, frame);
        ControleurDiagramme controleurDiagramme = new ControleurDiagramme(modele);
        //on ajoute le controleur
        frame.addKeyListener(controleurClavier);

        //ContentPane
        JPanel contentPane = (JPanel) frame.getContentPane();
        contentPane.setPreferredSize(new Dimension(LARGEUR_FEN,  HAUTEUR_FEN));
        contentPane.setLayout(new BorderLayout());

        //Menu
        JPanel menu = new JPanel();
        menu.setPreferredSize(new Dimension(LARGEUR_FEN,TAILLE_MENU));

        //ajout d'un bouton ouvrir
        JButton boutonOuvrir = new JButton ("Ouvrir");
        menu.add(boutonOuvrir);
        boutonOuvrir.addActionListener(controleurMenu);

        contentPane.add(menu, BorderLayout.NORTH);

        //PanelPrincipal
        JSplitPane panelPrincipal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        panelPrincipal.setOneTouchExpandable(true);

        contentPane.add(panelPrincipal, BorderLayout.CENTER);

        //VueArborescence
        VueArborescence vueArborescence = new VueArborescence();
        panelPrincipal.setDividerLocation(TAILLE_ARBORESCENCE);

        modele.ajouterObservateur(vueArborescence);
        panelPrincipal.setLeftComponent(vueArborescence);

        //VueDiagramme
        VueDiagramme vueDiagramme = new VueDiagramme(modele);

        modele.ajouterObservateur(vueDiagramme);
        panelPrincipal.setRightComponent(vueDiagramme);
        modele.setVueDiagramme(vueDiagramme);
        vueDiagramme.addMouseListener(controleurDiagramme);

        //affichage
        frame.setVisible(true);
        frame.pack();
    }

}
