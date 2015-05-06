<?php
	require "conexion_bd.php";

    $conn = new mysqli($HOST, $USUARIO, $CONTRASENA, $BD);

	// Check connection
	if ($conn->connect_error)
	    die(false);

	if (!empty($_POST["nombre"]) && !empty($_POST["contrasena"])) {
		$user = $_POST["nombre"];
		$pass = $_POST["contrasena"];

		$query = "INSERT INTO usuarios (nombre, contrasena) VALUES ('$user', '$pass')";

		if (!$conn->query($query))
			die(false);

		echo true;

		$conn->close();
	} else
	    echo false;
?>