package pizzashop.katalog;

import java.util.ArrayList;
import java.util.Iterator;

import java.util.List;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import static org.salespointframework.core.Currencies.*;

import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.ProductIdentifier;

import org.javamoney.moneta.Money;


/**
 * Eine Liste von Zutaten einer {@link Pizza}.
 *
 */

@Entity
public class PizzaListe extends Product{
	
	public enum Backgrad {
		UNGEBACKEN, GEBACKEN
	}
	
	private Backgrad backgrad;
	
	@OneToMany(mappedBy = "pizza")
	private List<Bestelleinheit> zutatenListe = new ArrayList<>();
	
	
	@SuppressWarnings({ "deprecation" }) 
	public PizzaListe(){
		super("pizza", Money.of(6.50, EURO));
		this.zutatenListe = getList();
		this.backgrad = Backgrad.UNGEBACKEN;
	}
	
	
	public Iterator<Bestelleinheit> iterator() {
		return zutatenListe.iterator();
	}
	
	/**
	 * Hilfsmethode für for-each über PizzaListe.
	 * @param Iterator<Map<String, MonetaryAmount>> iterator
	 * @return Iterable<Map<String, MonetaryAmount>>
	 */
	public Iterable<Bestelleinheit> iterableAusIterator(Iterator<Bestelleinheit> iterator){
		return new Iterable<Bestelleinheit>() {
			@Override
			public Iterator<Bestelleinheit> iterator(){
				return iterator;
			}
		};
	}
	
	public boolean isEmpty() {
		return zutatenListe.isEmpty();
	}
	
	public List<Bestelleinheit> getList() {
		return zutatenListe;
	}
	
	public void addZutat(Bestelleinheit zutat){ 
		zutatenListe.add(zutat);
	}

	public void deleteZutat(ProductIdentifier id){
		zutatenListe.removeIf(zutat -> id != null && zutat.getId() != null && zutat.getId().equals(id));
	}

	public boolean containsZutat(Bestelleinheit zutat){
		for (Bestelleinheit pizzaZutat : zutatenListe){
			if (pizzaZutat.equals(zutat)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Berechnet den Preis einer Pizza mit einem Grundpreis von 6,50€.
	 * 
	 * @return MonetaryAmount sum
	 */
	public MonetaryAmount getSum() {
		MonetaryAmount sum = Money.of(6.50, EURO);
		Iterator<Bestelleinheit> i = zutatenListe.iterator();
		while (i.hasNext()) {
			Bestelleinheit einheit = i.next();
			
			sum = sum.add(einheit.getPrice());
		}
		return sum;
	}	
	
	public void clear() {
		zutatenListe.clear();
		
	}
	
	/**
	 * kopiert die Liste in eine neue leere Liste
	 * @return PizzaListe copied
	 */
	public PizzaListe copy() {
		PizzaListe copied = new PizzaListe();
		Iterator<Bestelleinheit> i = zutatenListe.iterator();
		while(i.hasNext()) {
			Bestelleinheit einheit = i.next();
			
			copied.addZutat(einheit);
			
		}
		return copied;
	}
	
	
	public void backen(){
		this.backgrad = Backgrad.GEBACKEN;
	}

	public String getBackgrad(){return backgrad.name();}

}