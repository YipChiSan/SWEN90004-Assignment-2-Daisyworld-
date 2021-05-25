import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Ground extends DaisyWorldThread {
    
    private ArrayList<ArrayList<Patch>> ground;
    private double globalTemp;
    private int num_of_white = 0;
    private int num_of_black = 0;
    private int current_year;
    private int end_year;


    //All the input parameter
    private int size;
    private double solar_luminosity;
    private double start_persent_whites;
    private double start_persent_blacks;
    private double albedo_of_whites;
    private double albedo_of_blacks;
    private String scenario; 
    private double albedo_of_surface;

    //calculate actual number of Dasiys in the initial state.
    private int start_whites;
    private int start_blacks;


    //CSV file related parameter
    private File csvfile = new File("DaisyWorld.csv");
    private BufferedWriter csvWriter;

    public Ground(int size, double solar_luminosity, double start_persent_whites, double start_persent_blacks, 
    double albedo_of_blacks, double albedo_of_whites, String scenario, 
    double albedo_of_surface, int end_year) {
        this.size = size;
        this.solar_luminosity = solar_luminosity; 
        this.start_persent_whites = start_persent_whites;
        this.start_persent_blacks = start_persent_blacks;
        this.albedo_of_blacks = start_blacks;
        this.albedo_of_blacks = albedo_of_blacks;
        this.albedo_of_whites = albedo_of_whites;
        this.scenario = scenario;
        this.albedo_of_surface = albedo_of_surface;
        this.end_year = end_year;
        this.start_whites = (int) Math.floor(size*size*start_persent_whites*0.01);
        this.start_blacks = (int) Math.floor(size*size*start_persent_blacks*0.01);
        init();
        
    }

    public double getGlobalTemp() {
        return this.globalTemp;
    }

    public Patch getPatch(int x, int y) {
        return this.ground.get(x).get(y);
    }


    //initialize methods
    //init the world
    public void init(){
        createWorld(size, solar_luminosity);
        addNeighbors(ground);
        current_year = 0;
        updateGlobalTemp();
        initSeeding();
        initCSV();
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

    private ArrayList<Patch> getGrondNeighbours(int i, int j){
    	ArrayList<Patch> groundNeighbours = new ArrayList<Patch>();
        if (j-1 >= 0) {
            Patch leftPatch = this.ground.get(i).get(j-1);
            groundNeighbours.add(leftPatch);
        }

        if (j + 1 < size) {
            Patch rightPatch = this.ground.get(i).get(j+1);
            groundNeighbours.add(rightPatch);
        }

        if (i - 1 >= 0) {
            Patch upPatch = this.ground.get(i - 1).get(j);
            groundNeighbours.add(upPatch);
        }

        if (i + 1 < size) {
            Patch downPatch = this.ground.get(i + 1).get(j);
            groundNeighbours.add(downPatch);
        }

        if ((i - 1 >= 0) && (j - 1 >= 0)) {
            Patch upLeftPatch = this.ground.get(i - 1).get(j - 1);
            groundNeighbours.add(upLeftPatch);
        }

        if ((i + 1 < size) && (j - 1 >= 0)) {
            Patch downLeftPatch = this.ground.get(i + 1).get(j - 1);
            groundNeighbours.add(downLeftPatch);
        }

        if ((i - 1 >= 0) && (j + 1 < size)) {
            Patch upRightPatch = this.ground.get(i - 1).get(j + 1);
            groundNeighbours.add(upRightPatch);
        }

        if ((i + 1 < size) && (j + 1 < size)) {
            Patch downRightPatch = this.ground.get(i + 1).get(j + 1);
            groundNeighbours.add(downRightPatch);
        }
    	return groundNeighbours;
    }



    //Random seeding the ground
    private void initSeeding(){
        Random ram = new Random();
        while(start_blacks > 0){
            Patch patch = ground.get(ram.nextInt(size)).get(ram.nextInt(size));
            if(patch.getAlbedo() == albedo_of_surface){
                Daisy black = new Daisy();
                black.initialiseAsBlack();
                patch.setDaisy(black);//set black
                start_blacks--;
                num_of_black++;
            }
        }
        while(start_whites > 0){
            Patch patch = ground.get(ram.nextInt(size)).get(ram.nextInt(size));
            if(patch.getAlbedo() == albedo_of_surface){
                Daisy white = new Daisy();
                white.initialiseAsWhite();
                patch.setDaisy(white);//set white
                start_whites--;
                num_of_white++;
            }
        }
    }


    //initialize csv file format
    private void initCSV(){
        try{
            csvWriter = new BufferedWriter(new FileWriter(csvfile, false));
            csvWriter.write("initial states");
			
			csvWriter.newLine();
            csvWriter.write("start-%-whites" + "," + "start-%-blacks" +","
                            + "albedo-of-whites" + "," + "albedo-of-blacks" + "," +
					        "solar-luminosity" + "," + "albedo-of-surface" + "," +
					        "scenario" + "," + "," + "end_year");
            
            csvWriter.newLine();
            csvWriter.write(start_persent_whites + "," + start_persent_blacks + ","
                            + albedo_of_whites + "," + albedo_of_blacks + "," +
                            solar_luminosity + "," + albedo_of_surface +","+
                            scenario + "," + "," + end_year);
            
            csvWriter.newLine();

            csvWriter.newLine();
            csvWriter.newLine();
            csvWriter.write("Current year" + "," + "White Daisy numbers" + "," + "Black Daisy numbers" + ","
            				+ "Global Temprature" + "," + "Luminosity");
            
            csvWriter.newLine();
            csvWriter.write(current_year + "," + num_of_white + "," + num_of_black + "," + globalTemp + "," + solar_luminosity);	
            
            csvWriter.newLine();  



            csvWriter.close();
        }catch (IOException e) {
			e.printStackTrace();
		}
    }

    //update methods
    //update global temperature
    private void updateGlobalTemp() {
        double tempSum = 0;
        int size = this.ground.size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tempSum += this.ground.get(i).get(j).getLocalTemp();
            }
        }

        this.globalTemp = tempSum / (size * size);
    }


    //update numbers of daisies
    private void updateNumbers(){
        num_of_black = 0;
        num_of_white = 0;
        
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Patch patch = ground.get(i).get(j);
                if(patch.getAlbedo() == albedo_of_whites){
                    num_of_white += 1;
                }else if(patch.getAlbedo() == albedo_of_blacks){
                    num_of_black += 1;
                }
            }
        }
    }

    private void seeding(){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Patch patch = ground.get(i).get(j);
                
                if(patch.isThereDaisy() == true){
                    patch.getDaisy().updateDaisy(getGrondNeighbours(i,j), patch.getLocalTemp());
                }
                
            }
        }
    }

    private void updatePatchTemp(){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.ground.get(i).get(j).updateTemp();             
            }
        }
    }

    private void diffusePatch(){
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                this.ground.get(i).get(j).diffuse();            
            }
        }
    }

    private void scenarioEffect(){
        if(scenario == "ramp"){
            if(current_year > 200 && current_year <= 400){
                solar_luminosity += 0.005;
            }
            if(current_year > 600 && current_year <= 800){
                solar_luminosity -= 0.0025;
            }
        }else if(scenario == "low") solar_luminosity = 0.6;
        else if(scenario == "our") solar_luminosity = 1.0;
        else if(scenario == "high") solar_luminosity = 1.4;
    }

    //writing the current status
    public void writeCSV() {
		try {
			csvWriter = new BufferedWriter(new FileWriter(csvfile, true));

            csvWriter.write(current_year + "," + num_of_white + "," + num_of_black + "," + globalTemp + "," + solar_luminosity);	
            
            csvWriter.newLine();     
            csvWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

    public void run(){
        System.out.println("start count");
        while (current_year < end_year)
            try {
                current_year+=1;
                
                scenarioEffect();

                updatePatchTemp();
                

                diffusePatch();

                seeding();

                addNeighbors(ground);

                updateNumbers();

                updateGlobalTemp();
                
                writeCSV();
                

            } catch (Exception e) {
                System.out.println(e);
            }
        }

        
    }

