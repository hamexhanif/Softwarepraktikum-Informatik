package pizzashop.personal;

import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Implementierung der Geschäftslogik in Bezug auf {@link Personal}s.
 *
 * @author Muhammad Fakhruddin
 */
@Service
@Transactional
public class PersonalManagement {

	public static final Role BOSS_ROLE = Role.of("BOSS");
	public static final Role LIEFERBOTE_ROLE = Role.of("LIEFERBOTE");
	public static final Role MITARBEITER_ROLE = Role.of("MITARBEITER");
	public static final Role BAECKER_ROLE = Role.of("BAECKER");

	private final PersonalRepository personals;
	private final UserAccountManagement userAccounts;
	
	/**
	 * Erzeugt ein neues {@link PersonalManagement} mit dem angegebenen {@link PersonalRepository} und
	 * {@link UserAccountManagement}.
	 *
	 * @param personals sollte nicht {@literal null} sein.
	 * @param userAccounts sollte nicht {@literal null} sein.
	 */
	PersonalManagement(PersonalRepository personals, UserAccountManagement userAccounts) {

		Assert.notNull(personals, "PersonalRepository must not be null!");
		Assert.notNull(userAccounts, "UserAccountManagement must not be null!");

		this.personals = personals;
		this.userAccounts = userAccounts;
	}
	
	/**
	 * Erstellt einen neuen {@link Personal} unter Verwendung der im {@link PersonalRegistrationForm} angegebenen Informationen.
	 *
	 * @param form sollte nicht {@literal null} sein.
	 * @return der neuen {@link Personal}-Instanz.
	 */
	public Personal createPersonal(PersonalRegistrationForm form) {
		
		var role = BOSS_ROLE;

		Assert.notNull(form, "Registration form sollte nicht null sein!");

		var password = UnencryptedPassword.of(form.getPassword());
		
		if(form.getJob().equals("mitarbeiter")) {
			role = MITARBEITER_ROLE;
		}
		
		if((form.getJob()).equals("lieferbote")){
			role = LIEFERBOTE_ROLE;
		}
		
		if((form.getJob()).equals("bäcker")){
			role = BAECKER_ROLE;
		}
		
		var userAccount = userAccounts.create(form.getName(), password, role);

		return personals.save(new Personal(userAccount, form.getJob()));
	}

	/**
	 * Prüft, ob es bereits eine {@Personal} mit demselben Benutzernamen gibt
	 *
	 * @param username ist String.
	 * @return true, wenn Benutzername bereits existiert, sonst false.
	 */
	public boolean containsUsername(String username){
		for (UserAccount user : userAccounts.findAll()){
			if (user.getUsername().equals(username)){
				return true;
			}
		}
		return false;
	}
	/**
	 * Löscht einen {@link Personal} mit der angegebenen ID aus dem System.
	 *
	 * @param id sollte nicht {@literal null} sein.
	 */
	public void deletePersonal(long id) {
		personals.deleteById(id);
	}
	
	/**
	 * Gibt alle {@link Personal}s zurück, die derzeit im System verfügbar sind.
	 *
	 * @return aller {@link Personal} Entitäten.
	 */
	public Streamable<Personal> findAll() {
		return personals.findAll();
	}
	/**
	 * Gibt eine {@link Personal} mit der angegebenen ID aus dem System zurück.
	 *
	 * @para id ist long.
	 * @return eine {@link Personal} mit der angegebenen ID.
	 */
	public Personal findById(long id){return personals.findById(id).get();}
}
