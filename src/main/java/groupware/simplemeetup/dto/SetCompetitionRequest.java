package groupware.simplemeetup.dto;

import groupware.simplemeetup.enumerations.Category;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class SetCompetitionRequest {

    private String title;

    private String contents;

    @Enumerated(EnumType.STRING)
    private Category category;

    private Long personnelLowerBound;

    private Long personnelUpperBound;

    private LocalDate activityDurationFrom;

    private LocalDate activityDurationTo;

    private LocalDate enrollDurationFrom;

    private LocalDate enrollDurationTo;
}
