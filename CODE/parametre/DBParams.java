package CODE.parametre;

/**
 * classe abstraite contenant les paramètres de du SGBD
 */
public abstract class DBParams {

  /**
   * Variable contenant le chemin vers lequel se trouve les fichiers du SGBD notamment les fichiers de données
   */
  public static String DBPath;
  

  /**
   * Variable contenant le nombre de frame
   */
  public static int frameCount;

  /**
   * Variable contenant la taille d'une page
   */
  public static int SGBDPageSize;

  /**
   * Variable contenant le nombre de fichier de données
   */
  public static int DMFileCount;
}
