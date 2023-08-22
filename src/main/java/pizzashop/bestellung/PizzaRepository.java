package pizzashop.bestellung;

import org.springframework.data.repository.CrudRepository;

import org.salespointframework.catalog.ProductIdentifier;

import pizzashop.katalog.PizzaListe;

public interface PizzaRepository extends CrudRepository<PizzaListe, ProductIdentifier>{

	@Override
	Iterable<PizzaListe> findAll();

}
