package pizzashop.personal;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.salespointframework.useraccount.Password.UnencryptedPassword;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.mysema.commons.lang.Assert;

@SpringBootTest
@AutoConfigureMockMvc
class PersonalManagementUnitTests {
	
	@Autowired PersonalManagement personalManagement;
	@Autowired PersonalRepository personalRepository;
	
	@Test
	void createsUserAccountWhenCreatingALieferbote() {

		// Given
		// … a PersonalRepository returning customers handed into save(…),
		PersonalRepository repository = mock(PersonalRepository.class);
		when(repository.save(any())).then(i -> i.getArgument(0));

		// … a UserAccountManager
		UserAccountManagement userAccountManager = mock(UserAccountManagement.class);
		UserAccount userAccount = mock(UserAccount.class);
		when(userAccountManager.create(any(), any(), any())).thenReturn(userAccount);

		// … and the PersonalManagement using both of them,
		PersonalManagement personalManagement = new PersonalManagement(repository, userAccountManager);

		// When
		// … a registration form is submitted
		PersonalRegistrationForm form = new PersonalRegistrationForm("name", "password", "passVerify", "lieferbote");
		Personal personal = personalManagement.createPersonal(form);

		// Then
		// … a user account creation has been triggered with the proper data and role
		verify(userAccountManager, times(1)) //
				.create(eq(form.getName()), //
						eq(UnencryptedPassword.of(form.getPassword())), //
						eq(PersonalManagement.LIEFERBOTE_ROLE));

		// … the lieferbote has a user account attached
		assertThat(personal.getUserAccount()).isNotNull();
	}
	
	@Test
	void createsUserAccountWhenCreatingAMitarbeiter() {

		// Given
		// … a PersonalRepository returning customers handed into save(…),
		PersonalRepository repository = mock(PersonalRepository.class);
		when(repository.save(any())).then(i -> i.getArgument(0));

		// … a UserAccountManager
		UserAccountManagement userAccountManager = mock(UserAccountManagement.class);
		UserAccount userAccount = mock(UserAccount.class);
		when(userAccountManager.create(any(), any(), any())).thenReturn(userAccount);

		// … and the PersonalManagement using both of them,
		PersonalManagement personalManagement = new PersonalManagement(repository, userAccountManager);

		// When
		// … a registration form is submitted
		PersonalRegistrationForm form = new PersonalRegistrationForm("name", "password", "passVerify", "mitarbeiter");
		Personal mitarbeiter = personalManagement.createPersonal(form);

		// Then
		// … a user account creation has been triggered with the proper data and role
		verify(userAccountManager, times(1)) //
				.create(eq(form.getName()), //
						eq(UnencryptedPassword.of(form.getPassword())), //
						eq(PersonalManagement.MITARBEITER_ROLE));

		// … the mitarbeiter has a user account attached
		assertThat(mitarbeiter.getUserAccount()).isNotNull();
	}
	
	@Test
	void createsUserAccountWhenCreatingABoss() {

		// Given
		// … a PersonalRepository returning customers handed into save(…),
		PersonalRepository repository = mock(PersonalRepository.class);
		when(repository.save(any())).then(i -> i.getArgument(0));

		// … a UserAccountManager
		UserAccountManagement userAccountManager = mock(UserAccountManagement.class);
		UserAccount userAccount = mock(UserAccount.class);
		when(userAccountManager.create(any(), any(), any())).thenReturn(userAccount);

		// … and the PersonalManagement using both of them,
		PersonalManagement personalManagement = new PersonalManagement(repository, userAccountManager);

		// When
		// … a registration form is submitted
		PersonalRegistrationForm form = new PersonalRegistrationForm("name", "password", "passVerify", "boss");
		Personal boss = personalManagement.createPersonal(form);

		// Then
		// … a user account creation has been triggered with the proper data and role
		verify(userAccountManager, times(1)) //
				.create(eq(form.getName()), //
						eq(UnencryptedPassword.of(form.getPassword())), //
						eq(PersonalManagement.BOSS_ROLE));

		// … the boss has a user account attached
		assertThat(boss.getUserAccount()).isNotNull();
	}
	
	@Test
	void createsUserAccountWhenCreatingABäcker() {

		// Given
		// … a PersonalRepository returning customers handed into save(…),
		PersonalRepository repository = mock(PersonalRepository.class);
		when(repository.save(any())).then(i -> i.getArgument(0));

		// … a UserAccountManager
		UserAccountManagement userAccountManager = mock(UserAccountManagement.class);
		UserAccount userAccount = mock(UserAccount.class);
		when(userAccountManager.create(any(), any(), any())).thenReturn(userAccount);

		// … and the PersonalManagement using both of them,
		PersonalManagement personalManagement = new PersonalManagement(repository, userAccountManager);

		// When
		// … a registration form is submitted
		PersonalRegistrationForm form = new PersonalRegistrationForm("name", "password", "passVerify", "bäcker");
		Personal bäcker = personalManagement.createPersonal(form);

		// Then
		// … a user account creation has been triggered with the proper data and role
		verify(userAccountManager, times(1)) //
				.create(eq(form.getName()), //
						eq(UnencryptedPassword.of(form.getPassword())), //
						eq(PersonalManagement.BAECKER_ROLE));

		// … the bäcker has a user account attached
		assertThat(bäcker.getUserAccount()).isNotNull();
	}
	
	@Test
	void deletePersonalTest() {
		
		PersonalRegistrationForm form = new PersonalRegistrationForm("name", "password", "passVerify", "bäcker");
		Personal test = personalManagement.createPersonal(form);			
		personalManagement.deletePersonal(test.getId());
		assertFalse(personalRepository.findById(test.getId()).isPresent());
	}
	
	@Test
	void usernameAlreadyExists() {
		PersonalRegistrationForm form = new PersonalRegistrationForm("broly", "password", "passVerify", "bäcker");
		personalManagement.createPersonal(form);
		PersonalRegistrationForm form2 = new PersonalRegistrationForm("test", "password", "passVerify", "lieferbote");
		personalManagement.createPersonal(form2);		
		
		assertTrue(personalManagement.containsUsername("test"));
	}
	
	@Test
	void usernameNotExistsYet() {
		PersonalRegistrationForm form = new PersonalRegistrationForm("yoda", "password", "passVerify", "bäcker");
		personalManagement.createPersonal(form);
		PersonalRegistrationForm form2 = new PersonalRegistrationForm("test2", "password", "passVerify", "lieferbote");
		personalManagement.createPersonal(form2);		
		
		assertFalse(personalManagement.containsUsername("kaminari"));
	}
	
	@Test
	void findByIdTest() {
		PersonalRegistrationForm form = new PersonalRegistrationForm("r2d2", "password", "passVerify", "bäcker");
		Personal test = personalManagement.createPersonal(form);
		assertNotNull(personalManagement.findById(test.getId()));
	}
	
	
	
	
}
