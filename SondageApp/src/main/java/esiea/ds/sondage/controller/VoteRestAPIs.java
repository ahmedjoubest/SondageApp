package esiea.ds.sondage.controller;

import esiea.ds.sondage.message.request.VoteForm;
import esiea.ds.sondage.model.DateRendezVous;
import esiea.ds.sondage.model.Lieu;
import esiea.ds.sondage.model.Sondage;
import esiea.ds.sondage.model.User;
import esiea.ds.sondage.model.Vote;
import esiea.ds.sondage.repository.DateRendezVousRepository;
import esiea.ds.sondage.repository.LieuRepository;
import esiea.ds.sondage.repository.SondageRepository;
import esiea.ds.sondage.repository.UserRepository;
import esiea.ds.sondage.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class VoteRestAPIs {

	@Autowired
	SondageRepository sondageRepository;
	@Autowired
	VoteRepository voteRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	DateRendezVousRepository dateRendezVousRepository;
	@Autowired
	LieuRepository lieuRepository;

	@RequestMapping(value="/api/vote/find/{username}", method = RequestMethod.GET)
	public List<VoteForm> listVotes(@PathVariable String username){
		List<VoteForm> votes = new ArrayList<>();

		voteRepository.findAll().forEach(vote -> {
			if (vote.getUser().getUsername().equals(username)){
				VoteForm v = new VoteForm();
				v.setDate(vote.getDate());
				v.setLieu(vote.getLieu());
				v.setSondage(vote.getSondage().getId());
				v.setSondageTitre(vote.getSondage().getTitre());

				votes.add(v);
			}
		});
		return votes;
	}

	@RequestMapping(value="/api/vote/update", method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	public void updateVote(@RequestBody VoteForm vote) {
		System.out.println(vote);

		Sondage sondage = sondageRepository.findById(vote.getSondage()).orElse(null);

		User user = userRepository.findByUsername(vote.getUser()).orElse(null);


		Vote sd = voteRepository.findByUserAndSondage(user,sondage).orElse(null);

		if(sd != null)
		{
			sd.setDate(vote.getDate());
			sd.setLieu(vote.getLieu());
		}

		voteRepository.save(sd);
	}

	@RequestMapping(value="/api/sondage/vote", method = RequestMethod.POST)
	@PreAuthorize("hasRole('USER')")
	public Vote voteSondage(@RequestBody VoteForm vote) {
		System.out.println(vote);

		Sondage sondage = sondageRepository.findById(vote.getSondage()).orElse(null);

		User user = userRepository.findByUsername(vote.getUser()).orElse(null);


		Vote sd = voteRepository.findByUserAndSondage(user,sondage).orElse(null);

		if(sd != null)
			return null;

		sd = new Vote();
		sd.setUser(user);
		sd.setSondage(sondage);
		sd.setDate(vote.getDate());
		sd.setLieu(vote.getLieu());

		System.out.println(sd);

		 return voteRepository.save(sd);
	}

}
