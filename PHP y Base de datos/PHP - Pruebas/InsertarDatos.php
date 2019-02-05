<?php
require_once 'login.php';//archivo login.php tendria que guardar los datos
//se puede sustituir por db_connect
$db_server = mysql_connect($db_hostname, $db_username, $db_password);
if (!$db_server) die("No puede conectar con MySQL: " . mysql_error());
mysql_select_db($db_database, $db_server)
or die("No puede seleccionar la base de datos: " . mysql_error());
if (isset($_POST['delete']) && isset($_POST['Nombre']))
{
$Nombre = get_post('isbn');
$query = "DELETE FROM 'usuario' WHERE Nombre='$Nombre'";
if (!mysql_query($query, $db_server))
echo "DELETE failed: $query
" .
mysql_error() . "

";
}
if (isset($_POST['Nombre']) 
{
$isbn = get_post('Nombre');
$query = "INSERT INTO clasicos VALUES" .
"('$Nombre')";
if (!mysql_query($query, $db_server))
echo "INSERT failed: $query
" .
mysql_error() . "

";
}
echo <<<_END
<form action="sqltest.php" method="post">
Nombre <input type="text" name="Nombre">
<input type="submit" value="Añade registro">
</form>
_END;
$query = "SELECT * FROM clasicos";


}
?>