docker run -d --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password -e MYSQL_ROOT_HOST=% -e MYSQL_DATABASE=mySchema mysql/mysql-server:latest
change ddl-auto: create