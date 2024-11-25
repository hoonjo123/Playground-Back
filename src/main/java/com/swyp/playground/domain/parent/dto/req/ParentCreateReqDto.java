package com.swyp.playground.domain.parent.dto.req;

import com.swyp.playground.domain.child.domain.ChildGenderType;
import com.swyp.playground.domain.parent.domain.ParentRoleType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ParentCreateReqDto {

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "유효한 이메일 형식이어야 합니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,20}$",
            message = "비밀번호는 6~20자 영문 대 소문자, 숫자, 특수문자를 포함해야 합니다."
    )
    private String password;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    @NotBlank(message = "주소는 필수 입력 값입니다.")
    private String address;

    @NotNull(message = "보호자 역할은 필수 입력 값입니다.")
    private ParentRoleType role;

    @NotNull(message = "생년월일은 필수 입력 값입니다.")
    private LocalDate birthDate;

    @NotBlank(message = "소개는 필수 입력 값입니다.")
    private String introduce;


    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    @Pattern(
            regexp = "^01[0-9]{8,9}$",
            message = "전화번호는 10~11자리 숫자로 입력하세요. 예: 01012345678"
    )
    private String phoneNumber;

    @NotNull(message = "자녀 수는 필수 입력 값입니다.")
    @Min(value = 1, message = "자녀 수는 최소 1명 이상이어야 합니다.")
    private Integer childCount;

    @Size(max = 5, message = "최대 5명의 자녀만 입력 가능합니다.")
    private List<ChildDto> children;


    @Getter
    @Setter
    public static class ChildDto {
        @NotNull(message = "자녀의 성별은 필수 입력 값입니다.")
        private ChildGenderType gender;

        @NotNull(message = "자녀의 생년월일은 필수 입력 값입니다.")
        private LocalDate birthDate;
    }
}
