<?php
    require "conexion_bd.php";

    $conn = new mysqli($HOST, $USUARIO, $CONTRASENA, $BD);

    // Check connection
    if ($conn->connect_error)
        die("false");

    if (!empty($_POST["id"]) && !empty($_POST["contrasena"]) && !empty($_POST["gcm"])) {
        $id = $_POST["id"];
        $pass = $_POST["contrasena"];
        $gcm = $_POST["gcm"];

        $query = "UPDATE usuarios SET id_gcm = '$gcm' WHERE id = '$id' AND contrasena = '$pass'";

        if (!$conn->query($query))
            die("false");

        echo "true";

        $conn->close();
    } else
        echo "false";
?>