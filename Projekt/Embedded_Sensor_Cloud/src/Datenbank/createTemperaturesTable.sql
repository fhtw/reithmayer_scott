--drop table temperature

create table temperature (
    id INT PRIMARY KEY IDENTITY, 
    date DATE NOT NULL, 
    temp FLOAT NOT NULL)

--commit