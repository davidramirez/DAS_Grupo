<?php
	require "conexion_bd.php";

    $conn = new mysqli($HOST, $USUARIO, $CONTRASENA, $BD);

	// Check connection
	if ($conn->connect_error)
		die('false');

	if (!empty($_POST["accion"])) {
		$accion = $_POST["accion"];
		$where = "";
		$limit = "";

		switch($accion) {
			case "ultimas":
				$limit = "LIMIT " . (!empty($_POST["cuantas"])) ? $_POST["cuantas"] : "10";
				break;
			case "usuario":
				$where = (!empty($_POST["id"])) ? "AND usuarios.id = ". $_POST["id"] : "";
				$limit = "LIMIT " . (!empty($_POST["cuantas"])) ? $_POST["cuantas"] : "10";
				break;
			case "mejores":
				break;
			case "etiqueta":
				$where = (!empty($_POST["id"])) ? "AND etiquetas.id_etiq = " . $_POST["id"] : "";
				$limit = "LIMIT " . (!empty($_POST["cuantas"])) ? $_POST["cuantas"] : "10";
				break;
		}

		$query = "SELECT historia.id as id, historia.titulo as titulo, usuarios.nombre as nombre, historia.fecha as fecha FROM usuarios, historia, etiquetas WHERE usuarios.id = historia.id_us AND historia.id = etiquetas.id_hist $where $limit";
		$sql = $conn->query($query);
		$historias = array();

		while ($historia = $sql->fetch_assoc()) {
			$aux['id'] = $historia['id'];
			$aux['titulo'] = $historia['titulo'];
			$aux['usuario'] = $historia['nombre'];
			$aux['fecha'] = $historia['fecha'];
			array_push($historias, $aux);
		}

		header("Content-Type: application/json");
		//echo json_encode($notes);
		echo json_encode($historias);

		$conn->close();
	} else
		echo 'false';
?>