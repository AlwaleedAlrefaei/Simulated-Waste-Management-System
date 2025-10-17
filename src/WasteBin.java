public class WasteBin {
    String id;
    String location;
    int fillLevel;
    String status;

    public WasteBin(String id, String location, int fillLevel, String status) {
        this.id = id;
        this.location = location;
        this.fillLevel = fillLevel;
        this.status = status;
    }
}