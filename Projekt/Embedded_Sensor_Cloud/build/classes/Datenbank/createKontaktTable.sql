drop table contacts

create table contacts (
    id INT PRIMARY KEY IDENTITY, 
    uid VARCHAR(50),
    compName VARCHAR(50),
    firstName VARCHAR(50),
    name VARCHAR(50),
    compId INT,
    birth DATE,
    address VARCHAR(50) NOT NULL,
    plz INT NOT NULL,
    city VARCHAR(50) NOT NULL
)

--commit