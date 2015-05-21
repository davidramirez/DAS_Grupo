<?php
    function enviar_push_comentarios($historia) {
        require "conexion_bd.php";
        
        $conn = new mysqli($HOST, $USUARIO, $CONTRASENA, $BD);

        // Check connection
        if ($conn->connect_error)
            die('1');

        $query = "SELECT usuarios.id_gcm AS id_gcm FROM usuarios, suscripciones WHERE usuarios.id_gcm IS NOT NULL AND usuarios.id = suscripciones.id_us AND usuarios.avisar_nuevo_historia = 'true' AND suscripciones.id_hist = $historia";
        
        $sql = $conn->query($query);
        $suscriptores = array();
        
        while ($suscriptor = $sql->fetch_assoc()) {
            $usuario = $suscriptor['id_gcm'];
            array_push($suscriptores, $usuario);
        }

        $query = "SELECT titulo FROM historia WHERE id = $historia";
        $sql = $conn->query($query);

        $nHistoria = $sql->fetch_assoc();

        $conn->close();

        $texto = "Nuevos comentarios en '" . $nHistoria['titulo'] . "'";

        push($suscriptores, $texto);
    }

    function enviar_push_historias($historia) {
        require "conexion_bd.php";
        
        $conn = new mysqli($HOST, $USUARIO, $CONTRASENA, $BD);

        // Check connection
        if ($conn->connect_error)
            die('false');

        $query = "SELECT usuarios.id_gcm AS id_gcm FROM usuarios WHERE usuarios.id_gcm IS NOT NULL AND usuarios.avisar_nuevo_historia = 'true'";
        $sql = $conn->query($query);
        $suscriptores = array();

        while ($suscriptor = $sql->fetch_assoc()) {
            $usuario = $suscriptor['id_gcm'];
            array_push($suscriptores, $usuario);
        }

        $conn->close();

        $texto = "Nueva historia: " . $historia;

        push($suscriptores, $texto);
    }

    function push($usuarios, $texto) {
        // 00.- variables
        $api_key = "AIzaSyBR-RD18q05kYVwwntyRu7nQZZzQmRHLCs";
        $GCM_url = "https://android.googleapis.com/gcm/send";

        // 01.- Preparamos la cabecera del mensaje:
        $cabecera = array(
            "Authorization: key=" . $api_key,
            "Content-Type: application/json"
        );

        // 02.- Preparamos el contenido del mensaje:
        $data = array(
            'App' => $texto
        );
        
        $info = array(
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

        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);

        // Enviamos el mensaje:
        curl_exec($ch);
        
        if (curl_errno($ch))
			echo 'GCM error: ' . curl_error($ch);
        
        // Cerramos el gestor de Curl:
        curl_close($ch);
    }
?>