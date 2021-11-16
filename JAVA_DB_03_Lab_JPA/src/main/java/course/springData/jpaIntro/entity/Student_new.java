package course.springData.jpaIntro.entity;


import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor

public class Student_new {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    @Column(name = "name")
    private String name;
    @Column(name = "registration_date")
    private Date registrationDate = new Date();



}
