package talksum.talksum.domain.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import talksum.talksum.domain.entity.Member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@Builder
public class MemberDto {

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    @Pattern(regexp = "[a-zA-Z0-9]{2,10}", message = "아이디는 영문 대/소문자, 숫자만 가능하며 2 ~ 10자리까지 가능합니다.")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대/소문자, 숫자, 특수문자만 가능합니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 입력값입니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z]{2,10}$", message = "이름은 2~10자의 한글과 영문 대/소문자만 사용 가능합니다.")
    private String name;

    /* 암호화된 password */
    public void encryptPassword(String BCryptpassword){
        this.password=BCryptpassword;
    }

    /* DTO -> Entity */
    public Member toEntity(){
        Member member=Member.builder()
                .loginId(loginId)
                .password(password)
                .name(name)
                .build();
        return member;
    }

}
