package pizzashop.kunde;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Verifikationsform {


	@Size(min=5, max=6, message = "{Verifikationsform.tan.Size}") //
	@NotEmpty(message = "{Verifikationsform.tan.NotEmpty}") //
	@Pattern(regexp = "[0-9]*", message = "{Verifikationsform.tan.Pattern}") //
	private final String tan;

	@Size(min=7, max=11, message = "{Verifikationsform.telefon.Size}") //
	@NotEmpty(message = "{Verifikationsform.telefon.NotEmpty}") //
	@Pattern(regexp = "[0-9]*", message = "{Verifikationsform.telefon.Pattern}") //
	private final String telefon;

	/**
	 * Die Verifikationsform für einen {@link Kunde} 
	 * 
	 * @param tan wird auf Gleichheit geprüft
	 * @param telefon wird auf Gleichheit geprüft
	 */

	public Verifikationsform(String tan, String telefon) {
		this.tan = tan;
		this.telefon = telefon;
	}

	public String getTan() {
		return tan;
	}

	public String getTelefon() {
		return telefon;
	}

	/**
	 * Verifizierte {@link Kunde} werden weitergeleitet zu "/order", ansonsten werden sie nicht weitergeleitet
	 * 
	 * @param kundenManagement
	 * @return null oder kunde
	 */
	
	public Kunde istVerifiziert(KundenManagement kundenManagement){
		for (Kunde kunde : kundenManagement.findAll()){
			if(kunde.getTan().equals(this.tan) && kunde.getTelefon().equals(this.telefon)){
				return kunde;
			}
		}
		return null;
	}
}
