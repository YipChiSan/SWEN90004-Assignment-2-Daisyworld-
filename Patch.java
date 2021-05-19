import java.util.ArrayList;
import java.lang.*;
import java.util.concurrent.CountDownLatch;

public class Patch extends DaisyWorldThread{
    private final  int xAxis;
    private final int yAxis;
    private ArrayList<Patch> neighbours;
    Daisy daisy;
    private final float diffusionRate;
    private final float surfaceAlbedo;
    private Float localTemp;
    private CountDownLatch latch;
    private float solarLumin;

    public Patch(int xAxis, int yAxis, CountDownLatch latch, List<Patch> neighbours, float surfaceAlbedo, float diffusionRate, float solarLumin) {
        super();
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.neighbours = neighbours;
        this.surfaceAlbedo = surfaceAlbedo;
        this.diffusionRate = diffusionRate;
        this.solarLumin = solarLumin;
        this.latch = latch;
        updataTemp();
    }

    public Patch(int xAxis, int yAxis, CountDownLatch latch, float solarLumin) {
        super();
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.latch = latch;
        this.solarLumin = solarLumin;
        this.neighbours = new ArrayList<>();
        this.surfaceAlbedo = 0.4;
        this.diffusionRate = 0.5;


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
        updataTemp();
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
    public synchronized void updataTemp() {
        Float localHeating = 72 * Math.log(getAbsorbedLumin(this.solarLumin)) + 80;
        this.localTemp = (this.localTemp + localHeating) / 2;
    }

    public synchronized Float getLocalTemp(){
        return this.localTemp;
    }

    public synchronized void addTemp(Float addedTemp){
        this.localTemp += addedTemp;
        updataTemp();
    }

    @Override
    /**
     * This is actually a diffuse function.
     * This patch will diffuse until none of its neighbors' temperature is lower than it.
     */
    public void run() {
        try{
            this.latch.await();
        } catch(InterruptedException e) {
            this.interrupt();
        }
        float tempChange = getLocalTemp() * this.diffusionRate / this.neighbours.size();
        boolean isChanged = false;
        while(!isInterrupted()){
            try{
                for (Patch patch : this.neighbours) {
                    if (patch.getLocalTemp() <= getLocalTemp()) {
                        patch.addTemp(tempChange);
                        isChanged = true;
                    }
                }
                if (isChanged) {
                    addTemp(-tempChange * this.neighbours.size()); 
                }
            } catch (InterruptedException e) {
                this.interrupt();
            }
        }
    }
}