#ifndef PARALLELCALCULATIONS_H
#define PARALLELCALCULATIONS_H

class ParallelCalculations {
	public:
		double avg;
		double max;
		double min;
		void calc( double **table, 
		           int size, 
		           int largerSegmentSize, 
		           int repetitions );
		double getAvg();
		double getMin();
		double getMax();
};

#endif

