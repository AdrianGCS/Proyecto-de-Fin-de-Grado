<?php
define('DB_USER', "Consultas"); // db user
define('DB_PASSWORD', "Consultas"); // db password 
define('DB_DATABASE', "proyecto"); // database name
define('DB_SERVER', "localhost"); // db server
 
$con = mysqli_connect(DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE);
// en teoria se puede borrar DB_DATABASE poniendolo asi
// mysql_select_db($db_database)
// Check connection
if(mysqli_connect_errno()){
	echo "Failed to connect to MySQL: " . mysqli_connect_error();
}
?>