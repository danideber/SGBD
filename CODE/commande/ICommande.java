package CODE.commande;

/**
 * Interface contenant les méthodes de base d'une commande
 */
public interface ICommande {

    /**
     * Méthode permettant de parser une commande
     * @throws Exception Exception
     */
    public void parsingCom() throws Exception;

    /**
     * Méthode permettant de savoir si une commande est valide
     * @return true si la commande est valide et false dans le cas contraire
     */
    public boolean comValide();

    /**
     * Méthode permettant d'exécuter une commande 
     * @throws Exception Exception
     */
    public void Execute () throws Exception;
}
