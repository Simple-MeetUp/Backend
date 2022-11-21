package groupware.simplemeetup.repository;

import groupware.simplemeetup.entity.Competition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CompetitionRepository extends JpaRepository<Competition, Long> {

//    List<Competition> findAllByStartTimeIsBeforeAndEndTimeIsAfter(LocalDate today1, LocalDate today2);
//
//    List<Competition> findAllByIdIsNotInAndStartTimeIsBeforeAndEndTimeIsAfter(List<Long> MissionIds, LocalDate today1, LocalDate today2);

    List<Competition> findAllByIdIsNotIn(List<Long> MissionIds);


}
