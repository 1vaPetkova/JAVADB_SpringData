package domain.entities;


import domain.enums.Prediction;

import javax.persistence.*;

@Entity
@Table(name = "result_predictions")
public class ResultPrediction extends BaseEntity {

    private Prediction prediction;


    @Enumerated(EnumType.STRING)
    @Column(name = "predictions")
    public Prediction getPrediction() {
        return prediction;
    }

    public void setPrediction(Prediction prediction) {
        this.prediction = prediction;
    }
}
