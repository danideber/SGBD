package CODE.couche2;

import java.nio.ByteBuffer;

import CODE.page.PageId;
import CODE.parametre.DBParams;

public class Frame {
	/**
     * Attribut représentant un buffer
     */
	private ByteBuffer buffer;

    /**
     * Attribut représentant un pageId
     */
	private PageId pageId;

    /**
     * Attribut représentant un pinCount
     */
	private int pin_count;

    /**
     * Attribut représentant un dirty
     */
	private boolean dirty;
	
	
	/**
     * Contructeur de la classe Frame
     */
	public Frame() {
		this.buffer = ByteBuffer.allocate(DBParams.SGBDPageSize);
		this.pageId = null;
		this.pin_count = 0;
		this.dirty = false;
	}



/**
 * Méthode permettant de renvoyer le buffer associé à une frame
 * @return buffer d'une frame
 */
	public ByteBuffer getBuffer() {
        this.buffer.position(0);
		return this.buffer;
	}

    /**
     * Méthode permettant de mettre à jour le buffer d'une frame
     * @param byteBuffer
     * @throws NullPointerException
     */
	public void setBuffer(ByteBuffer byteBuffer) throws NullPointerException {
        if (byteBuffer==null) {
            throw new NullPointerException("L'argument passé en paramètre est null");
        }
        else{
		this.buffer = byteBuffer;
        }
    }

    /**
     * Méthode permettant de renvoyer le pageId associé à une frame
     * @return pageId d'une frame
     */
	public PageId getPageId() {
		return pageId;
	}

    /**
     * Méthode permettant à jour le pageId d'un frame
     * @param pageId pageId
     * @throws NullPointerException NullPointerException
     */
	public void setPageId(PageId pageId) throws NullPointerException {
        if (pageId==null) {
            throw new NullPointerException("Le pageId est null");
        }
        else{
		this.pageId = pageId;
        }
	}


    /**
     * Méthode permettant de renvoyer le pincount associé à une frame
     * @return
     */
	public int getPin_count() {
		return pin_count;
	}

    /**
     * Méthode permettant de mettre à jour le pincount d'une frame
     * @param pin_count pinCount
     */
	public void setPin_count(int pin_count) {
		this.pin_count = pin_count;
	}

    /**
     * Méthode permettant de renvoyer le dirty associé à une frame
     * @return dirty
     */
	public boolean isDirty() {
		return dirty;
	}

    /**
     * Méthode permettant de mettre à jour le dirty d'une frame
     * @param dirty
     */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

    /**
     * Méthode permettant d'incrémenter le pincount d'une frame
     */
	public void incremPinCount(){
	this.pin_count++;
	}

     /**
     * Méthode permettant décrémenter le pincount d'une frame
     */
	public void decrePinCount(){
		if(pin_count>0){
			pin_count--;
		}
	}





}
