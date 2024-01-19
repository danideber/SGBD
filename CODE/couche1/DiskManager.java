package CODE.couche1;

import CODE.controle.file.Checkeur;
import CODE.page.PageId;
import CODE.parametre.DBParams;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * class DiskManager. le gestionnaire de l’espace
 * disque
 */
public class DiskManager {

  /**
   * Liste de page disponible
   */
  private ArrayList<PageId> page_dipo = null;

  /**
   * Liste des Id des fichiers
   */
  private ArrayList<Integer> listIdFile = null;

  /**
   * Nombre de pages allouer
   */
  private int nb_page_allouer;

  private static DiskManager dm = null;

  /**
   * Constructeur en private pour empÃªcher l'instanciation par lui
   */
  private DiskManager() {
    page_dipo = new ArrayList<PageId>();
    nb_page_allouer = 0;
    this.listIdFile = new ArrayList<>();
    for (int i = 1; i <= DBParams.DMFileCount; i++) {
      try {
        creerFichier(i);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }

  public void viderDiskManager() {
    nb_page_allouer = 0;
    page_dipo = new ArrayList<>();
    listIdFile=new ArrayList<>();
    dm = null;
  }

  /**
   * Méthode permettant d'instancier un DiskManager
   *
   * @return retourne un objet de type DiskManager
   *         si il n'y avait pas d'instance de DiskManager ou null dans le cas
   *         contraire
   */
  public static DiskManager getInstance() {
    if (dm == null) {
      dm = new DiskManager();

      return dm;
    }
    return dm;
  }

  /**
   * methode permettant de créer un fichier dans un répertoire bien défini
   *
   * @param fichierId contient l'id du fichier soit son numéro
   */
  public void creerFichier(int fichierId) throws Exception {
    if (fichierId>DBParams.DMFileCount) {
      throw new Exception("L 'id de fichier ne pas être supérieur à "+DBParams.DMFileCount);
    }
    if (listIdFile.size() == 0) {
      if (!(fichierId == 1)) {
        throw new Exception("L'id du premier fichier doit être par 1");
      }
    } else {
      if (fichierId <= listIdFile.get(listIdFile.size() - 1)) {
        throw new Exception("Id de fichier invalide");
      }
    }
    File file = new File(
      DBParams.DBPath + File.separator + "F" + fichierId + ".data"
    );

    try {
      file.createNewFile();
      listIdFile.add(fichierId);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Cette méthode alloue une page, c’est à dire réserve une nouvelle page à la
   * demande
   * d’une des couches au-dessus.
   * L’algorithme pour l’allocation doit être le suivant :
   * • Si une page désallouée précédemment (voir ci-dessous) est disponible,
   * l’utiliser
   * • Sinon, rajouter une page (c’est à dire, rajouter pageSize octets, avec une
   * valeur
   * quelconque, à la fin du fichier) dans le fichier de la plus petite taille
   * disponible (les
   * fichiers qui n’ont pas encore des pages rajoutées ont une taille 0, que vous
   * les ayez créés
   * ou pas)
   *
   * @return retourne/ PageId
   */
  public PageId AllocPage() {
    if (page_dipo.size() > 0) {
      PageId dispoPageId = page_dipo.get(0);
      page_dipo.remove(0);
      return dispoPageId;
    }

    String chemin = Checkeur.plus_petit_fichier();
    File fichier = new File(chemin);
    try (RandomAccessFile raf = new RandomAccessFile(fichier, "rwd");) {
      int pageIdx = (int) fichier.length() / DBParams.SGBDPageSize; // Me permet de trouver la position de la page
      // dans le fichier
      // offset
      raf.seek(fichier.length()); // Je me positionne Ã  la fin du fichier
      // allouer une page , reserver un espace memoire pour cette page
      for (int i = 0; i < DBParams.SGBDPageSize; i++) {
        raf.write((byte) 0);
      }
      this.nb_page_allouer += 1;

      int FileIx = Integer.parseInt(
        Character.toString(chemin.charAt(chemin.indexOf('.') - 1))
      );
      return new PageId(FileIx, pageIdx);
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   *
   * Cette méthode rempli l’argument buff avec le contenu disque de la page
   * identifiée par
   * l’argument pageId. Il s’agit d’une page qui « existe déjà »
   *
   * @param pageId identifiant de page
   * @param buff   un buffer
   */
  public void ReadPage(PageId pageId, ByteBuffer buff) {
    buff.position(0);
    if (pageId != null) {
      File fichier = new File(
        DBParams.DBPath + File.separator + "F" + pageId.getFileIdx() + ".data"
      );
      try (RandomAccessFile raf = new RandomAccessFile(fichier, "r");) {
        raf.seek(DBParams.SGBDPageSize * pageId.getPageIdx());

        for (int i = 0; i < DBParams.SGBDPageSize; i++) {
          buff.put(raf.readByte());
        }
      } catch (Exception e) {

      }
    }
  }

  /**
   * Cette méthode écrit le contenu de l’argument buff dans le fichier et à la
   * position indiqués par
   * l’argument pageId
   *
   * @param pageId un identifiant de page
   * @param buff   un buffer
   */
  public void WritePage(PageId pageId, ByteBuffer buff) {
    if (pageId != null) {
      File fichier = new File(
        DBParams.DBPath + File.separator + "F" + pageId.getFileIdx() + ".data"
      );
      try (RandomAccessFile raf = new RandomAccessFile(fichier, "rw")) {
        buff.position(0);
        raf.seek(DBParams.SGBDPageSize * pageId.getPageIdx());
        while (buff.hasRemaining()) {
          raf.write(buff.get());
        }
        buff.position(0);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Cette méthode désalloue une page, et la rajoute dans la liste des pages
   * disponibles
   *
   * @param pageId un PageId
   */
  public void DeallocPage(PageId pageId) {
    if (pageId != null && nb_page_allouer > 0) {
      File fichier = new File(
        DBParams.DBPath + File.separator + "F" + pageId.getFileIdx() + ".data"
      );
      try (RandomAccessFile raf = new RandomAccessFile(fichier, "rw")) {
        raf.seek(DBParams.SGBDPageSize * pageId.getPageIdx());
        byte[] donnee = new byte[DBParams.SGBDPageSize];
        raf.write(donnee);
        this.page_dipo.add(pageId);
        nb_page_allouer--;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Cette méthode retourne le nombre courant de pages allouées auprès du
   * DiskManager.
   * Par exemple, après deux appels à AllocPage et un appel à DeallocPage elle
   * doit retourner 1
   *
   * @return retourne le nombre courant de pages allouées
   */
  public int GetCurrentCountAllocPages() {
    return this.nb_page_allouer;
  }
}
