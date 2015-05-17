<?php
	require "conexion_bd.php";

    $conn = new mysqli($HOST, $USUARIO, $CONTRASENA, $BD);

	// Check connection
	if ($conn->connect_error)
		die('false');

	if (!empty($_POST["id"])) {
		$id = $_POST["id"];

		$query = "SELECT usuarios.nombre as nombre, historia.titulo as titulo, historia.descripcion as descripcion, historia.fecha as fecha FROM historia, usuarios WHERE usuarios.id = historia.id_us AND historia.id = $id";
        $sql = $conn->query($query);

        $historia = array();

        while ($campo = $sql->fetch_assoc()) {
            $aux['autor'] = $campo['nombre'];
            $aux['titulo'] = $campo['titulo'];
            $aux['descripcion'] = $campo['descripcion'];
            $aux['fecha'] = $campo['fecha'];
            array_push($historia, $aux);
        }

        $query = "SELECT etiqueta.id as id, etiqueta.nombre as nombre FROM etiqueta, etiquetas WHERE etiqueta.id = etiquetas.id_etiq AND etiquetas.id_hist = $id";
        $sql = $conn->query($query);

        $etiquetas = array();

        while ($campo = $sql->fetch_assoc()) {
			$aux = array();
            $aux['id'] = $campo['id'];
            $aux['nombre'] = $campo['nombre'];
            array_push($etiquetas, $aux);
        }

        $historia['etiquetas'] = $etiquetas;

        $query = "SELECT usuarios.nombre as nombre, comentarios.texto as texto, comentarios.fecha as fecha FROM comentarios, usuarios WHERE comentarios.id_us = usuarios.id AND comentarios.id_hist = $id";
        $sql = $conn->query($query);

        $comentarios = array();

        while ($campo = $sql->fetch_assoc()) {
			$aux = array();
            $aux['nombre'] = $campo['nombre'];
            $aux['texto'] = $campo['texto'];
            $aux['fecha'] = $campo['fecha'];
            array_push($comentarios, $aux);
        }

        $historia['comentarios'] = $comentarios;

        $query = "SELECT path FROM imagenes WHERE id_hist = $id";
        $sql = $conn->query($query);

        $fotos = array();

        while ($campo = $sql->fetch_assoc()) {
			$aux = array();
            $aux['path'] = $campo['path'];
            array_push($fotos, $aux);
        }

        $historia['fotos'] = $fotos;

        header("Content-Type: application/json");
        echo json_encode($historia);
	} else
	    echo "false";

?>