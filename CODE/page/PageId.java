package CODE.page;
import java.io.Serializable;

public class PageId implements Serializable{
	
	//Id du fichier qui le contient
	private int FileIdx;

	//Id de l'emplacement de la page dans le fichier. 0 sera pour la premiÃ¨re page et ainsi de suite
	private int PageIdx;


	/**
	 * Constructeur du PageId
	 * @param fileIdx Id du fichier qui le contient
	 * @param pageIdx
	 */
	public PageId(int fileIdx, int pageIdx) throws Exception{
		if(fileIdx<1){
			throw new Exception("L'id du fichier est icnorrecte");
		}
		if (pageIdx<0) {
			throw new Exception("L'id de la page est incorrecte");
		}
		this.FileIdx = fileIdx; 
		this.PageIdx = pageIdx;
	}


/**
 * Méthode permettant de renvoyer l'id du fichier de la page
 * @return id du fichier
 */
public int getFileIdx() {
	return FileIdx;
}


/**
 * Méthode permettant de modifier l'id du fichier de la page
 */
public void setFileIdx(int fileIdx) {
	FileIdx = fileIdx;
}


/**
 * Méthode permettant de renvoyer l'id de la position de la page dans le fichier
 * @return id de la position de la page dans le fichier
 */
public int getPageIdx() {
	return PageIdx;
}


/**
 * Méthode permettant de modifier l'id de la position de la page dans le fichier
 */
public void setPageIdx(int pageIdx) throws Exception {
	if (pageIdx<0) {
		throw new Exception("Id de la page incorrecte");
	}
	else{
	PageIdx = pageIdx;

	}
}

/**
 * Méthode permettant de comparer dux PageId. Elle renvoie true si les deux PageId sont égales, ou false dans le cas contraire
 * @param pageId pageId à comparer
 * @return true si les pageId sont égales ou false dans le cas contraire
 */
public boolean equals(PageId pageId) {
	return (pageId.FileIdx==this.FileIdx && pageId.PageIdx==this.PageIdx);
}


}

