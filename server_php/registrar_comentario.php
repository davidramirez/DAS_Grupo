<?php
	require "conexion_bd.php";
	require "funciones_push.php";

    $conn = new mysqli($HOST, $USUARIO, $CONTRASENA, $BD);

	// Check connection
	if ($conn->connect_error)
	    die('false');

	if (!empty($_POST["id_usuario"]) && !empty($_POST["id_historia"]) && !empty($_POST["comentario"])) {
		$usuario = $_POST["id_usuario"];
		$historia = $_POST["id_historia"];
		$comentario = $_POST["comentario"];

		$query = "INSERT INTO comentarios (id_hist, id_us, texto, fecha) VALUES ($usuario, $historia, '$comentario', NOW())";

		if (!$conn->query($query))
		    die('false');

		$query = "INSERT INTO suscripciones (id_hist, id_us) VALUES ($usuario, $historia)";

        if (!$conn->query($query))
            die('false');

		echo 'true';

		$conn->close();

		enviar_push($historia);
	} else
	    echo 'false';
?>