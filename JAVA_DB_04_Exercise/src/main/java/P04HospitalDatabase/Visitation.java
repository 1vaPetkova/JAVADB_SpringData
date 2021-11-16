package P04HospitalDatabase;

import P01Gringotts.entities.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "visitations")
public class Visitation extends BaseEntity {

    private Date visitationDate;
    private String comment;

    private Patient patient;



    public Visitation() {
    }

    @Column(name = "visitation_date")
    public Date getVisitationDate() {
        return visitationDate;
    }

    public void setVisitationDate(Date date) {
        this.visitationDate = date;
    }

    @Column(name = "visitation_comment")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @ManyToOne
    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }
}
