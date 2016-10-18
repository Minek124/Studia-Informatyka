#ifndef EVOLUTION_H
#define EVOLUTiON_H

class Evolution {
	public:

		int **neighboursCounter;
		bool allocated;
		Evolution();
		void createNeighbours(bool **table, int sideLength);
		void evolve( bool **table, int sideLength, int steps );
		void neighbourCount( bool **table, int sideLength, long *result );
};

#endif

