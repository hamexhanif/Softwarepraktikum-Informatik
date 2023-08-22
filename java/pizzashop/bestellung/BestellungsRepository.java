package pizzashop.bestellung;

import org.salespointframework.order.OrderIdentifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

public interface BestellungsRepository extends CrudRepository<PizzashopBestellung, OrderIdentifier> {
	static final Sort DEFAULT_SORT = Sort.by("orderIdentifier").descending();

	@Override
	Iterable<PizzashopBestellung> findAll();

	Iterable<PizzashopBestellung> findByOrderType(PizzashopBestellung.OrderType orderType, Sort sort);
	
	default Iterable<PizzashopBestellung> findByOrderType(PizzashopBestellung.OrderType orderType) {
		return findByOrderType(orderType, DEFAULT_SORT);
	}


}
