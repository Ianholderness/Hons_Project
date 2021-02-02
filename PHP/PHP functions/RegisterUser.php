<?php

session_start();
include('Functions/function.php');

$db = createConnection();


	$fname = $_GET['Fname'];
	$Sname = $_GET['Sname'];
	$Email = $_GET['Email'];
	$Pword = $_GET['Pword'];

	$salt = makeSalt(16);
	$hash = makeHash($Pword, $salt, 50);


	//echo "fname : $fname </br> Sname :$Sname </br> Email : $Email <br/> Pword : $Pword <br/> Salt : $salt <br/> Hash :$hash";

	$sql = "Insert into tblUsers(fName, sName, Email, salt, hash) Values(?,?,?,?,?)";
	$stmt = $db->prepare($sql);
	$stmt->bind_param("sssss",$fname, $Sname, $Email, $salt, $hash);
	$stmt->execute();

	$sql = "Select UID from tblUsers where Email =?";
	$stmt = $db->prepare($sql);
	$stmt->bind_param("s", $Email);
	$stmt->execute();
	$stmt->store_result();
	$stmt->bind_result($Uid);
	$stmt->fetch();

	$json[]=array(
			'UserID' => $Uid
			);
	echo json_encode($json);
	$stmt->close();
	$db->close();


?>
