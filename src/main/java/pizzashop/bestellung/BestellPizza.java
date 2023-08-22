package pizzashop.bestellung;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;

import pizzashop.katalog.Bestelleinheit.BestellType;

import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.ProductIdentifier;

/**
 * Pizza-Klasse, erbt von {@link Product} und kann einen {@link ProductIdentifier}
 * speichern, um eine PizzaListe zuordnen zu können.
 *
 */

@Entity
public class BestellPizza extends Product{
	
	private ProductIdentifier pizzaId;
	
	private String name;
	
	private MonetaryAmount preis;
	
	private BestellType typ;
	
	
	@SuppressWarnings("deprecation")
	public BestellPizza() {
		
	}
	
	public BestellPizza(String name, MonetaryAmount preis, BestellType typ, ProductIdentifier pizzaId) {
		super(name, preis);
		this.typ = typ;
		this.pizzaId = pizzaId;
	}
	
	public ProductIdentifier getPizzaId() {
		return pizzaId; 
	}
	
	public MonetaryAmount getPreis() {
		return preis;
	}
	
	public BestellType getTyp() {
		return typ;
	}
	
}
