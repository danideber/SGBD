package CODE.couche2;

import CODE.couche1.DiskManager;
import CODE.page.PageId;
import CODE.parametre.DBParams;
import java.nio.ByteBuffer;

public class BufferManager {

  /**
   * Tableau de zones mémoires correspondant à la case, là où on va
   * donc copier le contenu des pages disque
   */
  private static Frame[] all_frames = null;

  /**
   * Attribut static représentant une instance du BufferManager
   */
  private static BufferManager instance = null;

  /**
   * Attribut représentant une instance du DiskManager
   */
  private DiskManager dm = null;

  /**
   * Constructeur du BufferManager
   */
  private BufferManager() {
    all_frames = new Frame[DBParams.frameCount];
    for (int i = 0; i < DBParams.frameCount; i++) {
      all_frames[i] = new Frame();
    }
    dm=dm.getInstance();
  }

  /**
   * Méthode pour remettre le bufferManager à 0
   */
  public static void viderBufferManager() {
    getInstance();
  }

  public static BufferManager getInstance() {
    if (instance == null) {
      instance = new BufferManager();
    }
    return instance;
  }

  public Frame[] getFrame() {
    return all_frames;
  }

  /**
   *
   */
  public void vider_buff_pool() {
    for (int i = 0; i < DBParams.frameCount; i++) {
      all_frames[i] = new Frame();
    }
  }

  /**
   * Cette méthode répond à une demande de page venant des couches plus hautes, et
   * donc
   * retourner un des buffers associés à une frame.
   *
   * @param pageId un pageId
   * @return retourne un buffer qui sera rempli avec le contenu de la page
   *         désignée par l’argument pageId
   */
  public ByteBuffer GetPage(PageId pageId) {
    ByteBuffer buff = ByteBuffer.allocate(DBParams.SGBDPageSize);
    buff.position(0);
    if (exist_pageId(pageId) != -1) {
      all_frames[exist_pageId(pageId)].incremPinCount();
      return all_frames[exist_pageId(pageId)].getBuffer();
    }

    for (int i = 0; i < all_frames.length; i++) {
      if (all_frames[i].getPageId() == null) {
        all_frames[i].setPageId(pageId);
        dm.ReadPage(pageId, buff);
        all_frames[i].setBuffer(buff);
        all_frames[i].setPin_count(all_frames[i].getPin_count() + 1);

        return all_frames[i].getBuffer();
      }
    }

    if (all_frames.length == DBParams.frameCount) {
      int indice = lfu();

      if (all_frames[indice].isDirty()) {
        dm.WritePage(
          all_frames[indice].getPageId(),
          all_frames[indice].getBuffer()
        );
      }
      all_frames[indice].setPageId(pageId);
      dm.ReadPage(pageId, buff);
      all_frames[indice].setBuffer(buff);
      all_frames[indice].setPin_count(1);

      return all_frames[indice].getBuffer().flip();
    }

    return null;
  }

  /**
   * Cette méthode décrémente le pin_count et actualise le flag dirty de la page
   * (et aussi actualise
   * potentiellement des informations concernant la politique de remplacement).
   *
   * @param pageId
   * @param valdirty
   */
  public void FreePage(PageId pageId, boolean valdirty) {
    int indice = 0;
    for (int i = 0; i < all_frames.length; i++) {
      if (
        all_frames[i].getPageId() != null &&
        all_frames[i].getPageId().equals(pageId)
      ) {
        indice = i;
        break;
      }
    }
    if (valdirty == true && all_frames[indice].isDirty()) {
      all_frames[indice].decrePinCount();
    } else {
      all_frames[indice].decrePinCount();

      //Si le dirty au niveau de la frame n'est pas true, on le fait passer à true
      if(!all_frames[indice].isDirty() && valdirty==true){
        all_frames[indice].setDirty(valdirty);
      }

    }


  }

  /**
   * Cette méthode s’occupe de :
   * ◦ l’écriture de toutes les pages dont le flag dirty = 1 sur disque
   * ◦ la remise à 0 de tous les flags/informations et contenus des buffers
   * (buffer pool « vide »)
   */

  public void FushAll() {
    for (int i = 0; i < all_frames.length; i++) {
      if (all_frames[i].isDirty()) {
        dm.WritePage(all_frames[i].getPageId(), all_frames[i].getBuffer());
      }
    }

    vider_buff_pool();
  }

  

  /**
   * Retourne vrai si le pool des frames est vide et false dans le cas contraire
   * @return
   */
  public boolean pool_vide() {
    for (int i = 0; i < all_frames.length; i++) {
      if (all_frames[i].getPageId() != null) {
        return false;
      }
    }
    return true;
  }

  
  /**
   * Permet de voir si une page existe dans la liste des frames
   * 
   * @param pageId
   * @return
   */
  public int exist_pageId(PageId pageId) {
    for (int i = 0; i < all_frames.length; i++) {
      if (
        all_frames[i].getPageId() != null &&
        all_frames[i].getPageId().equals(pageId)
      ) {
        return i;
      }

      if (
        (
          all_frames[i].getPageId() != null &&
          (all_frames[i].getPageId().getPageIdx() == pageId.getPageIdx()) &&
          (all_frames[i].getPageId().getFileIdx() == pageId.getFileIdx())
        )
      ) {
        return i;
      }
    }
    return -1;
  }


  /**
   * Retourne l'indice du pool buffer avec la case qui sera remplac�e grace a lfu
   * @return
   */
  public int lfu() {
    int indice = 0;

    for (int i = 0; i < all_frames.length; i++) {
      if (all_frames[i].getPin_count() == 0) {
        indice = i;
      }
    }

    return indice;
  }
}
