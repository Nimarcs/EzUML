package introspection;

import modele.classe.*;

import java.io.File;
import java.lang.reflect.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
	 * @param f chemin absolue du fichier ou l'on veut faire un diagramme
	 * @return
	 */
	public ObjectClasse introspectionClasse(File f) throws MalformedURLException {
		Class cls = new ChargementClasse().chargerClass(f, 1);

		if(cls==null){
			return null;
		} else {
			ObjectClasse obc= this.faireIntrospection(cls);
			return obc;
		}

	}


	/**
	 * methode qui permet de construire l'ObjetClasse a partir de la classe en parametre
	 *
	 * @param cls
	 * @return
	 */
	private ObjectClasse faireIntrospection(Class cls){
		ObjectClasse obc = null;

		//on regarde si les classes sont des Enum, Interfaces, Abstract ou une Classe
		int m = cls.getModifiers();
		if (cls.isEnum()) {
			 obc = new Enumeration(cls.getName(), cls.getPackage().getName(), 0, 0);
		} else if (Modifier.isInterface(m)) {
			 obc = new Interface(cls.getName(), cls.getPackage().getName(), 0, 0);
		}else if (Modifier.isAbstract(m)) {

			//on regarde si la classe herite d'une autre classe
			if (!cls.getSuperclass().getName().equals("java.lang.Object")){
				Extendable e;
				try{
					e= new Abstraite(cls.getName(), cls.getPackage().getName(), 0, 0);
				} catch (NullPointerException ex ){
					e= new Abstraite(cls.getName(), "", 0, 0);
				}
				int mExtend =cls.getSuperclass().getModifiers();
				if ( Modifier.isAbstract(mExtend)) {
					e.changerExtends((Extendable) new Abstraite(cls.getSuperclass().getName(), cls.getSuperclass().getPackage().getName(), 0, 0));
					obc = e;

				} else{
					e.changerExtends((Extendable) new Classe(cls.getSuperclass().getName(), cls.getSuperclass().getPackage().getName(), 0, 0));
					obc = e;
				}
			} else {
				try{
					obc = new Abstraite(cls.getName(), cls.getPackage().getName(), 0, 0);
				} catch (NullPointerException e ){
					obc = new Abstraite(cls.getName(), "" , 0, 0);
				}
			}


			} else {

			//on regarde si il herite d'une classe
			if (!cls.getSuperclass().getName().equals("java.lang.Object")){
				Extendable e;
				try{
					e= new Classe(cls.getName(), cls.getPackage().getName(), 0, 0);
				} catch (NullPointerException ex ){
					e= new Classe(cls.getName(), "", 0, 0);
				}

				int mExtend =cls.getSuperclass().getModifiers();
				//on regarde si la classe hérité est une classe abstraite ou une classe
				if ( Modifier.isAbstract(mExtend)) {
					e.changerExtends((Extendable) new Abstraite(cls.getSuperclass().getName(), cls.getSuperclass().getPackage().getName(), 0, 0));
					obc = e;
				} else{
					e.changerExtends((Extendable) new Classe(cls.getSuperclass().getName(), cls.getSuperclass().getPackage().getName(), 0, 0));
					obc = e;
				}

			} else {
				try{
					obc = new Classe(cls.getName(), cls.getPackage().getName(), 0, 0);
				} catch (NullPointerException e ){
					obc = new Classe(cls.getName(), "" , 0, 0);
				}
			}
		}

		//Puis on regarde les methode, attributs et les constructeurs
		if (!cls.isEnum()){
			//Les Constructeurs
			introConstructeur(cls, obc);
			//les Methodes
			introMethode(cls, obc);
			//Les attributs
			introAttribut(cls, obc);

			//methode qui donne toutes les interfaces de la classe introspecter
			Class[] claInter= cls.getInterfaces();
			//ces classe on les developpe en faisant une nouvelle introspection
			for (Class inter : claInter) {
				//peut avoir probleme que l'on retourne pas une interface, mais normalement cela marche
				//on trouve l'interface tant quel ce trouve dans le meme packages ou bien un import
				obc.ajouterImplements((Interface) this.faireIntrospection(inter));
			}
		} else {
			//Les enum sont considérés comme des attributs
			introAttribut(cls, obc);
		}

		return obc;
	}

	/**
	 * methode privée qui permet de faire l'introspection des attributs de la classe en parametre
	 * Pour ajouter un ou des attributs a l'bjectClasse
	 * @param cls
	 * @param obc
	 * @return
	 */
	private void introAttribut(Class cls, ObjectClasse obc ){
		//methode qui donne toute les attributs declare dans cls
		Field[] fdd = cls.getDeclaredFields();

		for (Field attri : fdd) {
			String nom= attri.getName();
			String typeAttribut = attri.getAnnotatedType().getType().getTypeName();
			System.out.println("1 : "+ typeAttribut);

			if (Pattern.matches("class(.*)", typeAttribut)) typeAttribut = typeAttribut.substring(6);

			// la fonction getmodifiers est un int qui change en fonction des particularites du Field
			int mAtt = attri.getModifiers();

			//methode qui regarde si l'attribut est public, privée, protected
			Statut s =avoirStatut(mAtt);

			boolean statique = Modifier.isStatic(mAtt);
			boolean finale= Modifier.isFinal(mAtt);

			Attribut atb = new Attribut(nom, s, typeAttribut, statique, finale);
			//ajout de l'attribut dans l'ObjectClass
			obc.ajouterAttribut(atb);

		}
	}

	/**
	 * methode privée qui permet de faire l'introspection des méthodes de la classe en parametre
	 * Pour ajouter une ou plusieurs methode a l'bjectClasse
	 * @param cls
	 * @param obc
	 * @return
	 */
	private void introMethode(Class cls, ObjectClasse obc ){

		//liste de toutes les methodes declare, quel soit public/privés/protected
		Method[] meth = cls.getDeclaredMethods();
		for (Method methode : meth) {
			String nom= methode.getName();
			String typeRetour= methode.getAnnotatedReturnType().getType().getTypeName();

			//tableau qui contient le nom des classes en parametres
			AnnotatedType[] c = methode.getAnnotatedParameterTypes();
			List<String> tabList = new ArrayList<String>();
			for (AnnotatedType cla : c) {
				tabList.add(cla.getType().getTypeName());
			}
			//modifier qui change en fonction de ce que contient la methode, public/privée/abstraite/finale/...
			int mMet = methode.getModifiers();

			//regarde si la methode est public, privée, protected
			Statut s =avoirStatut(mMet);
			boolean statique = Modifier.isStatic(mMet);
			boolean abstraite = Modifier.isAbstract(mMet);
			boolean finale= Modifier.isFinal(mMet);

			//creation de la methode
			Methode m= new Methode(nom, s, typeRetour, tabList,abstraite, statique, finale);
			//ajout de la methode dans l'ObjectClass
			obc.ajouterMethode(m);
		}
	}

	/**
	 * methode privée qui permet de faire l'introspection des constructeurs de la classe en parametre
	 * Pour ajouter un ou des constructeurs a l'bjectClasse
	 * les constructeurs sont considérés comme des méthodes dans l'objectClasse
	 * @param cls
	 * @param obc
	 * @return
	 */
	private void introConstructeur(Class cls, ObjectClasse obc ){

		//Methode qui donne un tableau avec tout les constructeurs declare
		Constructor[] cons = cls.getDeclaredConstructors();

		for (Constructor co : cons) {
			String nom=co.getName();

			// la fonction getmodifiers est un int qui change en fonction des particularites du Field
			int mCon = co.getModifiers();
			//regarde si le constructeur est public, privée, protected
			Statut s =avoirStatut(mCon);

			//tableau qui contient le nom des classes en parametres
			AnnotatedType[] c = co.getAnnotatedParameterTypes();
			List<String> tabList = new ArrayList<String>();
			for (AnnotatedType cla : c) {
				tabList.add(cla.getType().getTypeName());
			}

			Methode m= new Methode(nom, s, null, tabList,false, false, false);
			//ajout de constructeur dans l'ObjectClass, on considère qu'un constructeur est une méthode de l'ObjectClass
			obc.ajouterMethode(m);

		}
	}

	/**
	 * methode privée qui en fonction du int quel recoit, renvoie le statut associe a ce nombre
	 * @param mod int
	 * @return Statut
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
