package pizzashop.bestellung;

import java.util.ArrayList;
import java.util.Iterator;

import javax.money.MonetaryAmount;

import static org.salespointframework.core.Currencies.*;

import org.javamoney.moneta.Money;
import org.springframework.data.util.Streamable;

import pizzashop.katalog.PizzaListe;

/**
 * ArrayList, die {@link PizzaListe} Objekte für eine Bestellung auflistet.
 *
 */
public class PizzaContainer implements Streamable<PizzaListe>{

	private ArrayList<PizzaListe> container = new ArrayList<PizzaListe>();
	
	@Override
	public Iterator<PizzaListe> iterator() {
		return container.iterator();
	}
	
	public void addToContainer(PizzaListe pizzaList) {
	
		container.add(pizzaList);
	}

	public void clear() {
		container.clear();
	}

	/**
	 * Berechnet die Summe der Preise aller Pizzen im Container.
	 * @return MonetaryAmount sum
	 */
	public MonetaryAmount getSum() {
		MonetaryAmount sum = Money.of(0.00, EURO);
		Iterator<PizzaListe> i = container.iterator();
		while(i.hasNext()) {
			PizzaListe pizza = i.next();
			sum = sum.add(pizza.getSum());
		}
		return sum;
	}

	
	public int size() {
		return container.size();
	}
}
