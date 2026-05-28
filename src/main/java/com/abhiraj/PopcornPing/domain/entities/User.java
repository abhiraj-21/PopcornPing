package com.abhiraj.PopcornPing.domain.entities;

import com.abhiraj.PopcornPing.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private Boolean isEnabled;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "verification_expiration")
    private LocalDateTime verificationCodeExpiresAt;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<UserMovieTracker> movies = new ArrayList<>();

    @PrePersist
    private void onCreate(){
        this.createdAt = LocalDateTime.now();
        if(roles.isEmpty()){
            roles.add(Role.USER);
        }
    }

}
