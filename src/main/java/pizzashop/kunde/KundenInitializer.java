package pizzashop.kunde;

import java.util.List;
import org.salespointframework.core.DataInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
@Order(1)
class KundenInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(KundenInitializer.class);
	private final KundenManagement kundenManagement;

	/**
	 * erstellt einen neuen {@link KundenInitializer} mit gegebenen {@link KundenEinträge}.
	 * 
	 * @param kundenManagement sollte nicht {@literal null} sein
	 */
	
	KundenInitializer(KundenManagement kundenManagement) {

		Assert.notNull(kundenManagement, "KundenRepository must not be null!");		

		this.kundenManagement = kundenManagement;
	}

	@Override
	public void initialize() {


		LOG.info("Creating default users and customers.");

		List.of(
				new Registrierungsform("Max", "Muster", "Straße 1", "0123456789"),
				new Registrierungsform("Josefine", "Schmidt", "Straße 2", "6574834765"),
				new Registrierungsform("Sarah", "Müller", "Straße 3", "78923098756"),
				new Registrierungsform("Paul", "Meier", "Straße 4", "75829658")
		).forEach(kundenManagement::createKunden);
	}
}
