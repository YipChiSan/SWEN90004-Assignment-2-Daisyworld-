
public class Sim {
	public static double albedo_of_whites;
	public static double albedo_of_blacks;
    public static void main(String[] args) {


        double start_persent_whites = Double.parseDouble(args[0]);
        double start_persent_black = Double.parseDouble(args[1]);
        double start_persent_yellows = Double.parseDouble(args[2]);
        albedo_of_whites = Double.parseDouble(args[3]);
        albedo_of_blacks = Double.parseDouble(args[4]);
        

        String scenario = "maintain";
        switch(args[5]){
            case "ramp":
                scenario = "ramp";
                break;
            case "maintain":
                scenario = "maintain";
                break;
            case "low":
                scenario = "low";
                break;
            case "our":
                scenario = "our";
                break;
            case "high":
                scenario = "high";
                break;
        }

        double solar_luminosity = Double.parseDouble(args[6]);
        double albedo_of_surface = Double.parseDouble(args[7]);
        int end_year = Integer.parseInt(args[8]);

        if(start_persent_black + start_persent_whites + start_persent_yellows > 100 ||
	   albedo_of_whites >0.99 ||
	   albedo_of_blacks > 0.99 ||
	   solar_luminosity > 3 ||
	   albedo_of_surface > 1){
            System.out.println("invalid input detected.");
        }else{
            Ground ground = new Ground(30, 
				       solar_luminosity,
				       start_persent_whites, 
				       start_persent_black, 
				       start_persent_yellows, 
				       albedo_of_blacks, 
				       albedo_of_whites, 
				       scenario,
				       albedo_of_surface, 
				       end_year);
            ground.run();
        }

    }

    
}
