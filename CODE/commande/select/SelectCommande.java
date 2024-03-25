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
   *Méthode permettant de retourner le libéllé d'une commande
   * @return le libéllé de la commande
   */
  public String getLibCommande() {
    return this.libCommande;
  }

  /**
   * Méthode permettant de renvoyer le nom d'une relation
   * @return le nom de la relation
   */
  public String getNomRelation() {
    return this.nomRelation;
  }

  /**
   * Méthode permettant d'obtenir la liste des conditions
   * @return la liste des conditions
   */
  public List<String> getListCondition() {
    return this.listeCondition;
  }

  /**
   * Méthode permettant d'obtenir la liste des records valides
   * @return la liste des records valides
   */
  public List<String> getListeRecValide() {
    return this.listeRecordsValide;
  }

  /**
   * Méthode permettant le formattage d'une commande
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
   * Méthode permettant de vérifier si une commande est valide
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
   * Méthode permettant de retourner le libéllé d'une condition
   * @param indiceCond l'indice de la condition
   * @param libCondition le libéllé de la condition
   * @return le libéllé de la condition
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

    for (int i = 5; i < this.libCommande.split(" ").length; i += 2) {
      sb.append(this.libCommande.split(" ")[i] + " ");
    }
    return sb.toString();
  }

  /**
   *Méthodes permettant d'obtenir une liste représentant les conditions
   * @return
   */
  public List<String> listeCondCondition() {
    List<String> conditions = new ArrayList<>();
    for (int i = 0; i < getLibCond().split(" ").length; i++) {
      String cond=getLibCond().split(" ")[i];
      if (!cond.equals("")) {
        conditions.add(getLibCond().split(" ")[i]);
      }
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
      boolean conValid=false;
      for (int i = 0; i < this.listeCondition.size(); i++) {
        String libCond = "";
        libCond = affectCondition(i, libCond);

        conValid = comparRec(libCond, record,i);

        //Dans le cas où une condition est fausse, on sort de la boucle
        if(!conValid){
          break;
        }

      }

      if (conValid) {
        String recVal=record.getRecvalues().toString();
        String reValFormatted=recVal.substring(1, recVal.length()-1);
        listeRecordsValide.add(reValFormatted.trim());
      }
    }
  }

  /**
   * Méthode permettant de renvoyer le nom d'une colonne à partir d'une condition donnée
   * @param listeCondition
   * @return la nom de la colonne
   */
  public String getNomColFromCond(String condition){
    if (condition.contains("<>")) {
      return condition.split("<>")[0];
    }
    else if (condition.contains("<=")) {
      return condition.split("<=")[0];
    }
    else if (condition.contains(">=")) {
      return condition.split(">=")[0];
    }
    else if (condition.contains("<")) {
      return condition.split("<")[0];
    }
    else if (condition.contains(">")) {
      return condition.split(">")[0];
    }
    else if (condition.contains("=")) {
      return condition.split("=")[0];
    }
    return condition;
  }

  /**
   * Méthode permettant d'obtenir la valeur passer en paramètre d'une condition
   * @param condition
   * @return
   */
  public String getRecValFromCond(String condition){
    if (condition.contains("<>")) {
      return condition.split("<>")[1];
    }
    else if (condition.contains("<=")) {
      return condition.split("<=")[1];
    }
    else if (condition.contains(">=")) {
      return condition.split(">=")[1];
    }
    else if (condition.contains("<")) {
      return condition.split("<")[1];
    }
    else if (condition.contains(">")) {
      return condition.split(">")[1];
    }
    else if (condition.contains("=")) {
      return condition.split("=")[1];
    }
    return condition;
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
  public boolean comparRec(String libCond, Record records,int idListCond) {
    String nomCol = getNomColFromCond(listeCondition.get(idListCond));
    int indCol = records.getTableInfo().getIndiceCol(nomCol);
    String recVal = records.getRecvalues().get(indCol).trim();
    String recValFromCond =getRecValFromCond( listeCondition.get(idListCond));
    switch (libCond) {
      case "=":
        {
          return (recVal.equals(recValFromCond));
        }
      case "<>":
        {
          return (!recVal.equals(recValFromCond));
        }
      case "<=":{
        if (
            records
              .getTableInfo()
              .getCol_info()
              .get(indCol)
              .getType_col()
              .contains("STRING")
          ) {
            return (recVal.compareTo(recValFromCond)<0 || recVal.equals(recValFromCond));
          } else {
            Double recValNum = Double.parseDouble(recVal);
            Double recValFromCondNum = Double.parseDouble(recValFromCond);
            return (recValNum <= recValFromCondNum);
          }
      }

      case ">=":{
        if (
            records
              .getTableInfo()
              .getCol_info()
              .get(indCol)
              .getType_col()
              .contains("STRING")
          ) {
            return (recVal.compareTo(recValFromCond)>0 || recVal.equals(recValFromCond));
          } else {
            Double recValNum = Double.parseDouble(recVal);
            Double recValFromCondNum = Double.parseDouble(recValFromCond);
            return (recValNum >= recValFromCondNum);
          }
      }

      case ">":
        {
          if (
            records
              .getTableInfo()
              .getCol_info()
              .get(indCol)
              .getType_col()
              .contains("STRING")
          ) {
            return (recVal.compareTo(recValFromCond)>0);
          } else {
            Double recValNum = Double.parseDouble(recVal);
            Double recValFromCondNum = Double.parseDouble(recValFromCond);
            return (recValNum > recValFromCondNum);
          }
        }
        case "<":
        {
          if (
            records
              .getTableInfo()
              .getCol_info()
              .get(indCol)
              .getType_col()
              .contains("STRING")
          ) {
            return (recVal.compareTo(recValFromCond)<0);
          } else {
            Double recValNum = Double.parseDouble(recVal);
            Double recValFromCondNum = Double.parseDouble(recValFromCond);
            return (recValNum < recValFromCondNum);
          }
        }
      default:
        return false;
    }
  }

  /**
   * Méthode permettant d'exécuter une commande Select
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
          String recVal=listeRecord.get(i).getRecvalues().toString();
        String reValFormatted=recVal.substring(1, recVal.length()-1);
          this.listeRecordsValide.add(
              reValFormatted
            );
        } else {
          ExecuteSelCond();
          return;
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
