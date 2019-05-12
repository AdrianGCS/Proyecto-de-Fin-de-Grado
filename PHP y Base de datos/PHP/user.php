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
$direccion = 0;

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
		echo(json_encode(array('result' => $result , 'id' => $id )));

		break;

		case "enfermo":

		$username = $_POST["username"];
		$lastname = $_POST["lastname"];
		$id = $_POST["id"];
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

		$idEnfermo = ($idEnfermo * 1024)/4;
		$idEnfermo = decoct($telefono);
		$idEnfermo = openssl_encrypt($telefono, 'aes128', 'Seguridad',0,'1234567812345678');
		
		insertexterno($cnn, $id, $idEnfermo);
		$result = RESULT_SUCCESS;

		echo(json_encode(array('result' => $result , 'Encriptado' => $Encript, 'codigo' => $idEnfermo, )));

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

			echo(json_encode(array('result' => $result , 'id' => $id )));

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
				$direccion=qrDireccion($cnn, $id);
				$result = RESULT_SUCCESS;
			}
			else
			{
				$result = RESULT_ERROR;
			}


			

			echo(json_encode(array('result' => $result , 'id' => $id ,  'Telefono' => $telefono, 'direccion' => $direccion)));
			break;

		case "Local":

			$id_enfermo = $_POST["id_enfermo"];
			$length = $_POST["length"];
			$altitude = $_POST["altitude"];
			$imei = $_POST["imei"];




			insertLocalizacion($cnn, $id_enfermo, $length, $altitude, $imei);
			
				$result = RESULT_SUCCESS;

				echo(json_encode(array('result' => $result)));


		break;
			//desencriptar
			//$id_enfermo=openssl_decrypt($id_enfermo,'aes128','Seguridad',0,'1234567812345678');	
			//$id_enfermo=octdec($id_enfermo);
			//$id_enfermo=($id_enfermo*4)/1024;

		case "DatosEnfermo":

			$id = $_POST["id"];

			$idEnfermoExt=IdEnfemoExt($cnn, $id);

			$datos = array();
			
			foreach ($idEnfermoExt as $val) {

			$temp = sacarDatosEnfermo($cnn, $idEnfermoExt[0]);
			array_push($datos, $temp);
			}
			$result = RESULT_SUCCESS;
			echo(json_encode(array('result' => $result ,'datos' => $datos)));
			break;

		case "DatosUsuario":

			$id = $_POST["id"];

			$datos = array();

			$temp = sacarUsuarios($cnn, $id);

			array_push($datos, $temp);

			$temp =sacarDatosUs($cnn, $id);

			array_push($datos, $temp);

			$result = RESULT_SUCCESS;

			echo(json_encode(array('result' => $result ,'datos' => $datos)));

			break;

		case "ActualizarUs":

			$id = $_POST["id"];
			$Nombre = $_POST["Nombre"];
			$Apellido = $_POST["Apellido"];
			$Correo = $_POST["Correo"];
			$Contrasenia= $_POST["Contrasenia"];
			actuUsuarios($cnn, $id ,$Nombre,$Apellido);
			actuDatosUs($cnn, $id ,$Correo, $Contrasenia);

			$result = RESULT_SUCCESS;
			echo(json_encode(array('result' => $result)));
			break;

}

//Print result as json
//echo(json_encode(array('result' => $result , 'id' => $id , 'Encriptado' => $Encript, 'Telefono' => $telefono, 'direccion' => $direccion)));
function actuUsuarios($cnn, $Id ,$Nombre , $Apellido)
{
	$query = "UPDATE USUARIO SET NOMBRE=? , APELLIDO=? WHERE ID=?";
	$stmt = $cnn->prepare($query);
	$stmt->bind_param("ssi", $Nombre,$Apellido, $Id);
	$stmt->execute();
	$stmt->close();
}
function actuDatosUs($cnn, $Id ,$Correo, $Contrasenia)
{
	$query = "UPDATE CUENTA SET CORREO=? , CONTRASENIA=? WHERE ID_USUARIO=?";
	$stmt = $cnn->prepare($query);
	$stmt->bind_param("ssi",$Correo , $Contrasenia, $Id);
	$stmt->execute();
	$stmt->close();
}
function sacarUsuarios($cnn, $Id)
{
	$query = "SELECT NOMBRE , APELLIDO FROM USUARIO WHERE ID=?";
	$stmt = $cnn->prepare($query);
	$stmt->bind_param("i", $Id);
	$stmt->bind_result($Nombre, $Apellido);
	$stmt->execute();
	$stmt->store_result();
	$stmt->fetch();
	$DatosUsuario = array('Nombre'=>$Nombre,'Apellido'=>$Apellido);
	$stmt->close();
	return $DatosUsuario;
}
function sacarDatosUs($cnn, $Id)
{
	$query = "SELECT CORREO , CONTRASENIA FROM CUENTA WHERE ID_USUARIO=?";
	$stmt = $cnn->prepare($query);
	$stmt->bind_param("i", $Id);
	$stmt->bind_result($Correo, $Contrasenia);
	$stmt->execute();
	$stmt->store_result();
	$stmt->fetch();
	$DatosUsuario = array('Correo'=>$Correo,'Contrasenia'=>$Contrasenia);
	$stmt->close();
	return $DatosUsuario;
}
function insertLocalizacion($cnn, $id_enfermo, $length, $altitude, $imei)
{

	$query = "INSERT INTO LOCALIZACION(ID_ENFERMO , LONGITUD , LATITUD , IMEI) VALUES(?, ?, ?, ?)";
	$stmt = $cnn->prepare($query);
	$stmt->bind_param("isss", $id_enfermo, $length, $altitude, $imei);
	$stmt->execute();
	$stmt->close();
}

function insertAnonimo($cnn, $id, $imei)
{

	$query = "INSERT INTO ANONIMO(IMEI_EXTERNO ,ID_ENFERMO) VALUES(?, ?)";
	$stmt = $cnn->prepare($query);
	$stmt->bind_param("si", $imei, $id );
	$stmt->execute();
	$stmt->close();

}
function insertexterno($cnn, $id, $id_enfermo)
{

	$query = "INSERT INTO EXTERNO(ID_ENFERMO ,ID_USUARIO) VALUES(?, ?)";
	$stmt = $cnn->prepare($query);
	$stmt->bind_param("ii", $id_enfermo, $id );
	$stmt->execute();
	$stmt->close();
}



function insertEnfermo($cnn, $id, $username ,$lastname, $phone, $adress )
{

	$query = "INSERT INTO ENFERMO(ID_USUARIO , NOMBRE, APELLIDO, TELEFONO_CONTACTO, DIRECCION) VALUES(?, ?, ?, ?, ?)";
	$stmt = $cnn->prepare($query);
	$stmt->bind_param("issis", $id, $username ,$lastname, $phone, $adress);
	$stmt->execute();
	$stmt->close();
	return $cnn->insert_id;

}


function insertCuenta($cnn, $mail, $pwd, $id)
{

	$query = "INSERT INTO CUENTA(ID_USUARIO , CORREO, CONTRASENIA) VALUES(?, ?, ?)";
	$stmt = $cnn->prepare($query);
	$stmt->bind_param("iss", $id, $mail, $pwd);
	$stmt->execute();
	$stmt->close();

}
function insertUser($cnn, $username, $lastname)
{

	$query = "INSERT INTO USUARIO(NOMBRE, APELLIDO) VALUES(?, ?)";
	$stmt = $cnn->prepare($query);
	$stmt->bind_param("ss", $username, $lastname);
	$stmt->execute();
	$stmt->close();
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
	$stmt->close();

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
	$stmt->close();

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
	$stmt->close();
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
	$stmt->close();
	return $id;


}
function qrDireccion($cnn, $id)
{
	$query = "SELECT DIRECCION FROM ENFERMO WHERE ID = ?";
	$stmt = $cnn->prepare($query);
	$stmt->bind_param("i", $id);
	$stmt->bind_result($direccion);
	$stmt->execute();
	$stmt->store_result();
	$stmt->fetch();
	$stmt->close();
	return $direccion;


}

function IdEnfemoExt($cnn, $ID_USUARIO)
{
	$query = "SELECT ID_ENFERMO FROM EXTERNO WHERE ID_USUARIO=?";
	$stmt = $cnn->prepare($query);
	$stmt->bind_param("i", $ID_USUARIO);
	$stmt->bind_result($Ext);
	$stmt->execute();
	$stmt->store_result();
	$rowcount = $stmt->num_rows;
	$IdEnfemoExt = array();
	while ($stmt->fetch()) {
		array_push($IdEnfemoExt, $Ext);
	}
	$stmt->close();
	return $IdEnfemoExt;
}

function sacarDatosEnfermo($cnn, $Id)
{
	$query = "SELECT NOMBRE , APELLIDO, TELEFONO_CONTACTO, DIRECCION  FROM ENFERMO WHERE ID=?";
	$stmt = $cnn->prepare($query);
	$stmt->bind_param("i", $Id);
	$stmt->bind_result($Nombre, $Apellido, $Telefono ,$Direccion);
	$stmt->execute();
	$stmt->store_result();
	$stmt->fetch();
	$datosEnfermor = array('Nombre'=>$Nombre,'Apellido'=>$Apellido,'Telefono'=>$Telefono,'Direccion'=>$Direccion);
	$stmt->close();
	return $datosEnfermor;
}