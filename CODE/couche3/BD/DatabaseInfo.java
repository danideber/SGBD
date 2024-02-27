package CODE.couche3.BD;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import CODE.couche3.TableInfo;

public class DatabaseInfo implements Serializable {
    /**
     * Liste des tables de la bases de données
     */
	private List<TableInfo> tables_infos;

    /**
     * Nombre de relation
     */
	public int compt_rel;

    /**
     * Attribut static du DatabseInfo
     */
	private static DatabaseInfo objet = null;

    /**
     * Constructeur du DatabaseInfo
     */
	private DatabaseInfo() {
		this.tables_infos = new ArrayList<TableInfo>();
		compt_rel = 0;
	}

    /**
     * Constructeur du DatabseInfo
     * @param tab Liste des TableInfo
     * @param compt_rel Nombre de relation
     */
	private DatabaseInfo(List<TableInfo> tab, int compt_rel) {
		this.tables_infos = tab;
		this.compt_rel = compt_rel;
	}

    /**
     * 
     * @return retourne un objet de type DatabseInfo
     */
	public static DatabaseInfo getInstance() {
		if (objet == null) {
			objet = new DatabaseInfo();
			return objet;
		} else {
			return objet;
		}
	}

	public static DatabaseInfo getInstance(List<TableInfo> tab, int compt_rel) {
		if (objet == null) {
			objet = new DatabaseInfo(tab, compt_rel);
			return objet;
		} else {
			return objet;
		}
	}

	public void viderDataBaseInfo() {
		tables_infos = new ArrayList<>();
		compt_rel = 0;
	}

	public void Init() throws Exception {

		File fichier = new File("DB/DBInfo.txt");

		if (!fichier.exists()) {
			fichier.createNewFile();
		} else {
			try (FileInputStream fis = new FileInputStream(fichier);
					ObjectInputStream ois = new ObjectInputStream(fis);

			) {

				DatabaseInfo d = (DatabaseInfo) ois.readObject();
				objet=d;
			}
 			catch (Exception e) {

			}
		}
	}

	public void Finish() {

		try (
				FileOutputStream fichier = new FileOutputStream("DB/DBInfo.txt");
				ObjectOutputStream oos = new ObjectOutputStream(fichier);) {
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
	public void setTables_infos(List<TableInfo> tab) throws NullPointerException {
        if (tab==null) {
            throw new NullPointerException("La liste des tableInfo est nulle");
        }
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
     * Mathode permettant d'ajouter une table à une DatabaseInfo
     * @param table_info la tableInfo qui sera rajoutée
     */
	public void AddTableInfo(TableInfo table_info) {
		if (this.tables_infos == null) {
			this.tables_infos = new ArrayList<TableInfo>();
		}
		if (GetTableInfo(table_info.getNom_rel()) != null) {
			this.tables_infos.remove(GetTableInfoIndex(table_info.getNom_rel()));

		}
		this.tables_infos.add(table_info);
		this.compt_rel++;
	}

    /**
     * Méthode permettant de retourner une tableInfo grâce au nom de la relation
     * @param nom_rel
     * @return tableInfo ou null si le nom de la relation ne correspond à aucune table
     */
	public TableInfo GetTableInfo(String nom_rel) {
        if (this.tables_infos.size()==0) {
            return null;
        }
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
	public int GetTableInfoIndex(String nom_rel) {
		int i = 0;
		for (TableInfo ti : this.tables_infos) {
			i++;
			if (ti.getNom_rel().equals(nom_rel)) {

				return i;
			}
		}
		return 0;
	}

}
