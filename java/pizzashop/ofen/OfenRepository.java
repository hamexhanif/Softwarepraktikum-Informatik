package pizzashop.ofen;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

/**
 * Eine Repository-Schnittstelle zur Verwaltung von {@link Ofen}-Instanzen.
 *
 * @author Muhammad Fakhruddin
 */
interface OfenRepository extends CrudRepository<Ofen, Long> {

	/**
	 * Neu deklariert {@link CrudRepository#findAll()}, um ein {@link Streamable} anstelle von {@link Iterable} zurückzugeben.
	 */
	@Override
	Streamable<Ofen> findAll();
	/**
	 * Gibt unbenutzte bzw. freie Öfen zurück.
	 * 
	 * @param free ist boolean
	 * @return {@link Streamable}<{@link Ofen}> 
	 */
	Streamable<Ofen> findByFree(boolean free);	
}
