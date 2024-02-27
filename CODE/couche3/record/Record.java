package CODE.couche3.record;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import CODE.couche3.TableInfo;
import CODE.parametre.DBParams;

/**
 * 
 */
public class Record {
	/**
	 * Attribut correspondant à une tableInfo
	 */
	private TableInfo tableInfo;

	/**
	 * Liste des valeurs du record
	 */
	private List<String> recvalues;

	/**
	 * 
	 * @param tableInfo
	 */
	public Record(TableInfo tableInfo) throws NullPointerException {
		if (tableInfo==null) {
			throw new NullPointerException("La tableInfo est nulle");
		}
		else{
			this.tableInfo = tableInfo;
			recvalues = new ArrayList<String>();
		}

	}

	/**
	 * 
	 * @param tableInfo
	 * @param listValues
	 */
	public Record(TableInfo tableInfo, List<String> listValues) throws NullPointerException {
		if (tableInfo==null) {
			throw new NullPointerException("La tableInfo est nulle");	
		}
		else{
		this.tableInfo = tableInfo;

		this.recvalues = new ArrayList<String>();

		for (int i = 0; i < listValues.size(); i++) {
			recvalues.add(listValues.get(i));
		}
		this.recvalues = listValues;
		}
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getRecvalues() {
		return this.recvalues;
	}

	
	/**
	 * 
	 * @return
	 */
	public TableInfo getTableInfo() {
		return this.tableInfo;
	}

	public void setTableInfo(TableInfo tableInfo){
		this.tableInfo=tableInfo;
	}

	/**
	 * 
	 * @param buff
	 * @param pos
	 * @return
	 */
	public int writeToBuffer(ByteBuffer buff, int pos) {
		int nb_octets = 0, tailleCol = 0;
		String type_col = "", val_record = "";
		// Je me positionne à la position pos
		buff.position(pos);

		// On va parcourir en nous basant sur le nombre de colonnes
		for (int i = 0; i < tableInfo.getNb_col(); i++) {
			// Affectation de svaleurs
			type_col = tableInfo.getCol_info().get(i).getType_col();
			tailleCol = tableInfo.getCol_info().get(i).getTaille_col();
			val_record = recvalues.get(i).trim();

			if (type_col.toUpperCase().equals("INT")) {
				int intValue = Integer.parseInt(val_record);

				if (buff.capacity() >= DBParams.SGBDPageSize) {
					buff.putInt(intValue);
					nb_octets += Integer.BYTES;

				} else {
					System.out.println("Le ByteBuffer n'a pas suffisamment d'espace pour écrire un entier.");
				}
			}

			else if (type_col.toUpperCase().equals("FLOAT")) {
				buff.putFloat(Float.parseFloat(val_record));
				nb_octets += 4;

			}

			else if (type_col.toUpperCase().substring(0, 6).equals("STRING")) {
				for (int j = 0; j < tailleCol; j++) {
					if (val_record.length() < tailleCol && j >= val_record.length()) {
						buff.putChar(' ');
						nb_octets += 2;
					} else {
						buff.putChar(val_record.charAt(j));
						nb_octets += 2;
					}

				}
			}

			else {
				for (int j = 0; j < tailleCol; j++) {
					if (val_record.length() < tailleCol && j >= val_record.length()) {
						buff.putChar(' ');
						nb_octets += 2;
					} else {
						buff.putChar(val_record.charAt(j));
						nb_octets += 2;
					}

				}

			}

		}

		return nb_octets;
	}


	/**
	 * 
	 * @param buff
	 * @param pos
	 * @return
	 */
	public int readFromBuffer(ByteBuffer buff, int pos) {
		int nb_octets = 0, tailleCol = 0;
		;
		String type_col = "", val_record = "";
		// Je me positionne à la position pos
		buff.position(pos);

		// On vide la liste des records si elle n'est pas vide
		if (recvalues.size() > 0) {
			recvalues.clear();
		}


		// On av parcourir en nous basant sur le nombre de colonnes
		for (int i = 0; i < tableInfo.getNb_col(); i++) {
			tailleCol = tableInfo.getCol_info().get(i).getTaille_col();
			// Affectation de svaleurs
			type_col = tableInfo.getCol_info().get(i).getType_col();

			if (type_col.toUpperCase().equals("INT")) {

				recvalues.add("" + buff.getInt() + "");

				nb_octets += 4;
			}

			else if (type_col.toUpperCase().equals("FLOAT")) {
				recvalues.add("" + buff.getFloat() + "");
				nb_octets += 4;

			}

			else if (type_col.toUpperCase().startsWith("STRING")) {

				StringBuilder sb = new StringBuilder();

				for (int j = 0; j < tailleCol; j++) {
					sb.append(buff.getChar());

				}
				recvalues.add("" + sb.toString() + "");

				nb_octets += 2 * tailleCol;
			}

			else {
				StringBuilder sb = new StringBuilder();

				for (int j = 0; j < tailleCol; j++) {
					sb.append(buff.getChar());

				}
				recvalues.add("" + sb.toString() + "");

				nb_octets += 2 * tailleCol;

			}

		}

		return nb_octets;
	}


	/**
	 * 
	 * @param valuesRecord
	 */
	public void setRecvalues(List<String> valuesRecord) {
		this.recvalues = valuesRecord;
	}



}