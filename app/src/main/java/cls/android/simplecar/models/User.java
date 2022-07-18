package cls.android.simplecar.models;

public class User {
    private String firstName;
    private String secondName;
    private String email;
    private int phone;
    private Subscription subscription;
    private String uidFire;
    private String uidSimpleCar;
    private int amountVehicles;
    private String accessTokenSmartCar;
    private String authClientSmartCar;
    private String authSmartCar;

    public User(String firstName, String secondName, String email, int phone, Subscription subscription, String uidFire, String uidSimpleCar, int amountVehicles, String accessTokenSmartCar, String authClientSmartCar, String authSmartCar) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.phone = phone;
        this.subscription = subscription;
        this.uidFire = uidFire;
        this.uidSimpleCar = uidSimpleCar;
        this.amountVehicles = amountVehicles;
        this.accessTokenSmartCar = accessTokenSmartCar;
        this.authClientSmartCar = authClientSmartCar;
        this.authSmartCar = authSmartCar;
    }

    public User() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public String getUidFire() {
        return uidFire;
    }

    public void setUidFire(String uidFire) {
        this.uidFire = uidFire;
    }

    public String getUidSimpleCar() {
        return uidSimpleCar;
    }

    public void setUidSimpleCar(String uidSimpleCar) {
        this.uidSimpleCar = uidSimpleCar;
    }

    public int getAmountVehicles() {
        return amountVehicles;
    }

    public void setAmountVehicles(int amountVehicles) {
        this.amountVehicles = amountVehicles;
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
