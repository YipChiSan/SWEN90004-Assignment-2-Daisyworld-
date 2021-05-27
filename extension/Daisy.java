import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
public class Daisy {
	private volatile int age;
	private double albedo;
	private boolean alive;
	private boolean isYellow = false;
	
	// randomise the age of the daisy, only invoke in the initialise phrase
	public void initialiseRandomAge() {
		//
		Random rand = new Random();
		age = rand.nextInt(25);
	}
    
	// set daisy as black
	public void  initialiseAsBlack () {
		age = 0;
		albedo = Sim.albedo_of_blacks;
		alive = true;
	}

    // set daisy as white
	public void initialiseAsWhite () {
		age = 0;
		albedo = Sim.albedo_of_whites;
		alive = true;
	}

	// set daisy as yellow
	public void initialiseAsYellow () {
		age = 0;
		albedo = (Sim.albedo_of_blacks + Sim.albedo_of_whites) / 2;
		isYellow = true;
		alive = true;
	}

	//Check whether is a yellow daisy or not
	public boolean isYellow() {
		return this.isYellow;
	}
	
	// state transition of a daisy between years
	// invoke by patch each patch pass along with the neighbours and its local
    	// temperature
	public void updateDaisy(ArrayList<Patch> neighbours, double localTemp) {
		age++;
		// hard coded here 
		if (age <= 25) {
			
			// Yello daisy can change its color when the localTemp is too hot or too cold
			if (isYellow()) {
				// Yellow daisy's albedo cannot be higher than white daisy
				if (localTemp > 30 && this.albedo < Sim.albedo_of_whites) { 
					this.albedo += 0.025; //Increase albedo if local temp is too high
				// Yellow daisy's albedo cannot be lower than black daisy
				} else if (localTemp < 10 && this.albedo > Sim.albedo_of_blacks) {
					this.albedo -= 0.025; //Decrease albedo if local temp is too low
				}
			}
			// End Yellow daisy specific routine

			Random r = new Random();
			double seedThreshold = 
            (0.1457 * localTemp) - (0.0032 * (localTemp*localTemp)) - 0.6443;
			double randomDouble = r.nextDouble();
			if (randomDouble < seedThreshold) {
				for (Iterator<Patch> iterator = neighbours.iterator(); 
                iterator.hasNext();) {
					Patch p = (Patch) iterator.next();
					if (p.isThereDaisy() ==  false) {
						Daisy d = new Daisy();
						if (this.albedo == Sim.albedo_of_blacks) {
							d.initialiseAsBlack();
						}
						if (this.albedo == Sim.albedo_of_whites) {
							d.initialiseAsWhite();
						}
						if (isYellow()) {
							d.initialiseAsYellow();
						}
						p.setDaisy(d);
						break;
					}
				}
			}
		}
		else {
            // exceed lifespan 25 
			alive = false;
		}
	}
	
	// returns the albedo of the daisy
	public double getAlbedo() {
		return this.albedo;
	}
	
	// if this returned false the patch should set the daisy as null in patch
	// the the resource will get GC
	public boolean isAlive() {
		return this.alive;
	}	
}
