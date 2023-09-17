package talksum.talksum.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import talksum.talksum.domain.dto.MemberDto;

import javax.validation.constraints.Pattern;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "member")
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberId")
    private Long memberId;

    @Column(name = "loginId", unique = true, nullable = false)
    private String loginId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    public Member(Long memberId, String loginId, String password, String name){
        this.memberId=memberId;
        this.loginId=loginId;
        this.password=password;
        this.name=name;
    }



    /* Entity -> DTO */
    public MemberDto toDTO(){
        return MemberDto.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .build();
    }
}
