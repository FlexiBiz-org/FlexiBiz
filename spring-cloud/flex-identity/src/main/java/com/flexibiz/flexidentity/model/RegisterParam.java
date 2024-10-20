package com.flexibiz.flexidentity.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterParam {
    private String username;
    private String password;
    private List<String> roles;
}

