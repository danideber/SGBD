package TESTUNITAIRE.couche3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import CODE.couche3.ColInfo;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ColInfoTest {

  @ParameterizedTest
  @CsvSource({ "ffatf,string(6)", ",int", "da,varstring(6)" })
  public void constructeurTest(String nom, String type) throws Exception {
    if (nom == null || type == null) {
      assertThrows(
        Exception.class,
        () -> {
          ColInfo colInfo = new ColInfo(nom, type);
        }
      );
    } else {
      ColInfo colInfo = new ColInfo(nom, type);
    }
  }

  @Test
  public void equalsTest() throws Exception {
    ColInfo colInfo1 = new ColInfo("Nom", "STRING(6)");
    ColInfo colInfo2 = new ColInfo("nom", "string(5)");
    ColInfo colInfo3 = new ColInfo("Prenom", "int");

    assertTrue(colInfo1.equals(colInfo2));
    assertTrue(colInfo2.equals(colInfo1));
    assertTrue(colInfo2.equals(colInfo2));
    assertFalse(colInfo3.equals(colInfo1));
  }

  @ParameterizedTest
  @CsvSource({ "ffatf,string(6)", "D,int", "da,varstring(6)","D, FLOAT" })
  public void getTypeTest(String nom, String type) throws Exception {
    ColInfo colInfo1 = new ColInfo(nom, type);
    ColInfo colInfo2 = new ColInfo(nom, type);
    ColInfo colInfo3 = new ColInfo(nom, type);
    ColInfo colInfo4 = new ColInfo(nom, type);

    List<ColInfo> listeColInfo = new ArrayList<>();
    listeColInfo.add(colInfo1);
    listeColInfo.add(colInfo2);
    listeColInfo.add(colInfo3);
    listeColInfo.add(colInfo4);

    for (ColInfo colInfo : listeColInfo) {
      assertTrue(colInfo.typeValide(type));
      if (type.toUpperCase().startsWith("STRING")) {
        assertTrue(colInfo.getType_col().equals("STRING"));
        assertTrue(colInfo.getTaille_col()>0);
      }
      else if (type.toUpperCase().startsWith("VARSTRING")) {
        assertTrue(colInfo.getType_col().equals("VARSTRING"));
        assertTrue(colInfo.getTaille_col()>0);
      }
      else if (type.toUpperCase().startsWith("INT")) {
        assertTrue(colInfo.getType_col().equals("INT"));
      }
      else{
        assertTrue(colInfo.getType_col().equals("FLOAT"));
      }
      
    }
  }
}
