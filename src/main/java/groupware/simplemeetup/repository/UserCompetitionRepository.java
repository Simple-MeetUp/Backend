package groupware.simplemeetup.repository;

import groupware.simplemeetup.entity.User;
import groupware.simplemeetup.entity.UserCompetition;
import groupware.simplemeetup.enumerations.CompetitionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserCompetitionRepository extends JpaRepository<UserCompetition, Long> {

    List<UserCompetition> findAllByUser(User user);

    List<UserCompetition> findAllByUserAndStatus(User user, CompetitionStatus status);

    List<UserCompetition> findAllByUserAndStatusAndCompletionDate(User user, CompetitionStatus status, LocalDate completionDate);
}
