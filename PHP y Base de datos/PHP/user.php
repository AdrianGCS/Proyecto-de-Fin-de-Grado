<?php
include("PDOConnection.php");



define("ACTION_ADD_USER", "add");
define("ACTION_LOGIN", "login");
define("ACTION_ENFERMO", "enfermo");
define("RESULT_SUCCESS", 0);
define("RESULT_ERROR", 1);
define("RESULT_USER_EXISTS", 2);

$action = $_POST["action"];
$result = RESULT_ERROR;

$Encript= 8;
$id = 2;
$idEnfermo = 0;
$imei = 0;
$telefono = 0;

switch ($action) {

	case "add":

		$username = $_POST["username"];
		$lastname = $_POST["lastname"];
		$mail = $_POST["mail"];
		if(isExistUser($cnn, $mail))
		{
			$result = RESULT_USER_EXISTS;
		}
		else
		{	

			$mail = $_POST["mail"];
			$pwd = $_POST["password"];
			$id=insertUser($cnn, $username, $lastname);
			insertCuenta($cnn, $mail, $pwd, $id);
			$result = RESULT_SUCCESS;
		}

		break;

		case "enfermo":

		$username = $_POST["username"];
		$lastname = $_POST["lastname"];
		$phone = $_POST["phone"];
		$adress = $_POST["adress"];
		if (is_null($adress)) {

			$adress = "";
			$idEnfermo=insertEnfermo($cnn, $id, $username ,$lastname, $phone, $adress );

		}
		else{

		$idEnfermo=insertEnfermo($cnn, $id, $username ,$lastname, $phone, $adress );
		
		}

		$data = $idEnfermo . '/' . $username. '/' . $lastname . '/' . $phone;
				
		$Encript= openssl_encrypt($data, 'aes128', 'Seguridad',0,'1234567812345678');

		$result = RESULT_SUCCESS;

		break;
	

		case "login":

			$mail = $_POST["mail"];
			$pwd = $_POST["password"];
			if(login($cnn, $mail, $pwd))
			{
				$id=loginId($cnn, $mail, $pwd);
				$result = RESULT_SUCCESS;
			}
			else
			{
				$result = RESULT_ERROR;
			}

		break;

		case "qr":
			$qr = $_POST["qr"];
			$qr= openssl_decrypt($qr, 'aes128', 'Seguridad',0,'1234567812345678');
			$array = explode("/", $qr , 4);

			$id = $array[0];
			//nombre= array[1]
			//apellido= array[2]
			$telefono = $array[3];

			if ( isExistEnfermo($cnn, $array[0], $array[1], $array[2], $array[3])) {
				$imei= $_POST["imei"];
				insertAnonimo($cnn, $id, $imei);
				$result = RESULT_SUCCESS;
			}
			else
			{
				$result = RESULT_ERROR;
			}
			break;

}

//Print result as json
echo(json_encode(array('result' => $result , 'id' => $id , 'Encriptado' => $Encript, 'Telefono' => $telefono)));

function insertAnonimo($cnn, $id, $imei)
{

	$query = "INSERT INTO ANONIMO(IMEI_EXTERNO ,ID_ENFERMO) VALUES(?, ?)";
	$stmt = $cnn->prepare($query);
	$stmt->bind_param("si", $imei, $id, );
	$stmt->execute();

}


function insertEnfermo($cnn, $id, $username ,$lastname, $phone, $adress )
{

	$query = "INSERT INTO ENFERMO(ID_USUARIO , NOMBRE, APELLIDO, TELEFONO_CONTACTO, DIRECCION) VALUES(?, ?, ?, ?, ?)";
	$stmt = $cnn->prepare($query);
	$stmt->bind_param("issis", $id, $username ,$lastname, $phone, $adress);
	$stmt->execute();
	return $cnn->insert_id;

}


function insertCuenta($cnn, $mail, $pwd, $id)
{

	$query = "INSERT INTO CUENTA(ID_USUARIO , CORREO, CONTRASENIA) VALUES(?, ?, ?)";
	$stmt = $cnn->prepare($query);
	$stmt->bind_param("iss", $id, $mail, $pwd);
	$stmt->execute();

}
function insertUser($cnn, $username, $lastname)
{

	$query = "INSERT INTO USUARIO(NOMBRE, APELLIDO) VALUES(?, ?)";
	$stmt = $cnn->prepare($query);
	$stmt->bind_param("ss", $username, $lastname);
	$stmt->execute();
	return $cnn->insert_id;

	

}
function isExistUser($cnn, $mail)
{
	$query = "SELECT * FROM CUENTA WHERE CORREO = ?";
	$stmt = $cnn->prepare($query);
	$stmt->bind_param("s", $mail);
	$stmt->execute();
	$stmt->store_result();
	$rowcount = $stmt->num_rows;

	return $rowcount;
}

function isExistEnfermo($cnn, $id, $username ,$lastname, $phone)
{
	$query = "SELECT * FROM ENFERMO WHERE ID =? AND NOMBRE = ? AND APELLIDO = ? AND TELEFONO_CONTACTO = ? ";
	$stmt = $cnn->prepare($query);
	$stmt->bind_param("issi", $id, $username ,$lastname, $phone);
	$stmt->execute();
	$stmt->store_result();
	$rowcount = $stmt->num_rows;

	return $rowcount;
}

function login($cnn, $mail, $pwd)
{
	$query = "SELECT * FROM CUENTA WHERE CORREO = ? AND CONTRASENIA = ?";
	$stmt = $cnn->prepare($query);
	$stmt->bind_param("ss", $mail , $pwd);
	$stmt->execute();
	$stmt->store_result();
	$rowcount =$stmt->num_rows;
	return $rowcount;

}

function loginId($cnn, $mail, $pwd)
{
	$query = "SELECT ID_USUARIO FROM CUENTA WHERE CORREO = ? AND CONTRASENIA = ?";
	$stmt = $cnn->prepare($query);
	$stmt->bind_param("ss", $mail , $pwd);
	$stmt->bind_result($id);
	$stmt->execute();
	$stmt->store_result();
	$stmt->fetch();
	
	return $id;


}

