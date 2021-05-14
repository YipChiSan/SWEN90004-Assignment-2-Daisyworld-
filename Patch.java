import java.util.ArrayList;

public class Patch {
    int xAxis;
    int yAxis;
    ArrayList<Patch> neighbours;
    float surfaceAlbedo;

    public Patch(int xAxis, int yAxis, List<Patch> neighbours, float surfaceAlbedo) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.neighbours = neighbours;
        this.surfaceAlbedo = surfaceAlbedo;

    }
}