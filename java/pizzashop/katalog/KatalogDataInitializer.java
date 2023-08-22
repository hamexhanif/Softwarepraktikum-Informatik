package pizzashop.katalog;

import static org.salespointframework.core.Currencies.*;

import org.salespointframework.useraccount.UserAccountManagement;
import pizzashop.katalog.Bestelleinheit.BestellType;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


/**
 * Eine {@link DataInitializer} Implementation, die einige Standard {@link Bestelleinheit} erstellt.
 *
 * @see DataInitializer
 */

@Component
@Order(3)
public class KatalogDataInitializer implements DataInitializer{

	private static final Logger LOG = LoggerFactory.getLogger(KatalogDataInitializer.class);

	private final BestellKatalog bestellKatalog;


	/**
	 * Kreirt neuen {@link KatalogDataInitializer} mit einem gegebenen {@link BestellKatalog}.
	 *
	 * @param bestellKatalog sollte nicht {@literal null} sein.
	 */
	KatalogDataInitializer(BestellKatalog bestellKatalog) {

		Assert.notNull(bestellKatalog, "bestellKatalog sollte nicht null sein!");

		this.bestellKatalog = bestellKatalog;
	}


	@Override
	public void initialize() {

		//Überspringe Initialisierung, wenn schon Bestelleinheiten vorhanden sind
		if (bestellKatalog.findAll().iterator().hasNext()){
			return;
		}
		LOG.info("Erstellen der Standard-Bestelleinheiten.");

		//Standard-Zutaten
		bestellKatalog.save(new Bestelleinheit("Tomate", Money.of(1.50, EURO), BestellType.ZUTAT));
		bestellKatalog.save(new Bestelleinheit("Mozarella", Money.of(0.75, EURO), BestellType.ZUTAT));
		bestellKatalog.save(new Bestelleinheit("Mais", Money.of(3.25, EURO), BestellType.ZUTAT));
		bestellKatalog.save(new Bestelleinheit("Cheddar", Money.of(1.25, EURO), BestellType.ZUTAT));
		bestellKatalog.save(new Bestelleinheit("Hünchen", Money.of(1.25, EURO), BestellType.ZUTAT));
		bestellKatalog.save(new Bestelleinheit("Parmesan", Money.of(1.25, EURO), BestellType.ZUTAT));
		bestellKatalog.save(new Bestelleinheit("Rucola", Money.of(0.25, EURO), BestellType.ZUTAT));
		bestellKatalog.save(new Bestelleinheit("Schinken", Money.of(0.50, EURO), BestellType.ZUTAT));
		bestellKatalog.save(new Bestelleinheit("Ananas", Money.of(2.25, EURO), BestellType.ZUTAT));
		bestellKatalog.save(new Bestelleinheit("Paprika", Money.of(0.75, EURO), BestellType.ZUTAT));
		bestellKatalog.save(new Bestelleinheit("Pilze", Money.of(0.45, EURO), BestellType.ZUTAT));


		//Standard-Salate
		bestellKatalog.save(new Bestelleinheit("Römersalat", Money.of(10.77, EURO), BestellType.SALAT));
		bestellKatalog.save(new Bestelleinheit("Couscoussalat", Money.of(9.39, EURO), BestellType.SALAT));
		bestellKatalog.save(new Bestelleinheit("Kartoffelsalat", Money.of(8.99, EURO), BestellType.SALAT));

		//Standard-Getränke
		bestellKatalog.save(new Bestelleinheit("Cola", Money.of(2.50, EURO), BestellType.GETRAENK));
		bestellKatalog.save(new Bestelleinheit("Fanta", Money.of(1.50, EURO), BestellType.GETRAENK));
		bestellKatalog.save(new Bestelleinheit("Sprite", Money.of(1.99, EURO), BestellType.GETRAENK));
		bestellKatalog.save(new Bestelleinheit("Orangensaft", Money.of(3.99, EURO), BestellType.GETRAENK));
		bestellKatalog.save(new Bestelleinheit("Tomatensaft", Money.of(2.75, EURO), BestellType.GETRAENK));

		//Standard-Essgarnitur
		bestellKatalog.save(new Bestelleinheit("Standard-Essgarnitur", Money.of(15.00, EURO), BestellType.ESSGARNITUR));
		bestellKatalog.save(new Bestelleinheit("Familien-Essgarnitur", Money.of(20.00, EURO), BestellType.ESSGARNITUR));


		
	}

}

