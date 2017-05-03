<?php
    $con = mysqli_connect("macrosoft.czfbseaeohsk.us-east-1.rds.amazonaws.com", "cjs176", "dolphin-fin", "photopod", "3306");

    $username = $_POST["username"];
    $capsule_name = $_POST["capsule_name"];

    $statement = mysqli_prepare($con, "SELECT * FROM photo WHERE username = ? AND capsule_name = ?");
    mysqli_stmt_bind_param($statement, "ss", $username, $capsule_name);
    mysqli_stmt_execute($statement);

    mysqli_stmt_store_result($statement);
    mysqli_stmt_bind_result($statement, $id, $r_username, $r_capsule_name, $path);

    $response = array();

    while(mysqli_stmt_fetch($statement)){        
        $response["username"] = $r_username;
        $response["capsule_name"] = $r_capsule_name;
        $response["path"] = $path;
        $data[] = $response;
    }

    echo json_encode($data);
?>
