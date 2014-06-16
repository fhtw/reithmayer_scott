drop table invoice

create table invoice (
    id INT PRIMARY KEY IDENTITY (100001, 1), 
    date DATE NOT NULL,
    contactId INT NOT NULL constraint fk_contact_id foreign key references contacts(id),
    dueDate DATE NOT NULL,
    comment VARCHAR(50),
    message VARCHAR(250)
)

--commit

