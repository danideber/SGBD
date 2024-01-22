package CODE.couche3.record;

import java.nio.ByteBuffer;

import CODE.couche2.BufferManager;
import CODE.couche3.TableInfo;
import CODE.page.PageId;
import CODE.parametre.DBParams;

public class RecordIterator {
    private TableInfo tabInfo;
    private PageId pageId;
    private int position;
    private ByteBuffer byteBuffer = null;
    private Record record = null;

    // Tant qu'il est inférieur au nbRecord alors on ne renvoie pas null
    private int indiceSuiteRecord ;

    // Taille du record que l'on récupère
    private int tailleRecord;

    // Nombre courant de record
    private int nbRecord;

    // Position du slot directory courant
    private int numRecord;

    public RecordIterator(TableInfo tabInfo, PageId pageId) {
        this.tabInfo = tabInfo;
        this.pageId = pageId;
        this.position = 8;

        indiceSuiteRecord=0;

        // Je vais me position au premier slot directory
        numRecord = DBParams.SGBDPageSize - 16;

        byteBuffer = BufferManager.getInstance().GetPage(pageId);

        // Je me position vers le nombre d'entrée de slot directory
        byteBuffer.position(DBParams.SGBDPageSize - 8);

        this.nbRecord = byteBuffer.getInt();

        // Je me position au début de la page identifié
        byteBuffer.position(0);
        this.record = new Record(tabInfo);

    }

    /**
     * une méthode
     * record GetNextRecord(), avec record un Record.
     * Nous allons appeler itérativement cette méthode pour parcourir les records de
     * la page.
     * La méthode retournera null lorsqu’il ne reste plus de record sur la page.
     * 
     * @return
     */
    public Record GetNextRecord() {
        if(numRecord==0){
            numRecord= DBParams.SGBDPageSize - 16;
        }
        if (indiceSuiteRecord < nbRecord) {
            byteBuffer.position(numRecord);

            // Je me positionne au début
            this.position = byteBuffer.getInt();
            this.tailleRecord = byteBuffer.getInt();
            // Pour me positionner au début du prochain slot directory
            this.numRecord -= 8;

            record.readFromBuffer(byteBuffer, position);
            
            indiceSuiteRecord++;

            return record;
        }

        return null;

    }

    /**
     * qui signale qu’on n’utilise plus cet itérateur. Cette méthode libère la page
     * de données auprès
     * du BM.
     */
    public void Close() {
        System.out.println("Cet itérateur n'est pas utilisé");
        BufferManager.getInstance().FreePage(pageId, false);
    }

    /**
     * 
     */
    public void Reset() {
        indiceSuiteRecord=0;
        numRecord=0;
    }

    /**
     * 
     * @return
     */
    public int getNbSlotDirectory() {
        return nbRecord;
    }

    /**
     * 
     * @return
     */
    public TableInfo getTableInfo() {
        return tabInfo;
    }
}
