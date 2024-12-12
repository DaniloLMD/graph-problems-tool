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
ll dp[1 << V][V];
vector<int> come_from[V];

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

void HamiltonianCycle() {
    for(int f = 0; f < (int)arestas.size();f++) {
        int start = arestas[f].first;
        int end = arestas[f].second;
        
        come_from[end].push_back(start);
        come_from[start].push_back(end);

    }

    dp[1][0] = 1;

    if(N_vertices == 2) {
        if((int)arestas.size() <= 1) {
            cout << "NOT HAMILTONIAN CYCLE\n";
        }else {
            cout << "IT IS A HAMILTONIAN CYCLE\n";
        }
        return;
    }

    for(int s = 2;s < (1 << N_vertices); s++) {
        if((s & (1 << 0)) == 0) continue;

        for(int end = 0; end < N_vertices;end++) {
            if((s & (1 << end)) == 0) continue;

            int prev = s - (1 << end);

            for(int j : come_from[end]) {
                if((s & (1 << j))) {
                    if(dp[prev][j] == 1) {
                        dp[s][end] = 1;
                    }
                }
            }
        }
    }

    int answer = 0;

    for(int i = 0;i < N_vertices;i++) {
        if(dp[(1 << N_vertices) - 1][i] == 1 && edge(i , 0) >= 1) {
            answer = 1;
        }
    }

    if(answer) {
        cout << "IT IS A HAMILTONIAN CYCLE\n";
    }else {
        cout << "IT IS NOT A HAMILTONIAN CYCLE\n";
    }
}

// Input the number of vertices and adjacency matrix
int main(int argc, char** argv) {
    if (argc > 1){
		graphAdjMatrixFromFile(argv[1]);

        HamiltonianCycle();
		free(mat);
	}

	return 0;
}