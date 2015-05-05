<?php
    include("conexion_bd.php");

	$conn = new mysqli($HOST, $USUARIO, $CONTRASENA, $BD);

	// Check connection
	if ($conn->connect_error)
		die(false);

	if (!empty($_POST["user"]) && !empty($_POST["pass"])) {
		$user = $_POST["user"];
		$pass = $_POST["pass"];

		$query = "SELECT id FROM usuarios WHERE nombre = '$user' AND contrasena = '$pass'";

		$sql = $conn->query($query);
		$row = mysqli_fetch_array($sql);

		if (!empty($row)) {
			echo $row['id'];
		} else
			echo false;

		$conn->close();
	} else
		echo false;
?>