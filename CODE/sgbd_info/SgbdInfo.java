package CODE.sgbd_info;


/**
 * Cette classe stockera les différentes informations des bases de données du système
 */
public class SgbdInfo {
    private static SgbdInfo sgbdInfo=null;

    private SgbdInfo(){

    }

    public SgbdInfo getInstance(){
        if (sgbdInfo==null) {
            sgbdInfo=new SgbdInfo();
            return sgbdInfo;
        }
        else{
            return sgbdInfo;
        }
    }
    
}
