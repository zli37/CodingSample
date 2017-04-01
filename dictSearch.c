#include <stdio.h>
#include <stdbool.h>
#include <string.h>
#include <stdlib.h>

#define MAX_WORD_LENGTH 65

int compare (char *, char *);
struct Node* buildTreeFromFile();
void print_preorder(struct Node*);
void print_inorder(struct Node*);
void print_postorder(struct Node*);
bool found_in_tree(char *, struct Node*);
void use_tree_searching(struct Node*);
struct Node* insert(struct Node*, char *);


struct Node {
	char  data[MAX_WORD_LENGTH];
	struct Node* left;
	struct Node* right;
};

struct Node* insert(struct Node* tree, char * word) {
	if (tree == NULL) {
		struct Node* node = (struct Node *) malloc (sizeof(struct Node));
	        strncpy(node->data,word,sizeof(node->data));
        	node->data[sizeof(node->data)-1] = '\0';
        	node->left = NULL;
        	node->right = NULL;
        	return node;
	}
	else {
		if (compare(tree->data,word) == 3) 
			tree->left = insert(tree->left,word);
		else
			tree->right = insert(tree->right,word);
		return tree;
	}
} 



// compare function to compare two words
// same -> 1
// a < b -> 2
// else -> 3
int compare (char * worda, char * wordb) {
	// length of word a and b
	int lena, lenb, lenmin;
	lena = strlen(worda);
	lenb = strlen(wordb);

	if (lena == lenb) {
		for(int i = 0;i<lena;i++) {
			if (worda[i] == wordb[i]) continue;
			else if (worda[i] < wordb[i]) return 2;
			else return 3;
		}
		return 1;
	}
	else {
		if (lena > lenb) lenmin = lenb;
		else lenmin = lena;
	
		for(int i = 0;i < lenmin;i++) {
			if (worda[i] < wordb[i]) return 2;
			else if (worda[i] > wordb[i]) return 3;
		}
		
		if (lena < lenb) return 2;
		else return 3;
	}
}	 

struct Node* buildTreeFromFile() {
	FILE *file = fopen("dictionary","r");
	if (file == NULL) {
		printf("Input file not found.\n");
		return NULL;
	}
	char word[MAX_WORD_LENGTH] ; 
	struct Node * tree = NULL;
	
	while(fscanf(file, "%s", word) == 1) {
		// use these num to build the tree
		tree = insert(tree,word);
	}
	fclose(file);
	return tree; // return tree root node here.
}



void print_preorder(struct Node* tree) {
	if (tree != NULL) {
		printf("%s ", tree->data);
		print_preorder(tree->left);
		print_preorder(tree->right);
	}
}

void print_inorder(struct Node* tree) {
	if (tree != NULL) {
		print_inorder(tree->left);
		printf("%s ", tree->data);
		print_inorder(tree->right);
	}
}

void print_postorder(struct Node* tree) {
	if (tree != NULL) {
		print_postorder(tree->left);
		print_postorder(tree->right);
		printf("%s ", tree->data);
	}

}	



bool found_in_tree(char *word_to_search, struct Node* tree) {
	// Implement this function.
	if (tree == NULL) return false;
	else {
		if (compare(word_to_search,tree->data) == 1) return true;
		else {
			if (compare(word_to_search,tree->data) == 2)
				return found_in_tree(word_to_search,tree->left);
			else
				return found_in_tree(word_to_search,tree->right); 
		}
	}
}


void use_tree_searching(struct Node* tree) {
	FILE *file = fopen("query","r");
	if (file == NULL) {
		printf("Input file not found.\n");
		return;
	}
	char word_to_search[MAX_WORD_LENGTH];
	while(fscanf(file, "%s", word_to_search) == 1) {
		if (found_in_tree(word_to_search, tree)) {
			printf("true ");
		} else {
			printf("false ");
		}
	}
	fclose(file);
}


int main (int argc , char * argv []) {
	struct Node* tree = buildTreeFromFile();
	print_preorder(tree);
	printf("\n");
	print_inorder(tree);
	printf("\n");
	print_postorder(tree);
	printf("\n");
	use_tree_searching(tree);
	printf("\n");
	free(tree);
	return 0;
}
