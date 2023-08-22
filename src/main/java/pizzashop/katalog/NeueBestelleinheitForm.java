package pizzashop.katalog;

import javax.validation.constraints.*;

import org.javamoney.moneta.Money;

import static org.salespointframework.core.Currencies.EURO;

public class NeueBestelleinheitForm {

	@NotEmpty(message = "{NeueBestelleiheitForm.name.NotEmpty}") //
	@Pattern(regexp = "[A-Z|Ä|Ö|Ü]([a-z|ä|ö|ü])*", message = "{NeueBestelleinheitForm.name.Pattern}") //
	private final String name;

	@Min(value = 0, message = "{NeueBestelleiheitForm.preis.Min}") //
	@Positive(message = "{NeueBestelleiheitForm.preis.Positive}") //
	@NotNull(message = "{NeueBestelleiheitForm.preis.NotEmpty}")
	private final Money preis;

	private final Bestelleinheit.BestellType bestellType;

	/**
	 * Kreirt {@link NeueBestelleinheitForm} um die eingegebenen Daten überprüfen zu können,
	 * bevor eine neue {@link Bestelleinheit} kreirt wird.
	 *
	 * @param name der {@link Bestelleinheit}.
	 * @param preis der {@link Bestelleinheit}, wird in Money umgewandelt.
	 * @param bestellType  je nachdem welchen {@link pizzashop.bestellung.PizzashopBestellung.OrderType}
	 *                    die Bestelleinheit haben soll.
	 */
	public NeueBestelleinheitForm(String name, Number preis, String bestellType) {
		if (preis == null){
			preis = 0.00;
		}
		this.name = name;
		this.preis = Money.of(preis, EURO);
		if (bestellType == null){
			this.bestellType = Bestelleinheit.BestellType.ZUTAT;
		} else if (bestellType.equals("salat")){
			this.bestellType = Bestelleinheit.BestellType.SALAT;
		} else if (bestellType.equals("zutat")){
			this.bestellType = Bestelleinheit.BestellType.ZUTAT;
		} else{this.bestellType = Bestelleinheit.BestellType.GETRAENK;}
		System.out.println(bestellType);
	}

	public String getName() {
		return name;
	}

	public Money getPreis(){
		return preis;
	}

	public Bestelleinheit.BestellType getBestellType(){
		return bestellType;
	}

	/**
	 * Prüft ob der {@link BestellKatalog} schon eine {@link Bestelleinheit} mit dem gleichen Namen enthält,
	 * um Doppelungen zu verhindern.
	 *
	 * @param katalog der alle Bestelleinheiten enthält
	 *
	 * @return einen String, der gegebenfalls als Errormessage  angezeigt werden kann
	 */
	public String nameExistsError(BestellKatalog katalog){
		for(Bestelleinheit bestelleinheit: katalog.findAll().toList()){
			if (this.name.equals(bestelleinheit.getName())){
				return "Eine Bestelleinheit mit diesem Namen existiert bereits";
			}
		}
		return "";
	}
}
