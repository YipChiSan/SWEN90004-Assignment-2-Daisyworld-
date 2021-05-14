import java.util.ArrayList;

public class Patch {
    private final  int xAxis;
    private final int yAxis;
    private ArrayList<Patch> neighbours;
    Daisy daisy;
    private final float diffusionRate;
    private final float surfaceAlbedo;

    public Patch(int xAxis, int yAxis, List<Patch> neighbours, float surfaceAlbedo, float diffusionRate = 0.5) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.neighbours = neighbours;
        this.surfaceAlbedo = surfaceAlbedo;
        this.diffusionRate = diffusionRate;
    }

    public int getXAxis() {
        return this.xAxis;
    }

    public int getYAxis() {
        return this.yAxis;
    }

    public List<Patch> getNeighbours() {
        return this.neighbours.clone();
    }

    public void setDaisy(Daisy daisy) {
        this.daisy = daisy;
    }


}