package com.one.arpitInstituteAPI.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.one.arpitInstituteAPI.utils.UserRole;
import com.one.arpitInstituteAPI.utils.UserStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @Pattern(regexp = "\\d{10}", message = "Mobile Number must be 10 digits")
    @NotBlank(message = "Mobile Number is required")
    @Column(unique = true, nullable = false)
    private String mobileNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.SUPER_ADMIN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.ACTIVE;

    private String name;

    private String gender;

    private LocalDateTime lastLogin;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Lob
    @Column(name = "profile_picture", columnDefinition = "LONGBLOB")
    private byte[] profilePicture;

    @ManyToOne
    @JoinColumn(name = "society_id")
    @JsonBackReference // 🔁 Avoid circular reference with Society
    private Society society; // Nullable for super admin

    @OneToOne
    @JoinColumn(name = "flat_id", unique = true)
    @JsonBackReference // 🔁 This breaks the cycle
    private Flat flat;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference // 🔁 This breaks the cycle
    private List<FcmToken> fcmTokens = new ArrayList<>();

    @Column(name = "SUPER_ADMIN", columnDefinition = "TINYINT(1) DEFAULT 0")
    private boolean superAdmin = false; // default to false
}