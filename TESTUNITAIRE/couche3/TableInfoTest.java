package TESTUNITAIRE.couche3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import CODE.couche3.TableInfo;
import org.junit.Test;

public class TableInfoTest {

  @Test
  public void constructeurTest() throws Exception {
    assertThrows(
      Exception.class,
      () -> {
        TableInfo tableInfo1 = new TableInfo(null, 0);
      }
    );

    assertDoesNotThrow(() -> {
      TableInfo tableInfo2 = new TableInfo("da", 0);
    });
  }

  @Test
  public void equalsTest() throws Exception {
    TableInfo tableInfo1 = new TableInfo("Etudiant", 0);

    TableInfo tableInfo2 = new TableInfo("ETUDIANT", 2);

    TableInfo tableInfo3 = new TableInfo("PRof", 0);

  
    assertTrue(tableInfo1.equals(tableInfo2));
    assertTrue(tableInfo1.equals(tableInfo1));   
    assertFalse(tableInfo1.equals(tableInfo3));
  }

 
}
