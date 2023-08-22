package pizzashop.kunde;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

interface KundenEinträge extends CrudRepository<Kunde, Long> {

	@Override
	Streamable<Kunde> findAll();
}
