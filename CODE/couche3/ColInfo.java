package CODE.couche3;

import java.io.Serializable;
import java.util.List;

/**
 *  Classe représentant une Colonne dans une raltion
 */
public class ColInfo implements Serializable {

  public static final String[] LISTE_TYPE = {
    "INT",
    "FLOAT",
    "STRING",
    "VARSTRING",
  };

  /**
   * Attribut représentant le nom d'une colonne
   */
  private String nom_col;

  /**
   * Attribut représentant le type d'une colonne
   */
  private String type_col;

  /**
   * Attribut pour la taille d'une colonne
   */
  private int taille;

  /**
   *
   * @param nom
   * @param type
   */
  public ColInfo(String nom, String type) throws Exception {
    if (nom == null || type == null) {
      throw new NullPointerException();
    }
    if (nom.length() == 0) {
      throw new IllegalArgumentException(
        "Le nom de la colonne ne doit avoir au moins un carctère"
      );
    }
    if (type.length() == 0) {
      throw new IllegalArgumentException(
        "Le type de la colonne ne doit avoir au moins un carctère"
      );
    }
    if (!typeValide(type)) {
      throw new IllegalArgumentException("Le type de la colonne est invalide");
    }
    this.nom_col = nom.toUpperCase();
    if (type.toUpperCase().startsWith("STRING")) {
      this.type_col = "STRING";
      this.taille = Integer.parseInt(type.substring(7, type.length()-1));
    } else if (type.toUpperCase().startsWith("VARSTRING")) {
      this.type_col = "VARSTRING";
      this.taille = Integer.parseInt(type.substring(10, type.length()-1));
    }
    else{
    this.type_col = type.toUpperCase();
    }
  }

  /**
   *
   * @param nom
   * @param type
   * @param taille
   */
  public ColInfo(String nom, String type, int taille) throws Exception {
    if (nom == null || type == null) {
      throw new NullPointerException();
    }
    if (!typeValide(type)) {
      throw new IllegalArgumentException("Le type de la colonne est invalide");
    }
    this.nom_col = nom.toUpperCase();
    this.type_col = type.toUpperCase();

    if (type.toUpperCase().startsWith("STRING")) {
      this.taille = taille;
    }
    if (type.toUpperCase().startsWith("VARSTRING")) {
      this.taille = taille;
    }
  }

  public boolean typeValide(String type) {
    for (int i = 0; i < LISTE_TYPE.length; i++) {

      if (type.toUpperCase().startsWith(LISTE_TYPE[i])) {
    
        return true;
      }
    }
    return false;
  }

  /**
   *
   * @return
   */
  public String getNom_col() {
    return nom_col;
  }

  /**
   *
   * @return
   */
  public int getTaille_col() {
    return taille;
  }

  /**
   *
   * @param taille
   */
  public void setTaille_col(int taille) {
    this.taille = taille;
  }

  /**
   *
   * @param nom_col
   */
  public void setNom_col(String nom_col) throws NullPointerException {
    if (nom_col == null) {
      throw new NullPointerException("Le nom de la colonne ne peut être nul");
    }
    this.nom_col = nom_col.toUpperCase();
  }

  /**
   *
   * @return
   */
  public String getType_col() {
    return type_col;
  }

  /**
   *
   * @param type_col
   */
  public void setType_col(String type_col) throws NullPointerException {
    if (type_col == null) {
      throw new NullPointerException("Le type de la colonne ne peut être nul");
    }
    this.type_col = type_col.toUpperCase();
  }

  /**
   * Méthode permettant de savoir si dux colonnes sont égales
   * @param colInfo
   * @return
   */
  public boolean equals(ColInfo colInfo) {
    if (colInfo == null) {
      return false;
    } else {
      return (this.nom_col.equals(colInfo.getNom_col()));
    }
  }
}
