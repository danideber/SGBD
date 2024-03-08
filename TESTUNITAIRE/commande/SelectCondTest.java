package TESTUNITAIRE.commande;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
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

public class SelectCondTest {

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
  public void getListConditionTest() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:int)");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (1)");
    ic1.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id=1");

    assertEquals(sc.getListCondition().size(), 1);

    assertEquals(sc.getListCondition().get(0), "ID=1");
  }



  @Test
  public void getListConditionTest2() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:int,nom:varstring(4))");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (1,ad)");
    ic1.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id=1 and nom=a");

    assertEquals(sc.getListCondition().size(), 2);

    assertEquals(sc.getListCondition().get(0), "ID=1");

    assertEquals(sc.getListCondition().get(1), "NOM=A");
  }

  @Test
  public void condEqualIntTest() throws Exception{
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:int)");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (1)");
    ic1.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id=1");
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 1);
    assertEquals(sc.getListeRecValide().get(0), "1");

  }

  @Test
  public void condEqualFloatTest() throws Exception{
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:float)");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (7.5)");
    ic1.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id=7.5");
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 1);
    assertEquals(sc.getListeRecValide().get(0), "7.5");

  }

  @Test
  public void condEqualStringTest() throws Exception{
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE Etudiant (id:string(8))");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into etudiant values (daniel)");
    ic1.Execute();

    InsertCommande ic2 = new InsertCommande("insert into etudiant values (dani)");
    ic2.Execute();

    SelectCommande sc = new SelectCommande("select * from etudiant where id=daniel");
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 1);
    assertEquals(sc.getListeRecValide().get(0), "DANIEL");

    SelectCommande sc1 = new SelectCommande("select * from etudiant where id=dani");
    sc1.Execute();

    assertEquals(sc1.getListeRecValide().size(), 1);
    assertEquals(sc1.getListeRecValide().get(0), "DANI");

  }

  @Test
  public void condEqualVarStringTest() throws Exception{
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:int)");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (1)");
    ic1.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id=1");
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 1);
    assertEquals(sc.getListeRecValide().get(0), "[1]");

  }


  @Test
  public void condNotEqualIntTest() throws Exception{
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:int)");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (1)");
    ic1.Execute();

    InsertCommande ic2 = new InsertCommande("insert into num values (7)");
    ic2.Execute();

    InsertCommande ic3 = new InsertCommande("insert into num values (9)");
    ic3.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id<>1");
    sc.Execute();

  }

  }



