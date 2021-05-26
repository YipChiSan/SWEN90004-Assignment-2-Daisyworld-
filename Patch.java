import java.util.ArrayList;


public class Patch{
    private final  int xAxis;
    private final int yAxis;
    private ArrayList<Patch> neighbours;
    Daisy daisy;
    private final double diffusionRate;
    private final double surfaceAlbedo;
    private double localTemp;
    private double solarLumin;

    public Patch(int xAxis, int yAxis, ArrayList<Patch> neighbours, double surfaceAlbedo, double diffusionRate, double solarLumin) {
        super();
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.neighbours = neighbours;
        this.surfaceAlbedo = surfaceAlbedo;
        this.diffusionRate = diffusionRate;
        this.solarLumin = solarLumin;
        updateTemp();
    }

    public Patch(int xAxis, int yAxis, double solarLumin) {
        super();
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.solarLumin = solarLumin;
        this.neighbours = new ArrayList<>();
        this.surfaceAlbedo = 0.4;
        this.diffusionRate = 0.5;
        updateTemp();


    }

    public int getXAxis() {
        return this.xAxis;
    }

    public int getYAxis() {
        return this.yAxis;
    }

    public void addNeighbour(Patch neighbor) {
        this.neighbours.add(neighbor);
    }

    public ArrayList<Patch> getNeighbour() {
        return this.neighbours;
    }

    public void setDaisy(Daisy daisy) {
        this.daisy = daisy;
        updateTemp();
    }

    public Daisy getDaisy(){
        return daisy;
    }

    public Boolean isThereDaisy() {
        if (this.daisy != null) {
            return this.daisy.isAlive(); //Assume there is a function to check the survivability of the daisy
        } else {
            return false;
        }
    }

    private double getAbsorbedLumin(double solarLumin) {
        
        return (1 - getAlbedo()) * solarLumin;
        
    }

    /**
     * This function should be run right after a patch is created.
     * @param solarLumin Solar luminosity
     */
    public void updateTemp() {
        double localHeating = 72 * Math.log(getAbsorbedLumin(this.solarLumin)) + 80;
        this.localTemp = (this.localTemp + localHeating) / 2;
    }

    public double getLocalTemp(){
        return this.localTemp;
    }

    public void addTemp(double addedTemp){
        this.localTemp += addedTemp;
        updateTemp();
    }

    public double getAlbedo(){
        if (isThereDaisy()) {
            return daisy.getAlbedo();
        } else{
        return surfaceAlbedo;
        }
    }
  
    /**
     * This is a diffuse function.
     * This patch will diffuse until none of its neighbors' temperature is lower than it.
     */
    public void diffuse() {

        double tempChange = getLocalTemp() * this.diffusionRate / this.neighbours.size();
        boolean isChanged = false;
        
            
        for (Patch patch : this.neighbours) {
            if (patch.getLocalTemp() <= getLocalTemp()) {
                patch.addTemp(tempChange);
                isChanged = true;
            }
        }
            if (isChanged) {
                addTemp(-tempChange * this.neighbours.size()); 
            }
            
        
    }
}