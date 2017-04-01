#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <assert.h>
#include <ctype.h>
#include <string.h>
#include "sort.h"
#include <sys/types.h>
#include <sys/stat.h>
#include <time.h>

void sort(rec_t *, int, int);
int separate(rec_t *, int, int);

void usage (char *prog) {
	fprintf(stderr, "Usage: %s -i inputfile -o outputfile\n", prog);
	exit(1);
}

void invalFiles (char *prog) {
	fprintf(stderr, "Error: Cannot open file %s\n", prog);
	exit(1);
}

void sort (rec_t * recArray, int a, int b) {
	int pivot;
	
	if (a < b) {
		pivot = separate(recArray, a, b);
		sort(recArray, a, pivot - 1);
		sort(recArray, pivot + 1, b);
	}
}

int separate (rec_t * recArray, int a, int b) {
	int p, i, j;
	rec_t temp;
	p = recArray[a].key;
	i = a + 1;
	j = b;

	while (1) {
		while (recArray[i].key <= p && i <= b) i++;
		while (recArray[j].key > p) j--;
		if (i >= j)
			break;
		temp = recArray[i];
		recArray[i] = recArray[j];
		recArray[j] = temp;
	} 
	
	temp = recArray[a];
	recArray[a] = recArray[j];
	recArray[j] = temp;

	return j;
}

int main (int argc, char *argv[]) {
	// arguments
	char *inFile = "/no/such/infile";
	char *outFile = "/no/such/outfile";
	
	// input params
	int c;
	opterr = 0;
	while ((c = getopt(argc, argv, "i:o:")) != -1) {
		switch (c) {
		case 'i':
			inFile = strdup(optarg);
			break;
		case 'o':
			outFile = strdup(optarg);
			break;
		default:
			usage(argv[0]);
		}
	}

	// open and create input and output file
	int input = open(inFile, O_RDONLY);
	if (input < 0) {
		invalFiles(inFile);
	}

	// finds out the size of the dataset
	int num;
	struct stat buffer;
	fstat(input, &buffer);
	num = buffer.st_size / 100;

	int output = open(outFile, O_WRONLY|O_CREAT|O_TRUNC, S_IRWXU);
	if (output < 0) {
		invalFiles(outFile);
	}

	// create a array that can hold all the dataset
	rec_t * dataset = (rec_t *) malloc( num * sizeof(rec_t));

	// reads elements from input file and store in an array
	int counter = 0;
	while (1) {
		int rf;
		rf = read(input, &dataset[counter], sizeof(rec_t));
		if (rf == 0) // 0 indicates EOF
			break;
		counter++;
		if (rf < 0) {
			perror("read");
			exit(1);
		}
	} 

	// conduct quick sort
	sort(dataset, 0, num-1);

	for(int i = 0;i < num;i++) {
		int wf;
		wf = write(output, &dataset[i], sizeof(rec_t));
		if (wf != sizeof(rec_t)) {
			perror("write");
			exit(1);
		}
	}
	
	free(dataset);	
	(void) close(input);
	(void) close(output);

	return 0;
}


