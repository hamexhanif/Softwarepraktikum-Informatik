package pizzashop.kunde;

import javax.validation.Valid;

import org.salespointframework.order.OrderManagement;
import org.salespointframework.order.OrderStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pizzashop.bestellung.PizzashopBestellung;

@Controller
@PreAuthorize("isAuthenticated()")
@SessionAttributes({"kunde"})
class KundenController {
	private final KundenManagement kundenManagement;
	private final OrderManagement<PizzashopBestellung> orderManagement;
	KundenController(KundenManagement kundenManagement, OrderManagement<PizzashopBestellung> orderManagement) {
		Assert.notNull(kundenManagement, "KundenManagement must not be null!");
		this.kundenManagement = kundenManagement;
		this.orderManagement = orderManagement;
	}

	/**
	 * Fügt {@link KundenEinträge} einen neuen {@link Kunde} zu
	 * 
	 * @param form wird geprüft ob alle Eingaben valide sind
	 * @param result falls Fehler gefunden werden, werden diese angezeigt
	 * @return redirect zu "/" 
	 */

	@PostMapping("/register")
	String registerNew(@Valid Registrierungsform form, Errors result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			return "register";
		}

		kundenManagement.createKunden(form);
		attributes.addFlashAttribute("erfolgreich", "registrierung.erfolgreich");
		return "redirect:/kundenliste";
	}

	/**
	 * Zeigt die {@link Registrierungsform} an, die benötigt wird um ein {@link Kunde} zu erstellen
	 * 
	 * @param model
	 * @param form falls ein neuer {@link Kunde} erstellt werden soll
	 * @return "/register"
	 */
	
	@PreAuthorize("hasAnyRole('BOSS', 'MITARBEITER')")
	@GetMapping("/register")
	String register(Model model, Registrierungsform form) {
		return "register";
	}

	/**
	 * Zeigt alle {@link KundenEinträge} von {@link Kunde} 
	 * 
	 * @param model
	 * @return "/kundenliste"
	 */
	
	@PreAuthorize("hasAnyRole('BOSS', 'MITARBEITER')")
	@GetMapping("/kundenliste")
	String kunden(Model model) {
		model.addAttribute("title", "Kunden");
		model.addAttribute("kundenListe", kundenManagement.findAll());
		return "kundenliste";
	}
	
	/**
	 * Löscht einen {@link Kunde} von {@link KundenEinträge} anhand seiner ID
	 * 
	 * @param userId 
	 * @return "/kundenliste"
	 */
	
	@PostMapping("/kundenliste/{userId}/loeschen")
	String loeschen( @PathVariable long userId) {
		kundenManagement.deleteKunde(userId);
		return "redirect:/kundenliste";
	}

	/**
	 * Zeigt die {@link Verifikationsform} an, die benötigt wird um ein {@link Kunde} zu verifizieren
	 * 
	 * @param model
	 * @param form wird geprüft ob alle Eingaben valide sind
	 * @return "/verifikation"
	 */
	
	@PreAuthorize("hasAnyRole('BOSS', 'MITARBEITER')")
	@GetMapping("/verifikation")
	String verify(Model model, Verifikationsform form) {
		return "verifikation";
	}

	/**
	 * Leitet einen verifizierten Kunden weiter zur Bestellung
	 * 
	 * @param form wird geprüft ob alle Eingaben valide sind
	 * @param errors falls Fehler gefunden werden, werden diese angezeigt
	 * @param model
	 * @param result damit auch globale Fehler angezeigt werden können.
	 * @return redirect zu "/order"
	 */
	
	@PostMapping("/verifikation")
	String verifiziereKunde(@Valid Verifikationsform form, Errors errors, Model model, BindingResult result) {
		Kunde kunde = form.istVerifiziert(kundenManagement);
		if (kunde == null) {
			ObjectError error = new ObjectError("globalError", "Es konnte kein Kunde verifiziert werden.");
			result.addError(error);
		} else{
			for (PizzashopBestellung bestellung : orderManagement.findBy(OrderStatus.OPEN)){
					if (kunde.generiereKundendaten().equals(bestellung.getKundenDaten())){
						ObjectError error2 = new ObjectError("globalError", "Dieser Kunde hat noch eine Bestellung offen.");
						result.addError(error2);
					}
			}
		}
		if (result.hasErrors() || errors.hasErrors()) {
			return "verifikation";
		}
		model.addAttribute("kunde", kunde);
		return "redirect:/order";
	}


	
}
