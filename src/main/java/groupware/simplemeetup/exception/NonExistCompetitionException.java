package groupware.simplemeetup.exception;

import org.springframework.http.HttpStatus;

public class NonExistCompetitionException extends BaseException {
    private final static String message = "해당하는 공모전이 존재하지 않습니다";

    private final static HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;

    public NonExistCompetitionException() {
        super(message, httpStatus);
    }
}
