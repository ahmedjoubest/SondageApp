package esiea.ds.sondage.message.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class VoteForm {

    @NotBlank
    @Size(min = 3, max = 50)
    private Long sondage;

    @NotBlank
    @Size(min = 3, max = 50)
    private String user;
    
    private String date;
    private String lieu;

    public Long getSondage() {
        return sondage;
    }

    public void setSondage(Long sondage) {
        this.sondage = sondage;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    @Override
    public String toString() {
        return "VoteForm{" +
                ", sondage=" + sondage +
                ", user='" + user + '\'' +
                ", date='" + date + '\'' +
                ", lieu='" + lieu + '\'' +
                '}';
    }
}
