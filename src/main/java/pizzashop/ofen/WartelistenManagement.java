package pizzashop.ofen;

import pizzashop.bestellung.PizzaContainer;
import pizzashop.katalog.PizzaListe;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;


import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.order.OrderIdentifier;
import org.springframework.stereotype.Service;

@Service
public class WartelistenManagement {

	private LinkedHashMap<PizzaListe, OrderIdentifier> warteliste = new LinkedHashMap<PizzaListe, OrderIdentifier>();

	public WartelistenManagement(LinkedHashMap<PizzaListe, OrderIdentifier> warteliste) {
		
		this.warteliste = warteliste;
		
	}
	
	public LinkedHashMap<PizzaListe, OrderIdentifier> getWarteliste(){
		
		return warteliste;
	}
	
	public void aufWarteliste(PizzaContainer pizzaContainer, OrderIdentifier orderId){
		
		for(PizzaListe pizza : pizzaContainer) {
			
			warteliste.put(pizza, orderId);
		}
		
	}

	
public LinkedHashMap<PizzaListe, OrderIdentifier> vonWarteliste(PizzaListe pizza){
		
		for(Iterator<PizzaListe> iterator = warteliste.keySet().iterator(); iterator.hasNext();) {
			PizzaListe eintrag = iterator.next();
			if(eintrag.equals(pizza)) {
				iterator.remove();
			}
		}
		return warteliste;
	}
	
	public Set<PizzaListe> keySet(){
		
		return warteliste.keySet();
	}
	
	public OrderIdentifier get(PizzaListe pizza) {
		
		return warteliste.get(pizza);
	}
	
	public PizzaListe findById(ProductIdentifier id) {
		PizzaListe findePizza = null;
		for(PizzaListe pizza : warteliste.keySet()) {
			if(pizza.getId().equals(id)) {
				findePizza = pizza;
			}
		}
		return findePizza;
	}
	
	public int size() {
		
		return warteliste.size();
	}
}

