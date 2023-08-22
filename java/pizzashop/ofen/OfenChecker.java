package pizzashop.ofen;

import java.time.LocalDateTime;

import javax.transaction.Transactional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
/**
 * Prüft, ob ein Ofen schon fertig beim Backen ist
 * 
 * @author Muhammad Fakhruddin
 */
@Component
class OfenChecker {
	
	private final OfenRepository ofenRepository;
	/**
	 * Erzeugt ein neues {@link OfenChecket} mit dem angegebenen {@link OfenRepository}.
	 * 
	 * @param ofenRepopsitory sollte nicht {@literal null} sein.
	 */
	public OfenChecker(OfenRepository ofenRepopsitory){
		
		Assert.notNull(ofenRepopsitory, "OfenRepository sollte nicht null sein!");
		
		this.ofenRepository = ofenRepopsitory;
	}
	/**
	 * Prüft jede Sekunde, ob genutzte {@link Ofen} die Backzeit überschritten haben,
	 * falls ja, setzt deren Status frei zurück.
	 */
	@Transactional
	@Scheduled(fixedRate = 1000)
	public void checkOfen() {
		LocalDateTime now = LocalDateTime.now();
		for(Ofen ofen : ofenRepository.findByFree(false)) {
			if(now.isAfter(ofen.getTime().plusSeconds(15))) {
				ofen.setFree(true);
				ofenRepository.save(ofen);
			}
		}
	}
	
}
