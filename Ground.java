import java.util.ArrayList;

public class Ground extends DaisyWorldThread {
    
    private ArrayList<ArrayList<Patch>> ground;
    private double globalTemp;
    private int num_of_white;
    private int num_of_black;
    private int current_year;
    private int end_year;


    //All the input parameter
    private int size;
    private double solar_luminosity;
    private int start_whites;
    private int start_blacks;
    private double albedo_of_whites;
    private double albedo_of_blacks;
    private String scenario; 
    private double albedo_of_surface;



    public Ground(int size, double solar_luminosity, int start_whites, int start_blacks, 
    double albedo_of_blacks, double albedo_of_whites, String scenario, 
    double albedo_of_surface, int end_year) {
        this.size = size;
        this.solar_luminosity = solar_luminosity; 
        this.start_whites = start_whites;
        this.albedo_of_blacks = start_blacks;
        this.albedo_of_blacks = albedo_of_blacks;
        this.albedo_of_whites = albedo_of_whites;
        this.scenario = scenario;
        this.albedo_of_surface = albedo_of_surface;
        this.end_year = end_year;
        
    }

    public void init(){
        createWorld(size, solar_luminosity);
        addNeighbors(ground);
        current_year = 0;
        updateGlobalTemp();

    }


    //create DaisyWorld Patches
    private void createWorld(int size, double solar_luminosity){
        this.ground = new ArrayList<ArrayList<Patch>>();
        for (int i = 0; i < size; i++) {
            this.ground.add(new ArrayList<Patch>());
            for (int j = 0; j < size; j++) {
                ArrayList<Patch> currentRow = this.ground.get(i);
                currentRow.add(new Patch(i, j, solar_luminosity));
            }
        }
    }
    //set all neighbors to every patch
    private void addNeighbors(ArrayList<ArrayList<Patch>> ground) {
        Integer size = ground.size();
        for (int i = 0; i < size; i++) {
            ArrayList<Patch> currentRow = this.ground.get(i);
            for (int j = 0; j < size; j++) {
                Patch currentPatch = currentRow.get(j);
                if (j-1 >= 0) {
                    Patch leftPatch = this.ground.get(i).get(j-1);
                    currentPatch.addNeighbour(leftPatch);
                }

                if (j + 1 < size) {
                    Patch rightPatch = this.ground.get(i).get(j+1);
                    currentPatch.addNeighbour(rightPatch);
                }

                if (i - 1 >= 0) {
                    Patch upPatch = this.ground.get(i - 1).get(j);
                    currentPatch.addNeighbour(upPatch);
                }

                if (i + 1 < size) {
                    Patch downPatch = this.ground.get(i + 1).get(j);
                    currentPatch.addNeighbour(downPatch);
                }

                if ((i - 1 >= 0) && (j - 1 >= 0)) {
                    Patch upLeftPatch = this.ground.get(i - 1).get(j - 1);
                    currentPatch.addNeighbour(upLeftPatch);
                }

                if ((i + 1 < size) && (j - 1 >= 0)) {
                    Patch downLeftPatch = this.ground.get(i + 1).get(j - 1);
                    currentPatch.addNeighbour(downLeftPatch);
                }

                if ((i - 1 >= 0) && (j + 1 < size)) {
                    Patch upRightPatch = this.ground.get(i - 1).get(j + 1);
                    currentPatch.addNeighbour(upRightPatch);
                }

                if ((i + 1 < size) && (j + 1 < size)) {
                    Patch downRightPatch = this.ground.get(i + 1).get(j + 1);
                    currentPatch.addNeighbour(downRightPatch);
                }

            }
        }
    }

    public double getGlobalTemp() {
        return this.globalTemp;
    }

    public Patch getPatch(int x, int y) {
        return this.ground.get(x).get(y);
    }

    private void updateGlobalTemp() {
        double tempSum = 0;
        int size = this.ground.size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tempSum += this.ground.get(i).get(j).getLocalTemp();
            }
        }

        this.globalTemp = tempSum;
    }



    public void run(){
        Integer size = this.ground.size();
        while (!isInterrupted()){
            try {
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        this.ground.get(i).get(j).updateTemp();
                        this.ground.get(i).get(j).diffuse();
                        updateGlobalTemp();
                    }
                }

            } catch (Exception e) {
                this.interrupt();
            }
        }

        
    }
}
