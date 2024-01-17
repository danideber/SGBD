package TESTUNITAIRE.couche1;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import CODE.couche1.DiskManager;
import CODE.parametre.DBParams;
import java.io.File;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DiskManagerTest {

  @BeforeAll
  public static void BeforeAll() {
    DBParams.DMFileCount = 1;
    DBParams.DMFileCount = 3;
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
  @ValueSource(strings = { "6","5","16","-5" })
  public void creerFichierTest2(int value) throws Exception {
    DiskManager dm = DiskManager.getInstance();
    assertThrows(Exception.class,()-> dm.creerFichier(value));
  }
}
