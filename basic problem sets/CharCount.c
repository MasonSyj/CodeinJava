#include <stdio.h>

int main(int argc, char *argv[]) {
	int numBlank = 0;
	int numNUmber = 0;
	int numLetter = 0;
	int numOther = 0;
	char c;
	while ( (c = getchar()) != '\n'){
		if (c == ' ')
			numBlank++;
		else if (c >= '0' && c <= '9')
			numNUmber++;
		else if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
			numLetter++;
		else 
			numOther++;
	}
	printf("numBlank= %d, numNUmber= %d, numLetter= %d, numOther= %d", numBlank, numNUmber, numLetter, numOther);
}