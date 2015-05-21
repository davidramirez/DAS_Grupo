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

		$query = "INSERT INTO comentarios (id_hist, id_us, texto, fecha) VALUES ($historia, $usuario, '$comentario', NOW())";

		if (!$conn->query($query))
		    die('false');

		$query = "SELECT id_hist FROM suscripciones WHERE id_hist = $historia AND id_us = $usuario";
		$sql = $conn->query($query);
		if (!$sql->fetch_assoc()) {
			$query = "INSERT INTO suscripciones (id_hist, id_us) VALUES ($historia, $usuario)";
			$conn->query($query);
		}

		echo 'true';

		$conn->close();

		enviar_push_comentarios($historia);
	} else
	    echo 'false';
?>