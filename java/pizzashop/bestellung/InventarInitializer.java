package pizzashop.bestellung;

import org.salespointframework.catalog.Product;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import pizzashop.katalog.BestellKatalog;
import pizzashop.katalog.Bestelleinheit;

@Component
@Order(5)
class InventarInitializer implements DataInitializer {

	private final UniqueInventory<UniqueInventoryItem> inventar;
	private final BestellKatalog katalog;

	InventarInitializer(UniqueInventory<UniqueInventoryItem> inventar, BestellKatalog katalog) {

		Assert.notNull(inventar, "Inventar sollte nicht null sein!");
		Assert.notNull(katalog, "Bestellkatalog sollte nicht null sein!");

		this.inventar = inventar;
		this.katalog = katalog;
	}

	@Override
	public void initialize() {

		for (Bestelleinheit bestelleinheit: katalog.findAll()){
			inventar.save(new UniqueInventoryItem(bestelleinheit, Quantity.of(10000)));
		}
	}
}
