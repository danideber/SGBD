package CODE.controle.file;


import java.io.File;

import CODE.parametre.DBParams;


/**
 * Cette classe de contrôle permet d'effectuer certaines opérations de contrôles au niveau des fichiers
 */
public class Checkeur {


  /**
   *  Méthode static permettant de vérifier si il existe exactement DBParams.FileCount fichier . Dans le cas où un fichier n'existe pas, la méthode crée un 
   * nouveau fichier si possible
   * @return retourne le chemin vers le fichier
   */
  public static String file_pas_exis() {
    StringBuilder path = new StringBuilder();
    path.append(DBParams.DBPath + File.separator + "F");
    for (int i = 1; i <= DBParams.DMFileCount; i++) {
      path.append(i);
      path.append(".data");
      File fichier = new File(path.toString());
      if (!fichier.exists()) {
        return path.toString();
      }
    }
    return null;
  }



/**
 * Méthode static permettant de renvoyer le chemin vers le fichier le plus petit en terme d'octet
 * @return le chemin vers le fichier le plus petit
 */
  public static String plus_petit_fichier() {
    StringBuilder chemin = new StringBuilder();
    long taille_min = Long.MAX_VALUE;
    for (int i = 1; i <= DBParams.DMFileCount; i++) {
      StringBuilder path = new StringBuilder();

      path.append(DBParams.DBPath + File.separator + "F");

      path.append(i);
      path.append(".data");
      File f1 = new File(path.toString());

      if (f1.length() < taille_min && f1.exists()) {
        taille_min = f1.length();
        if (chemin.isEmpty()) {
          chemin.append(path.toString());
        }
        else{
          chemin.delete(0, chemin.length());
          chemin.append(path.toString());
        }
      }
    }
    return chemin.toString();
  }

}

