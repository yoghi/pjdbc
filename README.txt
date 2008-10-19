

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