package pizzashop.personal;
import java.util.List;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.UserAccountManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Initialisiert die Standardbenutzerkonten und -mitarbeiter. Die folgenden werden erstellt:
 * <ul>
 * <li>Ein admin namens "boss" und "boss2".</li>
 * <li>Lieferbote "hans", "jerome".</li>
 * <li>Mitarbeiter "dorothy", "samuel".</li>
 * <li>Bäcker "manuel", "peter".</li>
 * </ul>
 *
 * @author Muhammad Fakhruddin
 */
@Component
@Order(2)
class PersonalDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(PersonalDataInitializer.class);

	private final UserAccountManagement userAccountManagement;
	private final PersonalManagement personalManagement;
	
	/**
	 * Erstellt einen neuen {@link PersonalDataInitializer} mit gegebenen {@link UserAccountManagement} und
	 * {@link PersonalManagement}.
	 *
	 * @param userAccountManagement sollte nicht {@literal null} sein.
	 * @param personalManagement sollte nicht {@literal null} sein.
	 */
	PersonalDataInitializer(UserAccountManagement userAccountManagement, PersonalManagement personalManagement) {

		Assert.notNull(userAccountManagement, "UserAccountManagement sollte nicht null sein!");
		Assert.notNull(personalManagement, "PersonalManagement sollte nicht null sein!");

		this.userAccountManagement = userAccountManagement;
		this.personalManagement = personalManagement;
	}

	
	@Override
	public void initialize() {

		// Erstellung überspringen, wenn die Datenbank bereits gefüllt war
		if (userAccountManagement.findByUsername("boss").isPresent()) {
			return;
		}

		LOG.info("Erstellt Standardbenutzer (Mitarbeiter, Lieferbote, Bäcker & Boss).");
		
		var password = "123";
		
		List.of(
				new PersonalRegistrationForm("boss", password, password, "boss"),
				new PersonalRegistrationForm("boss2", password, password, "boss"),//
				new PersonalRegistrationForm("hans", password, password, "lieferbote"),
				new PersonalRegistrationForm("jerome", password, password, "lieferbote"),//
				new PersonalRegistrationForm("dorothy", password, password, "mitarbeiter"),
				new PersonalRegistrationForm("samuel", password, password, "mitarbeiter"),//
				new PersonalRegistrationForm("manuel", password, password, "bäcker"),
				new PersonalRegistrationForm("peter", password, password, "bäcker")//
				).forEach(personalManagement::createPersonal);		
	}
}
