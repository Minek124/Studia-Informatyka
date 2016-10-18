#include"Minimum.h"
#include<stdlib.h>   // potrzebne dla random()
#include<math.h>     // bo sin i cos
#include<iostream>
#include<sys/time.h>
#include<omp.h>

Minimum::Minimum(Function *f, double min, double max) : min(min), max(max), f(f) {
	bestX = bestY = (min + max) * 0.5;
	bestV = f->value(bestX, bestY);

	srandom((unsigned)time(NULL));
}

// metoda zwraca pozycje z wnetrza obszaru poszukiwania
double Minimum::limit(double x) {
	if (x < min) return min; // za male
	if (x > max) return max; // za duze
	return x; // x jest pomiedzy min a max, wiec zwracamy x
}

bool Minimum::haveTimeToContinue() {
	struct timeval tf;
	gettimeofday(&tf, NULL);
	double now = tf.tv_sec * 1000 + tf.tv_usec * 0.001;

	if (now < timeLimit) return true;  // limit czasu nie osiagniety
	return false; // juz po czasie, pora konczyc obliczenia
}

void Minimum::initializeTimeLimit(double msec) {
	struct timeval tf;
	gettimeofday(&tf, NULL);
	timeLimit = tf.tv_sec * 1000 + tf.tv_usec * 0.001 + msec; // ustawiamy czas zakonczenia obliczen    
}

void Minimum::find(double dr, int idleStepsLimit, double msec) {

	// ustalamy czas zakonczenia obliczen na msec od teraz
	initializeTimeLimit(msec);

	double x, y, v, xnew, ynew, vnew, r1, r2, r3;
	int seed;
	struct drand48_data drand_buf;
	unsigned tt = (unsigned)time(NULL);
	


#pragma omp parallel private(x, y, v, xnew, ynew, vnew, r1, r2, r3, seed, drand_buf)
	{
		seed = tt + omp_get_thread_num() * 1999;
		srand48_r(seed, &drand_buf);
		double localBestX = bestX;
		double localBestY = bestY;
		double localBestV = bestV;
		while (haveTimeToContinue()) {

			drand48_r(&drand_buf, &r1);
			drand48_r(&drand_buf, &r2);
			x = r1 * (max - min) + min;
			y = r2 * (max - min) + min;
			v = f->value(x, y);

			int idleSteps = 0;
			while (idleSteps < idleStepsLimit) {

				drand48_r(&drand_buf, &r3);
				
				r3 *= 6.28318;  // liczba losowa do 0 do 2 pi

				//std::cout << r3 << std::endl;
				//std::cout << sin(r3) << " " << cos(r3) << std::endl;
				xnew = x + dr * sin(r3);
				ynew = y + dr * cos(r3);

				// upewniamy sie, ze nie opuscilismy przestrzeni poszukiwania rozwiazania
				xnew = limit(xnew);
				ynew = limit(ynew);

				// wartosc funkcji w nowym polozeniu
				vnew = f->value(xnew, ynew);

				if (vnew < v) {
					//std::cout << x << " " << y << " " << xnew << " " << ynew << std::endl;
					x = xnew;  // przenosimy sie do nowej, lepszej lokalizacji
					y = ynew;
					v = vnew;
					idleSteps = 0; // resetujemy licznik krokow, bez poprawy polozenia
				}
				else {
					idleSteps++; // nic sie nie stalo
				}
			} // przez idleStepsLimit nic znalezlismy lepszej pozycji
			if (v < localBestV) {  // znalezlismy najlepsze polozenie dla watku
				localBestV = v;
				localBestX = x;
				localBestY = y;
				//std::cout << "New better position: " << x << ", " << y << " value = " << v << std::endl;
			}
		}
#pragma omp critical
		{
			if (localBestV < bestV) {  // znalezlismy najlepsze polozenie globalnie
				bestV = localBestV;
				bestX = localBestX;
				bestY = localBestY;
				std::cout << "New better FINAL position: " << localBestX << ", " << localBestY << " value = " << localBestV << std::endl;
			}
		}
	}
}

