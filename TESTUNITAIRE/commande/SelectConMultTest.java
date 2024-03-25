package TESTUNITAIRE.commande;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.Test;

import CODE.commande.createTable.CreateTable;
import CODE.commande.insert.InsertCommande;
import CODE.commande.select.SelectCommande;
import CODE.couche1.DiskManager;
import CODE.couche2.BufferManager;
import CODE.couche3.FileManager;
import CODE.parametre.DBParams;

public class SelectConMultTest {
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

    InsertCommande ic2 = new InsertCommande("insert into num values (5)");
    ic2.Execute();

    InsertCommande ic3 = new InsertCommande("insert into num values (0)");
    ic3.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id=1 and id=5");

    assertEquals(sc.getListCondition().size(), 2);

    assertEquals(sc.getListCondition().get(0), "ID=1");
    assertEquals(sc.getListCondition().get(1), "ID=5");
  }

  @Test
  public void getListConditionTest2() throws Exception {
    init();
    CreateTable ct1 = new CreateTable(
      "CREATE TABLE NUM (id:int,nom:varstring(4))"
    );
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (1,ad)");
    ic1.Execute();

    SelectCommande sc = new SelectCommande(
      "select * from num where id=1 and nom=a"
    );

    assertEquals(sc.getListCondition().size(), 2);

    assertEquals(sc.getListCondition().get(0), "ID=1");

    assertEquals(sc.getListCondition().get(1), "NOM=A");
  }

  @Test
  public void condEqualIntTest() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:int,age:int)");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (1,20)");
    ic1.Execute();

    InsertCommande ic2 = new InsertCommande("insert into num values (5,14)");
    ic2.Execute();

    InsertCommande ic3 = new InsertCommande("insert into num values (0,20)");
    ic3.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id=1 and age=20");
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 1);
    assertEquals(sc.getListeRecValide().get(0), "1, 20");
  }

  @Test
  public void condNotEqualIntTest() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:int,age:int)");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (1,20)");
    ic1.Execute();

    InsertCommande ic2 = new InsertCommande("insert into num values (5,14)");
    ic2.Execute();

    InsertCommande ic3 = new InsertCommande("insert into num values (0,21)");
    ic3.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id<>1 and age<>20");
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 2);
    assertEquals(sc.getListeRecValide().get(0), "5, 14");
    assertEquals(sc.getListeRecValide().get(1), "0, 21");
  }

  @Test
  public void condSupIntTest() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:int,age:int)");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (1,20)");
    ic1.Execute();

    InsertCommande ic2 = new InsertCommande("insert into num values (5,14)");
    ic2.Execute();

    InsertCommande ic3 = new InsertCommande("insert into num values (0,21)");
    ic3.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id>1 and age>12");
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 1);
    assertEquals(sc.getListeRecValide().get(0), "5, 14");
  }


  @Test
  public void condInfIntTest() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:int,age:int)");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (1,20)");
    ic1.Execute();

    InsertCommande ic2 = new InsertCommande("insert into num values (5,14)");
    ic2.Execute();

    InsertCommande ic3 = new InsertCommande("insert into num values (0,21)");
    ic3.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id<1 and age<22");
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 1);
    assertEquals(sc.getListeRecValide().get(0), "0, 21");
  }

  @Test
  public void condSupEqualIntTest() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:int,age:int)");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (1,20)");
    ic1.Execute();

    InsertCommande ic2 = new InsertCommande("insert into num values (5,14)");
    ic2.Execute();

    InsertCommande ic3 = new InsertCommande("insert into num values (0,21)");
    ic3.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id>=1 and age>=14");
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 2);
    assertEquals(sc.getListeRecValide().get(0), "1, 20");
    assertEquals(sc.getListeRecValide().get(1), "5, 14");
  }
}