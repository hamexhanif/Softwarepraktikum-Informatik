package pizzashop.bestellung;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.payment.Cash;
import org.salespointframework.payment.CreditCard;
import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import pizzashop.katalog.BestellKatalog;
import pizzashop.katalog.PizzaListe;
import pizzashop.kunde.Kunde;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.*;
import static org.salespointframework.core.Currencies.EURO;

public class PizzashopBestellungTests {

	@BeforeEach
	public void aufbau(){

	}

	@Test
	public void PaymentTest(){
		UserAccount personal = mock(UserAccount.class);
		Kunde kunde = mock(Kunde.class);

		PizzashopBestellung bestellungCash = new PizzashopBestellung(personal, Cash.CASH, PizzashopBestellung.OrderType.PICKUP, kunde.generiereKundendaten());
		PizzashopBestellung bestellungKreditkarte = new PizzashopBestellung(personal,
				new CreditCard("a", "b", "123", "c",
						LocalDateTime.of(2021, 10, 10, 15, 40),
						LocalDateTime.of(2027, 10, 10, 15, 40), "321",
						Money.of(2000, EURO), Money.of(2000, EURO)), PizzashopBestellung.OrderType.PICKUP, kunde.generiereKundendaten());
		assertEquals(bestellungCash.getPaymentMethodString(), "Bar", "m");
		assertEquals(bestellungKreditkarte.getPaymentMethodString(), "Kreditkarte", "m");
	}

	/*@Test
	public void allePizzenFertigTest(){
		UserAccount personal = mock(UserAccount.class);
		Kunde kunde = mock(Kunde.class);
		BestellKatalog bestellKatalog = mock(BestellKatalog.class);
		PizzaListe pizza = new PizzaListe();
		when(bestellKatalog.findById(any())).thenReturn(java.util.Optional.of(pizza));

		PizzashopBestellung bestellungUngebacken = new PizzashopBestellung(personal, Cash.CASH, PizzashopBestellung.OrderType.PICKUP, kunde.generiereKundendaten());
		bestellungUngebacken.addOrderLine(new PizzaListe(), Quantity.of(1));
		assertFalse(bestellungUngebacken.allePizzenFertig(bestellKatalog), "m");

		PizzashopBestellung bestellungGebacken = new PizzashopBestellung(personal, Cash.CASH, PizzashopBestellung.OrderType.PICKUP, kunde.generiereKundendaten());
		pizza.backen();
		bestellungGebacken.addOrderLine(pizza, Quantity.of(1));
		assertTrue(bestellungGebacken.allePizzenFertig(bestellKatalog), "m");


	}*/

}
