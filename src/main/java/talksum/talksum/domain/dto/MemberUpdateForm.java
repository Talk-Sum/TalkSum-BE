package talksum.talksum.domain.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MemberUpdateForm {
    @NotNull
    private Long memberId;

    @NotBlank
    @Pattern(regexp = "^[가-힣a-zA-Z]{2,10}$", message = "이름은 2~10자의 한글과 영문 대/소문자만 사용 가능합니다.")
    private String name;

    @NotBlank
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대/소문자, 숫자, 특수문자만 가능합니다.")
    private String password;

    /* 암호화된 password */
    public void encryptPassword(String BCryptpassword){
        this.password=BCryptpassword;
    }
}
