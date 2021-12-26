package introspection;

import modele.classe.ObjectClasse;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

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

		Class<?> cls = chargerClass(f, 1);
return null;
	}


	/**
	 * methode qui charge une class a partir d'un File donne
	 * Pour faire un classLoder il faut le chemin absolue jusqu'au repertoire du package si il y en a un
	 * exemple :
	 * chemin absolue :
	 * C:\\Users\\guill\\eclipse-workspace\\ReadFichier\\bin\\lireFichier\\lectureTXT.class
	 * le chemin pour construire le classLoader doit etre :
	 * C:\\Users\\guill\\eclipse-workspace\\ReadFichier\\bin\\
	 *
	 * avec lireFichier comme package le reste est utilisé pour trouve le fichier
	 * exemple : lireFichier.lectureTXT
	 *
	 *
	 * probleme, il peut avoir un packages dans en packages dans un packages...
	 * et on connait pas le nombre de packages qu'il y a dans le File, la
	 * methode teste donc tout d'abord pour aucun package
	 * Si le fichier n'a aucun package , on renvoie la Class
	 * sinon  on catch l'execption et refait la methode pour
	 * tester notre File avec un package puis ainsi de suite jusqu'a ce que le FIle n'est plus de repertoire
	 *
	 * @param f
	 * @param p est le nombre
	 * @return
	 * @throws MalformedURLException
	 * @throws ClassNotFoundException
	 */
	private Class<?> chargerClass(File f, int p) {
		Class cls = null;
		// s'arrete quand le nombre
		if (p <= compterBackQuote(f.getAbsolutePath())) {
			String[] s = new String[2];
			String cheminAbsolue = f.getAbsolutePath();

			int i = cheminAbsolue.length() - 1;
			int repertoirePackage = 0;
			String a = "\\";

			// boucle qui regarde si on trouve \ et qui s'arrete quand on a trouve le bon
			// nombre qui est celui de p
			while ((i > 0) && (repertoirePackage != p)) {
				// on regarde si le caractere courant est \
				if (a.charAt(0) == cheminAbsolue.charAt(i)) {
					repertoirePackage++;
				}
				i--;
			}

			// separe le string en 2 pour pouvoir faire le ClassLoader
			if (i != 0) {
				// la ligne suivante est celle qui va etre un nouveau File sans le fichier et
				// les packages associe a lui
				s[0] = cheminAbsolue.substring(0, i + 2);
				// le reste du string est donne pour rechercher
				s[1] = cheminAbsolue.substring(i + 2);
				// on enleve le .class
				s[1] = s[1].split(".class")[0];
				// on remplace \ par un point
				s[1] = s[1].replace("\\", ".");

			}
			try {

				// creation d'un classLoader
				File fl = new File(s[0]);
				URLClassLoader loader = new URLClassLoader(new URL[] { fl.toURL() }, getClass().getClassLoader());

				// la methode forname renvoie la classe associe a s[1]
				cls = Class.forName(s[1], false, loader);

			} catch (NoClassDefFoundError | ClassNotFoundException e) {
				// execption levee quand le loader n'est pas bon
				// cad le nouveau File n'est pas bon ou le fichier et inexistant
				p = p + 1;
				cls = chargerClass(f, p);
			} catch (MalformedURLException e) {
				//URL mal forme
				e.printStackTrace();
			}
		} else {
			System.out.println("on ne trouve pas votre fichier");
		}

		return cls;
	}

	/**
	 * methode qui compte le nombre de \ dans notre le string que l'on lui donne qui
	 * est le File
	 *
	 * @param text
	 * @return
	 */
	private int compterBackQuote(String text) {
		int count = 0;
		for (int i = 0; i < text.length(); i++) {
			if (text.charAt(i) == "\\".charAt(0)) {
				count++;
			}
		}
		return count;
	}

}
