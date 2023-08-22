package pizzashop.katalog;

import pizzashop.katalog.Bestelleinheit.BestellType;

import org.salespointframework.catalog.Catalog;
import org.springframework.data.domain.Sort;

public interface BestellKatalog extends Catalog<Bestelleinheit> {

	static final Sort DEFAULT_SORT = Sort.by("productIdentifier").descending();

	Iterable<Bestelleinheit> findByBestellType(BestellType type, Sort sort);

	default Iterable<Bestelleinheit> findByBestellType(BestellType type) {
		return findByBestellType(type, DEFAULT_SORT);
	}
 
	
}
