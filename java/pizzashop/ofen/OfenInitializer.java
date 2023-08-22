package pizzashop.ofen;

import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * Initialisiert die Erstellung von Ofen
 * 
 * @author Muhammad Fakhruddin
 */
@Component
@Order(10)
class OfenInitializer implements DataInitializer{
	
private static final Logger LOG = LoggerFactory.getLogger(OfenInitializer.class);
	
	private final OfenManagement ofenManagement;
	/**
	 * Erstellt einen neuen {@link OfenDataInitializer} mit gegebenen {@link OfenManagement}.
	 *
	 * @param ofenManagement sollte nicht {@literal null} sein.
	 */
	public OfenInitializer(OfenManagement ofenManagement){
		
		Assert.notNull(ofenManagement, "OfenManagement sollte nicht null sein!");
		
		this.ofenManagement = ofenManagement;
	}
	
	@Override
	public void initialize() {
		
		LOG.info("Erstellt 5 Standardofen.");
		
		for(int i=0;i < 5;i++) {
			ofenManagement.createOfen();
		}
	}
}
