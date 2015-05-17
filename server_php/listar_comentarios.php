<?php
	require "conexion_bd.php";

    $conn = new mysqli($HOST, $USUARIO, $CONTRASENA, $BD);

	// Check connection
	if ($conn->connect_error)
		die('false');

	if (!empty($_POST["id"])) {
		$id = $_POST["id"];

		$query = "SELECT usuarios.nombre AS nombre, comentarios.texto AS texto, comentarios.fecha AS fecha FROM usuarios, comentarios WHERE comentarios.id_hist = $id AND usuarios.nombre = (SELECT nombre FROM usuarios WHERE id = comentarios.id_us)";
		$sql = $conn->query($query);
		$comentarios = array();

		while ($comentario = $sql->fetch_assoc()) {
			$aux['usuario'] = $comentario['nombre'];
			$aux['texto'] = $comentario['texto'];
			$aux['fecha'] = $comentario['fecha'];
			array_push($comentarios, $aux);
		}

		header("Content-Type: application/json");
		echo json_encode($comentarios);

		$conn->close();
	} else
		echo 'false';
?>