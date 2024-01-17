package TESTUNITAIRE.controle.file;

import static org.junit.Assert.assertEquals;

import CODE.controle.file.Checkeur;
import CODE.parametre.DBParams;
import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CheckeurTest {

  @BeforeAll
  public static void BeforeAll() {
    DBParams.DMFileCount = 1;
    DBParams.DMFileCount = 3;
    DBParams.DBPath =
      "SGBD" + File.separator + "DB" + File.separator + "DATAFILE";
  }

  @Test
  public void testFileExist() {
    assertEquals(
      DBParams.DBPath + File.separator + "F1.data",
      Checkeur.file_pas_exis()
    );
  }

  @Test
  public void testPlusPetitFichier() {
    try {
      for (int i = 0; i < DBParams.DMFileCount; i++) {
        File file = new File(
          DBParams.DBPath + File.separator + "F" + i + ".data"
        );
        file.createNewFile();
      }

      assertEquals(
        DBParams.DBPath + File.separator + "F1.data",
        Checkeur.plus_petit_fichier()
      );
    } catch (IOException e) {} finally {

    }
  }

  
}
