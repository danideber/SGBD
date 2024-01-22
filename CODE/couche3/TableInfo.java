package CODE.couche3;

import CODE.page.PageId;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TableInfo implements Serializable {

  /**
   * Attribut pour le nom d'une relation
   */
  private String nom_rel;

  /**
   * Attribut pour le nombre de colonne
   */
  private int nb_col;

  /**
   * Liste contenant les ColInfo
   */
  private List<ColInfo> col_info;

  /**
   * Liste contenant le PageId relié à une tableInfo
   */
  private PageId headerPageId;

  /**
   * Contructeur de la tableInfo
   * @param nom nom de la relation
   * @param nb nombre de colonne
   * @param col liste des colonnes
   */
  public TableInfo(String nom, int nb, List<ColInfo> col) throws Exception {
    if (nom == null || col == null) {
      throw new Exception(
        "Un paramètre passé au contructeur de la tableInfo ne peut être nul"
      );
    }
    this.nom_rel = nom;
    this.nb_col = nb;
    this.col_info = col;
  }

  public TableInfo(String nom, int nb) throws Exception {
    if (nom == null) {
      throw new Exception("Le nom de la relation ne peut être nul");
    }
    this.nom_rel = nom.toUpperCase();
    this.nb_col = nb;
    this.col_info = new ArrayList<ColInfo>();
  }

  /**
   * Constructeur avec headerPageId
   *
   * @param nom    nom
   * @param nb     nombre
   * @param col    colonne
   * @param pageId page
   */
  public TableInfo(String nom, int nb, List<ColInfo> col, PageId pageId)
    throws NullPointerException {
    if (nom == null || col == null || pageId == null) {
      throw new NullPointerException();
    }
    this.nom_rel = nom.toUpperCase();
    this.nb_col = nb;
    this.col_info = col;
    this.headerPageId = pageId;
  }


  public TableInfo(String nom, int nb,  PageId pageId)
  throws NullPointerException {
  if (nom == null  || pageId == null) {
    throw new NullPointerException();
  }
  this.nom_rel = nom.toUpperCase();
  this.nb_col = nb;
  this.headerPageId = pageId;
}

  /**
   * Méthode renvoyant le nom d'une relation
   * @return
   */
  public String getNom_rel() {
    return nom_rel;
  }

  /**
   *
   * @param nom_rel
   */
  public void setNom_rel(String nom_rel) {
    this.nom_rel = nom_rel;
  }

  /**
   *
   * @return
   */
  public int getNb_col() {
    return nb_col;
  }

  /**
   *
   * @param nb_col
   */
  public void setNb_col(int nb_col) {
    this.nb_col = nb_col;
  }

  /**
   *
   * @return
   */
  public List<ColInfo> getCol_info() {
    return col_info;
  }

  /**
   *
   * @param col_info
   */
  public void setCol_info(List<ColInfo> col_info) {
    this.col_info = col_info;
  }

  /**
   *
   * @param col
   */
  public void addColInfo(ColInfo col) {
    this.col_info.add(col);
  }

  /**
   *
   * @return
   */
  public PageId getHeaderPage() {
    return headerPageId;
  }

  /**
   *
   * @param page
   */
  public void setHeaderPage(PageId page) {
    this.headerPageId = page;
  }

  /**
   *
   * @param nomCol
   * @return
   */
  public int getIndiceCol(String nomCol) {
    for (int i = 0; i < col_info.size(); i++) {
      if (nomCol.equals(col_info.get(i).getNom_col())) {
        return i;
      }
    }
    return -1;
  }

  /**
   *  Permet d'obtenir l'index d'une colonne par rapport à son nom
   * @param libCol 
   * @return 
   */
  public int indexCol(String libCol) {
    try {
      for (int i = 0; i < col_info.size(); i++) {
        if (col_info.get(i).getNom_col().equals(libCol)) {
          return i;
        }
      }
    } catch (Exception e) {}
    return -1;
  }

  /**
   * Méthode permettant de voir si deux tableInfo sont égales
   * @param tableInfo
   * @return
   */
  public boolean equals(TableInfo tableInfo) {
    if (tableInfo == null) {
      return false;
    }
    if (this==tableInfo) {
      return true;
    }
    return (
      this.nom_rel.equals(tableInfo.getNom_rel())
    );
  }
}
