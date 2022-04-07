package vue;

import modele.Sujet;

/**
 * Interface Observateur
 * Cette interface permet une implementation aisée de différentes vues
 */
public interface Observateur {

	/**
	 * Cette méthode est appelée lorsque le sujet est modifiée et que l'observateur doit en être informée
	 * @param sujet Sujet observée
	 */
	public void actualiser(Sujet sujet);

}
