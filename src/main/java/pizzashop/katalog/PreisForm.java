package pizzashop.katalog;

import org.javamoney.moneta.Money;
import static org.salespointframework.core.Currencies.EURO;

public class PreisForm{

	private final Money preis;

	/**
	 * Kreirt {@link PreisForm} um die eingegebenen Daten überprüfen zu können,
	 * bevor der Preis einer {@link Bestelleinheit} geändert wird.
	 *
	 * @param preis der {@link Bestelleinheit}, wird in Money umgewandelt.
	 */
	public PreisForm(Number preis){
		if (preis == null){
			preis = 0.00;
		}
		this.preis = Money.of(preis, EURO);
	}

	public Money getPreis() {
		return preis;
	}
}
