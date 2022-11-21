package groupware.simplemeetup.controller;

import groupware.simplemeetup.dto.EmailRequest;
import groupware.simplemeetup.dto.LogInRequest;
import groupware.simplemeetup.dto.SignUpRequest;
import groupware.simplemeetup.dto.StringResponse;
import groupware.simplemeetup.dto.UserResponse;
import groupware.simplemeetup.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/exists")
    public ResponseEntity<StringResponse> checkEmailAvailability(@RequestBody EmailRequest emailRequest) {
        return ResponseEntity.ok().body(userService.checkEmailAvailability(emailRequest));
    }

    @PostMapping("/nickname")
    public ResponseEntity<StringResponse> checkNicknameAvailability(@RequestParam("nickname") String nickname) {
        return ResponseEntity.ok().body(userService.checkNicknameAvailability(nickname));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok().body(userService.signUp(signUpRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> logIn(@RequestBody LogInRequest logInRequest) {
        return ResponseEntity.ok().body(userService.logIn(logInRequest));
    }

    @PostMapping("/reset")
    public ResponseEntity<StringResponse> resetPassword(@Valid @RequestBody EmailRequest emailRequest) {
        return ResponseEntity.ok().body(userService.resetPassword(emailRequest));
    }
}
