package pizzashop;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pizzashop.bestellung.BestellungsRepository;
import pizzashop.katalog.BestellKatalog;
import pizzashop.katalog.Bestelleinheit;
import pizzashop.kunde.Kunde;
import pizzashop.personal.PersonalManagement;

import static org.salespointframework.core.Currencies.EURO;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ZugriffTests {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private PersonalManagement personalManagement;

	@Autowired
	private BestellungsRepository bestellungsmanagement;

	@Autowired
	private BestellKatalog bestellKatalog;

	@Test
	@WithMockUser(username = "default", roles = "LIEFERBOTE")
	public void LieferbotenZugriffTest() throws Exception{

		mvc.perform(get("/register")).andExpect(status().isForbidden());
		mvc.perform(get("/kundenliste")).andExpect(status().isForbidden());
		mvc.perform(get("/verifikation")).andExpect(status().isForbidden());
		mvc.perform(get("/ofen_anzahl")).andExpect(status().isForbidden());
		mvc.perform(get("/personal/register")).andExpect(status().isForbidden());
		mvc.perform(get("/personal")).andExpect(status().isForbidden());
		mvc.perform(get("/getraenke")).andExpect(status().isForbidden());
		mvc.perform(get("/salat")).andExpect(status().isForbidden());
		//mvc.perform(get("/ingredients")).andExpect(status().isForbidden());
		mvc.perform(get("/angebot")).andExpect(status().isForbidden());
		//mvc.perform(get("/orderView")).andExpect(status().isOk());;
		mvc.perform(get("/cart")).andExpect(status().isForbidden());

		Kunde kunde = new Kunde("vor", "x", "x", "x");
		mvc.perform(get("/order").flashAttr("kunde", kunde)).andExpect(status().isForbidden());
		mvc.perform(get("/pizza").flashAttr("kunde", kunde)).andExpect(status().isForbidden());

		Bestelleinheit bestelleinheit = new Bestelleinheit("Käse", Money.of(1.30, EURO),  Bestelleinheit.BestellType.ZUTAT);
		bestellKatalog.save(bestelleinheit);
		mvc.perform(get("/bestelleinheit/"+bestelleinheit.getId())).andExpect(status().isForbidden());
		mvc.perform(get("/bill/"+ bestellungsmanagement.findAll().iterator().next().getId())).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "default", roles = "MITARBEITER")
	public void MitarbeiterZugriffTest() throws Exception{

		mvc.perform(get("/register")).andExpect(status().isOk());
		mvc.perform(get("/kundenliste")).andExpect(status().isOk());
		mvc.perform(get("/verifikation")).andExpect(status().isOk());
		mvc.perform(get("/ofen_anzahl")).andExpect(status().isForbidden());
		mvc.perform(get("/personal/register")).andExpect(status().isForbidden());
		mvc.perform(get("/personal")).andExpect(status().isForbidden());
		mvc.perform(get("/getraenke")).andExpect(status().isOk());
		mvc.perform(get("/salat")).andExpect(status().isOk());
		//mvc.perform(get("/ingredients")).andExpect(status().isOk());
		mvc.perform(get("/angebot")).andExpect(status().isForbidden());
		//mvc.perform(get("/orderView")).andExpect(status().isOk());
		mvc.perform(get("/cart")).andExpect(status().isOk());

		Kunde kunde = new Kunde("vor", "x", "x", "x");
		mvc.perform(get("/order").flashAttr("kunde", kunde)).andExpect(status().isOk());
		mvc.perform(get("/pizza").flashAttr("kunde", kunde)).andExpect(status().isOk());

		Bestelleinheit bestelleinheit = new Bestelleinheit("Käse", Money.of(1.30, EURO),  Bestelleinheit.BestellType.ZUTAT);
		bestellKatalog.save(bestelleinheit);
		mvc.perform(get("/bestelleinheit/"+bestelleinheit.getId())).andExpect(status().isForbidden());
		mvc.perform(get("/bill/"+ bestellungsmanagement.findAll().iterator().next().getId())).andExpect(status().isOk());
	}

	@Test
	@WithMockUser(username = "boss", roles = "BOSS")
	public void BossZugriffTest() throws Exception{

		mvc.perform(get("/register")).andExpect(status().isOk());
		mvc.perform(get("/kundenliste")).andExpect(status().isOk());
		mvc.perform(get("/verifikation")).andExpect(status().isOk());
		mvc.perform(get("/ofen_anzahl")).andExpect(status().isOk());
		mvc.perform(get("/personal/register")).andExpect(status().isOk());
		mvc.perform(get("/personal")).andExpect(status().isOk());
		mvc.perform(get("/getraenke")).andExpect(status().isOk());
		mvc.perform(get("/salat")).andExpect(status().isOk());
		//mvc.perform(get("/ingredients")).andExpect(status().isOk());
		mvc.perform(get("/angebot")).andExpect(status().isOk());
		mvc.perform(get("/orderView")).andExpect(status().isOk());
		mvc.perform(get("/cart")).andExpect(status().isOk());

		Kunde kunde = new Kunde("vor", "x", "x", "x");
		mvc.perform(get("/order").flashAttr("kunde", kunde)).andExpect(status().isOk());
		mvc.perform(get("/pizza").flashAttr("kunde", kunde)).andExpect(status().isOk());

		Bestelleinheit bestelleinheit = new Bestelleinheit("Käse", Money.of(1.30, EURO),  Bestelleinheit.BestellType.ZUTAT);
		bestellKatalog.save(bestelleinheit);
		mvc.perform(get("/bestelleinheit/"+bestelleinheit.getId())).andExpect(status().isOk());
		mvc.perform(get("/bill/"+ bestellungsmanagement.findAll().iterator().next().getId())).andExpect(status().isOk());
	}
}
