package introspection;

import modele.classe.Methode;
import modele.classe.ObjectClasse;
import modele.classe.Statut;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

/**
 * FacadeIntrospection permet de creer l'ObjetClasse a partir d'un File
 * 
 * @author Guillaume renard
 *
 */
public class FacadeIntrospection {

	public FacadeIntrospection() {

	} 

	/**
	 * methode qui permet de construire ObjetClasse a partir du parametre File on
	 * cherche a avoir la classe objet du File puis on cree ObjetClasse en fonction
	 * de lui
	 * 
	 * @param f
	 * @return
	 */
	public ObjectClasse introspectionClasse(File f)  {
		Class<?> cls = new ChargementClasse().chargerClass(f, 1);
		ObjectClasse obc=null;
		

		//Les methodes

		//liste de toutes les methodes declare, quel soit public/privés/protected
		Method[] meth = cls.getDeclaredMethods();
		for (Method methode : meth) {
			String nom= methode.getName();
			String typeRetour= methode.getReturnType().getName();

			Class[] c = methode.getParameterTypes();
			List<String> tabList = null;
			for (Class cla : c) {
				tabList.add(cla.getName());
			}
			//modifier qui change en fonction de ce que contient la methode, public/privée/abstraite/finale/...
			int mMet = methode.getModifiers();

			Statut s =avoirStatut(mMet);
			boolean statique = Modifier.isStatic(mMet);
			boolean abstraite = Modifier.isAbstract(mMet);
			boolean finale= Modifier.isFinal(mMet);

			//creation de la methode
			Methode m= new Methode(nom, s, typeRetour, tabList,abstraite, statique, finale);
			obc.ajouterMethode(m);
		}

		return obc;
	}

	/**
	 * methode qui en fonction du int quel recoit retourne un statut
	 *
	 * @param mod
	 * @return
	 */
	private Statut avoirStatut(int mod){
		Statut s = null;
		if (Modifier.isPrivate(mod)) {
			s = Statut.PRIVATE;
		} else if (Modifier.isProtected(mod)) {
			s = Statut.PROTECTED;
		} else if (Modifier.isPublic(mod)) {
			s = Statut.PUBLIC;
		}
		return s;
	}

}
