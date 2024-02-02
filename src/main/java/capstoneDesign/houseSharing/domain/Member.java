package capstoneDesign.houseSharing.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    private boolean man;

    private LocalDate birth;

    @Column(unique = true)
    private String login_id;

    private String login_pw;

    private int averageScore;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<TrustScore> trustScores = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<House> houses = new ArrayList<>();

    /**
     * 정적 팩토리 생성 메서드
     */
    public static Member createMember(String name, boolean man, LocalDate birth, String login_id, String login_pw) {
        Member member = new Member();
        member.setName(name);
        member.setMan(man);
        member.setBirth(birth);
        member.setLogin_id(login_id);
        member.setLogin_pw(login_pw);

        return member;
    }
}