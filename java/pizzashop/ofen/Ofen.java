package pizzashop.ofen;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Ofen {
	
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) long id;
	private boolean free;
	private LocalDateTime startTime;

	@SuppressWarnings("unused")
	private Ofen() {}
	
	public Ofen(boolean free, LocalDateTime startTime) {
		this.free= free;
		this.startTime = startTime;
	}
	
	public long getId() {
		return id;
	}
	
	public void setFree(boolean free) {
		 this.free = free;		 
	}
	
	public boolean isFree() {
		return free;
	}
	
	public void setTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	
	public LocalDateTime getTime() {
		return startTime;
	}
}
