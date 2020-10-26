package web.sondage.message.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class SondageForm {
    @NotBlank
    @Size(min = 3, max = 50)
    private String titre;

    @NotBlank
    @Size(min = 3, max = 50)
    private String description;
    
    private Set<String> dates;
    private Set<String> lieux;

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
}
