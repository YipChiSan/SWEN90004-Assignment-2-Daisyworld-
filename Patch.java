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

    public Patch(int xAxis, int yAxis, List<Patch> neighbours, float surfaceAlbedo, float diffusionRate) {
        super();
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
        return this.neighbours;
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

    /**
     * This function should be run right after a patch is created.
     * @param solarLumin Solar luminosity
     * @return Local temperature of this patch
     */
    public synchronized void updataTemp(Float solarLumin) {
        Float localHeating = 72 * Math.log(getAbsorbedLumin(solarLumin)) + 80;
        this.localTemp = (localTemp + localHeating) / 2;
    }

    public Float getLocalTemp(){
        return this.localTemp;
    }

    public synchronized void addTemp(Float addedTemp){
        this.localTemp += addedTemp;
    }

    @Override
    /**
     * This is actually a diffuse function.
     * This patch will diffuse until none of its neighbors' temperature is lower than it.
     */
    public void run() {
        float tempChange = this.localTemp * this.diffusionRate / this.neighbours.size();
        boolean isChanged = false;
        while(!isInterrupted()){
            try{
                for (Patch patch : this.neighbours) {
                    if (patch.getLocalTemp() <= this.localTemp) {
                        patch.addTemp(tempChange);
                        isChanged = true;
                    }
                }
                if (isChanged) {
                    this.localTemp -= tempChange * this.neighbours.size();
                }
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }
}