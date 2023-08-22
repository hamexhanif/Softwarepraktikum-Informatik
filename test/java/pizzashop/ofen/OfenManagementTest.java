package pizzashop.ofen;


import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import pizzashop.katalog.PizzaListe;


@SpringBootTest
@AutoConfigureMockMvc
public class OfenManagementTest {

	@Autowired OfenRepository ofenRepository;
	@Autowired OfenManagement ofenManagement;

	@Test
	void createOfenTest() {

		//create 5 Ofen
		for(int i=0;i < 5;i++) {
			ofenManagement.createOfen();
		}

		for(Ofen o : ofenManagement.findAll()) {
			assertNotNull(ofenManagement.getOfen(o.getId()));
		}		
	}

	@Test
	void adjustOfenButNothingChangeTest() {
		ofenManagement.adjustOfen(5);

		assertEquals(5, ofenManagement.showOfen());

	}
	@Test
	void addOfenTest() {

		ofenManagement.adjustOfen(10);

		assertEquals(10, ofenManagement.showOfen());

	}

	@Test
	void reduceOfenTest() {

		ofenManagement.adjustOfen(7);

		assertEquals(7, ofenManagement.showOfen());

	}

	@Test
	void backenFreeTest() {

		PizzaListe pizza = mock(PizzaListe.class);

		for(Ofen o : ofenManagement.findAll()) {
			ofenManagement.backen(o.getId(), pizza);
			return;
		}
	}

	@Test
	void backenUsedTest() {
		
		PizzaListe pizza = mock(PizzaListe.class);

		for(Ofen o : ofenManagement.findAll()) {
			o.setFree(false);
			ofenManagement.backen(o.getId(), pizza);
			return;
		}
	}
	
//	@Test
//	void getCountUsedOfenTest() {
//		for(Ofen o : ofenManagement.findAll()) {
//			o.setFree(false);
//		}
//		
//		assertEquals(ofenRepository.count(), ofenManagement.getCountUsedOfen());
//	}
}
