package tsp.algorithm.tournament;

import tsp.algorithm.individual.Chromosome;
import tsp.algorithm.population.Population;
import tsp.algorithm.util.FitnessCalculator;
import tsp.algorithm.util.RandomGenerator;

public class TournamentChooser implements Chooser {

	private FitnessCalculator fitnessCalculator;
	private RandomGenerator randomGenerator;

	private int tournamentSize;
	
	public TournamentChooser(FitnessCalculator fitnessCalculator, int tournamentSize) {
		this.randomGenerator = new RandomGenerator();
		this.fitnessCalculator = fitnessCalculator;
		this.tournamentSize = tournamentSize;
	}

	public TournamentChooser(RandomGenerator randomGenerator, FitnessCalculator fitnessCalculator, int tournamentSize) {
		this.randomGenerator = randomGenerator;
		this.fitnessCalculator = fitnessCalculator;
		this.tournamentSize = tournamentSize;
	}

	public Chromosome choose(Population population) {
		Chromosome bestParticipant = null;
		double bestParticipantFitness = 0.0;
		
		for (int i = 0; i < tournamentSize; i++) {
			int randomIndex = randomGenerator.generateIntInRangeInclusive(0, population.getSize() - 1);
			
			Chromosome participant = population.getChromosome(randomIndex);
			double participalFitness = fitnessCalculator.calculateFitness(participant);
			
			if(participalFitness > bestParticipantFitness) {
				bestParticipant = participant;
				bestParticipantFitness = participalFitness;
			}
			
		}

		return bestParticipant;
	}

	@Override
	public void resetForNewPopulation() {
		// do nothing
	}

}
