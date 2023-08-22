package pizzashop.kunde;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Registrierungsform {

	@NotEmpty(message = "{Registrierungsform.vorname.NotEmpty}")//
	@Pattern(regexp = "[A-Z|Ä|Ö|Ü]([a-z|ä|ö|ü])*", message = "{Registrierungsform.vorname.Pattern}") //
	private final String vorname;	
	
	@NotEmpty(message = "{Registrierungsform.nachname.NotEmpty}")
	@Pattern(regexp = "[A-Z|Ä|Ö|Ü]([a-z|ä|ö|ü])*", message = "{Registrierungsform.name.Pattern}") //
	private final String nachname;
	
	@NotEmpty(message = "{Registrierungsform.adresse.NotEmpty}")
	//@Pattern(regexp = "[A-Z|Ä|Ö|Ü]([a-z|ä|ö|ü])*", message = "{Registrierungsform.adresse.Pattern}") //
	private final String adresse;
    	
	@NotEmpty(message = "{Registrierungsform.telefon.NotEmpty}")
	@Size(min=7, max=11, message = "{Verifikationsform.telefon.Size}") //
	@Pattern(regexp = "[0-9]*", message = "{Verifikationsform.telefon.Pattern}") //
	private final String telefon;
				
	public Registrierungsform(String vorname, String nachname, String adresse, String telefon) {

		this.vorname = vorname;
		this.nachname = nachname;
		this.adresse = adresse;
		this.telefon = telefon;
	}

	public String getVorname() {
		return vorname;
	}
	
	public String getNachname() {
		return nachname;
	}
	
	public String getName() {
		return vorname + "" + nachname;
	}
	
	public String getAdresse() {
		return adresse;
	}

	public String getTelefon() {
		return telefon;
	}
}
