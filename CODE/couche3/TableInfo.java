package CODE.couche3;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import CODE.page.PageId;

public class TableInfo implements Serializable {
	private String nom_rel;
	private int nb_col;
	private List<ColInfo> col_info;
	private PageId headerPageId;

	public TableInfo(String nom, int nb, List<ColInfo> col) {
		this.nom_rel = nom;
		this.nb_col = nb;
		this.col_info = col;

	}

	public TableInfo(String nom, int nb) {
		this.nom_rel = nom;
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
	public TableInfo(String nom, int nb, List<ColInfo> col, PageId PageId) {
		this.nom_rel = nom;
		this.nb_col = nb;
		this.col_info = col;
		this.headerPageId = PageId;
	}

	public String getNom_rel() {
		return nom_rel;
	}

	public void setNom_rel(String nom_rel) {
		this.nom_rel = nom_rel;
	}

	public int getNb_col() {
		return nb_col;
	}

	public void setNb_col(int nb_col) {
		this.nb_col = nb_col;
	}

	public List<ColInfo> getCol_info() {
		return col_info;
	}

	public void setCol_info(List<ColInfo> col_info) {
		this.col_info = col_info;
	}

	public void addColInfo(ColInfo col) {
		this.col_info.add(col);
	}

	public PageId getHeaderPage(){
		return headerPageId;
	}

	public void setHeaderPage(PageId page){
		this.headerPageId=page;
	}

	public int getIndiceCol(String nomCol){
		for (int i = 0; i <col_info.size(); i++) {
			if(nomCol.equals(col_info.get(i).getNom_col())){
				return i;
			}
		}
		return -1;
	}

	/**
	 *  Permet d'obtenir l'index d'une colonne
	 * @param libCol
	 * @return
	 */
	public int indexCol(String libCol){
		try {
			for (int i = 0; i < col_info.size(); i++) {
				if(col_info.get(i).getNom_col().equals(libCol)){
					return i;
				}
			}
		} catch (Exception e) {
			
		}
		return -1;
	}


	public boolean equals(TableInfo tableInfo){
		if (tableInfo==null) {
			return false;
		}
		return 
		this.headerPageId.equals(tableInfo.getHeaderPage())&&
		this.nb_col==tableInfo.getNb_col() &&
		this.nom_rel.equals(tableInfo.getNom_rel());

		// return this.col_info.equals(tableInfo.col_info)&&
		// this.headerPageId.equals(tableInfo.getHeaderPage())&&
		// this.nb_col==tableInfo.getNb_col() &&
		// this.nom_rel.equals(tableInfo.getNom_rel());

	}

}
