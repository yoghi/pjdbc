

## INFO UTILIZZO ##

Il driver si puo attivare con l'url 

	jdbc:prolog:catalogDir
	
	oppure 

	jdbc:prolog:catalogDir:defaultSchema
	
Il database fornisce : 

SELECT
INSERT
UPDATE
DELETE

DROP TABLE
CREATE TABLE

con cluasole WHERE semplici (Es: id == 1) senza annidamenti

Alcuni database di test si trovano dentro main/resources/database, dove catalog1 è il catalog completo , catalog2 catalog parziale. 

Nel jar è contenuta anche una semplice GUI per poter provare immediatamente le potenzialità del driver. Per avviarla basta lanciare il Main. 

il covering a livello di test delle classi non è completo.


## CODICE ##

Il repository con il codice : http://code.google.com/p/pjdbc/

Testato con la JAVA VM : 1.5.0 (MacOsX 10.5.7)


## RELAZIONE ##

il sorgente della relazione e la relazione si trovano nell cartella doc