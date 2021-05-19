import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class Ground extends DaisyWorldThread {
    
    private ArrayList<ArrayList<Patch>> ground;

    public Ground(int size, double solarLumin) {
        this.ground = new ArrayList<ArrayList<Patch>>();
        for (int i = 0; i < size; i++) {
            this.ground.add(new ArrayList<Patch>());
            for (int j = 0; j < size; j++) {
                ArrayList<Patch> currentRow = this.ground.get(i);
                currentRow.add(new Patch(i, j, solarLumin));
            }
        }
        addNeighbors(this.ground);
    }

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

    public Patch getPatch(int x, int y) {
        return this.ground.get(x).get(y);
    }

    public void run(){
        Integer size = this.ground.size();
        while (!isInterrupted()){
            try {
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        this.ground.get(i).get(j).updateTemp();
                        this.ground.get(i).get(j).diffuse();
                    }
                }

            } catch (InterruptedException e) {
                this.interrupt();
            }
        }

        
    }
}
