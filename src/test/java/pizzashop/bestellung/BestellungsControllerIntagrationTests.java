package pizzashop.bestellung;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.salespointframework.order.Cart;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import pizzashop.AbstractIntegrationTests;
import pizzashop.katalog.Bestelleinheit;
import pizzashop.katalog.PizzaListe;

import java.sql.Time;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;
import static org.salespointframework.core.Currencies.EURO;

@SpringBootTest

public class BestellungsControllerIntagrationTests extends AbstractIntegrationTests {

	@Autowired
	BestellungsController controller;

	@Test
	public void accessTest(){
		PizzaListe pizza = new PizzaListe();

		assertThatExceptionOfType(AuthenticationException.class) //
				.isThrownBy(() -> controller.umsatzEinsehen(new ExtendedModelMap()));

		assertThatExceptionOfType(AuthenticationException.class) //
				.isThrownBy(() -> controller.thisPizza(new ExtendedModelMap(), pizza));
	}

	@Test
	@WithMockUser(roles = "BOSS")
	public void cartLeerenTest(){
		ExtendedModelMap model = new ExtendedModelMap();
		PizzaContainer pContainer = mock(PizzaContainer.class);
		when(pContainer.getSum()).thenReturn(Money.of(1, EURO));

		PizzaListe pizza = new PizzaListe();
		Bestelleinheit bestelleinheit = new Bestelleinheit("Römersalat", Money.of(10.77, EURO), Bestelleinheit.BestellType.SALAT);
		Cart cart = new Cart();

		cart.addOrUpdateItem(pizza, Quantity.of(1));
		cart.addOrUpdateItem(bestelleinheit, Quantity.of(2));

		controller.basket(cart, pContainer, model);
		assertFalse(cart.isEmpty());

		controller.korbLeeren(cart, pContainer);
		assertTrue(cart.isEmpty());
		System.out.println(cart);

	}

	@Test
	@WithMockUser(roles = "BOSS")
	public void umsatzTest(){
		ExtendedModelMap model = new ExtendedModelMap();

		controller.umsatzEinsehen(model);
		assertThat(model.get("totalUmsatz")).isNotNull();
		assertThat( model.get("umsatzListe")).isNotNull();

	}

	@Test
	@WithMockUser(roles = "BOSS")
	public void cart(){
		ExtendedModelMap model = new ExtendedModelMap();

		Cart cart = mock(Cart.class);
		when(cart.getPrice()).thenReturn(Money.of(1, EURO));
		PizzaContainer pizzaContainer = mock(PizzaContainer.class);
		when(pizzaContainer.getSum()).thenReturn(Money.of(2,EURO));
		when(pizzaContainer.size()).thenReturn(0);
		controller.basket(cart, pizzaContainer, model);

		//assertThat(model.get("warteZeit").toString()).isEqualTo(new Time(0,15,0).toString());
		assertThat(model.get("total")).isEqualTo(Money.of(3, EURO));
	}


}
