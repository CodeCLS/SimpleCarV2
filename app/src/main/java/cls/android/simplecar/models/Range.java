package cls.android.simplecar.models;

public class Range {
    private double percent = -1.0;
    private double amount = -1.0;

    public Range(double percent, double amount) {
        this.percent = percent;
        this.amount = amount;
    }

    public Range() {
    }

    public double getPercent() {
        return percent;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

