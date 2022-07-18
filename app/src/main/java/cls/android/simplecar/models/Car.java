package cls.android.simplecar.models;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

import cls.android.simplecar.tools.DateUtil;


@Entity(tableName = "cars",indices = {@Index(value = {"smartCarId"},
        unique = true)})
public class Car {
    @PrimaryKey(autoGenerate = true)
    private Long roomId;
    @ColumnInfo(name = "smartCarId")
    private String smartCarId = "";
    private String model = "";
    private String name = "";
    private String brand = "";
    private Long year = -1L;
    private Boolean isLocked = true;
    private Boolean isElectric = false;

    private ArrayList<String> hasPermissions = new ArrayList<>();
    private Location location = new Location(51.5,-0.127);
    private Double driveProductAmount = -1.0;
    private Double driveProductAmountPercent = -1.0;

    private Double driveDuration = -1.0;
    private Integer tirePressure = 0;
    private Boolean canHeat = true;
    private Boolean isAirCondOn = false;
    private String vin = "None";
    @Ignore
    public Car() {
    }
    @Ignore
    public Car(String smartCarId) {
        this.smartCarId = smartCarId;
    }

    public Car(Long roomId, String smartCarId, String model, String name, String brand, Long year, Boolean isLocked, Boolean isElectric, ArrayList<String> hasPermissions, Location location, Double driveProductAmount, Double driveProductAmountPercent, Double driveDuration, Integer tirePressure, Boolean canHeat, Boolean isAirCondOn, String vin) {
        this.roomId = roomId;
        this.smartCarId = smartCarId;
        this.model = model;
        this.name = name;
        this.brand = brand;
        this.year = year;
        this.isLocked = isLocked;
        this.isElectric = isElectric;
        this.hasPermissions = hasPermissions;
        this.location = location;
        this.driveProductAmount = driveProductAmount;
        this.driveProductAmountPercent = driveProductAmountPercent;
        this.driveDuration = driveDuration;
        this.tirePressure = tirePressure;
        this.canHeat = canHeat;
        this.isAirCondOn = isAirCondOn;
        this.vin = vin;
    }



    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getSmartCarId() {
        return smartCarId;
    }

    public void setSmartCarId(String smartCarId) {
        this.smartCarId = smartCarId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public Boolean getLocked() {
        return isLocked;
    }

    public void setLocked(Boolean locked) {
        isLocked = locked;
    }

    public Boolean getElectric() {
        return isElectric;
    }

    public void setElectric(Boolean electric) {
        isElectric = electric;
    }

    public ArrayList<String> getHasPermissions() {
        return hasPermissions;
    }

    public void setHasPermissions(ArrayList<String> hasPermissions) {
        this.hasPermissions = hasPermissions;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Double getDriveProductAmount() {
        return driveProductAmount;
    }

    public void setDriveProductAmount(Double driveProductAmount) {
        this.driveProductAmount = driveProductAmount;
    }

    public Double getDriveProductAmountPercent() {
        return driveProductAmountPercent;
    }

    public void setDriveProductAmountPercent(Double driveProductAmountPercent) {
        this.driveProductAmountPercent = driveProductAmountPercent;
    }

    public Double getDriveDuration() {
        return driveDuration;
    }

    public void setDriveDuration(Double driveDuration) {
        this.driveDuration = driveDuration;
    }

    public Integer getTirePressure() {
        return tirePressure;
    }

    public void setTirePressure(Integer tirePressure) {
        this.tirePressure = tirePressure;
    }

    public Boolean getCanHeat() {
        return canHeat;
    }

    public void setCanHeat(Boolean canHeat) {
        this.canHeat = canHeat;
    }

    public Boolean getAirCondOn() {
        return isAirCondOn;
    }

    public void setAirCondOn(Boolean airCondOn) {
        isAirCondOn = airCondOn;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }


}
