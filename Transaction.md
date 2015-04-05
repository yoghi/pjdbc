Le transazioni possibili:

  * TRANSACTION\_NONE nessuna transazione
  * TRANSACTION\_READ\_UNCOMMITTED transizioni senza protezione
  * TRANSACTION\_READ\_COMMITTED transizioni con "dearty-read" protection
  * TRANSACTION\_REPEATABLE\_READ transizioni con "dearty-read e non-repeatable read" protection
  * TRANSACTION\_SERIALIZABLE transizioni full-protection