<?php
	require "conexion_bd.php";

    $conn = new mysqli($HOST, $USUARIO, $CONTRASENA, $BD);

	// Check connection
	if ($conn->connect_error)
	    die(false);

	if (!empty($_POST["id"]) && !empty($_POST["titulo"]) && !empty($_POST["descripcion"]) && !empty($_POST["fotos"])) {
		$id = $_POST["id"];
		$titulo = $_POST["titulo"];
		$descripcion = $_POST["descripcion"];

		$query = "INSERT INTO historia (id_us, titulo, descripcion, fecha) VALUES ('$id', '$titulo', '$descripcion', NOW())";

		if (!$conn->query($query))
			die(false);

		foreach ($_POST["fotos"] as &$foto) {
		    $path = $id_us . "_" . date('Ymd_His') . ".jpg";
            $binary = base64_decode($foto);
            header('Content-Type: bitmap; charset=utf-8');
            $file = fopen('imagenes/' . $path, 'wb');
            fwrite($file, $binary);
            fclose($file);

		    $query = "INSERT INTO imagenes (id_hist, path) VALUES ((SELECT id FROM historia WHERE id_us = '$id' AND titulo = '$titulo' AND descripcion = '$descripcion'), '$path')";

            if (!$conn->query($query))
            	die(false);
		}

		echo true;

		$conn->close();
	} else
	    echo false;
?>