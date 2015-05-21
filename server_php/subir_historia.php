<?php
	require "conexion_bd.php";
	require "funciones_push.php";

    $conn = new mysqli($HOST, $USUARIO, $CONTRASENA, $BD);

	// Check connection
	if ($conn->connect_error)
	    die("false");

	if (!empty($_POST["id"]) && !empty($_POST["titulo"]) && !empty($_POST["descripcion"]) && !empty($_POST["fotos"]) && !empty($_POST["etiquetas"])) {
		$id = $_POST["id"];
		$titulo = $_POST["titulo"];
		$descripcion = $_POST["descripcion"];

		$query = "INSERT INTO historia (id_us, titulo, descripcion, fecha) VALUES ('$id', '$titulo', '$descripcion', NOW())";

		if (!$conn->query($query))
			die("false");
			
        $id_hist = $conn->insert_id; // getting the last id of the stories, the id of the storie just inserted.
        
		foreach ($_POST["etiquetas"] as &$etiqueta) {
			$query = "SELECT id FROM etiqueta WHERE nombre = '$etiqueta'"; // checking the existence of the tag.
			$sql = $conn->query($query);
			if (!$sql->fetch_assoc()) { // if does not exist, we create it.
				$query = "INSERT INTO etiqueta (nombre) VALUES ('$etiqueta')"; //
				if (!$conn->query($query))
					die("false");
			}

			$query = "INSERT INTO etiquetas (id_etiq, id_hist) VALUES ((SELECT id FROM etiqueta WHERE nombre = '$etiqueta'), $id_hist)"; // inserting story with the tag.
			if (!$conn->query($query))
				die("false");
		}
		
		$i = 1;

		foreach ($_POST["fotos"] as &$foto) {
		    $path = "h" . $id_hist . "u" . $id . "_" . date('Ymd_His') . "_" . $i . ".jpg";
            $binary = base64_decode($foto);
            header('Content-Type: jpg; charset=utf-8');
            $file = fopen('imagenes/' . $path, 'wb') or die("No file saved");
            fwrite($file, $binary);
            fclose($file);

		    $query = "INSERT INTO imagenes (id_hist, path) VALUES ($id_hist, '$path')";

            if (!$conn->query($query))
            	die("false");
            
            $i++;
		}

		echo "true";

		$conn->close();

		enviar_push_historias($titulo, $id_hist);
	} else
	    echo "false";
?>