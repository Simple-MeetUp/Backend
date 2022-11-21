package groupware.simplemeetup.entity;

import groupware.simplemeetup.enumerations.Category;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class Competition {

    @Id
    @GeneratedValue
    @Column(name = "competition_id")
    private Long id;

    private String title;

    private String contents;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Setter
    private Long personnelLowerBound;

    @Setter
    private Long personnelUpperBound;

    private LocalDate activityDurationFrom;

    private LocalDate activityDurationTo;

    private LocalDate enrollDurationFrom;

    private LocalDate enrollDurationTo;
}