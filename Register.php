<?php
    $con = mysqli_connect("macrosoft.czfbseaeohsk.us-east-1.rds.amazonaws.com", "cjs176", "dolphin-fin", "photopod", "3306");
//$con = mysqli_connect("localhost", "id1295544_macrosoft", "dolphin-fin", "id1295544_photopod");

    $username = $_POST["username"];
    $password = $_POST["password"];
    $statement = mysqli_prepare($con, "INSERT INTO user (username, password) VALUES (?, ?)");
    mysqli_stmt_bind_param($statement, "ss", $username, $password);
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;  
    
    echo json_encode($response);
?>