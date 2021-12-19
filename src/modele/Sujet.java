package modele;

import java.util.LinkedList;
import java.util.List;

/**
 * Classe abstraite permettant de devenir le sujet d'une observation
 *
 * Les classes heritant de sujet seront observable selon le patron de conception Observer
 * Classe abstraite necessaire pour l'implementation du patron d'architecture MVC
 *
 * Est une classe abstraite car ne doit pas pouvoir etre instancie.
 *
 * @author Marcus Richier
 */
public abstract class Sujet {

    /**
     * Liste des observateurs qui observe le sujet
     */
    private List<Observateur> listeObservateurs = new LinkedList<>();

    /**
     * Methode qui permet d'ajouter un observateur a la liste des observateurs,
     * s'il est deja present ne fait rien
     * @param observateur observateur a ajouter
     */
    public void ajouterObservateur(Observateur observateur){
        //on verifie s'il existe pas deja
        if (!listeObservateurs.contains(observateur)) {
            //on l'ajoute
            listeObservateurs.add(observateur);
        }
    }

    /**
     * Methode permettant de supprimer un observateur de la liste des observateurs,
     * s'il n'est pas present ne fait rien
     * @param observateur observateur a supprimer
     */
    public void supprimerObservateur(Observateur observateur){
        //la methode verifie deja s'il existe
        listeObservateurs.remove(observateur);
    }

    /**
     * methode qui notifie et actualise l'ensemble des observateurs enregistree
     */
    public void notifierObservateurs(){
        //on parcours la liste
        for (Observateur observateur: listeObservateurs) {
            //on actualise les observateurs
            observateur.actualiser(this);
        }

    }

}
