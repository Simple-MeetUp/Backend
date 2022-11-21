package groupware.simplemeetup.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageCompetitionResponse {

    private Long userId;

    private List<CompetitionResponse> competitions = new ArrayList<>();
}
