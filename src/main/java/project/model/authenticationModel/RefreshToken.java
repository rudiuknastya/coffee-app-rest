package project.model.authenticationModel;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public class RefreshToken {
    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMUBnbWFpbC5jb20iLCJpYXQiOjE3MDMwMDkyNjIsImV4cCI6MTcwMzYxNDA2Mn0.9g0Y0pGrOvjfGaFsmS8p5tHay0e9mLahlkWRbkhdu8s", required = true)
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
