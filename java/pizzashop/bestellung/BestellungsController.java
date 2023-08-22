package pizzashop.bestellung;


import java.time.LocalDateTime;
import java.util.*;

import static org.salespointframework.core.Currencies.*;

import org.javamoney.moneta.Money;

import org.salespointframework.catalog.Catalog;

import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.order.*;
import org.salespointframework.payment.Cash;
import org.salespointframework.payment.CreditCard;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.Role;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.salespointframework.quantity.Quantity;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pizzashop.bestellung.PizzashopBestellung.OrderType;
import pizzashop.katalog.Bestelleinheit;
import pizzashop.katalog.PizzaListe;
import pizzashop.kunde.Kunde;

import pizzashop.kunde.KundenManagement;

import pizzashop.ofen.WartelistenManagement;

import javax.money.MonetaryAmount;


@Controller
@PreAuthorize("isAuthenticated()")
@SessionAttributes({"cart", "pizzaListe", "pizzaContainer", "kunde"})
class BestellungsController {

	private final BestellungsRepository bestellungsmanagement;
	
	private final Catalog<BestellPizza> pizzaKatalog;
	
	private final OrderManagement<PizzashopBestellung> orderManagement;

	private final UniqueInventory<UniqueInventoryItem> inventar;
	
	private final WartelistenManagement warteliste;

	private final Zeitberechnung zeit; 

	private final KundenManagement kundenManagement;
	
	private final PizzenManagement pizzaManagement;

	BestellungsController(BestellungsRepository bestellungsmanagement, 
						  Catalog<BestellPizza> pizzaKatalog,
						  OrderManagement<PizzashopBestellung> orderManagement,
						  UniqueInventory<UniqueInventoryItem> inventar, 
						  WartelistenManagement warteliste,
						  Zeitberechnung zeit, KundenManagement kundenManagement,
						  PizzenManagement pizzaManagement) {

		this.bestellungsmanagement = bestellungsmanagement;
		this.pizzaKatalog = pizzaKatalog;
		this.orderManagement = orderManagement;
		this.inventar = inventar;
		this.warteliste = warteliste;
		this.zeit = zeit;
		this.kundenManagement = kundenManagement;
		this.pizzaManagement = pizzaManagement;
	}

	@ModelAttribute("cart")
	Cart initializeCart() {
		return new Cart();
	}

	@PostMapping("/cart")
	String addProduct(@RequestParam(required = false) Bestelleinheit product,
						@RequestParam(required = false) Integer number,
						@ModelAttribute PizzaContainer pizzaContainer,
						@ModelAttribute PizzaListe pizzaListe,
						@ModelAttribute Cart cart, 
						Model model) {

		if(pizzaListe != null && !pizzaListe.isEmpty()) {
			
			PizzaListe p = pizzaListe.copy();
			
			pizzaContainer.addToContainer(p);
			
			pizzaListe.clear();

			}

		if(product != null) {
			int count = number <= 0 || number > 10 ? 1 : number;

			cart.addOrUpdateItem(product, Quantity.of(count));
		}

		model.addAttribute("warteZeit", zeit.berechne(pizzaContainer.size()));
		model.addAttribute("total", cart.getPrice().add(pizzaContainer.getSum()));
		
		return "cart";
	}

	@PreAuthorize("hasAnyRole('BOSS', 'MITARBEITER')")
	@GetMapping("/cart")
	String basket(@ModelAttribute Cart cart, @ModelAttribute PizzaContainer pizzaContainer, Model model) {
		model.addAttribute("warteZeit", zeit.berechne(pizzaContainer.size()));
		model.addAttribute("total", cart.getPrice().add(pizzaContainer.getSum()));
		return "cart";
	}
	
	@PostMapping("/save")
	String finish(@ModelAttribute Cart cart, @ModelAttribute PizzaContainer pizzaContainer,
				  @LoggedIn Optional<UserAccount> userAccount, @ModelAttribute Kunde kunde,
				  @RequestParam("zahlTyp") String zahlTyp, @RequestParam("zustellTyp") OrderType zustellTyp, 
				  Model model, RedirectAttributes attributes) {
		
		model.addAttribute("zahlTyp", zahlTyp);
		model.addAttribute("zustellTyp", zustellTyp);

		kundenManagement.updateTan(kunde);

		PaymentMethod zahlungsTyp;
		if(zahlTyp.equals("CreditCard")) {
			zahlungsTyp = new CreditCard("a", "b", "123", "c", LocalDateTime.of(2021, 10, 10, 15, 40), 
								LocalDateTime.of(2027, 10, 10, 15, 40), "321", 
								Money.of(2000, EURO), Money.of(2000, EURO));
		} else {
			zahlungsTyp = Cash.CASH;
		}
		
		return userAccount.map(account -> {

			var order = new pizzashop.bestellung.PizzashopBestellung(account, zahlungsTyp,
							zustellTyp, kunde.generiereKundendaten());

			warteliste.aufWarteliste(pizzaContainer, order.getId());
			
			cart.addItemsTo(order);
			
			pizzaManagement.addPizzenTo(pizzaContainer, pizzaKatalog, inventar, order);
			
			bestellungsmanagement.save(order);
			orderManagement.save(order);

			if(zahlTyp.equals("CreditCard")){
				orderManagement.payOrder(order);
			}

			cart.clear();
			pizzaContainer.clear();
			attributes.addFlashAttribute("erfolgreich", "order.erfolgreich");
			return "redirect:/orderView";
		}).orElse("redirect:/cart");
	}
	
	@PostMapping("/leeren")
	String korbLeeren(@ModelAttribute Cart cart, @ModelAttribute PizzaContainer pizzaContainer) {
		pizzaContainer.clear();
		cart.clear();
		
		return "redirect:/cart";
		
	}
	
	@ModelAttribute("pizzaContainer")
	PizzaContainer initializePizzaContainer() {
		return new PizzaContainer();
	}

	@ModelAttribute("pizzaListe")
	PizzaListe initializePizza() {
		return new PizzaListe();
	}

	@PostMapping("/pizza") 
	String PizzaIngredients(@RequestParam Bestelleinheit product,
							@ModelAttribute PizzaListe pizzaListe, RedirectAttributes attributes) {

		if(pizzaListe.containsZutat(product)){
			attributes.addFlashAttribute("fail", "zutat.fail");
			return "redirect:/ingredients";
		}

		pizzaListe.addZutat(product);
    
		return "pizza";
	}

	@PreAuthorize("hasAnyRole('BOSS', 'MITARBEITER')")
	@GetMapping("/pizza")
	String thisPizza(Model model, @ModelAttribute PizzaListe pizzaListe) {


		model.addAttribute("pizzaListe", pizzaListe);


		return "pizza";
	}

	@GetMapping("/bill/{orderId}")
	String getBill(@PathVariable OrderIdentifier orderId, Model model) {
		if (orderManagement.contains(orderId)){
			PizzashopBestellung order = orderManagement.get(orderId).get();
			model.addAttribute("order", order);
			model.addAttribute("orderContent", order.getOrderLines());
			model.addAttribute("orderKunde", order.getKundenDaten());
		}
		return "bill";
	}

	@PreAuthorize("hasAnyRole('BOSS', 'MITARBEITER')")
	@PostMapping("/order/abbruch")
	String abbruch(@ModelAttribute Cart cart, @ModelAttribute PizzaContainer pizzaContainer){
	
		cart.clear();
		pizzaContainer.clear();
		return "redirect:/";
	}
	
	
	@PreAuthorize("hasAnyRole('BOSS', 'MITARBEITER')")
	@GetMapping("/order")
	String newOrder(@ModelAttribute("kunde") Kunde kunde){
		System.out.println(kunde.getId());
		return "order";
	}

	@PreAuthorize("hasAnyRole('BOSS', 'MITARBEITER', 'LIEFERBOTE')")
	@GetMapping("/orderView")
	String orderOverview(Model model, @LoggedIn Optional<UserAccount> userAccount) {

		List<PizzashopBestellung> lieferBestellungen = new ArrayList<PizzashopBestellung>();
		List<PizzashopBestellung> bezahlteBestellungen = new ArrayList<PizzashopBestellung>();
		List<PizzashopBestellung> abholBestellungen = new ArrayList<PizzashopBestellung>();

		// Bestellungen, die geliefert werden müssen
		for (PizzashopBestellung bestellung: bestellungsmanagement.findByOrderType(PizzashopBestellung.OrderType.DELIVERY)){
			if(!bestellung.isCompleted() && pizzaManagement.allePizzenFertig(bestellung)
					&& bestellung.getLieferbotenId().equals("")) {
				lieferBestellungen.add(bestellung);
			}

		}

		// Bestellungen, die abgeholt werden müssen
		for (PizzashopBestellung bestellung: bestellungsmanagement.findByOrderType(PizzashopBestellung.OrderType.PICKUP)){
			if(!bestellung.isCompleted() && pizzaManagement.allePizzenFertig(bestellung) &&
					bestellung.getLieferbotenId().equals("")) {
				abholBestellungen.add(bestellung);
			}
		}
		// Bestellungen, die noch quittiert werden müssen
		for (PizzashopBestellung bestellung: orderManagement.findBy(OrderStatus.OPEN)){
			if (userAccount.isPresent() && (bestellung.getLieferbotenId().equals(userAccount.get().getId().toString())
					|| userAccount.get().hasRole(Role.of("BOSS")) && !bestellung.getLieferbotenId().equals(""))){
				bezahlteBestellungen.add(bestellung);
			}
		}
		model.addAttribute("abholBestellungen", abholBestellungen);
		model.addAttribute("lieferBestellungen", lieferBestellungen);
		model.addAttribute("bezahlteBestellungen", bezahlteBestellungen);
		model.addAttribute("fertigeBestellungen", orderManagement.findBy(OrderStatus.COMPLETED));

		return "orderView";
	}

	@PreAuthorize("hasRole('BOSS')")
	@GetMapping("/umsatz")
	String umsatzEinsehen(Model model){
		HashMap<LocalDateTime, MonetaryAmount> dateMoneyMap = new HashMap<LocalDateTime, MonetaryAmount>();
		boolean dateExists = false;
		for (PizzashopBestellung bestellung: orderManagement.findBy(OrderStatus.COMPLETED)){
			for (LocalDateTime date : dateMoneyMap.keySet() ){
				if (bestellung.getDateCreated().getDayOfYear() == (date.getDayOfYear())
						&& bestellung.getDateCreated().getYear() == date.getYear()){
					MonetaryAmount moneyPerDay = dateMoneyMap.get(date).add(bestellung.getTotal());
					dateMoneyMap.replace(date, moneyPerDay);
					dateExists = true;
				}
			}
			if(!dateExists){
				dateMoneyMap.put(bestellung.getDateCreated(), bestellung.getTotal());
			}
			dateExists = false;
		}
		model.addAttribute("umsatzListe", dateMoneyMap);

		//Gesamtumsatz
		MonetaryAmount total = Money.of(0.00, EURO);
		for(LocalDateTime date: dateMoneyMap.keySet()) {
			total = total.add(dateMoneyMap.get(date));
		}
		model.addAttribute("totalUmsatz", total);
		return "umsatz";
	}


	@PostMapping("/abgeschlossen/{order}")
	String pizzaAbgeliefert(@PathVariable OrderIdentifier order, @LoggedIn Optional<UserAccount> userAccount){

		PizzashopBestellung bestellung = bestellungsmanagement.findById(order).get();
		if (!bestellung.isPaid() && userAccount.isPresent()){
			bestellung.setLieferbotenId(userAccount.get().getId().toString());
		} else if(bestellung.isPaid()){
			orderManagement.completeOrder(bestellung);
		}
		orderManagement.save(bestellung);
		return "redirect:/orderView";
	}

	@PostMapping("/bezahlt/{order}")
	String pizzaBezahlt(@PathVariable OrderIdentifier order ){

		PizzashopBestellung bestellung = bestellungsmanagement.findById(order).get();
		orderManagement.payOrder(bestellung);
		orderManagement.completeOrder(bestellung);
		orderManagement.save(bestellung);

		return "redirect:/orderView";
	}

	@PostMapping("/pizza/delete/{id}")
	String deleteZutat(@PathVariable ProductIdentifier id, @ModelAttribute PizzaListe pizzaListe) {
		pizzaListe.deleteZutat(id);
		return "redirect:/pizza";
	}

	@PostMapping("/cart/delete/{id}")
	String deleteBestelleinheit(@PathVariable ProductIdentifier id, @ModelAttribute Cart cart) {
		if (cart.getItem(id.getIdentifier()).get().getQuantity().equals(Quantity.of(1))){
			cart.removeItem(id.getIdentifier());
		}else {cart.addOrUpdateItem(cart.getItem(id.getIdentifier()).get().getProduct(), Quantity.of(-1));}

		return "redirect:/cart";
	}

}