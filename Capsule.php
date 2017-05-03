<?php
    $con = mysqli_connect("macrosoft.czfbseaeohsk.us-east-1.rds.amazonaws.com", "cjs176", "dolphin-fin", "photopod", "3306");

    $response = array();
    $username = $_POST["username"];
    $capsule_name = $_POST["capsule_name"];
    $num_photos = 0;

    $statement = mysqli_prepare($con, "INSERT INTO capsule (username, capsule_name, num_photos) VALUES (?, ?, ?)");
    mysqli_stmt_bind_param($statement, "ssi", $username, $capsule_name, $num_photos);
    $done = mysqli_stmt_execute($statement);

    if ($done) {
        $response["success"] = true;
    } else {
        $response["success"] = false;
    }

    echo json_encode($response);
?>
