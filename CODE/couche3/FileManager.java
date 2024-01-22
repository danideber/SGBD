package CODE.couche3;

import CODE.couche1.DiskManager;
import CODE.couche2.BufferManager;
import CODE.couche3.record.Record;
import CODE.couche3.record.RecordId;
import CODE.page.PageId;
import CODE.parametre.DBParams;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

  /**
   * attribut statique correspondant au FileManager
   */
  private static FileManager fm = null;

  /**
   * Constructeur du FileManager
   */
  private FileManager() {}

  /**
   *
   * @return Objet de type FileManager
   */
  public static FileManager getInstance() {
    if (fm == null) {
      fm = new FileManager();
    }

    return fm;
  }

  /**
   * Permet de trouver l'entrée M correspondant au nombre de records dans une page
   *  de données
   *
   * @return le nombre d'entrées
   */
  public int nombreEntree(ByteBuffer biteBuffer) {
    biteBuffer.position(DBParams.SGBDPageSize - 8);
    int m = biteBuffer.getInt();

    return m;
  }

  /**
   * Méthode permettant de trouver l'espace disponible sur une page de données
   *
   * @param dataPage page de données
   * @param bm BufferManager
   * @return espace disponible
   */
  public int espaceDispo(PageId dataPage, BufferManager bm) {
    ByteBuffer biteBuffer = bm.GetPage(dataPage);
    biteBuffer.position(DBParams.SGBDPageSize - 4);

    int positDebut = biteBuffer.getInt();

    biteBuffer.position(DBParams.SGBDPageSize - 8);

    int m = nombreEntree(biteBuffer);

    int positionFin = (DBParams.SGBDPageSize - 8) - (m * 8);

    bm.FreePage(dataPage, false);

    return positionFin - positDebut;
  }

  /**
   *
   * @param dataPage page de données
   * @param bm BufferManager
   * @param biteBuffer
   * @return
   */
  public int tailleToutRecord(PageId dataPage, BufferManager bm) {
    ByteBuffer biteBuffer = bm.GetPage(dataPage);
    biteBuffer.position(DBParams.SGBDPageSize - 4);
    int positDebut = biteBuffer.getInt();

    bm.FreePage(dataPage, false);
    return positDebut - 8;
  }

  /**
   * Méthode permettant de trouver la position de début de l'espace disponible
   * @param biteBuffer
   * @return position de début de l'espace disponible pour l'écriture
   */
  public int positionEcritureRecord(ByteBuffer biteBuffer) {
    biteBuffer.position(DBParams.SGBDPageSize - 4);
    int positDebut = biteBuffer.getInt();

    return positDebut;
  }

  /**
   * Méthode pour déplacer une dataPage vers la liste des data page pleines
   * @param dataPage page de données
   * @param tabInfo tableInfo
   * @throws Exception
   */
  public void deplacerToListePleine(PageId dataPage, TableInfo tabInfo)
    throws Exception {
    BufferManager bm = BufferManager.getInstance();
    ByteBuffer bf = bm.GetPage(tabInfo.getHeaderPage());

    ByteBuffer bfData = bm.GetPage(tabInfo.getHeaderPage());

    bf.position(8);

    int fileId = bf.getInt();
    int pageIx = bf.getInt();

    int fileIdData = dataPage.getFileIdx();
    int pageIxData = dataPage.getPageIdx();

    if (fileId == -1 && pageIx == -1) {
        bf.position(8);
        bf.putInt(fileIdData);
        bf.putInt(pageIxData);
    } else {
      while (fileId != -1 && pageIx != -1) {
        ByteBuffer byteBuffer = bm.GetPage(new PageId(fileId, pageIx));
        byteBuffer.position(0);
        fileId = byteBuffer.getInt();
        pageIx = byteBuffer.getInt();
        if (fileId == -1 && pageIx == -1) {
          byteBuffer.position(0);
          byteBuffer.putInt(fileIdData);
          byteBuffer.putInt(pageIxData);
          bm.FreePage(new PageId(fileId, pageIx), true);
        } else {
          bm.FreePage(new PageId(fileId, pageIx), false);
        }
      }
    }   

    bfData.position(0);
    bfData.putInt(fileId);
    bfData.putInt(pageIx);

    bm.FreePage(tabInfo.getHeaderPage(), true);
    bm.FreePage(dataPage, true);
  }

  /**
   * Cette méthode devra gérer :
   * • L’allocation d’une nouvelle page via AllocPage du DiskManager.
   * C’est le PageId rendu par AllocPage qui devra aussi être rendu par
   * createNewHeaderPage
   * • L’écriture dans la page allouée de deux PageIds « factices », correspondant
   * à des listes de
   * pages de données initialement vides.
   * Pour écrire, il faudra d’abord récupérer la page via le BufferManager, puis
   * la libérer auprès
   * du BufferManager (avec le bon flag dirty).
   *
   * @return PageId
   * @throws Exception
   */
  public PageId createNewHeaderPage() throws Exception {
    // Allocation d’une nouvelle page via AllocPage du DiskManage
    DiskManager dm = DiskManager.getInstance();
    PageId headerPage = dm.AllocPage();

    // récupérer la page via le BufferManager
    BufferManager bm = BufferManager.getInstance();
    ByteBuffer bf = bm.GetPage(headerPage);

    PageId page1 = new PageId(-1, -1);
    PageId page2 = new PageId(-1, -1);

    bf.position(0);
    bf.putInt(page1.getFileIdx());
    bf.putInt(page1.getPageIdx());

    bf.putInt(page2.getFileIdx());
    bf.putInt(page2.getPageIdx());

    // Libérer le buffer avec le bon flag dirty afin de modifier mon headerPage
    bm.FreePage(headerPage, true);

    return headerPage;
  }

  /**
   * ---------------------------------------- A continuer ----------------------------------------------
   * Cette méthode devra rajouter une page de données « vide » au Heap File
   * correspondant à la relation
   * identifiée par tabInfo, et retourner le PageId de cette page.
   * Pour cela, elle devra :
   * • allouer une nouvelle page via AllocPage du DiskManager. C’est le PageId
   * rendu par
   * AllocPage qui devra aussi être rendu par addDataPage
   * • chaîner la page dans la liste des pages « où il reste de la place » ; pour
   * cela, il faudra lire
   * et écrire des PageIds dans la page nouvellement créée, ainsi que (suivant
   * votre stratégie)
   * dans d’autres pages
   *
   * @param tabInfo
   * @return
   * @throws Exception
   */
  public PageId addDataPage(TableInfo tabInfo) throws Exception {
    // Allocation d’une nouvelle page via AllocPage du DiskManage
    DiskManager dm = DiskManager.getInstance();
    PageId dataPage = dm.AllocPage();

    int fileId = 0, pageIx = 0, fileIdData = 0, pageIxData = 0;

    // récupérer la page via le BufferManager
    BufferManager bm = BufferManager.getInstance();

    ByteBuffer bHeader = bm.GetPage(tabInfo.getHeaderPage());

    bHeader.position(0);
    fileId = bHeader.getInt();
    pageIx = bHeader.getInt();

    ByteBuffer bfData = bm.GetPage(dataPage);
    // Ecrire

    fileIdData = dataPage.getFileIdx();
    pageIxData = dataPage.getPageIdx();

    if (fileId == -1 && pageIx == -1) {
        bHeader.position(0);
        bHeader.putInt(fileIdData);
        bHeader.putInt(pageIxData);
        bm.FreePage(tabInfo.getHeaderPage(), true);
        
    } else {
      while (fileId != -1 && pageIx != -1) {
        ByteBuffer byteBuffer = bm.GetPage(new PageId(fileId, pageIx));
        byteBuffer.position(0);
        fileId = byteBuffer.getInt();
        pageIx = byteBuffer.getInt();
        if (fileId == -1 && pageIx == -1) {
          byteBuffer.position(0);
          byteBuffer.putInt(fileIdData);
          byteBuffer.putInt(pageIxData);
          bm.FreePage(new PageId(fileId, pageIx), true);
        } else {
          bm.FreePage(new PageId(fileId, pageIx), false);
        }
      }
    }

    bfData.position(0);
    bfData.putInt(fileId);
    bfData.putInt(pageIx);

    bfData.position(DBParams.SGBDPageSize - 4);
    bfData.putInt(8);

    bm.FreePage(dataPage, true);
    bm.FreePage(tabInfo.getHeaderPage(), true);

    return dataPage;
  }

  /**
   *
   * @param tableInfo
   * @param sizeRecord
   * @return
   * @throws Exception
   */
  public PageId getFreeDatPageId(TableInfo tableInfo, int sizeRecord)
    throws Exception {
    BufferManager bm = BufferManager.getInstance();

    // PageId dataPage = null;
    // int fileIdData = 0;
    // int pageIdData = 0;

    // // Je récupère la header page
    // ByteBuffer bBer = bm.GetPage(tableInfo.getHeaderPage());
    // bBer.position(0);
    // fileIdData = bBer.getInt();
    // pageIdData = bBer.getInt();

    // if (fileIdData == -1 && pageIdData == -1) {
    //     PageId nouvPage = addDataPage(tableInfo);
    //     tableInfo.setHeaderPage(nouvPage);
    //     return nouvPage;
    //     // return addDataPage(tableInfo);
    // }

    // int espaceDis = 0;

    // do {
    //     dataPage = new PageId(fileIdData, pageIdData);
    //     espaceDis = espaceDispo(dataPage, bBer);
    //     if (espaceDis >= sizeRecord) {
    //         System.out.println("ici");
    //         bm.FreePage(tableInfo.getHeaderPage(), false);
    //         break;
    //     }
    //     else{
    //         System.out.println("uhhuhu");
    //     }
    //     bBer.clear();
    //     bm.FreePage(dataPage, false);
    //     bBer = bm.GetPage(dataPage);
    //     bBer.position(0);
    //     fileIdData = bBer.getInt();
    //     pageIdData = bBer.getInt();

    // } while (fileIdData != -1 && pageIdData != -1);
    PageId headerPage = tableInfo.getHeaderPage();
    ByteBuffer headerByte = bm.GetPage(headerPage);
    headerByte.position(0);
    int fileId = headerByte.getInt();
    int pageId = headerByte.getInt();

    bm.FreePage(headerPage, false);

    while (fileId != -1 && pageId != -1) {
      PageId dataPageId = new PageId(fileId, pageId);
      ByteBuffer dataPageByte = bm.GetPage(dataPageId);

      int m = nombreEntree(dataPageByte);
      System.out.println("m: " + m);

      int espaceDis = espaceDispo(dataPageId, bm);
      bm.FreePage(dataPageId, false);

      if (espaceDis >= sizeRecord) {
        return dataPageId;
      } else {
        return fm.addDataPage(tableInfo);
      }
    }

    return null;
  }

  /**
   *
   * @param record
   * @param pageId
   * @return
   * @throws Exception
   */
  public RecordId writeRecordToDataPage(Record record, PageId pageId)
    throws Exception {
    BufferManager bm = BufferManager.getInstance();

    ByteBuffer byteBuffer = bm.GetPage(pageId);

    record.writeToBuffer(byteBuffer, positionEcritureRecord(byteBuffer));

    if (espaceDispo(pageId, bm) == 0) {
      deplacerToListePleine(pageId, record.getTableInfo());
    }

    bm.FreePage(pageId, true);
    return new RecordId(record);
  }

  /**
   *
   * @param tabInfo
   * @param pageId
   * @return
   */
  public List<Record> getRecordsInDataPage(TableInfo tabInfo, PageId pageId) {
    List<Record> listeRecord = new ArrayList<>();
    int tailleRecord = 8, positionRecup = 0;
    ByteBuffer byteBuff = null;
    ByteBuffer byteBuffRecup = null;
    BufferManager buffManag = BufferManager.getInstance();

    Record monRecord = null;

    byteBuff = buffManag.GetPage(pageId);
    byteBuffRecup = byteBuff;

    byteBuff.position(8);

    positionRecup = DBParams.SGBDPageSize - 8;
    int iteration = nombreEntree(byteBuffRecup);
    byteBuffRecup.position(positionRecup);

    for (int index = 0; index < iteration; index++) {
      byteBuffRecup.position(positionRecup);

      monRecord = new Record(tabInfo);

      tailleRecord += monRecord.readFromBuffer(byteBuff, tailleRecord);
      List<String> valuesRecord = monRecord.getRecvalues();
      listeRecord.add(new Record(tabInfo));
      listeRecord.get(index).setRecvalues(valuesRecord);
      // listeRecord.get(listeRecord.size()-1).setRecvalues(valuesRecord);
      // byteBuffRecup.position(positionRecup);
      // tailleRecord += byteBuffRecup.getInt();
      positionRecup -= 8;
    }

    buffManag.FreePage(pageId, false);

    return listeRecord;
  }

  public List<PageId> getDataPages(TableInfo tabInfo) throws Exception {
    List<PageId> listeDataPage = new ArrayList<>();

    BufferManager bm = BufferManager.getInstance();

    PageId dataPage = null;
    int fileIdData = 0;
    int pageIdData = 0;

    ByteBuffer bBer = bm.GetPage(tabInfo.getHeaderPage());
    bBer.position(0);
    fileIdData = bBer.getInt();
    pageIdData = bBer.getInt();

    while (fileIdData != -1 && pageIdData != -1) {
      dataPage = new PageId(fileIdData, pageIdData);

      listeDataPage.add(dataPage);

      bBer.clear();
      bm.FreePage(dataPage, false);
      bBer = bm.GetPage(dataPage);
      bBer.position(0);
      fileIdData = bBer.getInt();
      pageIdData = bBer.getInt();
    }

    bm.FreePage(tabInfo.getHeaderPage(), false);

    bBer = bm.GetPage(tabInfo.getHeaderPage());
    bBer.position(8);
    fileIdData = bBer.getInt();
    pageIdData = bBer.getInt();

    while (fileIdData != -1 && pageIdData != -1) {
      dataPage = new PageId(fileIdData, pageIdData);

      listeDataPage.add(dataPage);

      bBer.clear();
      bm.FreePage(dataPage, false);
      bBer = bm.GetPage(dataPage);
      bBer.position(0);
      fileIdData = bBer.getInt();
      pageIdData = bBer.getInt();
    }

    return listeDataPage;
  }

  // A voir
  // Je vais écrire le record dans une page de données avec assez d'espace (mon
  // petit test)
  /**
   *
   * @param record
   * @return
   * @throws Exception
   */
  public RecordId InsertRecordIntoTable(Record record) throws Exception {
    BufferManager bm = BufferManager.getInstance();

    // Je récupère la taille du record
    int tailleRecord = record.writeToBuffer(
      ByteBuffer.allocate(DBParams.SGBDPageSize),
      0
    );

    // Obtenir la datapage avec assez d'espace
    PageId pageAvecEspace = getFreeDatPageId(
      record.getTableInfo(),
      tailleRecord
    );

    ByteBuffer byteBuffer = bm.GetPage(pageAvecEspace);

    int positionDebut = positionEcritureRecord(byteBuffer);

    // J'écrit le record dans le buffer
    record.writeToBuffer(byteBuffer, positionDebut);

    // Je vais mettre à jour l'espace disponible
    byteBuffer.position(DBParams.SGBDPageSize - 4);
    byteBuffer.putInt(positionDebut + tailleRecord);

    // Je vais mettre à jour le nombre d'entrée M
    int m = nombreEntree(byteBuffer);
    byteBuffer.position(DBParams.SGBDPageSize - 8);
    byteBuffer.putInt(m + 1);

    // Je vais rajouter un slot directory
    int decalage = 8 + (m + 1) * 8;

    byteBuffer.position(DBParams.SGBDPageSize - decalage);
    byteBuffer.putInt(positionDebut);
    byteBuffer.putInt(tailleRecord);

    bm.FreePage(pageAvecEspace, true);

    return new RecordId(record);
  }

  // A voir
  public List<Record> GetAllRecords(TableInfo tabInfo) throws Exception {
    List<Record> listeRecord = new ArrayList<>();

    List<Record> listeRecordFinal = new ArrayList<>();

    List<PageId> listeDataPage = new ArrayList<>();

    // Je récuprère toutes les dataPages
    listeDataPage = getDataPages(tabInfo);

    // Je parcours la liste des pages de données
    for (int i = 0; i < listeDataPage.size(); i++) {
      listeRecord = getRecordsInDataPage(tabInfo, listeDataPage.get(i));

      // Je parcours la liste des records d'une page data
      for (int j = 0; j < listeRecord.size(); j++) {
        // Je rajoute un record dans ma liste de retour si la tableInfo est égale à
        // celle en paramètre
        listeRecordFinal.add(listeRecord.get(j));
      }
    }

    return listeRecordFinal;
  }
}
