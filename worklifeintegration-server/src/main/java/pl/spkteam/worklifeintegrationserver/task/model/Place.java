package pl.spkteam.worklifeintegrationserver.task.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

//import java.util.Date;

@Data
@Entity
public class Place {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private long transportTimeMinutes;
    //private Date openingTime;
    //private Date closingTime;
}

//nie wiem czy wchodzic tak gleboko ze dodacczas otwarcia i zamkniecia
