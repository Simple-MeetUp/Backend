package groupware.simplemeetup.dto;

import groupware.simplemeetup.enumerations.Category;
import groupware.simplemeetup.enumerations.CompetitionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionResponse {

    private Long competitionId;

    private CompetitionStatus status;

    private String title;

    private String contents;

    private Category category;

    private Long personnelLowerBound;

    private Long personnelUpperBound;

    private LocalDate activityDurationFrom;

    private LocalDate activityDurationTo;

    private LocalDate enrollDurationFrom;

    private LocalDate enrollDurationTo;
}
