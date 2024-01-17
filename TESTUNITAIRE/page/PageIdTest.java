package TESTUNITAIRE.page;


import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import CODE.page.PageId;

public class PageIdTest {
    
    @ParameterizedTest
    @CsvSource({"1,2","0,3","-1,9837","2,-9"})
    public void createPageIdTest(int fileId, int pageId){

        if (fileId<1) {
            assertThrows(Exception.class,()->{ new PageId(fileId, pageId);});
        }
        else if (pageId<0) {
            assertThrows(Exception.class,()->{ new PageId(fileId, pageId);});
        }
        else{
      
            assertDoesNotThrow(()->{
                new PageId(fileId, pageId);
            });
            
        }
        
    }
}
