package esiea.ds.sondage.model;

import javax.persistence.*;

@Entity
public class Lieu {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nom;
	private String ville;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sondage")
	private Sondage sondage;

	public Lieu(){

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
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
