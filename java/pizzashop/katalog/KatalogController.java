package pizzashop.katalog;

import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.*;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import pizzashop.katalog.Bestelleinheit.BestellType;

import javax.validation.Valid;

@Controller
@PreAuthorize("isAuthenticated()")
@SessionAttributes({"pizzaListe"})
class KatalogController {

	private final BestellKatalog katalog;

	private final UniqueInventory<UniqueInventoryItem> inventar;


	KatalogController(BestellKatalog bestellKatalog, UniqueInventory<UniqueInventoryItem> inventar) {
		this.katalog = bestellKatalog;
		this.inventar = inventar;

	}

	/**
	 * Zeigt alle {@link Bestelleinheit}en aus dem {@link BestellKatalog},
	 * die den {@link pizzashop.bestellung.PizzashopBestellung.OrderType} GETRAENK haben
	 *
	 * @param model .
	 * @return den Namen der Ansicht.
	 */
	@PreAuthorize("hasAnyRole('BOSS', 'MITARBEITER')")
	@GetMapping("/getraenke")
	String getraenkeKatalog(Model model) {

		model.addAttribute("katalog", katalog.findByBestellType(BestellType.GETRAENK));
		model.addAttribute("title", "katalog.getraenk.title");

		return "katalog";
	}

	@PreAuthorize("hasAnyRole('BOSS', 'MITARBEITER')")
	@GetMapping("/essgarnituren")
	String essgarnitur(Model model) {

		model.addAttribute("katalog", katalog.findByBestellType(BestellType.ESSGARNITUR));
		model.addAttribute("title", "katalog.essgarnitur.title");

		return "katalog";
	}

	/**
	 * Zeigt alle {@link Bestelleinheit}en aus dem {@link BestellKatalog},
	 * die den {@link pizzashop.bestellung.PizzashopBestellung.OrderType} SALAT haben
	 *
	 * @param model .
	 * @return den Namen der Ansicht.
	 */
	@PreAuthorize("hasAnyRole('BOSS', 'MITARBEITER')")
	@GetMapping("/salat")
	String salatKatalog(Model model) {

		model.addAttribute("katalog", katalog.findByBestellType(BestellType.SALAT));
		model.addAttribute("title", "katalog.salat.title");

		return "katalog";
	}

	/**
	 * Zeigt alle {@link Bestelleinheit}en aus dem {@link BestellKatalog},
	 * die den {@link pizzashop.bestellung.PizzashopBestellung.OrderType} ZUTAT haben
	 *
	 * @param model .
	 * @return den Namen der Ansicht.
	 */
	@PreAuthorize("hasAnyRole('BOSS', 'MITARBEITER')")
	@GetMapping("/ingredients")
	String ingredientList(Model model, @SessionAttribute PizzaListe pizzaListe) {


		model.addAttribute("ingredients", katalog.findByBestellType(BestellType.ZUTAT));
		model.addAttribute("title", "katalog.ingredient.title");
		
		return "ingredients";
	}

	/**
	 * Fügt dem {@link BestellKatalog} eine neue {@link Bestelleinheit} hinzu, falls das {@link NeueBestelleinheitForm}
	 * valide ist, sonst werden die Fehler angezeigt.
	 *
	 * @param form wird auf Validität geprüft.
	 * @param errors falls die Eingaben nicht valide sind.
	 * @param result damit auch globale Fehler angezeigt werden können.
	 *
	 * @return die view /angebot, wenn es Fehler gibt und sonst redirect zum /angebot.
	 */
	@PreAuthorize("hasRole('BOSS')")
	@PostMapping("/angebot")
	String neueBestelleinheit(@Valid NeueBestelleinheitForm form, Errors errors, BindingResult result) {
		String nameError = form.nameExistsError(katalog);
		if (!nameError.isEmpty()){
			ObjectError error = new ObjectError("globalError", "Eine Bestelleinheit mit diesem Namen existiert bereits.");
			result.addError(error);
		}
		if (errors.hasErrors() || result.hasErrors()) {
			return "angebot";
		} else {
			Bestelleinheit neueBestelleinheit = new Bestelleinheit(form.getName(), form.getPreis(), form.getBestellType());
			katalog.save(neueBestelleinheit);
			inventar.save(new UniqueInventoryItem(neueBestelleinheit, Quantity.of(10)));
			System.out.println(form.getBestellType()+ neueBestelleinheit.getBestellType());

		}

		return "redirect:/angebot/";
	}


	/**
	 * Zeigt alle {@link Bestelleinheit}en aus dem {@link BestellKatalog},
	 *
	 * @param model .
	 * @param form falls eine neue {@link Bestelleinheit} hinzugefügt werden soll.
	 * @return den Namen der Ansicht.
	 */
	@PreAuthorize("hasRole('BOSS')")
	@GetMapping("/angebot")
	String angebot(Model model, NeueBestelleinheitForm form) {

		model.addAttribute("angebot", katalog.findByAllCategories());
		return "angebot";
	}

	/**
	 * Zeigt eine {@link Bestelleinheit}, die angepasst werden kann.
	 *
	 * @param bestelleinheit wird als Pathvariable übergeben.
	 * @param model .
	 * @param preisform um den Preis anpassen zu können.
	 *
	 * @return den Namen der Ansicht.
	 */
	@PreAuthorize("hasRole('BOSS')")
	@GetMapping("/bestelleinheit/{bestelleinheit}")
	String bestelleinheit(@PathVariable Bestelleinheit bestelleinheit, Model model, PreisForm preisform) {

		model.addAttribute("bestelleinheit", bestelleinheit);

		return "bestelleinheit";
	}

	/**
	 * Ändert den Preis einer {@link Bestelleinheit}en aus dem {@link BestellKatalog},
	 *
	 * @param bestelleinheit deren Preis verändert werden soll, als Pathvariable.
	 * @param preisForm die Eingabe validiert und als Money übergibt.
	 *
	 * @return redirect zu /angebot.
	 */
	@PreAuthorize("hasRole('BOSS')")
	@PostMapping("/bestelleinheit/{bestelleinheit}/preis")
	String preis( @PathVariable Bestelleinheit bestelleinheit, PreisForm preisForm) {
		bestelleinheit.setPrice(preisForm.getPreis());
		katalog.save(bestelleinheit);
		return "redirect:/angebot";
	}


	/**
	 * Löscht {@link Bestelleinheit} aus dem {@link BestellKatalog}.
	 *
	 * @param bestelleinheit, die gelöscht werden soll.
	 *
	 * @return redirect zu /angebot.
	 */
	@PreAuthorize("hasRole('BOSS')")
	@PostMapping("/bestelleinheit/{bestelleinheit}/loeschen")
	String loeschen( @PathVariable Bestelleinheit bestelleinheit) {
		inventar.delete(inventar.findByProduct(bestelleinheit).get());
		katalog.delete(bestelleinheit);


		return "redirect:/angebot";
	}


}
