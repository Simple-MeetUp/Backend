package groupware.simplemeetup.dto;


import lombok.Getter;

@Getter
public class LogInRequest {

    private String email;

    private String password;
}
