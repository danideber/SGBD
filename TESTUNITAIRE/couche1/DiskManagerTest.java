package TESTUNITAIRE.couche1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import CODE.couche1.DiskManager;
import CODE.page.PageId;
import CODE.parametre.DBParams;
import java.io.File;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DiskManagerTest {

  @BeforeEach
  public static void beforeEach() {
    DBParams.DMFileCount = 2;
    DBParams.SGBDPageSize=4096;
    DBParams.DBPath =
      "SGBD" + File.separator + "DB" + File.separator + "DATAFILE";
  }

  /**
   * Permet de verifier que l'on peut créer un fichier
   * @param value
   * @throws Exception
   */
  @ParameterizedTest
  @ValueSource(strings = { "1" })
  public void creerFichierTest1(int value) throws Exception {
    DiskManager dm = DiskManager.getInstance();
    dm.creerFichier(value);
  }

  /**
   * Permet de tester que l'on le peut pas avoir comme id de fichier qui commence par 1
   * @param value
   * @throws Exception
   */
  @ParameterizedTest
  @ValueSource(strings = { "6", "5", "16", "-5" })
  public void creerFichierTest2(int value) throws Exception {
    DiskManager dm = DiskManager.getInstance();
    assertThrows(Exception.class, () -> dm.creerFichier(value));
  }

  @Test
  public void allocPageTest() {
    DiskManager dm = DiskManager.getInstance();
    assertEquals(0,dm.GetCurrentCountAllocPages());
    dm.AllocPage();
    dm.AllocPage();
    assertEquals(2,dm.GetCurrentCountAllocPages());
  }

  /**
   * Le but ici est de tester si il y a une page dispnible, es ce que la méthode
   * AllocPage, va nous la retourner
   */
  @Test
  public void allocPageTest2(){
    DiskManager dm = DiskManager.getInstance();
    PageId page1=dm.AllocPage();
    dm.DeallocPage(page1);
    PageId page2=dm.AllocPage();
    assertTrue(page1.equals(page2));

  }

  @Test
  public void deallocPageTest(){
    DiskManager dm = DiskManager.getInstance();
    assertThrows(Exception.class,()->{
      dm.DeallocPage(new PageId(1, 0));
    });
    assertEquals(0,dm.GetCurrentCountAllocPages());
    PageId page1=dm.AllocPage();
    dm.AllocPage();
    dm.DeallocPage(page1);
    assertEquals(1,dm.GetCurrentCountAllocPages());
  }

}
