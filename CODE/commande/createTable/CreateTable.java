package CODE.commande.createTable;

import CODE.commande.ICommande;
import CODE.couche3.ColInfo;
import CODE.couche3.DatabaseInfo;
import CODE.couche3.FileManager;
import CODE.couche3.TableInfo;
import CODE.page.PageId;
import java.util.ArrayList;
import java.util.List;

public class CreateTable implements ICommande {

  private String nomRelation;
  private int nbColonne;
  private List<String> nomsColonnes;
  private List<String> typesColonnes;
  private PageId headerPage;
  private TableInfo tableInfo;
  private List<ColInfo> listeColonne;
  private String libCommande;

	

  /**
   * Méthode représentant le constructeur de notre programme
   * @param libCommande la commande saisie par l'utilisateur
   */
  public CreateTable(String libCommande) {
    this.nomRelation = "";
    this.nbColonne = 0;
    this.nomsColonnes = new ArrayList<>();
    this.typesColonnes = new ArrayList<>();
    this.listeColonne = new ArrayList<>();
    this.libCommande = libCommande;
  }

  @Override
  public void parsingCom() throws Exception {
    if (comValide()) {
      String attrType;
      this.nomRelation = this.libCommande.split(" ")[2].toUpperCase();
      attrType = this.libCommande.split(" ")[3];
      attrType = attrType.substring(1, attrType.length() - 1);
      this.nbColonne = attrType.split(",").length;
      for (int i = 0; i < this.nbColonne; i++) {
        this.nomsColonnes.add(attrType.split(",")[i].split(":")[0].toUpperCase());
        this.typesColonnes.add(attrType.split(",")[i].split(":")[1].toUpperCase());
      }

      for (int i = 0; i < this.nbColonne; i++) {
        if (this.typesColonnes.get(i).toUpperCase().startsWith("STRING")) {

          int taille = Integer.parseInt(
            this.typesColonnes.get(i)
              .substring(7, this.typesColonnes.get(i).length() - 1)
          );
          this.typesColonnes.set(i, "STRING");
          this.listeColonne.add(
              new ColInfo(
                this.nomsColonnes.get(i),
                this.typesColonnes.get(i),
                taille
              )
            );


        } else if (
          this.typesColonnes.get(i).toUpperCase().startsWith("VARSTRING")
        ) {
      
          int taille = Integer.parseInt(
            this.typesColonnes.get(i)
              .substring(10, this.typesColonnes.get(i).length() - 1)
          );
          this.typesColonnes.set(i, "VARSTRING");
          this.listeColonne.add(
              new ColInfo(
                this.nomsColonnes.get(i),
                this.typesColonnes.get(i),
                taille
              )
            );

        } else {
          this.listeColonne.add(
              new ColInfo(this.nomsColonnes.get(i), this.typesColonnes.get(i))
            );
        }
      }
    } else {
      throw new Exception("Commande create invalide");
    }
  }

  @Override
  public boolean comValide() {
    if (this.libCommande.split(" ").length != 4) {
      return false;
    } else {
      return true;
    }
  }

  @Override
  public void Execute() throws Exception {
    parsingCom();
    DatabaseInfo databaseInfo = DatabaseInfo.getInstance();
    for (TableInfo tableInfo : databaseInfo.getTables_infos()) {
      if (nomRelation.equals(tableInfo.getNom_rel())) {
        System.err.println("Une table " + nomRelation + " existe déjà");
        throw new Exception();
      }
    }
    FileManager fileManag = FileManager.getInstance();
    PageId page = fileManag.createNewHeaderPage();

    this.headerPage = page;

    if(this.listeColonne.size()==0){
      this.tableInfo=new TableInfo(nomRelation, nbColonne, page);
    }
    else{
      this.tableInfo=new TableInfo(nomRelation, nbColonne, listeColonne, page);
    }
    fileManag.addDataPage(this.tableInfo);
    DatabaseInfo dbInfo = DatabaseInfo.getInstance();
    dbInfo.AddTableInfo(this.tableInfo);
  }


  public String getNomRelation() {
		return this.nomRelation;
	}

	public void setNomRelation(String nomRelation) {
		this.nomRelation = nomRelation;
	}

	public int getNbColonne() {
		return this.nbColonne;
	}

	public void setNbColonne(int nbColonne) {
		this.nbColonne = nbColonne;
	}

	public List<String> getNomsColonnes() {
		return this.nomsColonnes;
	}

	public void setNomsColonnes(List<String> nomsColonnes) {
		this.nomsColonnes = nomsColonnes;
	}

	public List<String> getTypesColonnes() {
		return this.typesColonnes;
	}

	public void setTypesColonnes(List<String> typesColonnes) {
		this.typesColonnes = typesColonnes;
	}

	public PageId getHeaderPage() {
		return this.headerPage;
	}

	public void setHeaderPage(PageId headerPage) {
		this.headerPage = headerPage;
	}

	public TableInfo getTableInfo() {
		return this.tableInfo;
	}

	public void setTableInfo(TableInfo tableInfo) {
		this.tableInfo = tableInfo;
	}

	public List<ColInfo> getListeColonne() {
		return this.listeColonne;
	}

	public void setListeColonne(List<ColInfo> listeColonne) {
		this.listeColonne = listeColonne;
	}

	public String getLibCommande() {
		return this.libCommande;
	}

	public void setLibCommande(String libCommande) {
		this.libCommande = libCommande;
	}


}

