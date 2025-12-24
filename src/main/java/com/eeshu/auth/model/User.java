package com.eeshu.auth.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Builder
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        }
)
public class User extends BaseModel implements UserDetails {

//    @NotBlank
//    @Size(min = 3, max = 50)
//    @Column(nullable = false)
//    private String username;

//    @NotBlank
//    @Size(min = 8)
//    @Column(nullable = false)
    private String password;

//    @NotBlank
//    @Email
//    @Column(nullable = false)
    private String email;
//
//    @Size(max = 50)
    private String firstName;

//    @Size(max = 50)
    private String lastName;

//    @Pattern(regexp = "^[0-9]{10,15}$", message = "Invalid phone number")
    private String phone;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    private String city;
    private String state;
    private String country;
    private String countryCode;
    private String countryName;
    private String imageUrl;

    @Column(nullable = false)
    private boolean enabled = true;

    private LocalDateTime lastLogin;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider = Provider.LOCAL;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public  String getUsername() {
        return  this.email;
    }
    @Override
    public  boolean isEnabled() {
        return this.enabled;
    }
}
