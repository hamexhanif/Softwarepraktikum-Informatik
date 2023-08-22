package pizzashop.bestellung;

import java.sql.Time;

import org.springframework.stereotype.Service;

import pizzashop.ofen.Ofen;
import pizzashop.ofen.OfenManagement;
import pizzashop.ofen.WartelistenManagement;

/**
 * Klasse zur Berechnung der voraussichtlich benötigten Zeit bis zum Erhalt 
 * einer Bestellung ausgehend von 15 Minuten für die Lieferzeit und 6 Minuten 
 * benötigter Fertigstellungszeit pro Pizza.
 */
@Service
public class Zeitberechnung {

	private WartelistenManagement warteliste;
	private OfenManagement ofenManagement;
	
	public Zeitberechnung(WartelistenManagement warteliste, OfenManagement ofenManagement) {
		this.warteliste = warteliste;
		this.ofenManagement = ofenManagement;
	}
	
	/**
	 * Die einzige Methode der Klasse. Berechnet die vorraussichtliche Lieferzeit.
	 * @param int pizzaAnzahl
	 * @return Time
	 */
	
	public Time berechne(int pizzaAnzahl) {
		
		int belegt = 0;
		long lieferzeit = 900000;
		long backzeit = 360000;
		long bestellung = 360000;
		
		if(pizzaAnzahl == 0) {
			return new Time(lieferzeit - 3600000);
		} else {
			for(Ofen o : ofenManagement.findAll()) {
				if(o.isFree() == false) {
					belegt = belegt + 1;
				}
			}
		
			long wartezeit = ((warteliste.size() + belegt)/ofenManagement.showOfen())* backzeit;
		
			long rest = ((warteliste.size() + belegt) % ofenManagement.showOfen()) + pizzaAnzahl;
			if( rest > ofenManagement.showOfen()) {
			
				bestellung = bestellung + ((rest / ofenManagement.showOfen() * backzeit));
			}
				
			return new Time((lieferzeit + wartezeit + bestellung) - 3600000);
		}
	}
}
