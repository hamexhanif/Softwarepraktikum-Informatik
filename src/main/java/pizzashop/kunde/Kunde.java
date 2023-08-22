package pizzashop.kunde;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.Random;

@Entity
public class Kunde {
		
	private String vorname;
	private String nachname;
	private String telefon;
	private String adresse;
	private String tan;
	
		
	private @Id @GeneratedValue long id;

	@SuppressWarnings("unused")
	public Kunde() {}
	
	/**
	 * Fügt einen Kunden mit vorname, nachname, adresse und telefon hinzu, außerdem wird eine random generierte TAN
	 * für diesen Kunden erstellt.
	 * 
	 * @param String vorname
	 * @param String nachname
	 * @param String adresse
	 * @param String telefon
	 */
	
	public Kunde (String vorname, String nachname, String adresse, String telefon) {
		this.vorname = vorname;
		this.nachname = nachname;		
		this.adresse = adresse;
		this.telefon = telefon;
		Random genrator = new Random();
		int number = genrator.nextInt(899999)+100000;
		this.tan = String.format("%6d", number);
		System.out.println(this.nachname+"'s TAN: "+this.getTan()+" & Tel.Nr: "+ this.telefon);
	}
	
	public String getVorname() {
		return vorname;
	}

	public String getNachname() {
		return nachname;
	}
	
	public String getAdresse() {
		return adresse;
	}

	public String getTelefon() {
		return telefon;
	}
	
	public long getId() {
		return id;
	}

	public String getTan(){
		return this.tan;
	}

	/**
	 * erstellt eine neue random TAN, ohne das eine vorhandene TAN ausgesucht wird
	 */
	
	public void newTan(){
		Random genrator = new Random();
		String newTan = this.tan;
		while (this.tan.equals(newTan)){
			int number = genrator.nextInt(899999)+100000;
			newTan = String.format("%6d", number);
		}
		this.tan = newTan;
		System.out.println("Neue TAN: "+this.getTan()+" & Tel.Nr: "+ this.telefon);
	}

	/**
	 * generiert eine Liste von gegebenen Daten eines {@link Kunde}
	 * 
	 * @return userdata
	 */
	
	public ArrayList<String> generiereKundendaten(){
		ArrayList<String> userdata = new ArrayList<String>();
		userdata.add(this.vorname);
		userdata.add(this.nachname);
		userdata.add(this.adresse);
		userdata.add(this.tan);
		return userdata;
	}

	public void setVorname(String vorname){
		this.vorname = vorname;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public void setTan(String tan) {
		this.tan = tan;
	}
}