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

		swicth($accion) {
			case "ultimas":
				$limit = "LIMIT " + (!empty($_POST["cuantas"])) ? $_POST["cuantas"] : "10";
				break;
			case "usuario":
				$where = (!empty($_POST["id"])) ? "usuarios.id = " + $_POST["id"] : "";
				$limit = "LIMIT " + (!empty($_POST["cuantas"])) ? $_POST["cuantas"] : "10";
				break;
			/*case "mejores":
				break;*/
			case "etiqueta":
				$where = (!empty($_POST["id"])) ? "etiquetas.id_etiq = " + $_POST["id"] : "10";
				$limit = "LIMIT " + (!empty($_POST["cuantas"])) ? $_POST["cuantas"] : "10";
				break;
		}

		$query = "SELECT historia.id, historia.titulo, usuarios.nombre, historia.fecha FROM usuarios, historia, etiquetas WHERE usuarios.id = historia.id_us AND historia.id = etiquetas.id_hist AND $where $limit";
		$sql = $conn->query($query);

		$historias = array();

		while ($historia = $sql->fetch_assoc()) {
			$aux['id'] = $historia['historia.id'];
			$aux['titulo'] = $historia['historia.titulo'];
			$aux['usuario'] = $historia['usuarios.nombre'];
			$aux['fecha'] = $historia['historia.fecha'];
			array_push($historias, $aux);
		}

		header("Content-Type: application/json");
		echo json_encode($notes);

		$conn->close();
	} else
		echo 'false';
?>