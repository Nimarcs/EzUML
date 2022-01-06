package vue;

import modele.Sujet;


/**
 * 
 * Interface Observateur
 * 
 * Elle permet une implementation aisee de differentes vues
 */


public interface Observateur {
	public void actualiser(Sujet sujet);

}
