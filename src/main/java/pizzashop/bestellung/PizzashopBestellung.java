
package pizzashop.bestellung;


import org.salespointframework.catalog.Product;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderLine;
import org.salespointframework.payment.PaymentMethod;
import org.salespointframework.useraccount.UserAccount;

import pizzashop.katalog.BestellKatalog;
import pizzashop.katalog.Bestelleinheit;
import pizzashop.katalog.PizzaListe;

import javax.persistence.*;

import java.util.ArrayList;

/**
 * 
 * Erbt von Order und fügt zusätzliche Methoden hinzu.
 *
 */
@Entity
public class PizzashopBestellung extends Order{

	public  enum OrderType {
		DELIVERY, PICKUP
	}

	private String lieferbotenId = "";
	
	private OrderType orderType;

	private ArrayList<String> kundenDaten;

	@SuppressWarnings({ "unused", "deprecation" })
	public PizzashopBestellung() {}

	public PizzashopBestellung(UserAccount userAccount, PaymentMethod paymentMethod,
							   OrderType orderType, ArrayList<String> kundenDaten) {
		super(userAccount, paymentMethod);
		this.orderType = orderType;
		this.kundenDaten = kundenDaten;
	}

	public ArrayList<String> getKundenDaten(){return kundenDaten;}

	public OrderType getOrderType() {
		return orderType;
	}

	public void setLieferbotenId(String lieferbotenId) {
		this.lieferbotenId = lieferbotenId;
	}

	public String  getLieferbotenId() {
		return this.lieferbotenId;
	}

	public String getPaymentMethodString(){
		if (this.getPaymentMethod().toString().equals("Cash()")){
			return "Bar";
		}
		return "Kreditkarte";
	}
	
}
