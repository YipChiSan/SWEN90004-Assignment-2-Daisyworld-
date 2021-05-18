import java.util.ArrayList;
import java.lang.*;

public class Patch extends DaisyWorldThread{
    private final  int xAxis;
    private final int yAxis;
    private ArrayList<Patch> neighbours;
    Daisy daisy;
    private final float diffusionRate;
    private final float surfaceAlbedo;
    private Float localTemp;

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

    public Boolean isThereDaisy() {
        if (this.daisy != null) {
            return this.daisy.isAlive(); //Assume there is a function to check the survivability of the daisy
        } else {
            return false;
        }
    }

    private Float getAbsorbedLumin(Float solarLumin) {
        if (!isThereDaisy()) {
            return (1 - this.surfaceAlbedo) * solarLumin + this.diffusionRate;
        } else {
            return (1 - this.daisy.getAlbedo()) * solarLumin + this.diffusionRate;
        }
    }

    public Float getLocalTemp(Float solarLumin) {
        Float localHeating = 72 * Math.log(getAbsorbedLumin(solarLumin)) + 80;
        this.localTemp = (localTemp + localHeating) / 2
        return this.localTemp;
    }


}