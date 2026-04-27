package com.vinculo.domain.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(name = "user_name")
    private String userName;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_id")
    @JsonIgnore
    private Partner partner;

    @Column(name = "active")
    private boolean active;

    protected User() {
    }

    private User(Builder builder) {
        this.email = builder.email;
        this.password = builder.password;
        this.userName = builder.userName;
        this.role = builder.role;
        this.partner = builder.partner;
        this.active = builder.active;
    }

    public static Builder builder() {
        return new Builder();
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

    public static class Builder {
        private String email;
        private String password;
        private String userName;
        private Role role;
        private Partner partner;
        private boolean active = true;

        public Builder email(String email) { this.email = email; return this; }
        public Builder password(String password) { this.password = password; return this; }
        public Builder userName(String userName) { this.userName = userName; return this; }
        public Builder role(Role role) { this.role = role; return this; }
        public Builder partner(Partner partner) { this.partner = partner; return this; }
        public Builder active(boolean active) { this.active = active; return this; }

        public User build() {
            return new User(this);
        }
    }
}
