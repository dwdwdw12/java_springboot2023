package com.example.domain;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class MemberVO {
    private String memId = "userone";
    private String password = "onepass";
    private String email = "user@one.com";
}
