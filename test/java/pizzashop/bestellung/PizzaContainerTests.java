package pizzashop.bestellung;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableWithSize.iterableWithSize;

import org.javamoney.moneta.Money;

import static org.salespointframework.core.Currencies.*;

import org.junit.jupiter.api.Test;

import pizzashop.katalog.Bestelleinheit;
import pizzashop.katalog.Bestelleinheit.BestellType;
import pizzashop.katalog.PizzaListe;

public class PizzaContainerTests {
	
	private PizzaListe pizza1() {
		PizzaListe pizza = new PizzaListe();
		pizza.addZutat(new Bestelleinheit("Tomaten", Money.of(1.20, EURO), BestellType.ZUTAT));
		pizza.addZutat(new Bestelleinheit("Salami", Money.of(2.10, EURO), BestellType.ZUTAT));
		pizza.addZutat(new Bestelleinheit("Mozarella", Money.of(0.90, EURO), BestellType.ZUTAT));
		
		return pizza;
	}
	
	private PizzaListe pizza2() {
		PizzaListe pizza = new PizzaListe();
		pizza.addZutat(new Bestelleinheit("Mais", Money.of(1.40, EURO), BestellType.ZUTAT));
		pizza.addZutat(new Bestelleinheit("Ananas", Money.of(2.00, EURO), BestellType.ZUTAT));
		
		return pizza;
	}
	
	private PizzaContainer container() {
		PizzaContainer container = new PizzaContainer();
		container.addToContainer(pizza1());
		container.addToContainer(pizza2());
		
		return container;
	}
	
	private PizzaContainer leererContainer = new PizzaContainer();
	
	@Test
	void containerTest() {
		
		PizzaListe pizzaNeu = new PizzaListe();
		pizzaNeu.addZutat(new Bestelleinheit("Paprika", Money.of(1.80, EURO), BestellType.ZUTAT));
		
		PizzaContainer pizzaPlus = container();
		pizzaPlus.addToContainer(pizzaNeu);
		
		assertThat(pizzaPlus, iterableWithSize(3)); 
	}
	
	@Test
	void leererContainerTest() {
		
		PizzaListe pizzaNeu = new PizzaListe();
		pizzaNeu.addZutat(new Bestelleinheit("Paprika", Money.of(1.80, EURO), BestellType.ZUTAT));
		
		leererContainer.addToContainer(pizzaNeu);
		
		assertThat(leererContainer, iterableWithSize(1));
	}
	
	@Test
	void containerLeerenTest() {
		
		PizzaContainer leeren = container();;
		leeren.clear();
		
		assertTrue(leeren.isEmpty());
	}
	
	@Test
	void gesamtsummenTest() {
		
		assertEquals(container().getSum(), Money.of(20.60, EURO));
	}
}
