package TESTUNITAIRE.couche2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import CODE.couche1.DiskManager;
import CODE.couche2.BufferManager;
import CODE.page.PageId;
import CODE.parametre.DBParams;
import java.io.File;
import java.nio.ByteBuffer;
import org.junit.Test;

public class BufferManagerTest {

  public static DiskManager dm;
  public static BufferManager bm;

  public static void init() {
    DBParams.DMFileCount = 1;
    DBParams.SGBDPageSize = 4096;
    DBParams.frameCount = 2;
    DBParams.DBPath = "DB" + File.separator + "DATAFILE";
    dm = dm.getInstance();
    bm = bm.getInstance();
  }

  @Test
  public void getPageTest() throws Exception {
    init();
    PageId page = dm.AllocPage();
    ByteBuffer byteBuffer = bm.GetPage(page);
    byteBuffer.position(0);
    byteBuffer.putDouble(0.3);
    byteBuffer.position(0);
    assertEquals(0.3, byteBuffer.getDouble());
  }

  @Test
  public void getPageTest2() throws Exception {
    init();
    PageId page = dm.AllocPage();
    ByteBuffer byteBuffer = bm.GetPage(page);
    byteBuffer.position(0);
    byteBuffer.putInt(9);
    byteBuffer.position(0);
    bm.FreePage(page, true);
    // bm.FushAll();
    ByteBuffer byteBufferRead = bm.GetPage(page);
    byteBufferRead.position(0);
    assertEquals(9, byteBuffer.getInt());
  }

  @Test
  public void getPageFlushTest() throws Exception {
    init();
    PageId page = dm.AllocPage();
    ByteBuffer byteBuffer = bm.GetPage(page);
    byteBuffer.position(0);
    byteBuffer.putInt(9);
    bm.FreePage(page, true);
    bm.FushAll();
    ByteBuffer byteBufferRead = bm.GetPage(page);
    byteBufferRead.position(0);
    assertEquals(9, byteBuffer.getInt());
  }

  @Test
  public void pageExist() {
    init();
    PageId page = dm.AllocPage();
    PageId page2 = dm.AllocPage();
    bm.GetPage(page);
    bm.GetPage(page2);

    assertEquals(0, bm.exist_pageId(page));
    assertEquals(1, bm.exist_pageId(page2));
  }

  @Test
  public void poolVideTest() {
    init();
    assertTrue(bm.pool_vide());

    PageId page = dm.AllocPage();

    bm.GetPage(page);

    assertFalse(bm.pool_vide());

    bm.FushAll();

    assertTrue(bm.pool_vide());
  }

  @Test
  public void lfuTest() {
    init();
    PageId page1 = dm.AllocPage();
    PageId page2 = dm.AllocPage();
    PageId page3 = dm.AllocPage();

    bm.GetPage(page1);
    bm.GetPage(page2);
    bm.GetPage(page3);
    bm.FreePage(page2, false);

    assertEquals(1, bm.lfu());
  }
}
