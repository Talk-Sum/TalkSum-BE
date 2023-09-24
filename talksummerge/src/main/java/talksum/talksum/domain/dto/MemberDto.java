package talksum.talksum.domain.dto;

import lombok.*;
import talksum.talksum.domain.entity.Member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private Long memberId;

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    /* 암호화된 password */
    public void encryptPassword(String BCryptpassword){
        this.password=BCryptpassword;
    }

    /* DTO -> Entity */
    public Member toEntity(){
        Member member=Member.builder()
                .memberId(memberId)
                .loginId(loginId)
                .password(password)
                .name(name)
                .build();
        return member;
    }

}
