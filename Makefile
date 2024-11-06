#Se der erro, chamar o compile.sh antes de dar make novamente

all:
	./compile-c-projects.sh
	./compile.sh
	./run.sh