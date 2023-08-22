package pizzashop.personal;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

/**
 * Eine Repository-Schnittstelle zur Verwaltung von {@link Personal}-Instanzen.
 *
 * @author Muhammad Fakhruddin
 */
interface PersonalRepository extends CrudRepository<Personal, Long> {

	/**
	 * Neu deklariert {@link CrudRepository#findAll()}, um ein {@link Streamable} anstelle von {@link Iterable} zurückzugeben.
	 */
	@Override
	Streamable<Personal> findAll();
}
