package pizzashop.bestellung;


import org.javamoney.moneta.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.order.Cart;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.payment.Cash;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.ModelMap;
import pizzashop.katalog.BestellKatalog;
import pizzashop.katalog.Bestelleinheit;
import pizzashop.katalog.PizzaListe;
import pizzashop.kunde.KundenManagement;
import pizzashop.ofen.OfenManagement;
import pizzashop.personal.PersonalManagement;

import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.salespointframework.core.Currencies.EURO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BestellungsControllerTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private BestellKatalog bestellKatalog;

	@Autowired
	private BestellungsRepository bestellungsmanagement;

	@Autowired
	private OfenManagement ofenManagement;

	@Autowired
	private PersonalManagement personalManagement;

	@Autowired
	private KundenManagement kundenManagement;

	@Autowired
	private OrderManagement<PizzashopBestellung> orderManagement;

	@BeforeEach
	public void aufbau(){
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	public void abgeschlosseneBestellungenTest() throws Exception{
		var bestellungen = mvc.perform(get("/orderView")).andReturn().getModelAndView().getModelMap().get("fertigeBestellungen");
		int anzahl = this.anzahlBestellungen(bestellungen);
		bestellungen = mvc.perform(get("/orderView")).andReturn().getModelAndView().getModelMap().get("abholBestellungen");
		anzahl += this.anzahlBestellungen(bestellungen);
		bestellungen = mvc.perform(get("/orderView")).andReturn().getModelAndView().getModelMap().get("lieferBestellungen");
		anzahl += this.anzahlBestellungen(bestellungen);
		PizzashopBestellung bestellung = new PizzashopBestellung(personalManagement.findAll().stream().findFirst().get().getUserAccount(), Cash.CASH,
				PizzashopBestellung.OrderType.PICKUP,
				kundenManagement.findAll().stream().findFirst().get().generiereKundendaten());
		orderManagement.save(bestellung);
		bestellungsmanagement.save(bestellung);
		assertEquals(anzahl+1, bestellungsmanagement.count(), "Es sollte eine Bestellungen mehr sein.");
	}

	/*@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	public void umsatz() throws Exception{
		MonetaryAmount total = Money.of(0,EURO);
		/*for(PizzashopBestellung bestellung: orderManagement.findBy(OrderStatus.OPEN)){
			orderManagement.payOrder(bestellung);
		}
		for(PizzashopBestellung bestellung: orderManagement.findBy(OrderStatus.PAID)){
			orderManagement.completeOrder(bestellung);
		}*/
/*

		ModelMap bestellungen = mvc.perform(get("/orderView")).andReturn().getModelAndView().getModelMap();

		System.out.println(bestellungen.get("fertigeBestellungen"));
		for (PizzashopBestellung bestellung : (Streamable<PizzashopBestellung>) bestellungen.get("fertigeBestellungen")){
			total.add(bestellung.getTotal());
		}


		ModelMap modelMap = mvc.perform(get("/umsatz")).andExpect(status().isOk()).andReturn().getModelAndView().getModelMap();
		assertEquals( modelMap.get("totalUmsatz"), total, "m");

	}*/

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	public void bestellungenFertigstellenTest() throws Exception{
		var bestellungen = mvc.perform(get("/orderView")).andReturn().getModelAndView().getModelMap().get("abholBestellungen");
		for (Object bestellung : this.bestellungenToList(bestellungen)){
			PizzashopBestellung bestellung1 = (PizzashopBestellung) bestellung;
			if(!bestellung1.isCompleted() || bestellung1.isCanceled()){
				mvc.perform(post("/abgeschlossen/"+bestellung1.getId())).andExpect(status().isFound());
			}
		}
		bestellungen = mvc.perform(get("/orderView")).andReturn().getModelAndView().getModelMap().get("lieferBestellungen");
		for (Object bestellung : this.bestellungenToList(bestellungen)){
			PizzashopBestellung bestellung1 = (PizzashopBestellung) bestellung;
			if(!bestellung1.isCompleted() || bestellung1.isCanceled()){
				mvc.perform(post("/abgeschlossen/"+bestellung1.getId())).andExpect(status().isFound());
			}
		}
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	public void korbLeeren() throws Exception{
		PizzaContainer pizzaContainer = new PizzaContainer();
		pizzaContainer.addToContainer(new PizzaListe());
		Cart cartBefore = new Cart();

		HashMap<String, Object> sessionattr = new HashMap<String, Object>();
		sessionattr.put("pizzaContainer", pizzaContainer);
		sessionattr.put("cart", cartBefore.addOrUpdateItem(bestellKatalog.findAll().stream().findFirst().get(), Quantity.of(2)));

		mvc.perform(post("/leeren").sessionAttrs(sessionattr));
		ModelMap after = mvc.perform(get("/cart")).andExpect(status().isOk()).andReturn().getModelAndView().getModelMap();

		assertTrue(((Cart) after.get("cart")).isEmpty(), "Warenkorb sollte leer sein!");
		assertTrue(((PizzaContainer) after.get("pizzaContainer")).isEmpty(), "PizzaContainer sollte leer sein!");
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	public void addZutatenZuPizza() throws Exception{
		PizzaListe pizza = new PizzaListe();
		Bestelleinheit zutat = new Bestelleinheit("Zutat", Money.of(1, EURO), Bestelleinheit.BestellType.ZUTAT);
		Bestelleinheit zutat2 = new Bestelleinheit("Zutat2", Money.of(1, EURO), Bestelleinheit.BestellType.ZUTAT);
		pizza.addZutat(zutat);
		bestellKatalog.save(zutat2);
		bestellKatalog.save(zutat);

		HashMap<String, Object> sessionattr = new HashMap<String, Object>();
		sessionattr.put("pizzaListe", pizza);

		mvc.perform(post("/pizza").param("product",  zutat2.getId().toString()).sessionAttrs(sessionattr)).andExpect(status().isOk());
		mvc.perform(post("/pizza").param("product",  zutat.getId().toString())).andExpect(status().isOk());
		//mvc.perform(post("/pizza").param("product",  zutat2.getId().toString())).andExpect(status().isOk());

		mvc.perform(post("/pizza/delete/"+zutat.getId()).sessionAttrs(sessionattr)).andExpect(status().isFound()).andReturn().getModelAndView().getModelMap();
		ModelMap modelMap = mvc.perform(get("/pizza").sessionAttrs(sessionattr)).andReturn().getModelAndView().getModelMap();
		assertEquals(((PizzaListe) modelMap.get("pizzaListe")).getList().size(),1,"m");


		System.out.println(modelMap);

	}

	public int anzahlBestellungen(Object bestellungen){
		int anzahl = 0;
		try {
			for (Object  bestellung: (ArrayList) bestellungen){
				anzahl += 1;
			}
		}
		catch (Exception e){
			return 1;
		}
		return anzahl;
	}

	public ArrayList bestellungenToList(Object bestellungen){
		return (ArrayList) bestellungen;
	}

	@AfterEach
	public void abbauen(){

	}
}
