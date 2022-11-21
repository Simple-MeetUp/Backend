package groupware.simplemeetup.entity;

import groupware.simplemeetup.enumerations.CompetitionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class UserCompetition {

    @Id
    @GeneratedValue
    @Column(name = "user_competition_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "competition_id")
    private Competition competition;

    @Setter
    private CompetitionStatus status;

    @Setter
    private LocalDate completionDate;

    @Builder
    public UserCompetition(User user, Competition competition, CompetitionStatus status) {
        this.user = user;
        this.competition = competition;
        this.status = status;
    }
}