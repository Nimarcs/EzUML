package introspection;

import modele.classe.*;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
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
		Class cls = new ChargementClasse().chargerClass(f, 1);

		ObjectClasse obc=null;
		//les classes
		int m = cls.getModifiers();
		if (cls.isEnum()) {
			obc = new Enumeration(cls.getName(), cls.getPackageName(), 0, 0);
			//Les enum sont considérés comme des attributs
			introAttribut(cls, obc);
		} else if (Modifier.isInterface(m)) {
			obc = new Interface(cls.getName(), cls.getPackageName(), 0, 0);
			//Les Constructeurs
			introConstructeur(cls, obc);
			//les Methodes
			introMethode(cls, obc);
			}else if (Modifier.isAbstract(m)) {
			obc = new Abstraite(cls.getName(), cls.getPackageName(), 0, 0);
			//Les Constructeurs
			introConstructeur(cls,obc);
			//les Methodes
			introMethode(cls, obc);
			//Les attributs
			introAttribut(cls, obc);
			} else {
			obc = new Classe(cls.getName(), cls.getPackageName(), 0, 0);
			//Les Constructeurs
			introConstructeur(cls, obc);
			//les Methodes
			introMethode(cls, obc);
			//Les attributs
			introAttribut(cls, obc);
		}


		return obc;
	}

	/**
	 * methode privée qui permet de faire l'introspection des attributs de l'ObjectClasse en parametre
	 *
	 * @param cls
	 * @param obc
	 * @return
	 */
	private void introAttribut(Class cls, ObjectClasse obc ){
		//Les attributs

		Field[] fdd = cls.getDeclaredFields();

		for (Field attri : fdd) {
			String nom= attri.getName();
			String typeAttribut = attri.getType().getName();

			int mAtt = attri.getModifiers();

			Statut s =avoirStatut(mAtt);
			boolean statique = Modifier.isStatic(mAtt);
			boolean finale= Modifier.isFinal(mAtt);

			Attribut atb = new Attribut(nom, s, typeAttribut, statique, finale);
			obc.ajouterAttribut(atb);

		}
	}

	/**
	 * methode privée qui permet de faire l'introspection des méthodes de l'ObjectClasse en parametre
	 *
	 * @param cls
	 * @param obc
	 * @return
	 */
	private void introMethode(Class cls, ObjectClasse obc ){

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
	}

	/**
	 * methode privée qui permet de faire l'introspection des constructeurs de l'ObjectClasse en parametre
	 * les constructeurs sont considérés comme des méthodes dans l'objectClasse
	 * @param cls
	 * @param obc
	 * @return
	 */
	private void introConstructeur(Class cls, ObjectClasse obc ){
		//Les constructeurs

		Constructor[] cons = cls.getDeclaredConstructors();

		for (Constructor co : cons) {
			String nom=co.getName();
			int mCon = co.getModifiers();
			Statut s =avoirStatut(mCon);

			Class[] c = co.getParameterTypes();
			List<String> tabList = null;
			for (Class cla : c) {
				tabList.add(cla.getName());
			}

			Methode m= new Methode(nom, s, null, tabList,false, false, false);
			obc.ajouterMethode(m);

		}
	}

	/**
	 * methode privée qui en fonction du int quel recoit retourne un statut
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
