package esiea.ds.sondage.message.request;

import esiea.ds.sondage.model.Vote;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

public class SondageForm {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotBlank
    @Size(min = 3, max = 50)
    private String titre;

    @NotBlank
    @Size(min = 3, max = 50)
    private String description;
    
    private Set<String> dates;
    private Set<String> lieux;

    private List<Integer> datesNbr;
    private List<Integer> lieuxNbr;

    public List<Integer> getDatesNbr() {
        return datesNbr;
    }

    public void setDatesNbr(List<Integer> datesNbr) {
        this.datesNbr = datesNbr;
    }

    public List<Integer> getLieuxNbr() {
        return lieuxNbr;
    }

    public void setLieuxNbr(List<Integer> lieuxNbr) {
        this.lieuxNbr = lieuxNbr;
    }

    public Set<VoteForm> getVotes() {
        return votes;
    }

    public void setVotes(Set<VoteForm> votes) {
        this.votes = votes;
    }

    private Set<VoteForm> votes;

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

    public Set<String> getDates() {
        return dates;
    }

    public void setDates(Set<String> dates) {
        this.dates = dates;
    }

    public Set<String> getLieux() {
        return lieux;
    }

    public void setLieux(Set<String> lieux) {
        this.lieux = lieux;
    }

    @Override
    public String toString() {
        return "SondageForm{" +
                "id=" + id +
                ", titre='" + titre + '\'' +
                ", description='" + description + '\'' +
                ", dates=" + dates +
                ", lieux=" + lieux +
                '}';
    }
}
