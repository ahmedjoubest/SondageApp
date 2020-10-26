package web.sondage.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Sondage {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String titre;
	private String description;
	@OneToMany(mappedBy = "sondage", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<DateRendezVous> m_Date;
	@OneToMany(mappedBy = "sondage", cascade = CascadeType.ALL)
	@JsonIgnore
	private Set<Lieu> m_Lieu;

	public Sondage() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<DateRendezVous> getM_Date() {
		return m_Date;
	}

	public void setM_Date(Set<DateRendezVous> m_Date) {
		this.m_Date = m_Date;
	}

	public Set<Lieu> getM_Lieu() {
		return m_Lieu;
	}

	public void setM_Lieu(Set<Lieu> m_Lieu) {
		this.m_Lieu = m_Lieu;
	}

	public void finalize() throws Throwable {

	}

}
