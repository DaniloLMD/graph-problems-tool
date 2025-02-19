#include <iostream>
#include <set>
#include <vector>
#include <map>
#include <algorithm>
#include <stdlib.h>
#include <stdio.h>
#include <cstring>
#include <bitset>
#include <bits/stdc++.h>

using namespace std;
using ll = long long;

//Global variables
const int V = 20;

int *mat;
int N_vertices;
vector<pair<int,int>> arestas;
int curr_time = 1;

// Declarations
void graphAdjMatrixFromFile(char *fileName);
bool edge(int i, int j);


//Definitions

// Creates an adjacency matrix from file
void graphAdjMatrixFromFile(char *fileName) {
    if (fileName == NULL) {
        return;
    }

    FILE * filePointer;
    char line[1000];
    filePointer = fopen(fileName, "r");
    //fgets(line, 4, filePointer); // to ignore title
    fgets(line, 1000, filePointer);

    int i = 0;
    int numberOfVertice = 0;
    int lastIndex = -1;

    while (line[i] != NULL) {
        if (line[i] == ' ' && i > lastIndex + 1) {
            numberOfVertice++;
            lastIndex = i;
        }
        i++;
    }
    if (i > 0) {
        numberOfVertice++;
    }

	N_vertices = numberOfVertice;
	mat = (int *) malloc(N_vertices * N_vertices * sizeof (int));
	
    int j = 0;
    rewind(filePointer);
    for (i = 0; i < N_vertices; i++) {
        for (j = 0; j < N_vertices; j++) {
            char c = NULL;
            while (c == ' ' || c == NULL || c == '\n') {
                fscanf(filePointer, "%c", &c);
            }
            *(mat + i*N_vertices + j) = c - '0';

            if (*(mat + i * N_vertices + j) >= 1 && i != j) {
                arestas.push_back({i, j});
            }
        }
    }

    fclose(filePointer);
}

void binary(int n) {
    if (n > 1) {
        binary(n / 2);
    }
    printf("%d", n % 2);
}

// Returns true if there is an edge connecting i and j
// Returns false otherwise
bool edge(int i, int j) {
	return *(mat + i*N_vertices + j);
}

void MaximumCut() {
    int count = 0;

    for(int mask = 0;mask <= (1 << N_vertices) - 1;mask++) {
        int temp = 0;

        for(int j = 0;j < (int)arestas.size();j++) {
            int a = arestas[j].first, b = arestas[j].second;

            if((mask & (1 << a)) != 0 && (mask & (1 << b)) != 0) {
                continue;
            }
            else if((mask & (1 << a)) == 0 && (mask & (1 << b)) == 0) {
                continue;
            }else {
                temp++;
            }
        }
        count = max(count, temp);
    }
    cout << count / 2 << "\n";
}

// Input the number of vertices and adjacency matrix
int main(int argc, char** argv) {
    if (argc > 1){
		graphAdjMatrixFromFile(argv[1]);

        MaximumCut();
		free(mat);
	}

	return 0;
}