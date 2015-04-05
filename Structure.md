Visione generale di un driver JDBC;


## I componenti ##

  * Driver
  * Connection
  * Statement
  * ResultSet

## Comportamento ##

Il **Driver** viene invocato dal DriverManger del java e fornisce un oggetto **Connection** con il quale il richiedente presso il DriverManager potrà interagire con il database prolog. Una volta ottenuta la connessione sarà possibile richiedere uno **Statement** per le interrogazioni;  è inoltre sempre a livello di connessione che si hanno le funzionalità per le _transizioni_ (commit,rollback). Lo **Statement** esegue le operazioni di interrogazione e restituisce un **ResultSet**

## Transazioni ##
Le [transazioni](Transaction.md) proteggono da :

  * dirty reads
  * non-repeatable reads
  * phantom reads

e possono essere o meno supportate (anche in parte) dall'engine.