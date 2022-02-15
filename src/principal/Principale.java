package principal;

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

        //ContentPane
        JPanel contentPane = (JPanel) frame.getContentPane();
        contentPane.setPreferredSize(new Dimension(LARGEUR_FEN,  HAUTEUR_FEN));
        contentPane.setLayout(new BorderLayout());

        //Menu
        JPanel menu = new JPanel();
        menu.setPreferredSize(new Dimension(LARGEUR_FEN,TAILLE_MENU));

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

        //affichage
        frame.setVisible(true);
        frame.pack();
    }

}