package TESTUNITAIRE.couche1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import CODE.couche1.DiskManager;
import CODE.page.PageId;
import CODE.parametre.DBParams;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DiskManagerTest {

  public static DiskManager dm;

  @BeforeAll
  public static void beforeAll() {
    init();
  }

  public static void init() {
    dm = dm.getInstance();
    DBParams.DMFileCount = 2;
    DBParams.SGBDPageSize = 4096;
    DBParams.DBPath = "DB" + File.separator + "DATAFILE";
  }




  @ParameterizedTest
  @ValueSource(strings = { "1" })
  public void creerFichierTest1(int value) throws Exception {
    init();
    dm.creerFichier(value);
  }


  @Test
  public void allocPageTest() {
    init();
    assertEquals(0, dm.GetCurrentCountAllocPages());
    dm.AllocPage();
    dm.AllocPage();
    assertEquals(2, dm.GetCurrentCountAllocPages());
    System.out.println(2 == dm.GetCurrentCountAllocPages());
  }

    /**
   * Permet de tester que l'on le peut pas avoir comme id de fichier qui commence par 1
   * @param value
   * @throws Exception
   */
  @ParameterizedTest
  @ValueSource(strings = { "6", "5", "16", "-5" })
  public void creerFichierTest2(int value) throws Exception {
    dm = DiskManager.getInstance();
    assertThrows(Exception.class, () -> dm.creerFichier(value));
  }



  /**
   * Le but ici est de tester si il y a une page dispnible, es ce que la méthode
   * AllocPage, va nous la retourner
   */
  @Test
  public void allocPageTest2() {
    init();
    DiskManager dm = DiskManager.getInstance();
    PageId page1 = dm.AllocPage();
    dm.DeallocPage(page1);
    PageId page2 = dm.AllocPage();
    assertTrue(page1.equals(page2));
  }

  @Test
  public void deallocPageTest() {
    init();
    dm = DiskManager.getInstance();
    assertEquals(0, dm.GetCurrentCountAllocPages());
    PageId page1 = dm.AllocPage();
    dm.AllocPage();
    dm.DeallocPage(page1);
    assertEquals(1, dm.GetCurrentCountAllocPages());
  }

  @Test
  @Timeout(value = 10,unit = TimeUnit.SECONDS)
  public void readPageTest() {
    init();
    PageId page = dm.AllocPage();
    ByteBuffer byteBuffer = ByteBuffer.allocate(DBParams.SGBDPageSize);
    dm.ReadPage(page, byteBuffer);
  }

  @Test
  @Timeout(value = 10,unit = TimeUnit.SECONDS)
  public void writePageTest() {
    init();
    PageId page = dm.AllocPage();
    ByteBuffer byteBuffer = ByteBuffer.allocate(DBParams.SGBDPageSize);
    dm.WritePage(page, byteBuffer);
  }
}
