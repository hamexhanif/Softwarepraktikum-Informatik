package pizzashop.kunde;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.hamcrest.CoreMatchers.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.util.Streamable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import pizzashop.kunde.Kunde;
import pizzashop.kunde.KundenManagement;
import pizzashop.kunde.Registrierungsform;
import pizzashop.kunde.KundenEinträge;

@SpringBootTest
@AutoConfigureMockMvc
class KundenManagementUnitTests {
	
	@Autowired
	private KundenManagement kundenManagement;
	
	@Autowired
	private KundenEinträge kunden;
	
	@Test
	public void deleteKunde(){
		//Mock-kunden
		Registrierungsform form = new Registrierungsform("vorname", "nachname", "adresse", "telefon");
		Kunde kunde1 = kundenManagement.createKunden(form);			
		kunden.findById(kunde1.getId()).isPresent();
		kunden.deleteById(kunde1.getId());
		assertFalse(kunden.findById(kunde1.getId()).isPresent());
	}
		
	
	@Test
	public void updateTan() {
		//Mock-kunden		
		Registrierungsform form = new Registrierungsform("vorname", "nachname", "adresse", "telefon");
		Kunde kunde1 = kundenManagement.createKunden(form);
		String oldTan = kunde1.getTan();
		kundenManagement.updateTan(kunde1);
		String newTan = kunde1.getTan();
		assertNotSame(oldTan,newTan);
		
	}
	
}