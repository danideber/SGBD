package TESTUNITAIRE.commande;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.io.File;

import org.junit.Test;

import CODE.commande.createTable.CreateTable;
import CODE.commande.insert.InsertCommande;
import CODE.couche1.DiskManager;
import CODE.couche2.BufferManager;
import CODE.couche3.FileManager;
import CODE.couche3.BD.DatabaseInfo;
import CODE.parametre.DBParams;

public class InsertComTest {
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
  public void insertOneValue() throws Exception{
    init();
    CreateTable ct1=new CreateTable("CREATE TABLE NUM (id:int)");
    ct1.Execute();
    assertDoesNotThrow(()->{
    InsertCommande ic1=new InsertCommande("insert into num values (1)");
    ic1.Execute();
    });
    assertThrows(Exception.class, ()->{
      InsertCommande ic2=new InsertCommande("insert into ngygdm values (1)");
      ic2.Execute();
    });

    assertThrows(Exception.class, ()->{
      InsertCommande ic3=new InsertCommande("insert into num values (ugugyufg)");
      ic3.Execute();
    });
  }

  @Test
  public void floatTest() throws Exception{
    init();
    CreateTable ct1=new CreateTable("CREATE TABLE NUM (id:float)");
    ct1.Execute();
    assertDoesNotThrow(()->{
    InsertCommande ic1=new InsertCommande("insert into num values (1)");
    ic1.Execute();
    });

   assertEquals(1, fileManager.GetAllRecords(ct1.getTableInfo()).size());
  }

  @Test
  public void varStringTest() throws Exception{
    init();
    CreateTable ct1=new CreateTable("CREATE TABLE NUM (id:varstring(6))");
    ct1.Execute();
    assertDoesNotThrow(()->{
    InsertCommande ic1=new InsertCommande("insert into num values (1gyf)");
    ic1.Execute();

    InsertCommande ic2=new InsertCommande("insert into num values (1gyfhgygyg)");
    ic2.Execute();
    });
    assertEquals(2, fileManager.GetAllRecords(ct1.getTableInfo()).size());
  }



}
