package pizzashop.ofen;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pizzashop.AbstractIntegrationTests;

class OfenControllerTest extends AbstractIntegrationTests {
	
	@Autowired OfenController controller;

	/**
	 * Uses {@link WithMockUser} to simulate access by a user with boss role.
	 */
	@Test
	@WithMockUser(roles = "BOSS")
	void allowsAuthenticatedAccessToController() {

		ExtendedModelMap model = new ExtendedModelMap();

		controller.ofen(model);

		assertThat(model.get("ofen")).isNotNull();
	}
	
//	@Test
//	void adjustOfenTest() {
//		RedirectAttributes attr = mock(RedirectAttributes.class);
//		controller.adjustOfenNumber(5L, attr);
//	}

}
