package TESTUNITAIRE.commande;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import CODE.commande.createTable.CreateTable;
import CODE.couche1.DiskManager;
import CODE.couche2.BufferManager;
import CODE.couche3.BD.DatabaseInfo;
import CODE.couche3.FileManager;
import CODE.parametre.DBParams;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class CreateTableTest {

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
  public void nomRelTest() throws Exception {
    init();
    CreateTable ct = new CreateTable("create table etudiant (a:INT)");
    ct.parsingCom();
    assertEquals(ct.getNomRelation(), "ETUDIANT");
  }

  @Test
  public void comValTest() throws Exception {
    init();
    CreateTable ct = new CreateTable("create table etudiant(a:INT)");
    assertFalse(ct.comValide());
  }

  @Test
  public void getNombreColTest() throws Exception {
    init();
    CreateTable ct = new CreateTable("create table etudiant (a:INT,b:int)");
    ct.parsingCom();
    assertEquals(ct.getNbColonne(), 2);

    CreateTable ct2 = new CreateTable("create table etudiant (a:INT)");
    ct2.parsingCom();
    assertEquals(ct2.getNbColonne(), 1);
  }

  @Test
  public void colTest() throws Exception {
    init();
    List<String> colNom = new ArrayList<>();
    CreateTable ct1 = new CreateTable("create table etudiant (a:INT,b:int)");
    colNom.add("a");
    colNom.add("b");
    ct1.parsingCom();
    assertEquals(ct1.getNomsColonnes().get(0), "A");
    assertEquals(ct1.getNomsColonnes().get(1), "B");
    assertEquals(ct1.getTypesColonnes().get(0), "INT");
    assertEquals(ct1.getTypesColonnes().get(1), "INT");

    colNom = new ArrayList<>();
    CreateTable ct2 = new CreateTable(
      "create table etudiant (nom:string(3),b:int)"
    );
    colNom.add("nom");
    colNom.add("b");
    ct2.parsingCom();
    assertEquals(ct2.getNomsColonnes().get(0), "NOM");
    assertEquals(ct2.getTypesColonnes().get(0), "STRING");
    assertEquals(ct2.getListeColonne().get(0).getTaille_col(), 3);
    assertEquals(ct2.getNomsColonnes().get(1), "B");
    assertEquals(ct2.getTypesColonnes().get(1), "INT");

    colNom = new ArrayList<>();
    CreateTable ct3 = new CreateTable(
      "create table etudiant (nom:string(3),prenom:varstring(837))"
    );
    colNom.add("nom");
    colNom.add("b");
    ct3.parsingCom();
    assertEquals(ct3.getNomsColonnes().get(0), "NOM");
    assertEquals(ct3.getTypesColonnes().get(0), "STRING");
    assertEquals(ct3.getListeColonne().get(0).getTaille_col(), 3);
    assertEquals(ct3.getNomsColonnes().get(1), "PRENOM");
    assertEquals(ct3.getTypesColonnes().get(1), "VARSTRING");
    assertEquals(ct3.getListeColonne().get(1).getTaille_col(), 837);
  }

  @Test
  public void ExecuteTest() throws Exception {
    init();

    assertDoesNotThrow(() -> {
      CreateTable ct1 = new CreateTable("create table etudiant (a:INT,b:int)");
      ct1.Execute();
    });

    assertDoesNotThrow(() -> {
      CreateTable ct2 = new CreateTable(
        "create table prof (nom:string(3),prenom:varstring(837))"
      );
      ct2.Execute();
    });

    assertThrows(
      Exception.class,
      () -> {
        CreateTable ct3 = new CreateTable(
          "create table Prof (nom:string(3),prenom:varstring(837))"
        );
        ct3.Execute();
      }
    );
  }
}
