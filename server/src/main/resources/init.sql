DROP DATABASE IF EXISTS dis_exam_David_Amlak;
CREATE DATABASE dis_exam_David_Amlak;
Use dis_exam_David_Amlak;

CREATE TABLE IF NOT EXISTS customers (
  customer_id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(40) NOT NULL,
  PRIMARY KEY (customer_id)
);

CREATE TABLE IF NOT EXISTS accounts (
  account_id INT NOT NULL AUTO_INCREMENT,
  customer_id INT NOT NULL,
  balance INT NOT NULL,
  PRIMARY KEY (account_id),
  FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

CREATE TABLE IF NOT EXISTS tellers (
teller_id INT NOT NULL AUTO_INCREMENT,
username VARCHAR(245) NOT NULL,
password VARCHAR(245) NOT NULL,
PRIMARY KEY (teller_id)
);
INSERT INTO tellers (username, password) VALUES ("David", "A8405BD50FEE55EF6F8EB04870F4C0546FF08BF06FEA087C754194DEA2080DDA");
INSERT INTO customers(name) VALUES ("Philip Kaare Løventoft");
INSERT INTO accounts(customer_id, balance) VALUES (1, 1000);

