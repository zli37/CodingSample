#include <stdlib.h>
#include <stdio.h>
#include <stdbool.h>
#include <string.h>

#define MAX_NUM_LENGTH 10000

void multiply_and_print(char* num1, char* num2) {
	
	int num1_int[MAX_NUM_LENGTH] = {0};
	int num2_int[MAX_NUM_LENGTH] = {0};
	int mul_int[MAX_NUM_LENGTH] = {0};
	int len1, len2, lenmax;
	int temp;

	len1 = strlen(num1);
	len2 = strlen(num2);
	lenmax = len1 + len2;

	// save string num1 and num2 to int arraies
	// Note that the first digit is stored in
	// the second position of the array
	for (int i = 1;i <= len1;i++) {
		num1_int[i] = num1[len1-i] - '0';
	}
	for (int i = 1;i <= len2;i++) {
		num2_int[i] = num2[len2-i] - '0';
	}

	// multiplication
	for (int i = 1;i <= lenmax;i++) {
		int digit[MAX_NUM_LENGTH] = {0};
		for (int j = 1;j <= lenmax;j++) {
				temp = mul_int[i+j-1];
				mul_int[i+j-1] = (num1_int[i] * num2_int[j] + digit[i+j-1] + temp) % 10;
				digit[i+j] = (num1_int[i] * num2_int[j] + digit[i+j-1] + temp) / 10;
		}
	}

	int k = lenmax;
	while (mul_int[k] == 0) k--;
	for (int i = k;i >= 1;i--) {
		printf("%d",mul_int[i]);
	}

	printf("\n");
}

int main (int argc , char * argv []) {
    // input file should have to large numbers
	FILE *file = fopen("input","r");
	if (file == NULL) {
		printf("Input file not found.\n");
		return 1;
	}
	char num1[MAX_NUM_LENGTH];
	char num2[MAX_NUM_LENGTH];
	while(fscanf(file, "%s %s", num1, num2) == 2) {
		multiply_and_print(num1, num2);
	}
	fclose(file);	
	return 0 ;
}

