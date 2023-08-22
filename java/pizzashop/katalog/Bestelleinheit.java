package pizzashop.katalog;

import org.salespointframework.catalog.Product;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

//BasisKlasse für alle Produkte, die im Pizzashop verkauft werden, erbt von Product
@Entity
public class Bestelleinheit extends Product {
	
	public enum BestellType{
		ZUTAT, SALAT, GETRAENK, PIZZA, ESSGARNITUR,
	}

	private BestellType bestellType;
	
	@ManyToOne
	@JoinColumn(name="PIZZA_ID")
	private PizzaListe pizza;

	@SuppressWarnings({ "unused", "deprecation" })
	public Bestelleinheit() {}

	/**
	 * Kreirt neue {@link Bestelleinheit}, die dem {@link BestellKatalog} hinzugefügt werden kann.
	 *
	 * @param name der {@link Bestelleinheit}.
	 * @param preis der {@link Bestelleinheit}.
	 * @param bestellType  um zwischen Salat, Zutat, Getraenk, Pizza und Essgarnitur unterscheiden zu können.
	 */
	public Bestelleinheit(String name, MonetaryAmount preis, BestellType bestellType) {
		super(name, preis);
		this.bestellType = bestellType;
	}

	public String getBestellType(){
		return this.bestellType.name();
	}

}
