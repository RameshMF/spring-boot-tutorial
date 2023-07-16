
create table if not exists persistent_logins ( 
  username varchar(100) not null, 
  series varchar(64) primary key, 
  token varchar(64) not null, 
  last_used timestamp not null
);

CREATE TABLE IF NOT EXISTS users (
  id INT PRIMARY KEY,
  name VARCHAR(256) NOT NULL,
  email VARCHAR(256) NOT NULL,
  password VARCHAR(256) NOT NULL,
  CONSTRAINT uc_users_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS roles (
  id INT PRIMARY KEY,
  name VARCHAR(64) NOT NULL,
  CONSTRAINT uc_roles_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS user_role (
  user_id INT,
  role_id INT,
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (role_id) REFERENCES roles(id)
);

create table if not exists messages (
  id int primary key,
  content varchar(256) not null
);

delete from  user_role;
delete from  roles;
delete from  users;


INSERT INTO roles (id, name) VALUES 
(1, 'ROLE_ADMIN'),
(2, 'ROLE_ACTUATOR'),
(3, 'ROLE_USER');

INSERT INTO users (id, email, password, name) VALUES 
(1, 'admin@gmail.com', '$2a$10$hKDVYxLefVHV/vtuPhWD3OigtRyOykRLDdUAp80Z1crSoS1lFqaFS', 'Admin'),
(3, 'user@gmail.com', '$2a$10$ByIUiNaRfBKSV6urZoBBxe4UbJ/sS6u1ZaPORHF9AtNWAuVPVz1by', 'User');


insert into user_role(user_id, role_id) values
(1,1),
(1,2),
(1,3),
(3,2);
