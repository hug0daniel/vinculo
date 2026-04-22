package com.vinculo.domain.user.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    @Size(max = 100)
    private String userName;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    private Partner partner;

    @Column(name = "active")
    private boolean active;

    protected User() {
    }

    public User(String email, String password, String userName, Role role, Partner partner, boolean active) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.role = role;
        this.partner = partner;
        this.active = active;
    }

    public UUID getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getUserName() { return userName; }
    public Role getRole() { return role; }
    public Partner getPartner() { return partner; }
    public boolean isActive() { return active; }

    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setUserName(String userName) { this.userName = userName; }
    public void setRole(Role role) { this.role = role; }
    public void setPartner(Partner partner) { this.partner = partner; }
    public void setActive(boolean active) { this.active = active; }
}
