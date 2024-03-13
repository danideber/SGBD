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
  public void condEqualFloatTest() throws Exception {
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
  public void condEqualStringTest() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE Etudiant (id:string(8))");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande(
      "insert into etudiant values (daniel)"
    );
    ic1.Execute();

    InsertCommande ic2 = new InsertCommande(
      "insert into etudiant values (dani)"
    );
    ic2.Execute();

    SelectCommande sc = new SelectCommande(
      "select * from etudiant where id=daniel"
    );
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 1);
    assertEquals(sc.getListeRecValide().get(0), "DANIEL");

    SelectCommande sc1 = new SelectCommande(
      "select * from etudiant where id=dani"
    );
    sc1.Execute();

    assertEquals(sc1.getListeRecValide().size(), 1);
    assertEquals(sc1.getListeRecValide().get(0), "DANI");
  }

  @Test
  public void condEqualVarStringTest() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:varstring(1))");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (1)");
    ic1.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id=1");
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 1);
    assertEquals(sc.getListeRecValide().get(0), "1");
  }

  @Test
  public void condNotEqualIntTest() throws Exception {
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

    assertEquals(sc.getListeRecValide().size(), 2);
    assertEquals(sc.getListeRecValide().get(0), "7");
    assertEquals(sc.getListeRecValide().get(1), "9");
  }

  @Test
  public void condNotEqualFloatTest() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:Float)");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (8.3)");
    ic1.Execute();

    InsertCommande ic2 = new InsertCommande("insert into num values (0.7)");
    ic2.Execute();

    InsertCommande ic3 = new InsertCommande("insert into num values (9)");
    ic3.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id<>0.7");
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 2);
    assertEquals(sc.getListeRecValide().get(0), "8.3");
    assertEquals(sc.getListeRecValide().get(1), "9.0");
  }

  @Test
  public void condNotEqualStringTest() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:String(5))");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (dani)");
    ic1.Execute();

    InsertCommande ic2 = new InsertCommande("insert into num values (deb)");
    ic2.Execute();

    InsertCommande ic3 = new InsertCommande("insert into num values (z)");
    ic3.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id<>z");
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 2);
    assertEquals(sc.getListeRecValide().get(0), "DANI");
    assertEquals(sc.getListeRecValide().get(1), "DEB");
  }

  @Test
  public void condNotEqualVarStringTest() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:varstring(5))");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (dani)");
    ic1.Execute();

    InsertCommande ic2 = new InsertCommande("insert into num values (deb)");
    ic2.Execute();

    InsertCommande ic3 = new InsertCommande("insert into num values (z)");
    ic3.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id<>z");
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 2);
    assertEquals(sc.getListeRecValide().get(0), "DANI");
    assertEquals(sc.getListeRecValide().get(1), "DEB");
  }

  @Test
  public void condLowerIntTest() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:int)");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (3)");
    ic1.Execute();

    InsertCommande ic2 = new InsertCommande("insert into num values (2)");
    ic2.Execute();

    InsertCommande ic3 = new InsertCommande("insert into num values (93)");
    ic3.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id<93");
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 2);
    assertEquals(sc.getListeRecValide().get(0), "3");
    assertEquals(sc.getListeRecValide().get(1), "2");
  }

  @Test
  public void condSuperiorIntTest() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:int)");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (3)");
    ic1.Execute();

    InsertCommande ic2 = new InsertCommande("insert into num values (2)");
    ic2.Execute();

    InsertCommande ic3 = new InsertCommande("insert into num values (93)");
    ic3.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id>3");
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 1);
    assertEquals(sc.getListeRecValide().get(0), "93");
  }

  @Test
  public void condSuperiorFloatTest() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:float)");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (3.97)");
    ic1.Execute();

    InsertCommande ic2 = new InsertCommande("insert into num values (2.23)");
    ic2.Execute();

    InsertCommande ic3 = new InsertCommande("insert into num values (3.96)");
    ic3.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id>3.96");
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 1);
    assertEquals(sc.getListeRecValide().get(0), "3.97");
  }


  @Test
  public void condSuperiorStringTest() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:string(5))");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (a)");
    ic1.Execute();

    InsertCommande ic2 = new InsertCommande("insert into num values (c)");
    ic2.Execute();

    InsertCommande ic3 = new InsertCommande("insert into num values (b)");
    ic3.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id>a");
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 2);
    assertEquals(sc.getListeRecValide().get(0), "C");
    assertEquals(sc.getListeRecValide().get(1), "B");
  }


  @Test
  public void condLowerStringTest() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:string(5))");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (a)");
    ic1.Execute();

    InsertCommande ic2 = new InsertCommande("insert into num values (c)");
    ic2.Execute();

    InsertCommande ic3 = new InsertCommande("insert into num values (b)");
    ic3.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id<b");
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 1);
    assertEquals(sc.getListeRecValide().get(0), "A");
  }

  @Test
  public void condSuperiorOrEqualIntTest() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:int)");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (3)");
    ic1.Execute();

    InsertCommande ic2 = new InsertCommande("insert into num values (2)");
    ic2.Execute();

    InsertCommande ic3 = new InsertCommande("insert into num values (93)");
    ic3.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id>=3");
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 2);
    assertEquals(sc.getListeRecValide().get(0), "3");
    assertEquals(sc.getListeRecValide().get(1), "93");
  }

  @Test
  public void condLowerOrEqualIntTest() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:int)");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (3)");
    ic1.Execute();

    InsertCommande ic2 = new InsertCommande("insert into num values (2)");
    ic2.Execute();

    InsertCommande ic3 = new InsertCommande("insert into num values (93)");
    ic3.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id<=3");
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 2);
    assertEquals(sc.getListeRecValide().get(0), "3");
    assertEquals(sc.getListeRecValide().get(1), "2");
  }


  @Test
  public void condSuperiorOrEqualFloatTest() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:float)");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (3.9)");
    ic1.Execute();

    InsertCommande ic2 = new InsertCommande("insert into num values (20.2)");
    ic2.Execute();

    InsertCommande ic3 = new InsertCommande("insert into num values (93.0)");
    ic3.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id>=20.2");
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 2);
    assertEquals(sc.getListeRecValide().get(0), "20.2");
    assertEquals(sc.getListeRecValide().get(1), "93.0");
  }

  @Test
  public void condLowerOrEqualFloatTest() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:float)");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (2.3)");
    ic1.Execute();

    InsertCommande ic2 = new InsertCommande("insert into num values (2.2)");
    ic2.Execute();

    InsertCommande ic3 = new InsertCommande("insert into num values (3)");
    ic3.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id<=2.3");
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 2);
    assertEquals(sc.getListeRecValide().get(0), "2.3");
    assertEquals(sc.getListeRecValide().get(1), "2.2");
  }


  @Test
  public void condSuperiorOrEqualStringTest() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:string(4))");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (da)");
    ic1.Execute();

    InsertCommande ic2 = new InsertCommande("insert into num values (de)");
    ic2.Execute();

    InsertCommande ic3 = new InsertCommande("insert into num values (e)");
    ic3.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id>=de");
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 2);
    assertEquals(sc.getListeRecValide().get(0), "DE");
    assertEquals(sc.getListeRecValide().get(1), "E");
  }

  @Test
  public void condLowerOrEqualStringTest() throws Exception {
    init();
    CreateTable ct1 = new CreateTable("CREATE TABLE NUM (id:string(4))");
    ct1.Execute();

    InsertCommande ic1 = new InsertCommande("insert into num values (da)");
    ic1.Execute();

    InsertCommande ic2 = new InsertCommande("insert into num values (de)");
    ic2.Execute();

    InsertCommande ic3 = new InsertCommande("insert into num values (e)");
    ic3.Execute();

    SelectCommande sc = new SelectCommande("select * from num where id<=de");
    sc.Execute();

    assertEquals(sc.getListeRecValide().size(), 2);
    assertEquals(sc.getListeRecValide().get(0), "DA");
    assertEquals(sc.getListeRecValide().get(1), "DE");
  }


}
