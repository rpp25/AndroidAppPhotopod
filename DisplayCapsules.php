<?php
    $con = mysqli_connect("macrosoft.czfbseaeohsk.us-east-1.rds.amazonaws.com", "cjs176", "dolphin-fin", "photopod", "3306");

    $username = $_POST["username"];

    $statement = mysqli_prepare($con, "SELECT * FROM capsule WHERE username = ?");
    mysqli_stmt_bind_param($statement, "s", $username);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $username, $capsule_name, $num_photos);

    $response = array();

    while(mysqli_stmt_fetch($statement)){
        $response["capsule_name"] = $capsule_name;
        $data[] = $response;
    }

    echo json_encode($data);
?>