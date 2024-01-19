package CODE.couche3;

import java.io.Serializable;

public class ColInfo implements Serializable {
	private String nom_col;
	private String type_col;
	private int taille;

	public ColInfo(String nom, String type) {
		this.nom_col = nom;
		this.type_col = type;
	}

	public ColInfo(String nom, String type, int taille) {
		this.nom_col = nom;
		this.type_col = type;

		if (type.substring(0, 1).equals("S")) {
			this.taille = taille;
		}
		if (type.substring(0, 1).equals("V")) {
			this.taille = taille;
		}

	}

	public String getNom_col() {
		return nom_col;
	}

	public int getTaille_col() {
		return taille;
	}

	public void setTaille_col(int taille) {
		this.taille = taille;
	}

	public void setNom_col(String nom_col) {
		this.nom_col = nom_col;
	}

	public String getType_col() {
		return type_col;
	}

	public void setType_col(String type_col) {
		this.type_col = type_col;
	}

}
