package web.sondage.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class DateRendezVous {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String date;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sondage")
	private Sondage sondage;

	public DateRendezVous(){

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Sondage getSondage() {
		return sondage;
	}

	public void setSondage(Sondage sondage) {
		this.sondage = sondage;
	}

	public void finalize() throws Throwable {

	}

}
