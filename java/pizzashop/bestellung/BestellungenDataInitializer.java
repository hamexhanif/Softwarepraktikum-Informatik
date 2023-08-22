package pizzashop.bestellung;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.salespointframework.order.Cart;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.payment.Cash;
import org.salespointframework.payment.CreditCard;
import org.salespointframework.useraccount.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pizzashop.katalog.BestellKatalog;
import pizzashop.katalog.Bestelleinheit;
import pizzashop.katalog.PizzaListe;
import pizzashop.kunde.KundenManagement;
import pizzashop.personal.PersonalManagement;
import pizzashop.bestellung.PizzashopBestellung.OrderType;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.salespointframework.core.Currencies.EURO;


@Component
@Order(6)
public class BestellungenDataInitializer implements DataInitializer{

	private static final Logger LOG = LoggerFactory.getLogger(BestellungenDataInitializer.class);

	private final BestellungsRepository bestellungsmanagement;
	private final PersonalManagement personalManagement;
	private final KundenManagement kundenManagement;
	private final BestellKatalog katalog;
	private final OrderManagement<PizzashopBestellung> orderManagement;

	BestellungenDataInitializer(BestellungsRepository bestellungsmanagement, PersonalManagement personalManagement,
								KundenManagement kundenManagement, BestellKatalog katalog,
								OrderManagement<PizzashopBestellung> orderManagement){

		this.bestellungsmanagement = bestellungsmanagement;
		this.personalManagement = personalManagement;
		this.kundenManagement = kundenManagement;
		this.katalog = katalog;
		this.orderManagement = orderManagement;

	}

	public void initialize() {
		LOG.info("Kreire default Bestellungen.");

		UserAccount boss = personalManagement.findAll().stream().findFirst().get().getUserAccount();
		Cart cart = new Cart();
		Iterable<Bestelleinheit> salate = katalog.findByBestellType(Bestelleinheit.BestellType.SALAT);
		Iterable<Bestelleinheit> getraenke = katalog.findByBestellType(Bestelleinheit.BestellType.GETRAENK);
		Iterable<Bestelleinheit> zutaten = katalog.findByBestellType(Bestelleinheit.BestellType.ZUTAT);

		//#1 Bestelung zum Abholen
		ArrayList<String> kunde = kundenManagement.findAll().toList().get(0).generiereKundendaten();
		PizzashopBestellung bestellung1 = new PizzashopBestellung(boss,Cash.CASH, OrderType.PICKUP, kunde);
		cart.addOrUpdateItem(salate.iterator().next(), 2);
		cart.addOrUpdateItem(getraenke.iterator().next(), 1);
		cart.addItemsTo(bestellung1);
		cart.clear();
		bestellungsmanagement.save(bestellung1);
		orderManagement.save(bestellung1);

		//#2 Bestellung zum Liefern
		kunde = kundenManagement.findAll().toList().get(1).generiereKundendaten();
		PizzashopBestellung bestellung2 = new PizzashopBestellung(boss, Cash.CASH, OrderType.PICKUP, kunde);
		cart.addOrUpdateItem(salate.iterator().next(), 1);
		cart.addOrUpdateItem(getraenke.iterator().next(), 1);
		cart.addItemsTo(bestellung2);
		cart.clear();
		bestellungsmanagement.save(bestellung2);
		orderManagement.save(bestellung2);

		//#3 Bestellung zur Liefern
		kunde = kundenManagement.findAll().toList().get(2).generiereKundendaten();
		PizzashopBestellung bestellung3 = new PizzashopBestellung(boss, Cash.CASH, OrderType.DELIVERY, kunde);
		cart.addOrUpdateItem(salate.iterator().next(), 2);
		cart.addOrUpdateItem(getraenke.iterator().next(), 1);
		cart.addItemsTo(bestellung3);
		cart.clear();
		bestellungsmanagement.save(bestellung3);
		orderManagement.save(bestellung3);


		 //#4 Fertige Bestellung
		 PizzashopBestellung bestellung5 = new PizzashopBestellung(boss, Cash.CASH, OrderType.PICKUP, kunde);
		 cart.addOrUpdateItem(salate.iterator().next(), 1);
		 cart.addItemsTo(bestellung5);
		 cart.clear();
		 bestellungsmanagement.save(bestellung5);
		 orderManagement.save(bestellung5);
		 orderManagement.payOrder(bestellung5);
		 orderManagement.completeOrder(bestellung5);

		 //#5 Bestellung mit Karte
		PizzashopBestellung bestellung6 = new PizzashopBestellung(boss,
				new CreditCard("a", "b", "123", "c",
						LocalDateTime.of(2021, 10, 10, 15, 40),
				LocalDateTime.of(2027, 10, 10, 15, 40), "321",
				Money.of(2000, EURO), Money.of(2000, EURO)), OrderType.PICKUP, kunde);
		cart.addOrUpdateItem(salate.iterator().next(), 1);
		cart.addItemsTo(bestellung6);
		cart.clear();
		orderManagement.payOrder(bestellung6);
		orderManagement.save(bestellung6);

	}
}
