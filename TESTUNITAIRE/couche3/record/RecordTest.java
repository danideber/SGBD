package TESTUNITAIRE.couche3.record;

import CODE.couche3.ColInfo;
import CODE.couche3.TableInfo;
import CODE.couche3.record.Record;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class RecordTest {

  @Test
  public void constructeurTest() throws Exception {
    TableInfo tableInfo = new TableInfo("Etudiant", 3);
    List<ColInfo> listeCol = new ArrayList<>();
    List<String> recVal = new ArrayList<>();

    listeCol.add(new ColInfo("Matricule", "Int"));
    listeCol.add(new ColInfo("Nom", "string(19)"));
    listeCol.add(new ColInfo("prenom", "varstring(10)"));
    tableInfo.setCol_info(listeCol);

    Record record1 = new Record(tableInfo);
    recVal.add("1");
    recVal.add("DE-BERTHIN");
    recVal.add("Dani");

    record1.setRecvalues(recVal);

    assertEquals(record1.getTableInfo(), tableInfo);
    assertEquals(recVal,record1.getRecvalues());
    
  }
}
