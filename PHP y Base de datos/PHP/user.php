<?php
include("PDOConnection.php");

//Define some value
define("ACTION_ADD_USER", "add");
define("ACTION_LOGIN", "login");
define("RESULT_SUCCESS", 0);
define("RESULT_ERROR", 1);
define("RESULT_USER_EXISTS", 2);


$action = $_POST["action"];
$result = RESULT_ERROR;


if(isset($action))
{
	$username = $_POST["username"];
	$mail = $_POST["mail"];
	$pwd = $_POST["password"];
	
	$lastname = $_POST["lastname"];
	$id = 1 ;

	if(ACTION_ADD_USER == $action)
	{
		//Check exists user
		if(isExistUser($cnn, $mail))
		{
			$result = RESULT_USER_EXISTS;
		}
		else
		{
			insertUser($cnn, $username, $lastname);
			insertCuenta($cnn, $mail, $pwd, $id);

			$result = RESULT_SUCCESS;
		}
	}
	else{
		if(login($cnn, $mail, $pwd))
		{
			$result = RESULT_SUCCESS;
		}
		else
		{
			$result = RESULT_ERROR;
		}
	}
}
//Print result as json
echo(json_encode(array('result' => $result)));

function insertCuenta($cnn, $mail, $pwd, $id)
{
	$query = "INSERT INTO CUENTA(ID_USUARIO , CORREO, CONTRASENIA) VALUES(?, ?, ?)";
	$stmt = $cnn->prepare($query);
	$stmt->bindParam(1, $id);
	$stmt->bindParam(2, $mail);
	$stmt->bindParam(3, $pwd);
	$stmt->execute();
}
function insertUser($cnn, $username, $lastname)
{
	$query = "INSERT INTO USUARIO(NOMBRE, APELLIDO) VALUES(?, ?)";
	$stmt = $cnn->prepare($query);
	$stmt->bindParam(1, $username);
	$stmt->bindParam(2, $lastname);
	$stmt->execute();

}
function isExistUser($cnn, $mail)
{
	$query = "SELECT * FROM CUENTA WHERE CORREO = ?";
	$stmt = $cnn->prepare($query);
	$stmt->bindParam(1, $mail);
	$stmt->execute();
	$rowcount = $stmt->rowCount();
	//for debug
	//var_dump($rowcount);
	return $rowcount;
}

function sacarId($cnn, $username , $lastname)
{
	$query = "SELECT ID FROM USUARIO WHERE NOMBRE = ? AND APELLIDO = ?";
	$stmt = $cnn->prepare($query);
	$stmt->bindParam(1, $username );
	$stmt->bindParam(2,  $lastname );
	$stmt->execute();
	$row = mysqli_fetch_array( $stmt );
	return $row[1];



	
}
function login($cnn, $mail, $pwd)
{
	$query = "SELECT * FROM CUENTA WHERE CORREO = ? AND CONTRASENIA = ?";
	$stmt = $cnn->prepare($query);
	$stmt->bindParam(1, $mail);
	$stmt->bindParam(2, $pwd);
	$stmt->execute();
	$rowcount = $stmt->rowCount();
	//for debug
	//var_dump($rowcount);
	return $rowcount;
}