package DataModel;

public class Stock {
    private String owner;

    public Stock(String owner) {
        this.owner = owner;
    }
    public void changeOwner(String owner) {
        this.owner = owner;
    }
    public String getOwner() {
        return owner;
    }

}
