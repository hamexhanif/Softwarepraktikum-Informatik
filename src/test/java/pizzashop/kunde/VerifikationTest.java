package pizzashop.kunde;

import org.junit.jupiter.api.Test;
import org.springframework.data.util.Streamable;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class VerifikationTest {

	@Test
	public void istVerifiziertTest(){
		//Mock-kunden
		Kunde kunde1 = mock(Kunde.class);
		when(kunde1.getTan()).thenReturn("234561");
		when(kunde1.getTelefon()).thenReturn("0123456789");
		Kunde kunde2 = mock(Kunde.class);
		when(kunde2.getTan()).thenReturn("123456");
		when(kunde2.getTelefon()).thenReturn("0123456789");
		Kunde kunde3 = mock(Kunde.class);
		when(kunde3.getTan()).thenReturn("123456");
		when(kunde3.getTelefon()).thenReturn("987654321");

		//Mock-KundenManagement
		KundenManagement kundenManagement = mock(KundenManagement.class);
		when(kundenManagement.findAll()).thenReturn(Streamable.of(kunde1,kunde2,kunde3));

		Verifikationsform form = new Verifikationsform("123456", "0123456789");
		assertEquals(form.istVerifiziert(kundenManagement), kunde2, "m");
		//Tel.Nr richtig, Tan falsch
		form = new Verifikationsform("000000", "0123456789");
		assertNull(form.istVerifiziert(kundenManagement), "m");
		//Tan richtig, Tel.Nr falsch
		form = new Verifikationsform("123456", "0000000000");
		assertNull(form.istVerifiziert(kundenManagement), "m");
	}
}
