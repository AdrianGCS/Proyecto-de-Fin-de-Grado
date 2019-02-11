<?php

	
	// Datos de la base de datos
	$usuario = "Consultas";
	$password = "Consultas";
	$servidor = "localhost";
	$basededatos = "proyecto";
	

	$conexion = mysqli_connect( $servidor, $usuario,$password ) or die ("No se ha podido conectar al servidor de Base de datos");
	

	$db = mysqli_select_db( $conexion, $basededatos ) or die ( "Upps! Pues va a ser que no se ha podido conectar a la base de datos" );

	$consulta = "INSERT INTO `usuario` (`ID`, `Nombre`, `Apellido`) VALUES (NULL, 'prueba', 'txr');";

	mysqli_close( $conexion );
?>