INSERT INTO Role(name) values ('ROLE_ADMIN'), ('ROLE_USER');
INSERT INTO Privilege(name) values ('READ'), ('WRITE'), ('DELETE');
INSERT INTO roles_privileges(role_id, privilege_id) values (1, 1), (1, 2);
INSERT INTO User(username, password, status, created, full_name, email) value ('trungtrandb@gmail.com', '$2a$10$TlALWvTp2CWGzNET9bJUwe13UPZraNkQGoyfzqiZynJVIt1wa1Ktm', 'ACTIVE',NOW(), 'TrungTQ', 'trungtrandb@gmail.com');
INSERT INTO users_roles(user_id, role_id) values (1, 1), (1, 2);
