package groupware.simplemeetup.entity;

import groupware.simplemeetup.enumerations.Authority;
import groupware.simplemeetup.enumerations.Category;
import groupware.simplemeetup.enumerations.Gender;

import java.util.*;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@Setter

public class User {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String email;

    @Setter
    private String password;

    private Date birthday;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Authority authority = Authority.USER;

    @OneToMany(mappedBy = "user")
    private List<UserCompetition> competitions = new ArrayList<>();
}
