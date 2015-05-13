<?php
	require "conexion_bd.php";

    $conn = new mysqli($HOST, $USUARIO, $CONTRASENA, $BD);

	// Check connection
	if ($conn->connect_error)
		die('false');

	if (!empty($_POST["id"])) {
		$id = $_POST["id"];

		$query = "SELECT usuarios.nombre, historia.titulo, historia.descripcion, historia.fecha FROM historia, usuarios WHERE usuarios.id = historia.id_us AND historia.id = $id";
        $sql = $conn->query($query);

        $historia = array();

        while ($campo = $sql->fetch_assoc()) {
            $aux['autor'] = $campo['usuarios.nombre'];
            $aux['titulo'] = $campo['historia.titulo'];
            $aux['descripcion'] = $campo['historia.descripcion'];
            $aux['fecha'] = $campo['historia.fecha'];
            array_push($historia, $aux);
        }

        $query = "SELECT etiqueta.id, etiqueta.nombre FROM etiqueta, etiquetas WHERE etiqueta.id = etiquetas.id_etiq AND etiquetas.id_hist = $id";
        $sql = $conn->query($query);

        $etiquetas = array();

        while ($campo = $sql->fetch_assoc()) {
            $aux['id'] = $campo['etiqueta.id'];
            $aux['nombre'] = $campo['etiqueta.nombre'];
            array_push($etiquetas, $aux);
        }

        $historia['etiquetas'] = $etiquetas;

        $query = "SELECT usuarios.nombre, comentarios.texto, comentarios.fecha FROM comentarios, usuarios WHERE comentarios.id_us = usuarios.id AND comentarios.id_hist = $id";
        $sql = $conn->query($query);

        $comentarios = array();

        while ($campo = $sql->fetch_assoc()) {
            $aux['nombre'] = $campo['usuarios.nombre'];
            $aux['texto'] = $campo['comentarios.texto'];
            $aux['fecha'] = $campo['comentarios.fecha'];
            array_push($comentarios, $aux);
        }

        $historia['comentarios'] = $comentarios;

        $query = "SELECT path FROM imagenes WHERE id_hist = $id";
        $sql = $conn->query($query);

        $fotos = array();

        while ($campo = $sql->fetch_assoc()) {
            $aux['path'] = $campo['etiqueta.id'];
            array_push($fotos, $aux);
        }

        $historia['fotos'] = $fotos;

        header("Content-Type: application/json");
        echo json_encode($notes);
	} else
	    echo "false";

?>