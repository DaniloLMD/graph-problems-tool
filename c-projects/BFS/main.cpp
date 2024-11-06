#include <iostream>
#include <set>
#include <vector>
#include <map>
#include <algorithm>
#include <stdlib.h>
#include <stdio.h>
#include <cstring>
#include <queue>

using namespace std;


//Global variables
const int V = int(1e3);
int *mat;
int N_vertices;
int distances[V], visited[V];

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
        }
    }

    fclose(filePointer);
}

// Returns true if there is an edge connecting i and j
// Returns false otherwise
bool edge(int i, int j) {
	return *(mat + i*N_vertices + j);
}

void bfs(int v){
    queue<pair<int,int>> q;
    q.push({v, 0});
    
    memset(distances, -1, sizeof(distances));
    while(q.size()){    
        v = q.front().first;
        int d = q.front().second;
        q.pop();

        if(visited[v]) continue;
        visited[v] = 1;
        distances[v] = d;


        for(int u = 0; u < N_vertices; u++){
            if(edge(v, u)){
                q.push({u, d+1});
            }
        }
    }

}

// Input the number of vertices and adjacency matrix
int main(int argc, char** argv) {
    if (argc > 1){
		graphAdjMatrixFromFile(argv[1]);

        if(argc < 3){
            cout << "ERROR = No input\n";
            return 0;
        }

        int v = atoi(argv[2]);
        if(v >= N_vertices || !isdigit(argv[2][0])){
            cout << "ERROR = Invalid input\n";
            return 0;
        }

        bfs(v);

        cout << "Distances = {";
        for(int i = 0; i < N_vertices; i++){
            cout << distances[i];
            if(i+1 != N_vertices) cout << ", ";
        }
        cout << "}\n";

		free(mat);
	}

	return 0;
}