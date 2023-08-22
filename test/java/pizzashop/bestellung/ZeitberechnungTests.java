package pizzashop.bestellung;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.data.util.Streamable;

import pizzashop.ofen.Ofen;
import pizzashop.ofen.OfenManagement;
import pizzashop.ofen.WartelistenManagement;

public class ZeitberechnungTests {
	
	@Test
	void berechneTest() {

		WartelistenManagement warteliste = mock(WartelistenManagement.class);
		when(warteliste.size()).thenReturn(5);
		
		ArrayList<Ofen> ofenListe = new ArrayList<Ofen>();
		ofenListe.add(new Ofen(true, LocalDateTime.now()));
		ofenListe.add(new Ofen(false, LocalDateTime.now()));
		
		OfenManagement ofenManagement = mock(OfenManagement.class);
		when(ofenManagement.showOfen()).thenReturn((long) 2);
		when(ofenManagement.findAll()).thenReturn(Streamable.of(ofenListe));
		
		Zeitberechnung zeit = new Zeitberechnung(warteliste, ofenManagement);
		
		Time testZeit = new Time(3060000 - 3600000);
	
		assertTrue(testZeit.equals(zeit.berechne(4)));
	}
}
