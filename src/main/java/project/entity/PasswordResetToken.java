package project.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_password_reset_token")
public class PasswordResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String token;
    private LocalDateTime expirationDate;
    private static final int EXPIRATION = 20;
    @OneToOne
    @JoinColumn(nullable = false, name = "user_id", referencedColumnName = "id")
    private User user;

    public PasswordResetToken() {
    }

    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.expirationDate = calculateExpirationDate();
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }
    private LocalDateTime calculateExpirationDate() {
        return LocalDateTime.now().plusMinutes(EXPIRATION);
    }

    public void setExpirationDate() {
        this.expirationDate = LocalDateTime.now().plusMinutes(EXPIRATION);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
