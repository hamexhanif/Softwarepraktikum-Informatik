package pizzashop.kunde;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pizzashop.AbstractIntegrationTests;

class KundenControllerIntegrationTests extends AbstractIntegrationTests {

	@Autowired KundenController controller;

	/**
	 * Does not use any authentication and should raise a security exception.
	 */
	@Test
	void rejectsUnauthenticatedAccessToController() {
		
		Registrierungsform form = new Registrierungsform("vorname", "nachname", "adresse", "telefon");

		assertThatExceptionOfType(AuthenticationException.class) //
				.isThrownBy(() -> controller.kunden(new ExtendedModelMap()));
		
		assertThatExceptionOfType(AuthenticationException.class) //
				.isThrownBy(() -> controller.register(new ExtendedModelMap(), form));
	}

	/**
	 * Uses {@link WithMockUser} to simulate access by a user with boss role.
	 */
	@Test
	@WithMockUser(roles = "BOSS")
	void allowsAuthenticatedAccessToController() {

		ExtendedModelMap model = new ExtendedModelMap();

		controller.kunden(model);

		assertThat(model.get("kundenListe")).isNotNull();
	}
	
	@Test	
	@WithMockUser(roles = "BOSS")
	void registerNewKunde() {

		Errors result = mock(Errors.class);
		Registrierungsform form = new Registrierungsform("vorname", "nachname", "adresse", "telefon");
		RedirectAttributes attributes = mock(RedirectAttributes.class);
		 
		controller.registerNew(form, result, attributes);		
	}
		
}