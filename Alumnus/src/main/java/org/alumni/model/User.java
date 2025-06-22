package org.alumni.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
@NoArgsConstructor
@Document(collection = "users") // Use @Document for MongoDB collections
public class User implements UserDetails {

    @Id // This ID is from Spring Data, not JPA. MongoDB generates it automatically.
    private String id; // The ID in MongoDB is a String.

    private String name;

    @Indexed(unique = true) // Creates a unique index on the email field in MongoDB
    private String email;

    private String password;
    private String phone;

    private Role role; // Enums are stored as strings by default, which is perfect.

    // Alumni-specific fields
    private String companyName;
    private String companyRole;

    // Student-specific fields
    private String collegeName;
    private String branch;
    private String collegeId;

    // --- No changes are needed for the UserDetails implementation ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}