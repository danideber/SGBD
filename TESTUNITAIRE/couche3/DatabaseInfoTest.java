package TESTUNITAIRE.couche3;



import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import CODE.couche3.DatabaseInfo;
import CODE.couche3.TableInfo;

public class DatabaseInfoTest {
    @Test
    public void constructeurTest() throws Exception{
        DatabaseInfo dbaInfo=DatabaseInfo.getInstance();
        List<TableInfo> listeTable=new ArrayList<>();
        listeTable.add(new TableInfo("Etud", 2));
        DatabaseInfo databaseInfo=DatabaseInfo.getInstance(listeTable);
    }

    @Test
    public void initTest() throws Exception{
        DatabaseInfo dbaInfo=DatabaseInfo.getInstance();

        assertDoesNotThrow(()->dbaInfo.Init());

        List<TableInfo> listeTable=new ArrayList<>();
        listeTable.add(new TableInfo("Etud", 2));
        DatabaseInfo dbaInfo2=DatabaseInfo.getInstance(listeTable);

        assertDoesNotThrow(()->dbaInfo2.Init());  
    }

    @Test
    public void finishTest() throws Exception{
        DatabaseInfo dbaInfo=DatabaseInfo.getInstance();

        assertDoesNotThrow(()->dbaInfo.Finish());

        List<TableInfo> listeTable=new ArrayList<>();
        listeTable.add(new TableInfo("Etud", 2));
        DatabaseInfo dbaInfo2=DatabaseInfo.getInstance(listeTable);

        assertDoesNotThrow(()->dbaInfo2.Finish());  
    }
}
