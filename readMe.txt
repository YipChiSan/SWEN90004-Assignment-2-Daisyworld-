Step 1: Compile this java file, e.g. javac Sim.java

Version 1: Code without extension
Step 2: run the Sim.java file with 8 arguments,
		1st argument is the start percentage of white daisy, range between 0 to 50.
		2nd argument is the start percentage of black daisy, range between 0 to 50.
		3rd argument is the albedo of white daisy, range between 0.00 to 0.99.
		4th argument is the albedo of black daisy, range between 0.00 to 0.99.
		5th argument is the scenario of current test, there are 5 types: maintain, our, low, high, ramp.
		6th argument is solar luminosity in the world, range between 0.001 to 3.000.
		7th argument is the albedo of surface, range between 0.00 to 1.00
		8th argument is the max year, must be an integer, sugguest to be 1500.

For example: java Sim 20 20 0.75 0.25 maintain 0.8 0.4 1500

Version 2: Code with extension
Step 2: run the Sim.java file with 10 arguments,
		1st argument is the start percentage of white daisy, range between 0 to 100.
		2nd argument is the start percentage of black daisy, range between 0 to 100.
		3rd argument is the start percentage of yellow daisy, range between 0 to 100. The sum of first three arguments must be less than 100.

		4rd argument is the albedo of white daisy, range between 0.00 to 1.00
		5th argument is the albedo of black daisy, range between 0.00 to 1.00

		6th argument is the scenario of current test, there are 5 types: maintain,our, low, high, ramp.
		7th argument is solar luminosity in the world, range between 0.001 to 3.
		8th argument is the albedo of surface, range between 0.00 to 1.00
		9th argument is the max year, must be an integer, sugguest to be 1500.

For example: java Sim 20 20 5 0.75 0.25 maintain 0.8 0.4 1500

Step 3: Result
The result will be saved in a csv file, DaisyWorld.csv.
