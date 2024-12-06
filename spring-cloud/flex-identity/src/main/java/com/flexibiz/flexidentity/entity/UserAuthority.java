package com.flexibiz.flexidentity.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;

@Table("user_authority")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthority implements GrantedAuthority {

    @Id
    private Long id;
    private String role;
    @Column("user_id")
    @JsonIgnore
    private Long userId;

    public UserAuthority(String role, Long userId) {
        this.role = role;
        this.userId = userId;
    }
    @Override
    public String getAuthority() {
        return role;
    }
}

