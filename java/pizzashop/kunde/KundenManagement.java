package pizzashop.kunde;

import org.salespointframework.useraccount.Role;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@Transactional
public class KundenManagement {
	
	public static final Role CUSTOMER_ROLE = Role.of("CUSTOMER");
	
	private final KundenEinträge kunden;
	KundenManagement(KundenEinträge kunden) {
		
		Assert.notNull(kunden, "KundenEinträge must not be null!");	
		
		this.kunden = kunden;
	}

	/**
	 * erstellt einen neuen {@link Kunde} anhand der {@link Registrierungsform}
	 * 
	 * @param form wird geprüft ob alle Eingaben valide sind
	 * @return speichert einen neuen Kunden in die {@link KundenEinträge}
	 */
	
	public Kunde createKunden(Registrierungsform form) {
		
		Assert.notNull(form, "Registrierungsform must not be null!");

		return kunden.save(new Kunde( form.getVorname(), form.getNachname(), form.getAdresse(), form.getTelefon()));
		
	} 
		
	public Streamable <Kunde> findAll() {
		return kunden.findAll();
	}

	public Kunde findById(long id) {
		return kunden.findById(id).get();
	}

	public void updateTan(Kunde kunde){
		kunde.newTan();
		kunden.save(kunde);
	}
	
	public void deleteKunde (long userId) {
		if (kunden.findById(userId).isPresent()){
			kunden.deleteById(userId);
		}
	}
}

