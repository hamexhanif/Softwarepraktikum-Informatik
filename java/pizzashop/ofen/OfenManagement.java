package pizzashop.ofen;

import java.time.LocalDateTime;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import pizzashop.bestellung.PizzenManagement;
import pizzashop.katalog.PizzaListe;

/**
 * Implementierung der Geschäftslogik in Bezug auf {@link Ofen}s.
 *
 * @author Muhammad Fakhruddin
 */
@Service
@Transactional
public class OfenManagement {

	private final OfenRepository ofen;
	private final WartelistenManagement warteliste;
	private final PizzenManagement pizzaManagement;

	private static final Logger LOG = LoggerFactory.getLogger(OfenManagement.class);
	/**
	 * Erzeugt ein neues {@link OfenManagement} mit dem angegebenen {@link OfenRepository},
	 * {@link Wartelistenmanagement} und {@link PizzenManagement}. 
	 * 
	 * @param ofen sollte nicht {@literal null} sein.
	 * @param warteliste sollte nicht {@literal null} sein.
	 * @param pizzaManagement sollte nicht {@literal null} sein.
	 */
	OfenManagement(OfenRepository ofen, WartelistenManagement warteliste, PizzenManagement pizzaManagement){
		Assert.notNull(ofen, "OfenRepository sollte nicht null sein!");
		Assert.notNull(warteliste, "WartelistenManagement sollte nicht null sein!");
		Assert.notNull(pizzaManagement, "PizzenManagement sollte nicht null sein!");
		this.ofen=ofen;
		this.warteliste = warteliste;
		this.pizzaManagement = pizzaManagement;
	}	

	public Ofen createOfen() {
		return ofen.save(new Ofen(true, LocalDateTime.now()));
	}

	public long showOfen() {
		return ofen.count();
	}

	public Ofen getOfen(long i) {
		return ofen.findById(i).get();
	}
	/**
	 * Ändert die Anzahl von verfügbare {@link Ofen} in dem System.
	 * 
	 * @param desiredNumber
	 */
	public void adjustOfen(long desiredNumber) {

		long currentNumber = showOfen();		

		if(currentNumber == desiredNumber) {
			return;
		}

		if(currentNumber < desiredNumber) {
			long difference = desiredNumber - currentNumber;
			for(int i=0; i < difference; i++) {
				createOfen();
			}
		}else { // currentNumber > desiredNumber
			for(Ofen o : findAll()) {
				//if desired number of ofen reached, exit from method
				if(currentNumber == desiredNumber) {
					return;
					//only free oven can be deleted
				}else if(o.isFree()) {
					LOG.info("Ofen Nr. " + o.getId() + " wird gelöscht");
					ofen.delete(o);
					currentNumber--;
				}else {
					LOG.info("Ofen Nr. " + o.getId() + " ist gerade besetzt, kann nicht gelöscht werden");
				}		
			}
		}
	}

	/**
	 * Methode für den Backvorgang, es wird überprüft, ob der Ofen frei ist, 
	 * sollte dies der Fall sein, so wird die gegebene Pizza gebacken.
	 * @param  i ist long.
	 * @param  pizza {@link PizzaListe}
	 *
	 * @return boolean, true wenn backen möglich ist.
	 */
	public boolean backen(long i, PizzaListe pizza) {

		getOfen(i).setTime(LocalDateTime.now());

		ScheduledExecutorService scheduledExecutorService =
				Executors.newSingleThreadScheduledExecutor();

		if(getOfen(i).isFree()) {
			LOG.info("Ofen Nr.: " + (getOfen(i).getId()+ 1));			
			ofen.findById(i).get().setFree(false);
			LOG.info("Pizza ist " + pizza.getBackgrad());
			LOG.info("Pizza wird gebacken.");	

			warteliste.vonWarteliste(pizza);

			scheduledExecutorService.schedule(new Runnable() {
				public void run() {
					pizza.backen();			
					LOG.info("Pizza ist " + pizza.getBackgrad());
					pizzaManagement.deletePizza(pizza);
				}
			},
					15, //baking duration
					TimeUnit.SECONDS);
			return true;
				
		}else {
			LOG.info("Das Ofen Nr. " + getOfen(i).getId() + " ist gerade besetzt");
		}
		return false;
	}

	public Streamable<Ofen> findAll() {
		return ofen.findAll();
	}

	public int getCountUsedOfen(){
		int count = 0;
		for (Ofen ofen: ofen.findAll()){
			if (!ofen.isFree()){
				count += 1;
			}
		}
		return count;
	}

}
