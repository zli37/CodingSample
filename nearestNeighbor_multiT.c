#include <stdio.h>
#include <time.h>
#include <stdlib.h>
#include <assert.h>
#include <pthread.h>
#include <math.h>

/*
    This code is a practice of pthread library.
    The code computes the nearest neighbor of a query point in a data set.
    distance = sqrt(sum(square(q_i-p_i)))
    To compile the code:
    gcc nearestNeighbor_multiT.c -o nearestNeighbor -pthread
    GCC version 6.1.1

    To run the program:
    ./nearestNeighbor dataset.txt query.txt OUTPUT.txt NumThread
    example
    ./nearestNeighbor dataset.txt query.txt OUTPUT.txt 4
    
    The program requires well-formatted inputs. 
    
    dataset.txt:
    num_points(int) dimention(int)
    coordinates(float)
    
    query.txt:
    num_queries(int) dimention(int) // dimentions need to be the same
    coordinates(float)
    
    output.txt
    index of nearest neighbor (int)


File

*/




typedef struct __DataQuery {
	int data_point;
	int data_dimention;
	float **data;
	int query_point;
	int query_dimention;
	float **query;
	int neighbor_point;
	int *neighbor;
	// start point (inclusive)
	// end point (exclusive)
	int start_point;
	int end_point;
} DataQuery;

void *mythread(void *arg) {

	DataQuery *dataquery = (DataQuery *) arg;

	for(int k=dataquery->start_point;k<dataquery->end_point;k++) {
		dataquery->neighbor[k] = 0;
		double distance = 0.0;
		// To calculate and initialize distance, 
		// the square is ignored since the square root
		// will not affect the distance comparison
		for(int i=0;i<dataquery->query_dimention;i++) {
			distance = distance + (dataquery->query[k][i] - dataquery->data[0][i]) * (dataquery->query[k][i] - dataquery->data[0][i]);
			dataquery->neighbor[k] = 1; 
		}
		
		for(int i=1;i<dataquery->data_point;i++) {
			
			double distance_tmp = 0.0;	
	
			for(int j=0;j<dataquery->query_dimention;j++) {
				distance_tmp = distance_tmp + (dataquery->query[k][j] - dataquery->data[i][j]) * (dataquery->query[k][j] - dataquery->data[i][j]);
			}
			
			if(distance_tmp < distance) {
				distance = distance_tmp;
				dataquery->neighbor[k] = i + 1;
			}
		}
	}

	return NULL;
}



int main(int argc, char *argv[]) {
	time_t start, end;
	time (&start);
	
	// Initialize the struct
	DataQuery dataquery;
	
	// Read the dataset file
	dataquery.data_point = 0;
	dataquery.data_dimention = 0;

	FILE *fp1 = fopen(argv[1], "r");
	fscanf(fp1, "%d %d", &dataquery.data_point, &dataquery.data_dimention);
	// Initialize a 2D array
	dataquery.data = (float **) malloc(dataquery.data_point * sizeof(float *) + sizeof(DataQuery));
	for(int i=0;i<dataquery.data_point;i++) {
		dataquery.data[i] = (float *) malloc(dataquery.data_dimention * sizeof(float) + sizeof(DataQuery));
	}
	
	// fscanf the data to array
	for(int i=0;i<dataquery.data_point;i++) {
		for(int j=0;j<dataquery.data_dimention;j++) {
			fscanf(fp1, "%f", &dataquery.data[i][j]);
		}
	}
 	fclose(fp1);

	// Read the query file
	dataquery.query_point = 0;
	dataquery.query_dimention = 0;

	FILE *fp2 = fopen(argv[2], "r");
	fscanf(fp2, "%d %d", &dataquery.query_point, &dataquery.query_dimention);
	// Initialize a 2D array
	dataquery.query = (float **) malloc(dataquery.query_point * sizeof(float *) + sizeof(DataQuery));
	for(int i=0;i<dataquery.query_point;i++) {
		dataquery.query[i] = (float *) malloc(dataquery.query_dimention * sizeof(float) + sizeof(DataQuery));
	}

	// fscanf the query to array
	for(int i=0;i<dataquery.query_point;i++) {
		for(int j=0;j<dataquery.query_dimention;j++) {
			fscanf(fp2, "%f", &dataquery.query[i][j]);
		}
	}
	fclose(fp2);
	
	dataquery.neighbor_point = dataquery.query_point;
	dataquery.neighbor = (int *) malloc(dataquery.neighbor_point * sizeof(int) + sizeof(DataQuery));
	
	dataquery.start_point = 0;
	dataquery.end_point = dataquery.neighbor_point;	
	
	char *pNumb;
	int num_p = strtol(argv[4], &pNumb, 10);

	DataQuery *dataquery_thread = (DataQuery *) malloc(num_p * sizeof(DataQuery));

	int thread_size = dataquery.neighbor_point / num_p;
	
	for(int i=0;i<num_p;i++) {
		dataquery_thread[i] = dataquery;
		dataquery_thread[i].start_point = i * thread_size;
		dataquery_thread[i].end_point = (i + 1) * thread_size;
		if(i == num_p - 1)
			dataquery_thread[i].end_point = dataquery.neighbor_point;
	}	
	// Function mythread that finds the nearest neighbor
	// Because the lock has not been mentioned in the
	// statement of the homework; therefore, it is assumed
	// that lock is not needed to be implemented as no critical
	// section has been observed.
	pthread_t *p = (pthread_t *) malloc(num_p * sizeof(pthread_t));
	
	for(int i=0;i<num_p;i++) {
		pthread_create(&p[i], NULL, mythread, &dataquery_thread[i]);
	}

	for(int i=0;i<num_p;i++) {
		pthread_join(p[i], NULL);
	}
		


	// Output results to an output file
	FILE *fp3 = fopen(argv[3], "w");
	for(int i=0;i<dataquery.query_point;i++)
		fprintf(fp3, "%d\n", dataquery.neighbor[i]);
	fclose(fp3);


	// Free the memory
	for(int i=0;i<dataquery.data_point;i++)
		free(dataquery.data[i]);
	free(dataquery.data);
	for(int i=0;i<dataquery.query_point;i++)
		free(dataquery.query[i]);
	free(dataquery.query);
	free(dataquery.neighbor);
	free(p);
	free(dataquery_thread);

	time (&end);
	double dif = difftime(end, start);
	printf("Elasped time is %.01f seconds.\n", dif);
	return 0;
}
