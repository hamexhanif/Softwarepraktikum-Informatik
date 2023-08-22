package pizzashop.ofen;

import org.salespointframework.catalog.ProductIdentifier;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class BackForm {

	@NotNull
	private ProductIdentifier pizzaId;

	@NotNull
	private long ofenId;
	
	public void setPizzaId(ProductIdentifier pizzaId) {
		this.pizzaId = pizzaId;
	}
	
	public void setOfenId(long ofenId) {
		this.ofenId = ofenId;
	}
	
	public ProductIdentifier getPizzaId() {
		return pizzaId;
	}
	
	public long getOfenId() {
		return ofenId;
	}

}
