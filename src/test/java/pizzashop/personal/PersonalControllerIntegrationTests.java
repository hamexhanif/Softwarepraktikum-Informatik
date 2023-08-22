package pizzashop.personal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;
import pizzashop.AbstractIntegrationTests;

class PersonalControllerIntegrationTests extends AbstractIntegrationTests {

	@Autowired PersonalController controller;

	/**
	 * Does not use any authentication and should raise a security exception.
	 */
	@Test
	void rejectsUnauthenticatedAccessToController() {
		
		PersonalRegistrationForm form = new PersonalRegistrationForm("name", "password", "passVerify", "job");

		assertThatExceptionOfType(AuthenticationException.class) //
				.isThrownBy(() -> controller.personal(new ExtendedModelMap()));
		
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

		controller.personal(model);

		assertThat(model.get("personalList")).isNotNull();
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"boss", "mitarbeiter", "lieferbote", "bäcker", "something_else"})
	@WithMockUser(roles = "BOSS")
	void registerNewPersonal(String job) {

		Errors result = mock(Errors.class);
		PersonalRegistrationForm form = new PersonalRegistrationForm("name", "password", "passVerify", job);
		RedirectAttributes attr = mock(RedirectAttributes.class);
		BindingResult res = mock(BindingResult.class);
		controller.registerNew(form, res, result, attr);
	}
	
//	@Test
//	@WithMockUser(roles = "BOSS")
//	void deletePersonalTest() {
//		
//		
//		//controller.deletePersonal(lieferbote.getId());
//	}

}
