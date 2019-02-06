<?php
//--Datos del servidor mysql
$conn = new mysqli("localhost", "Consultas", "Consultas", "proyecto");
//--Arreglo
$json = array();
// Verificar conexión
echo "[";
if ($conn->connect_error) {
   //--Cuando la conexion es fallida!
    $json['conexion']= "Conexion Fallida!";
} else{
   //--Cuando la conexión es exitosa!
   $json['conexion']  = "Conexion Exitosa!";
}

//--Imprime el mensaje resultante.
echo json_encode($json);
echo "]";
//--Cierra conexion
$conn->close();
?>