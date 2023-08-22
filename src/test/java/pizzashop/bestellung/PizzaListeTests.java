package pizzashop.bestellung;

import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import static org.salespointframework.core.Currencies.*;

import pizzashop.katalog.Bestelleinheit;
import pizzashop.katalog.Bestelleinheit.BestellType;
import pizzashop.katalog.PizzaListe;

public class PizzaListeTests {

	Bestelleinheit gouda = new Bestelleinheit("Gouda", Money.of(0.60,  EURO), BestellType.ZUTAT);
	Bestelleinheit oliven = new Bestelleinheit("Oliven", Money.of(1.10,  EURO), BestellType.ZUTAT);
	Bestelleinheit champignions = new Bestelleinheit("Champignions", Money.of(1.80,  EURO), BestellType.ZUTAT);
	
	PizzaListe leerePizza = new PizzaListe();
	
	@Test
	void pizzaTest() {
		
		leerePizza.addZutat(gouda);
		leerePizza.addZutat(oliven);
		leerePizza.addZutat(champignions);
		
		assertThat(leerePizza.getList(), containsInAnyOrder(gouda, oliven, champignions));
	}
	
	@Test
	void doppelZutatTest() {
		
		leerePizza.addZutat(gouda);
		leerePizza.addZutat(gouda);
		
		assertThat(leerePizza.getList(), containsInAnyOrder(gouda, gouda));
	}
	
	@Test
	void listeLeerenTest() {
		
		PizzaListe pizzaLeeren = new PizzaListe();
		pizzaLeeren.addZutat(gouda);
		pizzaLeeren.clear();
		
		assertTrue(pizzaLeeren.isEmpty());
	}
	
	@Test
	void summenTest() {
		
		leerePizza.addZutat(gouda);
		leerePizza.addZutat(oliven);
		assertEquals(leerePizza.getSum(), Money.of(8.20, EURO));
	}
	
	@Test
	void pizzaKopieTest() {
		
		PizzaListe pizza = new PizzaListe();
		pizza.addZutat(champignions);
		
		PizzaListe kopie = pizza.copy();
		assertEquals(kopie.getList(), pizza.getList());
	}
}
