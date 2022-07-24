package cls.simplecar.models;

public class Account {
    private String firstName;
    private String secondName;
    private String smartCarAccountId;
    private String accessTokenSmartCar;
    private String authClientSmartCar;
    private String authSmartCar;

    public Account(String firstName, String secondName, String smartCarAccountId, String accessTokenSmartCar, String authClientSmartCar, String authSmartCar) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.smartCarAccountId = smartCarAccountId;
        this.accessTokenSmartCar = accessTokenSmartCar;
        this.authClientSmartCar = authClientSmartCar;
        this.authSmartCar = authSmartCar;
    }

    public Account() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getSmartCarAccountId() {
        return smartCarAccountId;
    }

    public void setSmartCarAccountId(String smartCarAccountId) {
        this.smartCarAccountId = smartCarAccountId;
    }

    public String getAccessTokenSmartCar() {
        return accessTokenSmartCar;
    }

    public void setAccessTokenSmartCar(String accessTokenSmartCar) {
        this.accessTokenSmartCar = accessTokenSmartCar;
    }

    public String getAuthClientSmartCar() {
        return authClientSmartCar;
    }

    public void setAuthClientSmartCar(String authClientSmartCar) {
        this.authClientSmartCar = authClientSmartCar;
    }

    public String getAuthSmartCar() {
        return authSmartCar;
    }

    public void setAuthSmartCar(String authSmartCar) {
        this.authSmartCar = authSmartCar;
    }
}
