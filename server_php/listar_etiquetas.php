<?php
	require "conexion_bd.php";

    $conn = new mysqli($HOST, $USUARIO, $CONTRASENA, $BD);

	// Check connection
	if ($conn->connect_error)
		die('false');

	$query = "SELECT etiqueta.id, etiqueta.nombre, COUNT(etiqueta.id) AS cantidad FROM (etiqueta INNER JOIN etiquetas ON etiqueta.id = etiquetas.id_etiq) GROUP BY etiqueta.id, etiqueta.nombre ORDER BY etiqueta.nombre";
	$sql = $conn->query($query);

	$etiquetas = array();

	while ($etiqueta = $sql->fetch_assoc()) {
		$aux['id'] = $etiqueta['etiqueta.id'];
		$aux['nombre'] = $etiqueta['etiqueta.nombre'];
		$aux['cantidad'] = $etiqueta['cantidad'];
		array_push($etiquetas, $aux);
	}

	header("Content-Type: application/json");
	echo json_encode($notes);

	$conn->close();
?>