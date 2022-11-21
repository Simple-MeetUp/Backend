package groupware.simplemeetup.dto;

import lombok.Getter;

@Getter
public class SubmitRequest {

    private String title;

    private String confirmPhoto; // 사진 받을건지 의문
}
