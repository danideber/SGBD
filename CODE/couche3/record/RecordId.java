package CODE.couche3.record;

import CODE.page.PageId;

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


    public RecordId(PageId id, int idx) {

        this.pageId = id;
        this.slotIdx = idx;
    }


    public RecordId(Record record) {
    }


    public PageId getPageId() {
        return this.pageId;
    }

    public void setPageId(PageId pageId) {
        this.pageId = pageId;
    }

    public int getSlotIdx() {
        return this.slotIdx;
    }

    public void setSlotIdx(int slotIdx) {
        this.slotIdx = slotIdx;
    }


}
