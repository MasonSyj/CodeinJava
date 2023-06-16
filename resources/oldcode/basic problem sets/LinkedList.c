#include <stdio.h>
#include <stdlib.h>

typedef struct node *ptrl;
struct node{
	int data;
	ptrl next;
};

//ptrl p;
//struct node node;

void add(int value, ptrl p){
	ptrl new = (ptrl)malloc(sizeof (struct node));
	ptrl temp = p;
	while (temp->next)
		temp = temp->next;
	temp->next = new;
	new->data = value;
	new->next = NULL;
}

ptrl findKth(int index, ptrl p){
	ptrl temp = p;
	int i = 0;
	while (temp != NULL && i < index){
		temp = temp->next;
		i++;
	}
	if (i == index)
		return temp;
	else{
		printf("Out of range\n,return 0");
		return NULL;
	}
}

int find(int value, ptrl p){
	ptrl temp = p;
	int index = 0;
	while (temp->data != value && temp != NULL){
		temp = temp->next;
		index++;
	}
	if (temp == NULL){
		printf("Not exist, return 0");
		return 0;
	}
	else {
		return index;
	}
}

void insertKth(int value, int index, ptrl p){
	ptrl insertion = (ptrl)malloc(sizeof (struct node));
	insertion->data = value;
	ptrl old = findKth(index, p);
	if (old == NULL){
		printf("index out of range, add to the last.\n");
		add(value, p);
	}
	else if (old == p){
		insertion->next = p;
		insertion = p;
	}
	else{
		insertion->next = old->next;
		old->next = insertion;
	}
}

void showAll(ptrl p){
	ptrl temp = p;
	while (temp-> next){
		printf("%d   ", temp->data);
		temp = temp->next;
	}
}

int main(int argc, char *argv[]) {
	ptrl p = (ptrl)malloc(sizeof (struct node));
	p->data = 10;
	p->next = NULL;
	add(12, p);
	add(15, p);
	add(18, p);
	add(25, p);
	add(233, p);
	int x = findKth(1, p)->data;
	int y = findKth(2, p)->data;
	printf("%d, %d\n", x, y);
//	insertKth(1, 4, p);
//	showAll(p);
	
}