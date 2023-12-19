package project.entity;

public enum UserStatus {
    NEW("Новий"),
    ACTIVE("Активний"),
    BLOCKED("Заблокований");
    private final String statusName;
    UserStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}
