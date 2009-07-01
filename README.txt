

DUE QUERY IN FILA FUNZIONANO?? query1; query2; ... no ma dovrebbero??

NB: due colonne con lo stesso nome non possono esistere!!!


Sintassi del javacc 

void methodName():
{
	// linguaggio java 
	// inizializzo variabili etc etc
}
{
  	//linguaggio javacc per esprireme il parsing  
    // ES. ci deve essere quello che è espresso da methodName2 seguito da ;
    methodName2() ";" { //java da eseguire dopo che si è trovato il matching }

}



> NOTE 
In caso di presenza di . nella sintassi si considera : 
schema.table_name.column

> NOTE 2 
Ho una richiesta diversa a seconda che sia 
SELECT
INSERT
DELETE
UPDATE

ognuna di loro ha campi diversi 


******** COSE DA FARE

Expression,Simple Expression

Caso Insert con values presi da una SubSelect();




/* predicati */
/* Es. lookAll(10,[10,20,10,30,10],L) tutte le posizioni del 10 */
lookAll(X,L,Ps):- findall(P,lookAny(X,L,P),Ps).
/* da definire meglio ...*/

max([H|T],M):-max([H|T],M,H). 
max([H|T],M,Temp):-H>Temp, max(T,M,H). 
max([H|T],M,Temp):-H<=Temp, max(T,M,Temp). 
max([],M,M).
maggiore([H|T],Paragone,Risultato) :-  true.