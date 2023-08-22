package pizzashop.personal;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.salespointframework.useraccount.UserAccount;

@Entity
public class Personal {

	private @Id @GeneratedValue long id;

	@OneToOne(cascade = CascadeType.ALL) //
	private UserAccount userAccount;
	
	private String job;

	@SuppressWarnings("unused")
	private Personal() {}

	
	public Personal(UserAccount userAccount, String job) {
		this.userAccount = userAccount;
		this.job = job;
	}

	public long getId() {
		return id;
	}
	
	public String getJob() {
		return job;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}
}
