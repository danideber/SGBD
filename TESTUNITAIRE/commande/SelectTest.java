package TESTUNITAIRE.commande;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import CODE.commande.createTable.CreateTable;
import CODE.commande.insert.InsertCommande;
import CODE.commande.select.SelectCommande;
import CODE.couche1.DiskManager;
import CODE.couche2.BufferManager;
import CODE.couche3.FileManager;
import CODE.parametre.DBParams;
import java.io.File;
import org.junit.Test;

public class SelectTest {

  private static DiskManager dm;
  private static BufferManager bm;
  private static FileManager fileManager;

  public static void init() {
    DBParams.DMFileCount = 1;
    DBParams.SGBDPageSize = 4096;
    DBParams.frameCount = 2;
    DBParams.DBPath = "DB" + File.separator + "DATAFILE";
    dm = DiskManager.getInstance();
    bm = BufferManager.getInstance();
    fileManager = FileManager.getInstance();
  }

  @Test
  public void SelectNoThrows() throws Exception {
    init();

    CreateTable ct1 = new CreateTable("create table etudiant (a:INT,b:int)");
    ct1.Execute();
    assertDoesNotThrow(() -> {
      SelectCommande sc = new SelectCommande("select * from etudiant");
    });
  }

  @Test
  public void SelectValideTest1() throws Exception {
    init();

    CreateTable ct1 = new CreateTable("create table etudiant (a:INT,b:int)");
    ct1.Execute();

    SelectCommande sc = new SelectCommande("select * from etudiant");
    assertTrue(sc.comValide());
  }

  @Test
  public void SelectValideTest2() throws Exception {
    init();

    CreateTable ct1 = new CreateTable("create table etudiant (a:INT,b:int)");
    ct1.Execute();

    SelectCommande sc = new SelectCommande("selct * from etudiant");
    assertFalse(sc.comValide());
  }

  @Test
  public void parsingTest() throws Exception {
    init();

    CreateTable ct1 = new CreateTable("create table etudiant (a:INT,b:int)");
    ct1.Execute();

    SelectCommande sc = new SelectCommande("select * from etudiant");
    sc.parsingCom();
    assertEquals("ETUDIANT", sc.getNomRelation());

    assertEquals(0, sc.getListCondition().size());
  }

  @Test
  public void ExecuteTest1() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:int)");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (1)");
    ic1.Execute();

    SelectCommande sc = new SelectCommande("select * from num");
    assertDoesNotThrow(() -> {
      sc.Execute();
    });

    assertEquals(1, sc.getListeRecValide().size());

    assertEquals("1", sc.getListeRecValide().get(0));
  }

  @Test
  public void ExecuteTest2() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:int)");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (1)");
    ic1.Execute();

    ic1.setLibCommande("insert into num values (7)");
    ic1.Execute();

    ic1.setLibCommande("insert into num values (97)");
    ic1.Execute();

    SelectCommande sc = new SelectCommande("select * from num");
    assertDoesNotThrow(() -> {
      sc.Execute();
    });

    assertEquals(3, sc.getListeRecValide().size());

    assertEquals("1", sc.getListeRecValide().get(0));
    assertEquals("7", sc.getListeRecValide().get(1));
    assertEquals("97", sc.getListeRecValide().get(2));
  }

  @Test
  public void ExecuteTest3() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE R (C1:INT,C2:VARSTRING(8),C3:STRING(3),C4:FLOAT)");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("INSERT INTO R VALUES (1,abeille,bla,2.8)");
    ic1.Execute();

    ic1.setLibCommande("INSERT INTO R VALUES (300,papillon,blu,0.7)");
    ic1.Execute();

    SelectCommande sc = new SelectCommande("select * from r");
    assertDoesNotThrow(() -> {
      sc.Execute();
    });

    assertEquals(2, sc.getListeRecValide().size());

    assertEquals("1, abeille , bla, 2.8", sc.getListeRecValide().get(0).toLowerCase());
    assertEquals("300, papillon, blu, 0.7", sc.getListeRecValide().get(1).toLowerCase());
  }
}
