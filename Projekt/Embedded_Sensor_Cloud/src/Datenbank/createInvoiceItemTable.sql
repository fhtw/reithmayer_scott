drop table invoiceItems

create table invoiceItems (
    id INT PRIMARY KEY IDENTITY, 
    description VARCHAR(50) NOT NULL,
    amount INT NOT NULL,
    price INT NOT NULL,
    ust INT NOT NULL,
    invoiceId INT NOT NULL constraint fk_invoice_id foreign key references invoice(id),

)

--commit