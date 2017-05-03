<?php
    $con = mysqli_connect("macrosoft.czfbseaeohsk.us-east-1.rds.amazonaws.com", "cjs176", "dolphin-fin", "photopod", "3306");

    $username = $_POST["username"];

    $statement = mysqli_prepare($con, "SELECT * FROM user WHERE username = ?");
    mysqli_stmt_bind_param($statement, "s", $username);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $user_id, $username, $password);

    $response = array();
    $response["success"] = false;

    while(mysqli_stmt_fetch($statement)){
        $response["user_id"] = $user_id;
        $response["username"] = $username;
        $response["success"] = true;
    }

    echo json_encode($response);
?>
