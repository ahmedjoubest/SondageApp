package esiea.ds.sondage.controller;

import esiea.ds.sondage.message.request.VoteForm;
import esiea.ds.sondage.model.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import esiea.ds.sondage.message.request.SondageForm;
import esiea.ds.sondage.model.DateRendezVous;
import esiea.ds.sondage.model.Lieu;
import esiea.ds.sondage.model.Sondage;
import esiea.ds.sondage.repository.DateRendezVousRepository;
import esiea.ds.sondage.repository.LieuRepository;
import esiea.ds.sondage.repository.SondageRepository;

import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class SondageRestAPIs {

	@Autowired
	SondageRepository sondageRepository;
	@Autowired
	DateRendezVousRepository dateRendezVousRepository;
	@Autowired
	LieuRepository lieuRepository;

	@RequestMapping(value="/api/sondage/find")
	public List<SondageForm> listSondages(){
		List<SondageForm> sondages = new ArrayList<>();
		sondageRepository.findAll().forEach(sondage -> {
			SondageForm s = new SondageForm();
			s.setId(sondage.getId());
			s.setTitre(sondage.getTitre());
			s.setDescription(sondage.getDescription());
			Set<String> dates = new HashSet<>();
			sondage.getM_Date().forEach(d -> {
				dates.add(d.getDate());
			});
			s.setDates(dates);
			Set<String> lieux = new HashSet<>();
			sondage.getM_Lieu().forEach(l->{
				lieux.add(l.getNom());
			});
			s.setLieux(lieux);
			Set<VoteForm> votes = new HashSet<>();
			sondage.getVotes().forEach(l->{
				votes.add(new VoteForm(l.getSondage().getId(),l.getSondage().getTitre(),l.getUser().getUsername(),l.getDate(),l.getLieu()));
			});
			s.setVotes(votes);
			sondages.add(s);
		});
		return sondages;
	}

	@RequestMapping(value="/api/sondage/add", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ADMIN')")
	public Sondage saveSondage(@RequestBody SondageForm sondage) {
		System.out.println(sondage);

		Sondage sondage1 = sondageRepository.findByTitre(sondage.getTitre()).orElse(null);
		if (sondage1 != null) {
			return sondage1;
		}

		sondage1 = new Sondage();

		sondage1.setTitre(sondage.getTitre());
		sondage1.setDescription(sondage.getDescription());

		Sondage newSondage = sondageRepository.save(sondage1);

		sondage.getDates().forEach(d->{
			DateRendezVous newDate = new DateRendezVous();
			newDate.setDate(d);
			newDate.setSondage(newSondage);
			dateRendezVousRepository.save(newDate);
		});

		sondage.getLieux().forEach(l->{
			Lieu newLieu = new Lieu();
			newLieu.setNom(l);
			newLieu.setSondage(newSondage);
			lieuRepository.save(newLieu);
		});

		System.out.println(sondage);

		return newSondage;
	}

	@RequestMapping(value="/api/sondage/update/{id}", method = RequestMethod.PUT)
	@PreAuthorize("hasRole('ADMIN')")
	public Sondage updateSondage(@RequestBody SondageForm sondage, @PathVariable long id) {
		System.out.println(sondage);

		Optional<Sondage> existDR = sondageRepository.findById(id);

		if (!existDR.isPresent())
			return null;

		Sondage sd = sondageRepository.findByTitre(sondage.getTitre() ).orElse(null);
		if (sd!=null && sd.getId()!=id) {
			return null;
		}

		sd = new Sondage();
		sd.setId(id);
		sd.setTitre(sondage.getTitre());
		sd.setDescription(sondage.getDescription());

		Sondage newSondage = sondageRepository.save(sd);

		dateRendezVousRepository.findAll().forEach(dateRendezVous -> {
			if(dateRendezVous.getSondage().getId() == id && !sondage.getDates().contains(dateRendezVous.getDate()))
				dateRendezVousRepository.delete(dateRendezVous);
		});
		sondage.getDates().forEach(d->{
			DateRendezVous newDate = new DateRendezVous();
			newDate.setDate(d);
			newDate.setSondage(newSondage);
			dateRendezVousRepository.save(newDate);
		});

		lieuRepository.findAll().forEach(lieu -> {
			if(lieu.getSondage().getId() == id && !sondage.getLieux().contains(lieu.getNom()))
				lieuRepository.delete(lieu);
		});
		sondage.getLieux().forEach(l->{
			Lieu newLieu = new Lieu();
			newLieu.setNom(l);
			newLieu.setSondage(newSondage);
			lieuRepository.save(newLieu);
		});

		return sondageRepository.save(sd);
	}

	@RequestMapping(value="/api/sondage/delete/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasRole('ADMIN')")
	public void deleteSondage(@PathVariable("id") Long id) {
		if(sondageRepository.existsById(id))
			sondageRepository.deleteById(id);
	}
}
