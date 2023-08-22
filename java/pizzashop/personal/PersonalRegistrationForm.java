package pizzashop.personal;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class PersonalRegistrationForm {

	@NotEmpty(message = "{RegistrationForm.name.NotEmpty}") //
	private final String name;

	@NotEmpty(message = "{RegistrationForm.password.NotEmpty}") //
	@Pattern(regexp="(?=.*[0-9])(?=.*[A-Z])(?=\\S+$).{8,}",
			message = "Das Passwort muss mindestens einen Großbuchstaben, " +
					"ein Sonderzeichen und eine Zahl enthalten und mindestens 8 Zeichen lang sein.")
	private final String password;
	
	@NotEmpty(message = "{RegistrationForm.passVerify.NotEmpty}")
	private String passVerify;
	
	@NotEmpty(message = "{RegistrationForm.job.NotEmpty}")
	private String job;
	
	//verify the inputed password
	@AssertTrue(message = "{RegistrationForm.passVerify.AssertTrue}")
  	private boolean isValid() {
		return this.password.equals(this.passVerify);
    }
	
	public PersonalRegistrationForm(String name, String password, String passVerify, String job) {

		this.name = name;
		this.password = password;
		this.passVerify = passVerify;
		this.job = job;
	}

	public String getName() {
		return name;
	}

	public String getPassword() {
		return password;
	}
	
	public String getPassVerify() {
		return passVerify;
	}
	
	public String getJob() {
		return job;
	}
}
