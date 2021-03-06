import java.util.ArrayList;


public class Patch{
    private ArrayList<Patch> neighbours;
    Daisy daisy;
    private final double diffusionRate;
    private final double surfaceAlbedo;
    private double localTemp;
    private double solarLumin;

    public Patch(ArrayList<Patch> neighbours, 
                 double surfaceAlbedo, 
                 double diffusionRate, 
                 double solarLumin) {
        super();
        this.neighbours = neighbours;
        this.surfaceAlbedo = surfaceAlbedo;
        this.diffusionRate = diffusionRate;
        this.solarLumin = solarLumin;
        this.localTemp = 0;
    }

    public Patch(double solarLumin) {
        super();
        this.solarLumin = solarLumin;
        this.neighbours = new ArrayList<>();
        this.surfaceAlbedo = 0.4;
        this.diffusionRate = 0.5;
        this.localTemp = 0;


    }

    //Add a patch to the neighbor list 
    public void addNeighbour(Patch neighbor) {
        this.neighbours.add(neighbor);
    }

    //Return neighbor list
    public ArrayList<Patch> getNeighbour() {
        return this.neighbours;
    }

    public void setDaisy(Daisy daisy) {
        this.daisy = daisy;
    }

    public Daisy getDaisy(){
        return daisy;
    }

    //Change solar luminosity if necessary
    public void setSolarLumin(double solarLumin) {
        this.solarLumin = solarLumin;
    }

    //Check whether there is a live daisy
    public Boolean isThereDaisy() {
        if (this.daisy != null) {
            return this.daisy.isAlive(); 
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
        double localHeating = 
            (72 * Math.log(getAbsorbedLumin(this.solarLumin)) + 80);
        this.localTemp = (this.localTemp + localHeating) / 2;
    }

    public double getLocalTemp(){
        return this.localTemp;
    }

    //Use when a neighbor patch want to diffuse some heat to this patch
    public void addTemp(double addedTemp){
        this.localTemp += addedTemp;
    }

    //Return albedo of this patch
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

        double tempChange = 
            (getLocalTemp() * this.diffusionRate / this.neighbours.size());
        
            
        for (Patch patch : this.neighbours) {
           
            patch.addTemp(tempChange);
            addTemp(-tempChange);
                
            
        }
            
        
           
            
        
    }
}
