package groupware.simplemeetup.enumerations;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {
    BACKEND("백엔드"),
    FRONTEND("프론트엔드"),
    MACHINELEARNING("머신러닝"),
    MOBILE("모바일"),
    GAME("게임");

    private final String value;
}
