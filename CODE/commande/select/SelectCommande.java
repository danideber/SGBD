package CODE.commande.select;

import CODE.commande.ICommande;
import CODE.couche3.DatabaseInfo;
import CODE.couche3.FileManager;
import CODE.couche3.TableInfo;
import CODE.couche3.record.Record;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe permettant de visualiser des données
 */
public class SelectCommande implements ICommande {

  /**
   * Nom de la relation (la table)
   */
  private String nomRelation;

  /**
   * Le contenu de la commande
   */
  private String libCommande;

  /**
   * La liste des conditions contenues dans la commande
   */
  private List<String> listeCondition;

  /**
   * La liste des records qui respectent la(les) conditions de la commande; s'il y a des conditions
   */
  private List<String> listeRecordsValide;

  /**
   * COnstructeur
   * @param commande libéllé de la commande
   */
  public SelectCommande(String commande) {
    this.libCommande = commande.toUpperCase();
    if (comValide()) {
      this.nomRelation = this.libCommande.split(" ")[3];
      this.listeCondition = listeCondCondition();
      this.listeRecordsValide = new ArrayList<>();
    }
  }

  /**
   * 
   * @return
   */
  public String getLibCommande() {
    return this.libCommande;
  }

  /**
   * 
   * @return
   */
  public String getNomRelation() {
    return this.nomRelation;
  }

  /**
   * 
   * @return
   */
  public List<String> getListCondition() {
    return this.listeCondition;
  }

  /**
   * 
   * @return
   */
  public List<String> getListeRecValide() {
    return this.listeRecordsValide;
  }

  /**
   * 
   */
  @Override
  public void parsingCom() {
    String[] tabCommande = this.libCommande.split(" ");
    nomRelation = tabCommande[3];
    listeCondition = new ArrayList<>();
    if (tabCommande.length > 4) {
      for (int i = tabCommande.length - 1; i > 0; i -= 2) {
        if (tabCommande[i].equals(nomRelation)) {
          break;
        } else {
          listeCondition.add(tabCommande[i]);
        }
      }
    }
  }


  /**
   * 
   */
  @Override
  public boolean comValide() {
    if (this.libCommande.split(" ").length == 1) {
      return false;
    }
    if (this.libCommande.split(" ").length < 4) {
      return false;
    }
    if (!this.libCommande.split(" ")[0].toUpperCase().equals("SELECT")) {
      return false;
    }
    if (!this.libCommande.split(" ")[2].toUpperCase().equals("FROM")) {
      return false;
    }
    return true;
  }

  /**
   * 
   * @param indiceCond
   * @param libCondition
   * @return
   */
  public String affectCondition(int indiceCond, String libCondition) {
    // Récupere la bonne condition
    if (listeCondition.get(indiceCond).contains("<>")) {
      libCondition = "<>";
    } else if (listeCondition.get(indiceCond).contains(">=")) {
      libCondition = ">=";
    } else if (listeCondition.get(indiceCond).contains("<=")) {
      libCondition = "<=";
    } else if (listeCondition.get(indiceCond).contains("<")) {
      libCondition = "<";
    } else if (listeCondition.get(indiceCond).contains(">")) {
      libCondition = ">";
    } else if (listeCondition.get(indiceCond).contains("=")) {
      libCondition = "=";
    }
    return libCondition;
  }

  /**
   * Méthode permettant d'obtenir la(les) condition(s) de la commande
   * @return la(les) condition(s)
   */
  public String getLibCond() {
    StringBuilder sb = new StringBuilder();

    for (int i = 3; i < this.libCommande.split(" ").length; i+=2) {
      sb.append(this.libCommande.split(" ")[i] + " ");
    }
    return sb.toString();
  }

  /**
   * 
   * @return
   */
  public List<String> listeCondCondition() {
    List<String> conditions = new ArrayList<>();
    for (int i = 0; i < getLibCond().split(" ").length; i++) {
      conditions.add(getLibCond().split(" ")[i]);
    }
    return conditions;
  }

  /**
   * Méthode permettant d'exécuter un select lorsqu'il ya une condition
   * @throws Exception
   */
  public void ExecuteSelCond() throws Exception {
    DatabaseInfo dbInfo = DatabaseInfo.getInstance();
    TableInfo tableInfo = dbInfo.GetTableInfo(nomRelation);
    FileManager fileManage = FileManager.getInstance();

    List<Record> listeRecord = fileManage.GetAllRecords(tableInfo);


    for (Record record : listeRecord) {
      record.getTableInfo().getCol_info().
      for (int i = 0; i < this.listeCondition.size(); i++) {
        String libCond = "";
        affectCondition(i, libCond);
      } 
    }
  }

  /**
   * Méthode permettant de vérifier si un record respecte une condition donnée. 
   * Elle renvoit true si le résultat est positif et false dans le cas contraire.
   * @param libCond l'opérateur de la condition
   * @param indCond l'indice de la condition dans la liste 
   * @param listeRecord la liste des records
   * @param indRec l'indice du record dans la liste
   * @return
   */
  public boolean comparRec(
    String libCond,
    int indCond,
    List<Record> listeRecord,
    int indRec
  ) {
    switch (libCond) {
      case "=":
        {
          return (
            listeRecord
              .get(indRec)
              .equals(this.listeCondition.get(indCond).split("=")[1])
          );
        }
        case "<>":
        {
          return (
            !listeRecord
              .get(indRec)
              .equals(this.listeCondition.get(indCond).split("=")[1])
          );
        }
      default:
        return false;
    }
  }

  /**
   * 
   */
  @Override
  public void Execute() throws Exception {
    DatabaseInfo dbInfo = DatabaseInfo.getInstance();
    TableInfo tableInfo = dbInfo.GetTableInfo(nomRelation);
    FileManager fileManage = FileManager.getInstance();

    List<Record> listeRecord = fileManage.GetAllRecords(tableInfo);

    for (int i = 0; i < listeRecord.size(); i++) {
      if (
        listeRecord.get(i).getTableInfo().getNom_rel().equals(nomRelation) &&
        listeRecord.get(i).getRecvalues().size() > 0
      ) {
        // Si on a pas de condition
        if (listeCondition.size() == 0) {
          this.listeRecordsValide.add(
              listeRecord.get(i).getRecvalues().toString()
            );
        }
        else{
          ExecuteSelCond();
        }
      } 
    }
    affiche(this.listeRecordsValide);
  }

  /**
   * Méthode permettant d'afficher les records
   * @param listeRecordValide liste des records
   */
  public void affiche(List<String> listeRecordValide) {
    for (String record : this.listeRecordsValide) {
      System.out.println(record);
    }
  }

}
