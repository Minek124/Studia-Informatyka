#include"Evolution.h"
#include"LifeRules.h"
#include <iostream>
#include <stdlib.h>
#include <omp.h>

Evolution::Evolution(){
	allocated = false; // nie ma nullptr ???
}

void Evolution::createNeighbours(bool **table, int sideLength) {

	if (!allocated){
		neighboursCounter = new int*[sideLength];
		for (int i = 0; i < sideLength; i++){
			neighboursCounter[i] = new int[sideLength];
		}
		allocated = true;
	}

#pragma omp parallel for collapse(2)
	for (int i = 0; i < sideLength; i++){
		for (int j = 0; j < sideLength; j++){
			neighboursCounter[i][j] = 0;
		}
	}

#pragma omp parallel for collapse(2)
	for (int i = 1; i < sideLength - 1; i++){
		for (int j = 1; j < sideLength - 1; j++){
			if (table[i - 1][j - 1]){
				neighboursCounter[i][j] += 1;
			}
			if (table[i - 1][j]){
				neighboursCounter[i][j] += 1;
			}
			if (table[i - 1][j + 1]){
				neighboursCounter[i][j] += 1;
			}
			if (table[i][j - 1]){
				neighboursCounter[i][j] += 1;
			}
			if (table[i][j + 1]){
				neighboursCounter[i][j] += 1;
			}
			if (table[i + 1][j - 1]){
				neighboursCounter[i][j] += 1;
			}
			if (table[i + 1][j]){
				neighboursCounter[i][j] += 1;
			}
			if (table[i + 1][j + 1]){
				neighboursCounter[i][j] += 1;
			}
		}
	}
}

void Evolution::evolve(bool **table, int sideLength, int steps) {
	int seed;
	double x, y;
	struct drand48_data drand_buf;
	
	for (int step = 0; step < steps; step++){

		createNeighbours(table, sideLength);

#pragma omp parallel private(x, y, seed, drand_buf)
		{
			seed = 1202107158 + omp_get_thread_num() * 1999;
			srand48_r(seed, &drand_buf);
#pragma omp for collapse(2)
			for (int i = 1; i < sideLength - 1; i++){
				for (int j = 1; j < sideLength - 1; j++){
					drand48_r(&drand_buf, &x);
					drand48_r(&drand_buf, &y);
					table[i][j] = LifeRules::isOn(table[i][j], neighboursCounter[i][j], x, y); // nie trzeba synchronizacji
				}
			}
		}
	}
}
void Evolution::neighbourCount(bool **table, int sideLength, long *result) {

#pragma omp parallel
	{
		int count;
		long localVector[9];
		for (int i = 0; i < 9; i++){
			localVector[i] = 0;
		}
#pragma omp for collapse(2) nowait
		for (int i = 1; i < sideLength - 1; i++){
			for (int j = 1; j < sideLength - 1; j++){
				count = 0;
				if(table[i][j]){
					localVector[0]+=1;
					if (table[i - 1][j - 1]){
						count+=1;
					}
					if (table[i - 1][j]){
						count+=1;
					}
					if (table[i - 1][j + 1]){
						count+=1;
					}
					if (table[i][j - 1]){
						count+=1;
					}
					if (table[i][j + 1]){
						count+=1;
					}
					if (table[i + 1][j - 1]){
						count+=1;
					}
					if (table[i + 1][j]){
						count+=1;
					}
					if (table[i + 1][j + 1]){
						count+=1;
					}
					localVector[count] += 1;
				}
			}
		}
#pragma omp critical
		{
			result[0] += localVector[0];
			result[1] += localVector[1];
			result[2] += localVector[2];
			result[3] += localVector[3];
			result[4] += localVector[4];
			result[5] += localVector[5];
			result[6] += localVector[6];
			result[7] += localVector[7];
			result[8] += localVector[8];
		}
	}
}



