<?php
	require "conexion_bd.php";

    $conn = new mysqli($HOST, $USUARIO, $CONTRASENA, $BD);

	// Check connection
	if ($conn->connect_error)
	    die(false);

	if (!empty($_POST["nombre"]) && !empty($_POST["contrasena"]) && !empty($_POST["aviso"])) {
		$user = $_POST["nombre"];
		$pass = $_POST["contrasena"];
		$aviso = $_POST["aviso"];

		$query = "INSERT INTO usuarios (nombre, contrasena, avisar_nuevo_historia) VALUES ('$user', '$pass','$aviso')";

		if (!$conn->query($query))
			{die(false);}

		echo 'true';

		$conn->close();
	} else
	    {echo 'false';}
?>