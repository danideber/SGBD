package CODE.couche3.record;

import CODE.page.PageId;


/**
 * 
 */
public class RecordId {

    /**
     * Page à laquelle le record appartient
     */
    private PageId pageId;

    /**
     * un entier qui est l’indice de la case du slot directory qui pointe vers le
     * record
     */

    private int slotIdx;


    /**
     * 
     * @param id
     * @param idx
     */
    public RecordId(PageId id, int idx) {

        this.pageId = id;
        this.slotIdx = idx;
    }


    /**
     * 
     * @param record
     */
    public RecordId(Record record) {
    }


    /**
     * 
     * @return
     */
    public PageId getPageId() {
        return this.pageId;
    }

    /**
     * 
     * @param pageId
     */
    public void setPageId(PageId pageId) {
        this.pageId = pageId;
    }

    /**
     * 
     * @return
     */
    public int getSlotIdx() {
        return this.slotIdx;
    }

    /**
     * 
     * @param slotIdx
     */
    public void setSlotIdx(int slotIdx) {
        this.slotIdx = slotIdx;
    }


}
