<?php

header('Content-type : bitmap; charset=utf-8');

if(isset($_POST["encoded_string"])) {
	$encoded_string = $_POST["encoded_string"];
	$image_name = $_POST["image_name"];
	$username = $_POST["username"];
	$capsule_name = $_POST["capsule_name"];

	$decoded_string = base64_decode($encoded_string);

	$path = 'images/'.$image_name;

	$file = fopen($path, 'wb');

	$is_written = fwrite($file, $decoded_string);
	fclose($file);

	if($is_written > 0) {		
		$con = mysqli_connect("macrosoft.czfbseaeohsk.us-east-1.rds.amazonaws.com", "cjs176", "dolphin-fin", "photopod", "3306");	
		$statement = mysqli_prepare($con, "INSERT INTO photo (username, capsule_name, path) VALUES (?, ?, ?)");
	    mysqli_stmt_bind_param($statement, "sss", $username, $capsule_name, $path);
	    $result = mysqli_stmt_execute($statement);

	    if($result) {
	    	echo "success";
	    } else {
	    	echo "failed";
	    }

	    mysqli_close($con);
	}
}