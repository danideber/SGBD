package CODE.commande.insert;

import CODE.commande.ICommande;
import CODE.couche3.DatabaseInfo;
import CODE.couche3.FileManager;
import CODE.couche3.TableInfo;
import CODE.couche3.record.Record;
import java.util.ArrayList;
import java.util.List;

public class InsertCommande implements ICommande {

  private String nomRelation;
  private int nbColonne;
  private List<String> valuesColonnes;
  private String commande;

  public InsertCommande(String libCommande) throws Exception {
    if (libCommande==null) {
      throw new Exception("La commande est nulle");
    }
    this.commande = libCommande.toUpperCase();
    this.valuesColonnes = new ArrayList<>();
    if (!comValide()) {
      throw new Exception("La commande est invalide! Reorter vous au manuel");
    }
    parsingCom();
  }

  public void setLibCommande(String libCommande)throws Exception {
    if (libCommande==null) {
      throw new Exception("La commande est nulle");
    }
    this.commande = libCommande.toUpperCase();
    this.valuesColonnes = new ArrayList<>();
    if (!comValide()) {
      throw new Exception("La commande est invalide! Reorter vous au manuel");
    }
    parsingCom();
  }

  @Override
  public void parsingCom() {

    this.nomRelation=this.commande.split(" ")[2];
    StringBuilder val=new StringBuilder();
    val.append(this.commande.split(" ")[4]);

    val.deleteCharAt(0);
    val.deleteCharAt(val.toString().length()-1);
    for (String valeur : val.toString().split(",")) {
      this.valuesColonnes.add(valeur);
    }
    this.nbColonne=this.valuesColonnes.size();
  }

  @Override
  public boolean comValide() {
    if (this.commande.split(" ").length!=5) {
      return false;
    }
    return true;
  }

  @Override
  public void Execute() throws Exception {
   
    FileManager fm = FileManager.getInstance();
    DatabaseInfo dbInfo = DatabaseInfo.getInstance();
    TableInfo tabInfo = dbInfo.GetTableInfo(nomRelation);
    Record monRecord = new Record(tabInfo);
    monRecord.setRecvalues(valuesColonnes);
    fm.InsertRecordIntoTable(monRecord);
  }
}
