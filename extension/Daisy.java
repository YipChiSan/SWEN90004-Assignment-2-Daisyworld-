import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
public class Daisy {
	private volatile int age;
	private double albedo;
	private boolean alive;
	
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
		alive = true;
	}
	
	// state transition of a daisy between years
	// invoke by patch each patch pass along with the neighbours and its local
    	// temperature
	public void updateDaisy(ArrayList<Patch> neighbours, double localTemp) {
		age++;
		// hard coded here 
		if (age <= 25) {
			Boolean isYello = this.albedo == (Sim.albedo_of_blacks + Sim.albedo_of_whites) / 2;
			// Yello daisy can change its color when the localTemp is too hot or too cold
			if (isYello) {
				if (localTemp > 30 && this.albedo < Sim.albedo_of_whites) {
					this.albedo += 0.025;
				} else if (localTemp < 10 && this.albedo > Sim.albedo_of_blacks) {
					this.albedo -= 0.025;
				}
			}
			//
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
						if (isYello) {
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
