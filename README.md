# S3C_S10_ARNOUT_RENARD_RICHIER_WEISS

# Projet EZuml
Le projet a pour objectif initial de réaliser un diagramme de classe à partir d'un ou plusieurs fichiers .class que l'utilisateur nous donne.
Au fur et à mesure des itérations, l’objectif est d'implémenter plus de fonctionnalités.

Les fonctionnalités que l'on a effectuées :
  - affichage d'un diagramme qui respecte les conventions des diagrammes de classe
  - afficher l'arborescence du projet graphiquement
  - chargement d'un/ plusieurs fichiers .class ou d'un repertoire ce qui pet à jour l'arborescence
  - affichage d'une classe d'un diagramme de classe (afichage nom/attribut/méthode de la classe)
  - possibilité de déplacer une classe
  - exporter une image du diagramme qui est chargée
  - sauvegarder un diagramme et recharger une sauvegarder
  - raccourci clavier pour accéder à certaine fonctionnalités


#Lancer l'application
##Pour pouvoir faire tourner notre application, il faut que java soit installé sur votre machine

Pour savoir si java est installé, mettez la commande "java -version" dans la boite de commande de votre explorateur.
  -Si cette commande vous envoie la version de java, vous pouvez lancer l'application
  -Si la commande renvoie l'erreur :"Command 'java' not found", cela veut dire que java n'est pas installé
  il faut aller sur le lien : https://www.java.com/fr/download/ pour téléchargez java et l'installer correctement

##Pour compiler notre projet :
  -En ligne de commande à la racine du projet on fait :
    
    javac -cp src -d out/  src/principal/Principale.java

    mkdir out\ressources

    Attention pour window, on a la commande :
      copy src\ressources\* out\ressources\
    et pour linux, on a la commande :
      cp src\ressources\ out\ressources\

    cd out\

    Pour lancer l'application :
    java principal.Principale
    
##Pour en faire une archive jar :
    Depuis le repertoire out/ :
  
    jar cfe ../jar/NomDeLarchive.jar principal.Principale *
  
    le jar sera creer dans le repertoire jar/ a la racine du projet
    Pour lancer l'application :
    java -jar ..\jar\ezUmlcm.jar
   
    

    
    
  
