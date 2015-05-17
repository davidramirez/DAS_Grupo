<?php
    require "conexion_bd.php";

    function enviar_push($historia) {
        $conn = new mysqli($HOST, $USUARIO, $CONTRASENA, $BD);

        // Check connection
        if ($conn->connect_error)
            die('false');

        $query = "SELECT usuarios.id_gcm AS id_gcm FROM usuarios, suscripciones WHERE usuarios.id = suscripciones.id_us AND usuarios.avisar_nuevo_historia = 'true' AND suscripciones.id_hist = $historia";
        $sql = $conn->query($query);
        $suscriptores = array();

        while ($suscriptor = $sql->fetch_assoc()) {
            $usuario = $suscriptor['id_gcm'];
            array_push($suscriptores, $usuario);
        }

        $query = "SELECT titulo FROM historias WHERE id = $historia";
        $sql = $conn->query($query);

        $nHistoria = $sql->fetch_assoc();

        $conn->close();

        push($suscriptores, $nHistoria['titulo']);
    }

    function push($usuarios, $historia) {
        // 01.- Preparamos la cabecera del mensaje:
        $cabecera = array(
            "Authorization: key=$api_key",
            "Content-Type: application/json"
        );

        // 02.- Preparamos el contenido del mensaje:
        $data = array(
            'App' => 'Nuevo comentario en ' . $historia
        );
        $info= array(
            "registration_ids" => $usuarios,
            "collapse_key" => "App",
            "time_to_live" => 200,
            "data" => $data
        );

        // Inicializamos el gestor de curl:
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $GCM_url);

        // Configuramos el mensaje:
        curl_setopt($ch, CURLOPT_HTTPHEADER, $cabecera);
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($info));
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);

        // Enviamos el mensaje:
        curl_exec($ch);

        // Cerramos el gestor de Curl:
        curl_close($ch);
    }
?>