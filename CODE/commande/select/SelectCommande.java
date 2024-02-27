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
      this.listeCondition = new ArrayList<>();
      this.listeRecordsValide = new ArrayList<>();
    }
  }

  public String getLibCommande() {
    return this.libCommande;
  }

  public String getNomRelation() {
    return this.nomRelation;
  }

  public List<String> getListCondition() {
    return this.listeCondition;
  }

  public List<String> getListeRecValide() {
    return this.listeRecordsValide;
  }

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

    for (int i = 5; i < this.libCommande.split(" ").length; i++) {
      sb.append(this.libCommande.split(" ")[i] + " ");
    }
    return sb.toString();
  }

  public List<String> listeCondCondition() {
    List<String> conditions = new ArrayList<>();
    for (int i = 0; i < getLibCond().split(" ").length; i++) {
      conditions.add(getLibCond().split(" ")[i]);
    }
    return conditions;
  }

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
      } else {
        String libCondition = "";
        String libColonneCondition = "";
        String libValCondition = "";

        Boolean indBool = true;

        for (
          int indiceCond = 0;
          indiceCond < listeCondition.size();
          indiceCond++
        ) {
          if (!indBool) {
            break;
          }
          libCondition = affectCondition(indiceCond, libCondition);

          // Récupérer le libéllé de la colonne
          libColonneCondition =
            listeCondition.get(indiceCond).split(libCondition)[0];

          // Récupérer la valeur de la colonne
          libValCondition =
            listeCondition.get(indiceCond).split(libCondition)[1];

          // Recherger à garder le record parfait
          // Récupérer le record en entier
          for (
            int indexVal = 0;
            indexVal < listeRecord.get(i).getRecvalues().size();
            indexVal++
          ) {
            if (indexVal == tableInfo.indexCol(libColonneCondition)) {
              if (libCondition.equals("=")) {
                if (
                  libValCondition
                    .trim()
                    .equals(
                      listeRecord
                        .get(i)
                        .getRecvalues()
                        .get(tableInfo.indexCol(libColonneCondition))
                        .trim()
                    )
                ) {
                  indBool = true;
                } else {
                  indBool = false;
                  break;
                }
              } else if (libCondition.equals("<>")) {
                if (
                  !libValCondition
                    .trim()
                    .equals(
                      listeRecord
                        .get(i)
                        .getRecvalues()
                        .get(tableInfo.indexCol(libColonneCondition))
                        .trim()
                    )
                ) {
                  indBool = true;
                } else {
                  indBool = false;
                  break;
                }
              } else if (libCondition.equals(">")) {
                try {
                  if (
                    Double.parseDouble(
                      listeRecord
                        .get(i)
                        .getRecvalues()
                        .get(tableInfo.indexCol(libColonneCondition))
                        .trim()
                    ) >
                    Double.parseDouble(libValCondition.trim())
                  ) {
                    indBool = true;
                  } else {
                    indBool = false;
                    break;
                  }
                } catch (Exception e) {
                  if (
                    libValCondition.trim().length() >
                    (
                      listeRecord
                        .get(i)
                        .getRecvalues()
                        .get(tableInfo.indexCol(libColonneCondition))
                        .trim()
                        .length()
                    )
                  ) {
                    indBool = true;
                  } else {
                    indBool = false;
                    break;
                  }
                }
              } else if (libCondition.equals("<")) {
                try {
                  if (
                    Double.parseDouble(
                      listeRecord
                        .get(i)
                        .getRecvalues()
                        .get(tableInfo.indexCol(libColonneCondition))
                        .trim()
                    ) <
                    Double.parseDouble(libValCondition.trim())
                  ) {
                    indBool = true;
                  } else {
                    indBool = false;
                    break;
                  }
                } catch (Exception e) {
                  if (
                    libValCondition
                      .trim()
                      .compareTo(
                        listeRecord
                          .get(i)
                          .getRecvalues()
                          .get(tableInfo.indexCol(libColonneCondition))
                          .trim()
                      ) <
                    0
                  ) {
                    indBool = true;
                  } else {
                    indBool = false;
                    break;
                  }
                }
              } else if (libCondition.equals("<=")) {
                if (
                  Double.parseDouble(
                    listeRecord
                      .get(i)
                      .getRecvalues()
                      .get(tableInfo.indexCol(libColonneCondition))
                      .trim()
                  ) <=
                  Double.parseDouble(libValCondition.trim())
                ) {
                  indBool = true;
                } else {
                  indBool = false;
                  break;
                }
              } else if (libCondition.equals(">=")) {
                if (
                  Double.parseDouble(
                    listeRecord
                      .get(i)
                      .getRecvalues()
                      .get(tableInfo.indexCol(libColonneCondition))
                      .trim()
                  ) >=
                  Double.parseDouble(libValCondition.trim())
                ) {
                  indBool = true;
                } else {
                  indBool = false;
                  break;
                }
              }
            }
          }
        }

        if (indBool) {
          this.listeRecordsValide.add(
              listeRecord.get(i).getRecvalues().toString()
            );
        }
      }
    }
    affiche(this.listeRecordsValide);
  }

  public void affiche(List<String> listeRecordValide) {
    for (String record : this.listeRecordsValide) {
      System.out.println(record);
    }
  }
}
