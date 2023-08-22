package pizzashop.katalog;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.salespointframework.core.Currencies.EURO;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.salespointframework.inventory.UniqueInventory;
import org.salespointframework.inventory.UniqueInventoryItem;
import org.salespointframework.quantity.Quantity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.HashMap;

@SpringBootTest
@AutoConfigureMockMvc
public class KatalogControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private BestellKatalog bestellKatalog;

	@Autowired
	private UniqueInventory<UniqueInventoryItem> inventar;

	private
	MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

	@BeforeEach
	public void aufbau(){
		params.add("name", "Käse");
		params.add("preis", "1.30");
		params.add("bestellType", "salat");
	}


	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	public void preisUpdateTest() throws Exception{
		Bestelleinheit bestelleinheit = bestellKatalog.findAll().stream().findFirst().get();
		mvc.perform(post("/bestelleinheit/"+bestelleinheit.getId()+"/preis").param("preis", "1.30")).andExpect(status().isFound());
		Bestelleinheit returnEinheit = bestellKatalog.findById(bestelleinheit.getId()).get();
		assertEquals(Money.of(1.30, EURO), returnEinheit.getPrice(), "Preis stimmt nicht");
	}

	@Test
	@WithMockUser(username = "default", roles = "MITARBEITER")
	public void mitarbeiterHasNoAccess() throws Exception{
		mvc.perform(get("/angebot")).andExpect(status().isForbidden());
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	public void addBestelleinheitToAngebotTest() throws Exception{
		int anzahl = bestellKatalog.findAll().toList().size();
		System.out.println(bestellKatalog.toString());
		mvc.perform(post("/angebot").params(params)).andExpect(status().isFound());
		int anzahl2 = bestellKatalog.findAll().toList().size();
		assertEquals(anzahl +1 , anzahl2, "Hinzugefügte Bestelleinheit sollte die Größe des Katalogs um 1 erhöhen");
		assertEquals(bestellKatalog.findByName("Käse").stream().findFirst().get().getPrice(),Money.of(1.30,EURO),
				"First is not Käse");
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	public void addExcistingNameTest() throws Exception{
		bestellKatalog.save(new Bestelleinheit("Käse", Money.of(1.30,EURO), Bestelleinheit.BestellType.ZUTAT));
		mvc.perform(post("/angebot").params(params)).andExpect(status().isOk());
		assertEquals(bestellKatalog.findByName("Käse").stream().count(), 1,
				"There is only one Käse im Katalog");
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	public void deleteBestelleinheitTest() throws Exception{
		Bestelleinheit bestelleinheit = new Bestelleinheit("Käse", Money.of(1.30,EURO), Bestelleinheit.BestellType.ZUTAT);
		bestellKatalog.save(bestelleinheit);
		inventar.save(new UniqueInventoryItem(bestelleinheit, Quantity.of(1000)));
		int count = bestellKatalog.findAll().toList().size();
		mvc.perform(post("/bestelleinheit/"+bestelleinheit.getId()+"/loeschen")).andExpect(status().isFound());
		assertThat(bestellKatalog.findByName("Käse")).isEmpty();
		assertEquals(count-1, bestellKatalog.findAll().toList().size(), "b");
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	public void getCorrectKatalogTest() throws Exception{
		Bestelleinheit bestelleinheit = new Bestelleinheit("Käse", Money.of(1.30,EURO), Bestelleinheit.BestellType.ZUTAT);
		mvc.perform(post("/angebot").params(params)).andExpect(status().isFound());
		Object katalog = mvc.perform(get("/salat")).andReturn().getModelAndView().getModelMap().get("katalog");
		assertEquals(bestellKatalog.findByBestellType(Bestelleinheit.BestellType.SALAT), katalog, "m");
		katalog = mvc.perform(get("/getraenke")).andReturn().getModelAndView().getModelMap().get("katalog");
		assertEquals(bestellKatalog.findByBestellType(Bestelleinheit.BestellType.GETRAENK), katalog, "m");
		katalog = mvc.perform(get("/essgarnituren")).andReturn().getModelAndView().getModelMap().get("katalog");
		assertEquals(bestellKatalog.findByBestellType(Bestelleinheit.BestellType.ESSGARNITUR), katalog, "m");

		PizzaListe pizzaListe = mock(PizzaListe.class);
		HashMap<String, Object> sessionattr = new HashMap<String, Object>();
		sessionattr.put("pizzaListe", pizzaListe);
		katalog = mvc.perform(get("/ingredients").sessionAttrs(sessionattr)).andReturn().getModelAndView().getModelMap().get("ingredients");
		assertEquals(bestellKatalog.findByBestellType(Bestelleinheit.BestellType.ZUTAT), katalog, "m");
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	public void bestelleinheitFormNameTest() throws Exception {
		int anzahl = bestellKatalog.findAll().toList().size();
		ArrayList<String> nameList = new ArrayList<String>();
		nameList.add("käse");
		nameList.add("KÄse");
		nameList.add("");
		nameList.add("Käse1");
		nameList.add("käSe");
		nameList.add("RotePaprika");
		for (String name : nameList) {
			params.set("name", name);
			mvc.perform(post("/angebot").params(params)).andExpect(status().isOk());
			assertEquals(anzahl, bestellKatalog.findAll().toList().size(), "n");
		}
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	public void bestelleinheitFormPreisTest() throws Exception {
		int anzahl = bestellKatalog.findAll().toList().size();
		ArrayList<String> preisList = new ArrayList<String>();
		preisList.add("-0");
		preisList.add("-0.06");
		preisList.add("-6786");
		preisList.add("preis");
		for (String preis : preisList) {
			params.set("preis", preis);
			mvc.perform(post("/angebot").params(params)).andExpect(status().isOk());
			assertEquals(anzahl, bestellKatalog.findAll().toList().size(), "n");
		}
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	public void nameExistsError() throws Exception{
		mvc.perform(post("/angebot").params(params)).andExpect(status().isFound());
		long anzahl = bestellKatalog.count();
		mvc.perform(post("/angebot").params(params)).andExpect(status().isOk());
		assertEquals(anzahl, bestellKatalog.count(), "The count should be the same.");
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	public void neueBestelleinheitFormTest() throws Exception {
		params.set("bestellType", "salat");
		mvc.perform(post("/angebot").params(params)).andExpect(status().isFound());
		assertEquals(bestellKatalog.findByName("Käse").stream().findFirst().get().getBestellType(),
				"SALAT", "The count should be the same.");
		Bestelleinheit kaese = bestellKatalog.findByName("Käse").stream().findFirst().get();
		inventar.delete(inventar.findByProduct(kaese).get());
		bestellKatalog.delete(kaese);

		params.set("bestellType", "zutat");
		mvc.perform(post("/angebot").params(params)).andExpect(status().isFound());
		assertEquals(bestellKatalog.findByName("Käse").stream().findFirst().get().getBestellType(),
				"ZUTAT", "The count should be the same.");

		kaese = bestellKatalog.findByName("Käse").stream().findFirst().get();
		inventar.delete(inventar.findByProduct(kaese).get());
		bestellKatalog.delete(kaese);

		params.set("bestellType", "getraenk");
		mvc.perform(post("/angebot").params(params)).andExpect(status().isFound());
		System.out.println(bestellKatalog.findByName("Käse").stream().findFirst().get().getBestellType());
		assertEquals(bestellKatalog.findByName("Käse").stream().findFirst().get().getBestellType(),
				"GETRAENK", "The count should be the same.");

	}

	@AfterEach
	public void abbauen() throws Exception{
		if (bestellKatalog.findByName("Käse").stream().findFirst().isPresent()){
			Bestelleinheit kaese = bestellKatalog.findByName("Käse").stream().findFirst().get();
			if (inventar.findByProduct(kaese).isPresent()){
				inventar.delete(inventar.findByProduct(kaese).get());
			}

			bestellKatalog.delete(kaese);
		}
		params.clear();

	}
}
