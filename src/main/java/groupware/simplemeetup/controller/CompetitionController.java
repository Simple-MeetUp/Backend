package groupware.simplemeetup.controller;

import groupware.simplemeetup.dto.CompetitionResponse;
import groupware.simplemeetup.dto.PageCompetitionResponse;
import groupware.simplemeetup.dto.SetCompetitionRequest;
import groupware.simplemeetup.entity.User;
import groupware.simplemeetup.enumerations.CompetitionStatus;
import groupware.simplemeetup.service.AuthService;
import groupware.simplemeetup.service.CompetitionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/competition")
public class CompetitionController {

    private final AuthService authService;
    private final CompetitionService competitionService;

    @GetMapping("/get")
    public ResponseEntity<PageCompetitionResponse> get(
            @RequestParam("status")
            CompetitionStatus status, HttpServletRequest httpServletRequest) {

        final User user = authService.getUserByToken(httpServletRequest);
        PageCompetitionResponse pageCompetitionResponse;

        if (status.equals(CompetitionStatus.AVAILABLE))
            pageCompetitionResponse = competitionService.getAvailable(user);
        else if (status.equals(CompetitionStatus.ONGOING))
            pageCompetitionResponse = competitionService.getOngoing(user);
        else
            pageCompetitionResponse = competitionService.getDone(user);

        return ResponseEntity.status(HttpStatus.OK)
                .body(pageCompetitionResponse);
    }

    @PutMapping("join")
    public ResponseEntity<CompetitionResponse> join(
            @RequestParam("mission") Long missionId,
            HttpServletRequest httpServletRequest) {

        final User user = authService.getUserByToken(httpServletRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(competitionService.join(missionId, user));
    }

    @DeleteMapping("cancel")
    public ResponseEntity<CompetitionResponse> cancel(
            @RequestParam("mission") Long missionId,
            HttpServletRequest httpServletRequest) {

        final User user = authService.getUserByToken(httpServletRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(competitionService.cancel(missionId, user));
    }

    @PostMapping("submit")
    public ResponseEntity<CompetitionResponse> submit(
            @RequestParam("mission") Long missionId,
            HttpServletRequest httpServletRequest) {

        final User user = authService.getUserByToken(httpServletRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(competitionService.submit(missionId, user));
    }

    /////////////////////////////// for manager ///////////////////////////////
    @GetMapping("/zget")
    public ResponseEntity<PageCompetitionResponse> zget() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(competitionService.zget());
    }

    @PostMapping("/set")
    public ResponseEntity<CompetitionResponse> set(@RequestBody SetCompetitionRequest setCompetitionRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(competitionService.set(setCompetitionRequest));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> delete(@RequestParam Long missionId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(competitionService.delete(missionId));
    }

    @PutMapping("/zmake")
    public ResponseEntity<Boolean> zmake() {
        return ResponseEntity.ok().body(competitionService.zmake());
    }
    /////////////////////////////// for manager ///////////////////////////////
}
