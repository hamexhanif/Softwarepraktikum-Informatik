package pizzashop.ofen;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pizzashop.bestellung.PizzenManagement;

@Controller
@PreAuthorize("isAuthenticated()")
class OfenController {
	
	private final OfenManagement ofenManagement;
	
	private final WartelistenManagement warteliste;
	
	private final PizzenManagement pizzaManagement;
	
	OfenController(OfenManagement ofenManagement, WartelistenManagement warteliste, PizzenManagement pizzaManagement){
		this.ofenManagement = ofenManagement;
		this.warteliste = warteliste;
		this.pizzaManagement = pizzaManagement;
	}
	
	@GetMapping("/ofen_anzahl")
	@PreAuthorize("hasRole('BOSS')")
	String ofen(Model model) {

		model.addAttribute("ofen", ofenManagement.showOfen());

		return "ofen_anzahl";
	}
	
	@PostMapping("/ofen_anzahl")
	String adjustOfenNumber(@RequestParam long desiredNumber,  RedirectAttributes attributes) {
		if (ofenManagement.getCountUsedOfen()>desiredNumber){
			attributes.addFlashAttribute("fail", "ofen_anzahl.nicht_möglich");
			return "redirect:/ofen_anzahl";
		}
		ofenManagement.adjustOfen(desiredNumber);
		return "redirect:/ofen_anzahl";
	}
	
	@GetMapping("/warteliste")
	@PreAuthorize("hasAnyRole('BAECKER', 'BOSS')")
	String warten(Model model) {
		for (Ofen of : ofenManagement.findAll()){
			System.out.println(of.getId() );

		}
		model.addAttribute("ofenlist", ofenManagement.findAll());
		
		model.addAttribute("wartePizza", warteliste.keySet());
		
		model.addAttribute("warteliste", warteliste);
		
		model.addAttribute("backForm", new BackForm());
		
		return "/warteliste";
	}
	
	@PostMapping("/backen")
	String backen(@ModelAttribute BackForm backForm, RedirectAttributes attributes) {

		if (!ofenManagement.getOfen(backForm.getOfenId()).isFree()) {
			attributes.addFlashAttribute("fail", "warteliste.ofen_besetzt");
			return "redirect:/warteliste";
		}
		if(backForm.getPizzaId()==null){
			attributes.addFlashAttribute("fail", "warteliste.pizzanull");
			return "redirect:/warteliste";
		}

		ofenManagement.backen(backForm.getOfenId(), pizzaManagement.findById(backForm.getPizzaId()).get());

		return "redirect:/warteliste";
	}
}
