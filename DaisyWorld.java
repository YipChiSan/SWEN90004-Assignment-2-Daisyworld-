/**
 * A super class for all threads in the Daisy world system
 */
public class DaisyWorld extends Thread {
    //NetLogo only provide a fixed map
    private Patch[][] world = new Patch[40][40];

    //All the input parameter
    private int start_whites;
    private int start_blacks;
    private double albedo_of_whites;
    private double albedo_of_blacks;
    private String scenario;
    private double solar_luminosity;
    private double albedo_of_surface;

    //default settings constructor
    public DaisyWorld(){
        
        this.start_whites = 20;
        this.albedo_of_blacks = 20;
        this.albedo_of_whites = 0.75;
        this.albedo_of_blacks = 0.25;
        this.scenario = "maintain current luminosity";
        this.solar_luminosity = 0.8;
        this.albedo_of_surface = 0.4;

    }

    //customized settings constructor
    public DaisyWorld(double startWhitePercent, double startBlackPercent, double albedoOfwhite, double albedoOfblack, 
        String scenario,double solarLuminosity,double albedoOfsurface, int years){

        this.startWhitePercent=startWhitePercent;
        this.startBlackPercent= startBlackPercent;
        this.albedoOfwhite = albedoOfwhite;
        this.albedoOfblack = albedoOfblack;
        this.scenario = scenario;
        this.solarLuminosity =solarLuminosity;
        this.albedoOfsurface = albedoOfsurface;
        this.numOfYears = years;

}
    

    // if this thread terminates, this exception provides a reason
    protected static Exception terminateException ;

    /**
     * Terminate this thread, and signal other threads to terminate using the
     * static variables.
     * 
     * @param exception
     *            an Exception detailing the reason for termination.
     */
    public static void terminate(Exception exception) {
        terminateException = exception;
        Thread.currentThread().interrupt();
    }

    /**
     * @return the reason for the termination.
     */
    public static Exception getTerminateException() {
        return terminateException;
    }
}