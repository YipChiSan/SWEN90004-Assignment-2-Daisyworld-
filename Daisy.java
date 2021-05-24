import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
public class Daisy {
	private volatile int age;
	private double albedo;
	private boolean alive;
	//////////////////////
	
	public void  initialiseAsBlack () {
		age = 0;
		albedo = Sim.albedo_of_blacks;
		alive = true;
	}
	public void initialiseAsWhite () {
		age = 0;
		albedo = Sim.albedo_of_whites;
		alive = true;
	}
	
	public void updateDaisy(ArrayList<Patch> neighbours, double localTemp) {
		
		age++;
		// hard coded here 
		if (age <= 25) {
			Random r = new Random();
			double seedThreshold = (0.1457 * localTemp) - (0.032 * (localTemp*localTemp)) - 0.6443;
			double randomDouble = r.nextDouble();
			if (randomDouble < seedThreshold) {
				for (Iterator<Patch> iterator = neighbours.iterator(); iterator.hasNext();) {
					Patch p = (Patch) iterator.next();
					if (p.isThereDaisy() ==  false) {
						Daisy d = new Daisy();
						if (this.albedo == Sim.albedo_of_blacks) {
							d.initialiseAsBlack();
						}
						if (this.albedo == Sim.albedo_of_whites) {
							d.initialiseAsWhite();
						}
						p.setDaisy(d);
						break;
					}
				}
			}
		}
		else {
			alive = false;
		}
			
	}
	
	public double getAlbedo() {
		return albedo;
	}
	public boolean isAlive() {
		// if this returned false the patch should set the daisy as null in patch
		// the the resource will get GC
		return alive;
	}
	
}