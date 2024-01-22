package TESTUNITAIRE.couche3.record;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import CODE.couche1.DiskManager;
import CODE.couche2.BufferManager;
import CODE.couche3.ColInfo;
import CODE.couche3.TableInfo;
import CODE.couche3.record.Record;
import CODE.parametre.DBParams;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class RecordTest {
  public static DiskManager dm;
  public static BufferManager bm;
public void init(){
    DBParams.DMFileCount = 1;
    DBParams.SGBDPageSize = 4096;
    DBParams.frameCount = 2;
    DBParams.DBPath = "DB" + File.separator + "DATAFILE";
    dm = dm.getInstance();
    bm = bm.getInstance();
}

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
    assertEquals(recVal, record1.getRecvalues());
  }

  @Test
  public void readFromBufferTest() throws Exception {
    init();
    TableInfo tableInfo = new TableInfo("Etudiant", 2);
    List<ColInfo> listeCol = new ArrayList<>();
    List<String> recVal = new ArrayList<>();

    listeCol.add(new ColInfo("Matricule", "Int"));
    listeCol.add(new ColInfo("Nom", "Float"));
    tableInfo.setCol_info(listeCol);

    Record record1 = new Record(tableInfo);

    
    ByteBuffer byteBuffer=ByteBuffer.allocate(DBParams.SGBDPageSize);

    byteBuffer.position(0);
    byteBuffer.putInt(34);
    byteBuffer.putFloat(4);

    record1.readFromBuffer(byteBuffer, 0);
    assertEquals(record1.getRecvalues().get(0), "34");
    assertEquals(record1.getRecvalues().get(1), "4.0");
  
  }

  @Test
  public void writeToBufferTest() throws Exception{
    init();
    TableInfo tableInfo = new TableInfo("Etudiant", 2);
    List<ColInfo> listeCol = new ArrayList<>();
    List<String> recVal = new ArrayList<>();

    listeCol.add(new ColInfo("Matricule", "Int"));
    listeCol.add(new ColInfo("Nom", "Float"));
    tableInfo.setCol_info(listeCol);

    Record record1 = new Record(tableInfo);

    
    ByteBuffer byteBuffer=ByteBuffer.allocate(DBParams.SGBDPageSize);

    recVal.add("34");
    recVal.add("4");

    record1.setRecvalues(recVal);

    record1.writeToBuffer(byteBuffer,0);
    
    byteBuffer.position(0);

    assertEquals(byteBuffer.getInt(), 34);
    assertTrue(byteBuffer.getFloat()==4);
  }
}
