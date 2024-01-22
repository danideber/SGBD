package CODE.couche3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DatabaseInfo implements Serializable {

  /**
   *
   */
  private List<TableInfo> tables_infos;

  /**
   * Attribut représentant le nombre de relation
   */
  public int compt_rel;

  /**
   *
   */
  private static DatabaseInfo objet = null;

  /**
   *
   */
  private DatabaseInfo() {
    this.tables_infos = new ArrayList<TableInfo>();
    compt_rel = 0;
  }

  /**
   *
   * @param tab
   * @param compt_rel
   */
  private DatabaseInfo(List<TableInfo> tab, int compt_rel) {
    this.tables_infos = tab;
    this.compt_rel = compt_rel;
  }

  /**
   *
   * @return
   */
  public static DatabaseInfo getInstance() {
    if (objet == null) {
      objet = new DatabaseInfo();
      return objet;
    } else {
      return objet;
    }
  }

  /**
   *
   * @param tab
   * @param compt_rel
   * @return
   */
  public static DatabaseInfo getInstance(List<TableInfo> tab) {
    if (objet == null) {
      objet = new DatabaseInfo(tab, tab.size());
      return objet;
    } else {
      return objet;
    }
  }

  /**
   *
   */
  public void viderDataBaseInfo() {
    tables_infos = new ArrayList<>();
    compt_rel = 0;
  }

  /**
   *
   * @throws Exception
   */
  public void Init() throws Exception {
    File fichier = new File("DB/DBInfo.txt");

    if (!fichier.exists()) {
      fichier.createNewFile();
    } else {
      try (
        FileInputStream fis = new FileInputStream(fichier);
        ObjectInputStream ois = new ObjectInputStream(fis);
      ) {
        DatabaseInfo d = (DatabaseInfo) ois.readObject();
        objet = d;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   *
   */
  public void Finish() {
    try (
      FileOutputStream fichier = new FileOutputStream("DB/DBInfo.txt");
      ObjectOutputStream oos = new ObjectOutputStream(fichier);
    ) {
      oos.writeObject(objet);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   *
   * @return
   */
  public List<TableInfo> getTables_infos() {
    return tables_infos;
  }

  /**
   *
   * @return
   */
  public int getCompt_rel() {
    return compt_rel;
  }

  /**
   *
   * @param tab
   */
  public void setTables_infos(List<TableInfo> tab) {
    this.tables_infos = tab;
  }

  /**
   *
   * @param compt
   */
  public void setCompt_rel(int compt) {
    this.compt_rel = compt;
  }

  /**
     * prend en argument une TableInfo et qui la rajoute à la liste et actualise le
	 compteur
     * @param table_info
     */
  public void AddTableInfo(TableInfo table_info) throws Exception {
    if (this.tables_infos == null) {
      throw new NullPointerException();
    } else if (GetTableInfo(table_info.getNom_rel()) == table_info) {
      throw new Exception("La table existe déjà");
    } else {
      this.tables_infos.add(table_info);
      this.compt_rel++;
    }
  }

  /**
   * prend en argument le nom d’une relation et retourne la TableInfo associée
   * @param nom_rel
   * @return
   */
  public TableInfo GetTableInfo(String nom_rel) {
    for (TableInfo ti : this.tables_infos) {
      if (ti.getNom_rel().equals(nom_rel)) {
        return ti;
      }
    }
    return null;
  }

  /**
   *
   * @param nom_rel
   * @return
   */
  public int GetTableInfoIndex(String nom_rel) throws Exception {
    int i = 0;
    for (TableInfo ti : this.tables_infos) {
      i++;
      if (ti.getNom_rel().equals(nom_rel)) {
        return i;
      }
    }
    throw new Exception("La table info n'existe pas");
  }
}
