<?php
    require "conexion_bd.php";

    $conn = new mysqli($HOST, $USUARIO, $CONTRASENA, $BD);

    // Check connection
    if ($conn->connect_error)
        die("false");

    if (!empty($_POST["id"]) && !empty($_POST["contrasena"]) && !empty($_POST["gcm"])&& !empty($_POST["aviso"]) && !empty($_POST["oldpass"])) {
        $id = $_POST["id"];
        $pass = $_POST["contrasena"];
		$oldpass = $_POST["oldpass"];
        $gcm = $_POST["gcm"];
        $aviso = $_POST["aviso"];

        $query = "UPDATE usuarios SET contrasena = '$pass' , avisar_nuevo_historia = '$aviso'  WHERE id = '$id' AND id_gcm = '$gcm' ";


        if (!$conn->query($query))
            die("false");

        echo "true";

        $conn->close();//*/
    } else
        echo "false";
?>