package project.model.authenticationModel;

import jakarta.validation.constraints.NotEmpty;

public class RefreshToken {
    @NotEmpty
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}