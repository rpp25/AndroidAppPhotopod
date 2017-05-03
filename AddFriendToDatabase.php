<?php
    $con = mysqli_connect("macrosoft.czfbseaeohsk.us-east-1.rds.amazonaws.com", "cjs176", "dolphin-fin", "photopod", "3306");    

    $owner_username = $_POST["owner_username"];
    $slave_username = $_POST["slave_username"];

    $statement = mysqli_prepare($con, "INSERT INTO friends_list (owner_username, slave_username) VALUES (?, ?)");
    mysqli_stmt_bind_param($statement, "ss", $owner_username, $slave_username);

    $response = array();
    $done = mysqli_stmt_execute($statement);

    if ($done) {
        $response["success"] = true;
    } else {
        $response["success"] = false;
    }

    echo json_encode($response);
?>
