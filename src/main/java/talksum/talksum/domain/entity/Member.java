package talksum.talksum.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import talksum.talksum.domain.dto.MemberDto;


//@NoArgsConstructor
@Entity
@Getter
@Table(name = "member")
@Builder
@NoArgsConstructor
@AllArgsConstructor
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


    /* Entity -> DTO */
    public MemberDto toDTO(){
        return MemberDto.builder()
                .memberId(memberId)
                .loginId(loginId)
                .password(password)
                .name(name)
                .build();
    }
}
