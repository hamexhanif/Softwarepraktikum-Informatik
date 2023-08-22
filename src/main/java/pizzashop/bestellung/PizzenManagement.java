package pizzashop.bestellung;

import java.util.Optional;

import javax.transaction.Transactional;

import org.salespointframework.catalog.Catalog;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.OrderLine;
import org.salespointframework.quantity.Quantity;
import org.springframework.stereotype.Service;

import pizzashop.katalog.Bestelleinheit.BestellType;
import pizzashop.katalog.PizzaListe;

/**
 * Service-Klasse für das Management von {@link BestellPizza}- und
 * {@link PizzaListe}-Einheiten.
 *
 */

@Service
@Transactional
public class PizzenManagement {
	
	private final PizzaRepository pizzen;
	
	private final Catalog<BestellPizza> pizzaKatalog;
	
	public PizzenManagement(PizzaRepository pizzen, Catalog<BestellPizza> pizzaKatalog) {
		
		this.pizzen = pizzen;
		this.pizzaKatalog = pizzaKatalog;
	}
	
	public void savePizza(PizzaListe pizza) {
		pizzen.save(pizza);
	}
	
	public void deletePizza(PizzaListe pizza) {
		pizzen.delete(pizza);
	}
	
	public Optional<PizzaListe> findById(ProductIdentifier id) {
		return pizzen.findById(id);	
	}
	
	/**
	 * Fügt alle im Container enthaltenen {@link PizzaListe}n als OrderLine zur gegebenen {@link PizzashopBestellung} hinzu.
	 * @param {@link PizzaContainer} container, {@link Catalog<BestellPizza>} katalog, {@link UniqueInventory<UniqueInventoryItem>} 
	 * inventar{@link PizzashopBestellung} bestellung
	 * @return PizzashopBestellung bestellung
	 */
	public PizzashopBestellung addPizzenTo(PizzaContainer container, Catalog<BestellPizza> katalog, 
			UniqueInventory<UniqueInventoryItem> inventar, 
			PizzashopBestellung bestellung) {
	for(PizzaListe pizza : container) {
		BestellPizza kp = new BestellPizza("Pizza", pizza.getSum(), BestellType.PIZZA, pizza.getId());
		
		pizzen.save(pizza);
		
		katalog.save(kp);
		inventar.save(new UniqueInventoryItem(kp, Quantity.of(1)));

		bestellung.addOrderLine(kp, Quantity.of(1));
		
	}
	
	return bestellung;
}
	
	/**
	 * Gibt an, ob alle Pizzen der Bestellung gebacken sind (und die Bestellung damit abgeschlossen werden kann)
	 * @return boolean 
	 */
	
	public boolean allePizzenFertig(PizzashopBestellung bestellung){
		
		for(OrderLine orderLine: bestellung.getOrderLines()){
			for(PizzaListe pizza : pizzen.findAll()) {
				if(orderLine.getProductName().equals("Pizza") &&
					pizza.getId().equals(pizzaKatalog.findById(orderLine.getProductIdentifier()).get().getPizzaId())) {
					
						return false;
				}	
			}
		}
		return true;
	}
}
