/** select all */
select * from employee;

/** equivalenze */
select name from employee;
select employee.name from employee;
select e.name from employee as e;
select $0 from employee;
select e.$0 from employee as e;
select employee.$0 from employee as e;
select employee.$0 from employee;

/** WHERE */
select $0,$1,$2 from employee where ( $0 > 1000 OR $2 < 2000 );
select $0,$1,$2 from employee where ( $0 > 1000 AND $2 < 2000 );
select $0,$1,$2 from employee where NOT $0 > 1000;

/** JOIN A 2 - FULL JOIN */
select employee.salary, dept.name from employee,dept;
select e.$0,e.$1,d.$1 from employee as e, dept as d;
select e.$0,e.$1 from employee as e, dept as d;

/** JOIN A 2 - FULL JOIN - CON WHERE CLAUSOLE */
select e.$0,e.$1,d.$1 from employee as e, dept as d where ( (e.$0 > 1000) OR (d.$1 > 1) );

/** JOIN A 2 CON WHERE CLAUSOLE x JOIN */
select e.$0,e.$1,d.$1 from employee as e, dept as d WHERE e.$0 = d.$1;
select e.$0,e.$1,d.$1 from employee as e, dept as d WHERE ( (e.$0 = d.$1) AND (d.$1 = 'smith') );

/** JOIN A 3 CON WHERE CLAUSOLE */
select e.$1,d.$0 from employee as e , dept as d , eta as et 
	where (e.$0 = d.$1) AND ( e.$1 = et.$0 ) AND (et.$1 < 40) AND ( e.$2 >= 1000 ) ;

select e.$1,d.$0 from employee as e , dept as d , eta as et 
	where (e.$0 = d.$1) AND ( e.$1 = et.$0 ) AND (et.$1 < 40) OR ( e.$2 > 2000 );
