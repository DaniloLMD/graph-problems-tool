#include <iostream>
#include <set>
#include <vector>
#include <map>
#include <algorithm>
#include <stdlib.h>
#include <stdio.h>
#include <cstring>

using namespace std;


//Global variables
const int V = int(1e3);
int *mat;
int N_vertices;
int tin[V], tout[V], visited[V];
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
        }
    }

    fclose(filePointer);
}

// Returns true if there is an edge connecting i and j
// Returns false otherwise
bool edge(int i, int j) {
	return *(mat + i*N_vertices + j);
}

void dfs(int v, int p){
    if(visited[v]) return;
    visited[v] = 1;
    tin[v] = curr_time++;

    for(int u = 0; u < N_vertices; u++){
        if(u == p) continue;
        if(edge(u, v)) dfs(u, v);
    }

    tout[v] = curr_time++;
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
        dfs(v, v);
        for(int i = 0; i < N_vertices; i++){    
            if(!visited[i]) dfs(i,i);            
        }

        vector<pair<int , int>> ordem;
        for(int i = 0; i < N_vertices; i++){
            ordem.push_back({tin[i], i});
        }
        
        sort(ordem.begin(), ordem.end());

        cout << "{";
        for(int i = 0;i < N_vertices;i++) {
            if(i + 1 != N_vertices) {
                cout << ordem[i].second << " , ";
            }else {
                cout << ordem[i].second;
            }
        }
        cout << "}";

		free(mat);
	}

	return 0;
}