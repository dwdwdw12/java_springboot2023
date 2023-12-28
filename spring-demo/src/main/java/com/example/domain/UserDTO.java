package com.example.domain;

import lombok.*;

@Getter
@Setter
//@NoArgsConstructor
//@AllArgsConstructor
@RequiredArgsConstructor
public class UserDTO {
    /*@NonNull        //@RequiredArgsConstructor 과 함께 쓰임. null을 체크한 후 NullPointerException 발생
    private String userId;
    @NonNull
    private String userPwd;*/
   /* public UserDTO(String userId, String userPwd){
        this.userId = userId;
        this.userPwd = userPwd;
    }*/

    private final String userId;    //기본 생성자를 만들 수 없음.
    private final String userPwd;
    private String email;

    public UserDTO(){           //직접 초기값을 넣어주어야 함.
        this.userId = "guest";
        this.userPwd = "anonymous";
    }
}
