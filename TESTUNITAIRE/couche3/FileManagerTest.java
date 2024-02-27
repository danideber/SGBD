package TESTUNITAIRE.couche3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import CODE.couche1.DiskManager;
import CODE.couche2.BufferManager;
import CODE.couche3.ColInfo;
import CODE.couche3.FileManager;
import CODE.couche3.TableInfo;
import CODE.couche3.record.Record;
import CODE.page.PageId;
import CODE.parametre.DBParams;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class FileManagerTest {

  private static DiskManager dm;
  private static BufferManager bm;
  private static FileManager fileManager;

  public static void init() {
    DBParams.DMFileCount = 1;
    DBParams.SGBDPageSize = 4096;
    DBParams.frameCount = 2;
    DBParams.DBPath = "DB" + File.separator + "DATAFILE";
    dm = dm.getInstance();
    bm = bm.getInstance();
    fileManager = FileManager.getInstance();
  }

  @Test
  public void createNewHeaderPageTest() throws Exception {
    init();
    PageId pageId = fileManager.createNewHeaderPage();

    ByteBuffer bufferPageId = bm.GetPage(pageId);
    bufferPageId.position(0);
    assertTrue(bufferPageId.getInt() == -1 && bufferPageId.getInt() == -1);

    assertTrue(pageId.getFileIdx() > 0 && pageId.getPageIdx() >= 0);
  }

  @Test
  public void addDataPageTest() throws Exception {
    init();
    PageId headerPage = fileManager.createNewHeaderPage();

    ByteBuffer bufferHeaderPage = bm.GetPage(headerPage);
    bufferHeaderPage.position(0);
    assertTrue(
      bufferHeaderPage.getInt() == -1 && bufferHeaderPage.getInt() == -1
    );
    TableInfo tableInfo = new TableInfo("Etudiant", 2, headerPage);
    PageId dataPage = fileManager.addDataPage(tableInfo);

    ByteBuffer bufferDataPage = bm.GetPage(dataPage);
    bufferDataPage.position(0);
    assertTrue(bufferDataPage.getInt() == -1 && bufferDataPage.getInt() == -1);

    assertFalse(headerPage.equals(dataPage));

    ByteBuffer bufferHeaderPage2 = bm.GetPage(tableInfo.getHeaderPage());
    bufferDataPage.position(0);
    assertTrue(
      bufferHeaderPage2.getInt() != -1 && bufferHeaderPage2.getInt() != -1
    );
  }

  @Test
  public void nombreEntreeTest() throws Exception {
    init();
    PageId pageId = fileManager.createNewHeaderPage();

    ByteBuffer byteBuffer = bm.GetPage(pageId);
    assertEquals(fileManager.nombreEntree(byteBuffer), 0);

    bm.FreePage(pageId, false);

    TableInfo tableInfo = new TableInfo("Etudiant", 2, pageId);
    // tableInfo.setHeaderPage(pageId);
    List<ColInfo> listeCol = new ArrayList<>();
    List<String> recVal = new ArrayList<>();

    listeCol.add(new ColInfo("Matricule", "Int"));
    listeCol.add(new ColInfo("Nom", "Float"));
    tableInfo.setCol_info(listeCol);

    Record record1 = new Record(tableInfo);

    recVal.add("34");
    recVal.add("4");

    record1.setRecvalues(recVal);

    ByteBuffer byteBuffer3 = bm.GetPage(pageId);
    int fileId2 = 0, pageId2 = 0;

    fileId2 = byteBuffer3.getInt();
    pageId2 = byteBuffer3.getInt();

    assertTrue(fileId2 == -1 && pageId2 == -1);

    ByteBuffer byteBuffer2 = bm.GetPage(pageId);
    byteBuffer2.position(0);
    assertTrue(byteBuffer2.getInt() == -1 && byteBuffer2.getInt() == -1);
    fileManager.InsertRecordIntoTable(record1);
    fileManager.InsertRecordIntoTable(record1);

    assertEquals(
      fileManager.nombreEntree(
        bm.GetPage(fileManager.getFreeDatPageId(tableInfo, 2))
      ),
      2
    );
    assertEquals(fileManager.GetAllRecords(tableInfo).size(), 2);
  }


}
