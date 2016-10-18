#include "ParallelCalculations.h"
#include "Functions.h"
#include <iostream>
#include <mpi.h>
#include <limits>

using namespace std;

void ParallelCalculations::calc(double **table, int size, int largerSegmentSize, int repetitions){
	int smallerSegmentSize = largerSegmentSize / 2;
	int processNumber;
	int rank;
	double localCalculations[3];
	localCalculations[0] = 0;
	localCalculations[1] = std::numeric_limits<double>::max(); // min
	localCalculations[2] = -1 * std::numeric_limits<double>::max(); // max
	MPI_Comm_size(MPI_COMM_WORLD, &processNumber);
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
	int lineSize = size / largerSegmentSize;
	int largeBlockNumber = lineSize * lineSize;
	int smallBlockNumber = lineSize * 4 + 1;
	int segmentNumber = largeBlockNumber + smallBlockNumber;
	int segment = rank;
	Functions f;
	while (segment < segmentNumber){
		if (segment < largeBlockNumber){
			int line = (segment / lineSize) * largerSegmentSize;
			int col = (segment % lineSize) * largerSegmentSize;
			//cout << "ja process " << rank << " licze duzy segment: " << segment << " tablica[" << line << "][" << col << "]" << endl;

			double **vectors = new double*[repetitions + 1];
			for (int i = 0; i < repetitions; i++){
				vectors[i] = new double[largerSegmentSize*largerSegmentSize];
			}
			int it = 0;
			for (int i = line; i < line + largerSegmentSize; i++){
				for (int j = col; j < col + largerSegmentSize; j++){
					vectors[0][it++] = table[i][j];
				}
			}

			for (int i = 0; i < repetitions; i++){
				vectors[i + 1] = f.large(vectors[i]);
			}


			for (int i = 0; i < largerSegmentSize*largerSegmentSize; i++){
				double num = f.veryComplicatedFunction(vectors[repetitions][i]);
				localCalculations[0] += num;
				if (localCalculations[1] > num){
					localCalculations[1] = num;
				}
				if (localCalculations[2] < num){
					localCalculations[2] = num;
				}
			}

			it = 0;
			for (int i = line; i < line + largerSegmentSize; i++){ // zapisanie wyniku do tablicy
				for (int j = col; j < col + largerSegmentSize; j++){
					table[i][j] = vectors[repetitions][it++];
				}
			}

			for (int i = 0; i < repetitions + 1; i++){ // delete vectors
				delete[] vectors[i];
			}
			delete[] vectors;
		}
		else{
			int segment2 = segment - largeBlockNumber;
			if (segment2 < (smallBlockNumber / 2)){  //columns
				int line = segment2*smallerSegmentSize;
				int col = largerSegmentSize * lineSize;
				//cout << "ja process " << rank << " licze male segmenty1: " << segment << " tablica[" << line << "][" << col << "]" << endl;

				double **vectors = new double*[repetitions + 1];
				for (int i = 0; i < repetitions; i++){
					vectors[i] = new double[smallerSegmentSize*smallerSegmentSize];
				}
				int it = 0;
				for (int i = line; i < line + smallerSegmentSize; i++){
					for (int j = col; j < col + smallerSegmentSize; j++){
						vectors[0][it++] = table[i][j];
					}
				}

				for (int i = 0; i < repetitions; i++){
					vectors[i + 1] = f.small(vectors[i]);
				}


				for (int i = 0; i < smallerSegmentSize*smallerSegmentSize; i++){
					double num = f.veryComplicatedFunction(vectors[repetitions][i]);
					localCalculations[0] += num;
					if (localCalculations[1] > num){
						localCalculations[1] = num;
					}
					if (localCalculations[2] < num){
						localCalculations[2] = num;
					}
				}

				/*it = 0;
				for (int i = line; i < line + smallerSegmentSize; i++){ // zapisanie wyniku do tablicy
				for (int j = col; j < col + smallerSegmentSize; j++){
				table[i][j] = vectors[repetitions][it++];
				}
				}*/

				for (int i = 0; i < repetitions + 1; i++){ // delete vectors
					delete[] vectors[i];
				}
				delete[] vectors;
			}
			else{ // lines
				int col = (segment2 - (smallBlockNumber / 2)) * smallerSegmentSize;
				int line = largerSegmentSize * lineSize;
				//cout << "ja process " << rank << " licze male segmenty2: " << segment << " tablica[" << line << "][" << col << "]" << endl;


				double **vectors = new double*[repetitions + 1];
				for (int i = 0; i < repetitions; i++){
					vectors[i] = new double[smallerSegmentSize*smallerSegmentSize];
				}
				int it = 0;
				for (int i = line; i < line + smallerSegmentSize; i++){
					for (int j = col; j < col + smallerSegmentSize; j++){
						vectors[0][it++] = table[i][j];
					}
				}

				for (int i = 0; i < repetitions; i++){
					vectors[i + 1] = f.small(vectors[i]);
				}


				for (int i = 0; i < smallerSegmentSize*smallerSegmentSize; i++){
					double num = f.veryComplicatedFunction(vectors[repetitions][i]);
					localCalculations[0] += num;
					if (localCalculations[1] > num){
						localCalculations[1] = num;
					}
					if (localCalculations[2] < num){
						localCalculations[2] = num;
					}
				}

				/*it = 0;
				for (int i = line; i < line + smallerSegmentSize; i++){ // zapisanie wyniku do tablicy
				for (int j = col; j < col + smallerSegmentSize; j++){
				table[i][j] = vectors[repetitions][it++];
				}
				}*/

				for (int i = 0; i < repetitions + 1; i++){ // delete vectors
					delete[] vectors[i];
				}
				delete[] vectors;
			}

		}
		segment += processNumber;
	}

	if (rank != 0) {
		MPI_Send(localCalculations, 3, MPI_DOUBLE, 0, 10, MPI_COMM_WORLD);
	}
	else{
		avg = localCalculations[0];
		min = localCalculations[1];
		max = localCalculations[2];
		MPI_Status status;
		for (int i = 1; i < processNumber; i++) {
			double msg[3];
			MPI_Recv(msg, 3, MPI_DOUBLE, i, 10, MPI_COMM_WORLD, &status);

			avg += msg[0];
			if (min > msg[1]){
				min = msg[1];
			}
			if (max < msg[2]){
				max = msg[2];
			}
		}
		avg /= (size*size);
	}


	/*if (rank == 0){
		for (int i = 0; i < size; i++){
		for (int j = 0; j < size; j++){
		cout << table[i][j] << " ";
		}
		endl(cout);
		}
		cout << localCalculations[0] << " " << localCalculations[1] << " " << localCalculations[2] << endl;
		}*/
}

double ParallelCalculations::getAvg(){
	return avg;
}

double ParallelCalculations::getMin(){
	return min;
}

double ParallelCalculations::getMax(){
	return max;
}