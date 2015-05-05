<?php
	include("conexion_bd.php");

    $conn = new mysqli($HOST, $USUARIO, $CONTRASENA, $BD);

	// Check connection
	if ($conn->connect_error)
	    die(false);

	if (!empty($_POST["user"]) && !empty($_POST["pass"])) {
		$user = $_POST["user"];
		$pass = $_POST["pass"];

		$query = "INSERT INTO usuarios (nombre, contrasena) VALUES ('$user', '$pass')";

		if (!$conn->query($query))
			die(false);

		echo true;

		$conn->close();
	} else
	    echo false;
?>