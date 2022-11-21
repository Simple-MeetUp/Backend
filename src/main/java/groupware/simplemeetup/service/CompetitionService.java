package groupware.simplemeetup.service;

import groupware.simplemeetup.entity.Competition;
import groupware.simplemeetup.entity.User;
import groupware.simplemeetup.entity.UserCompetition;
import groupware.simplemeetup.enumerations.Category;
import groupware.simplemeetup.enumerations.CompetitionStatus;
import groupware.simplemeetup.exception.NonExistCompetitionException;
import groupware.simplemeetup.repository.CompetitionRepository;
import groupware.simplemeetup.repository.UserCompetitionRepository;
import groupware.simplemeetup.repository.UserRepository;
import groupware.simplemeetup.dto.CompetitionResponse;
import groupware.simplemeetup.dto.PageCompetitionResponse;
import groupware.simplemeetup.dto.SetCompetitionRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompetitionService {

    private final CompetitionRepository competitionRepository;
    private final UserCompetitionRepository userCompetitionRepository;
    private final UserRepository userRepository;

    public PageCompetitionResponse getAvailable(User user) {
        List<Competition> competitions;

        List<UserCompetition> userCompetitions = userCompetitionRepository.findAllByUser(user);
        if (userCompetitions.isEmpty()) {
            competitions = competitionRepository.findAll();
        } else {
            List<Long> competitionIds = userCompetitionRepository.findAllByUser(user)
                .stream().map(userCompetition -> userCompetition.getCompetition().getId())
                .collect(Collectors.toList());
            competitions = competitionRepository.findAllByIdIsNotIn(competitionIds);
        }

        List<CompetitionResponse> competitionResponses = new ArrayList<>();
        for (Competition competition : competitions) {
            competitionResponses.add(new CompetitionResponse(
                competition.getId(),
                CompetitionStatus.AVAILABLE,
                competition.getTitle(),
                competition.getContents(),
                competition.getCategory(),
                competition.getPersonnelLowerBound(),
                competition.getPersonnelUpperBound(),
                competition.getActivityDurationFrom(),
                competition.getActivityDurationTo(),
                competition.getEnrollDurationFrom(),
                competition.getEnrollDurationTo()));
        }

        return new PageCompetitionResponse(user.getId(), competitionResponses);
    }

    public PageCompetitionResponse getOngoing(User user) {
        final LocalDate today = LocalDate.now();

        List<UserCompetition> goingMissions = userCompetitionRepository.findAllByUserAndStatus(user,
            CompetitionStatus.ONGOING);
        PageCompetitionResponse pageCompetitionResponse = new PageCompetitionResponse();
        pageCompetitionResponse.setUserId(user.getId());

        List<CompetitionResponse> competitionResponses = new ArrayList<>();

        for (UserCompetition userCompetition : goingMissions) {
            System.out.println(userCompetition.getId().toString());
            Competition competition = userCompetition.getCompetition();

            if (competition.getEnrollDurationTo().isAfter(today)) {
                competitionResponses.add(CompetitionResponse.builder()
                    .competitionId(competition.getId())
                    .status(CompetitionStatus.ONGOING)
                    .title(competition.getTitle())
                    .contents(competition.getContents())
                    .category(competition.getCategory())
                    .personnelLowerBound(competition.getPersonnelLowerBound())
                    .personnelUpperBound(competition.getPersonnelUpperBound())
                    .enrollDurationFrom(competition.getEnrollDurationFrom())
                    .enrollDurationTo(competition.getEnrollDurationTo()).build());
            }
        }
        pageCompetitionResponse.setCompetitions(competitionResponses);

        return pageCompetitionResponse;
    }

    public PageCompetitionResponse getDone(User user) {
        List<UserCompetition> doneMissions = userCompetitionRepository.findAllByUserAndStatus(user,
            CompetitionStatus.DONE);
        PageCompetitionResponse pageCompetitionResponse = new PageCompetitionResponse();
        pageCompetitionResponse.setUserId(user.getId());

        List<CompetitionResponse> competitionResponses = new ArrayList<>();

        for (UserCompetition userCompetition : doneMissions) {
            Competition competition = userCompetition.getCompetition();

            competitionResponses.add(CompetitionResponse.builder()
                .competitionId(competition.getId())
                .status(CompetitionStatus.DONE)
                .title(competition.getTitle())
                .contents(competition.getContents())
                .category(competition.getCategory())
                .personnelLowerBound(competition.getPersonnelLowerBound())
                .personnelUpperBound(competition.getPersonnelUpperBound())
                .enrollDurationFrom(competition.getEnrollDurationFrom())
                .enrollDurationTo(competition.getEnrollDurationTo()).build());
        }
        pageCompetitionResponse.setCompetitions(competitionResponses);

        return pageCompetitionResponse;
    }

    @Transactional
    public CompetitionResponse join(Long competitionId, User user) {
        System.out.println("오류1");
        Competition competition = competitionRepository.getById(competitionId);
        System.out.println("오류2");
        final UserCompetition savedUserCompetition = userCompetitionRepository.save(
            new UserCompetition(user, competition, CompetitionStatus.ONGOING));

        final Competition savedCompetition = competitionRepository.save(competition);

        return CompetitionResponse.builder()
            .competitionId(savedCompetition.getId())
            .status(savedUserCompetition.getStatus())
            .title(savedCompetition.getTitle())
            .contents(savedCompetition.getContents())
            .category(savedCompetition.getCategory())
            .personnelLowerBound(savedCompetition.getPersonnelLowerBound())
            .personnelUpperBound(savedCompetition.getPersonnelUpperBound())
            .enrollDurationFrom(savedCompetition.getEnrollDurationFrom())
            .enrollDurationTo(savedCompetition.getEnrollDurationTo()).build();
    }

    @Transactional
    public CompetitionResponse cancel(Long competitionId, User user) {
        Competition competition = competitionRepository.getById(competitionId);

        userCompetitionRepository.delete(findByCompetition(user, competition));

        final Competition savedCompetition = competitionRepository.save(competition);

        return CompetitionResponse.builder()
            .competitionId(savedCompetition.getId())
            .status(CompetitionStatus.NONE)
            .title(savedCompetition.getTitle())
            .contents(savedCompetition.getContents())
            .category(savedCompetition.getCategory())
            .personnelLowerBound(savedCompetition.getPersonnelLowerBound())
            .personnelUpperBound(savedCompetition.getPersonnelUpperBound())
            .enrollDurationFrom(savedCompetition.getEnrollDurationFrom())
            .enrollDurationTo(savedCompetition.getEnrollDurationTo()).build();
    }

    @Transactional
    public CompetitionResponse submit(Long competitionId, User user) {
        final Competition competition = competitionRepository.getById(competitionId);

        UserCompetition userCompetition = findByCompetition(user, competition);
        userCompetition.setStatus(CompetitionStatus.DONE);
        userCompetition.setCompletionDate(LocalDate.now());
        userCompetitionRepository.save(userCompetition);

        userRepository.save(user);

        return CompetitionResponse.builder()
            .competitionId(competition.getId())
            .status(userCompetition.getStatus())
            .title(competition.getTitle())
            .contents(competition.getContents())
            .category(competition.getCategory())
            .personnelLowerBound(competition.getPersonnelLowerBound())
            .personnelUpperBound(competition.getPersonnelUpperBound())
            .enrollDurationFrom(competition.getEnrollDurationFrom())
            .enrollDurationTo(competition.getEnrollDurationTo()).build();
    }

    private UserCompetition findByCompetition(User user, Competition competition) {

        for (UserCompetition userCompetition : user.getCompetitions()) {
            if (userCompetition.getCompetition().equals(competition)) {
                return userCompetition;
            }
        }
        throw new NonExistCompetitionException();
    }

    public PageCompetitionResponse zget() {
        List<CompetitionResponse> msr = new ArrayList<>();

        List<Competition> competitions = competitionRepository.findAll();

        for (Competition competition : competitions) {
            msr.add(CompetitionResponse.builder()
                .competitionId(competition.getId())
                .status(CompetitionStatus.NONE)
                .title(competition.getTitle())
                .contents(competition.getContents())
                .category(competition.getCategory())
                .personnelLowerBound(competition.getPersonnelLowerBound())
                .personnelUpperBound(competition.getPersonnelUpperBound())
                .enrollDurationFrom(competition.getEnrollDurationFrom())
                .enrollDurationTo(competition.getEnrollDurationTo()).build());
        }
        return PageCompetitionResponse.builder().competitions(msr).build();

    }


    public CompetitionResponse set(SetCompetitionRequest setCompetitionRequest) {
        final Competition competition = Competition.builder()
            .title(setCompetitionRequest.getTitle())
            .contents(setCompetitionRequest.getContents())
            .category(setCompetitionRequest.getCategory())
            .personnelLowerBound(setCompetitionRequest.getPersonnelLowerBound())
            .personnelUpperBound(setCompetitionRequest.getPersonnelUpperBound())
            .activityDurationFrom(setCompetitionRequest.getActivityDurationFrom())
            .activityDurationTo(setCompetitionRequest.getActivityDurationTo())
            .enrollDurationFrom(setCompetitionRequest.getEnrollDurationFrom())
            .enrollDurationTo(setCompetitionRequest.getEnrollDurationTo()).build();

        final Competition savedCompetition = competitionRepository.save(competition);

        return CompetitionResponse.builder()
            .competitionId(savedCompetition.getId())
            .status(CompetitionStatus.NONE)
            .title(savedCompetition.getTitle())
            .contents(savedCompetition.getContents())
            .category(competition.getCategory())
            .personnelLowerBound(competition.getPersonnelLowerBound())
            .personnelUpperBound(competition.getPersonnelUpperBound())
            .enrollDurationFrom(competition.getEnrollDurationFrom())
            .enrollDurationTo(competition.getEnrollDurationTo()).build();
    }

    public Boolean delete(Long missionId) {
        competitionRepository.deleteById(missionId);
        return true;
    }

    public Boolean zmake() {
        ArrayList<Competition> testMission = new ArrayList<>();

        testMission.add(Competition.builder()
            .title("백엔드 서버 개발")
            .contents("안드로이드 푸쉬를 캐치해서 저장하고 시간 순, 어플 별로 관리하는 서버를 개발합니다.")
            .category(Category.BACKEND)
            .personnelLowerBound(5L)
                .personnelUpperBound(15L)
            .activityDurationFrom(LocalDate.of(2022, 9, 1))
            .activityDurationTo(LocalDate.of(2022, 12, 30))
            .enrollDurationFrom(LocalDate.of(2022, 8, 1))
            .enrollDurationTo(LocalDate.of(2022, 8, 30)).build());

        testMission.add(Competition.builder()
            .title("플러터 웹 및 앱 개발")
            .contents("쿠폰 사용 어플이 웹 및 앱에서 반응형으로 동작할 수 있도록 개발합니다.")
            .category(Category.FRONTEND)
            .personnelLowerBound(3L)
            .personnelUpperBound(6L)
            .activityDurationFrom(LocalDate.of(2022, 9, 15))
            .activityDurationTo(LocalDate.of(2022, 11, 20))
            .enrollDurationFrom(LocalDate.of(2022, 8, 1))
            .enrollDurationTo(LocalDate.of(2022, 9, 10)).build());

        competitionRepository.saveAll(testMission);

        return true;
    }
    /////////////////////////////// for manager ///////////////////////////////
}