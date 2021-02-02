<?php

// Creates the database connected and returns this to be uses by other functions
function createConnection() {
	$host=""; // Sets wher the server is
	$user=""; // sets the user
	$userpass=''; // Userpassword
	$schema="";
	$conn = new mysqli($host,$user,$userpass,$schema);
	if(mysqli_connect_errno()) {
		echo "Could not connect to database: ".mysqli_connect_errno();
		exit;
	}
	return $conn; // returns the databse connection
}


function makeSalt($saltLength) {
	$randomString= bin2hex(mcrypt_create_iv($saltLength, MCRYPT_DEV_URANDOM));
return $randomString;
}

// Encrypts the users password
function makeHash($plainText,$salt,$n) {
	$hash=$plainText.$salt;
	for($i=0;$i<$n;$i++) {
		$hash=hash("sha256",$plainText.$hash.$salt);
	}
	return $hash;
}

function checkUser($Email){
	$emailexists=0;
	$emailadd = $Email;

	$db = createConnection();
	$sql = "Select Email from tblUsers where Email=?";
	$stmt = $db->prepare($sql);
	$stmt->bind_param("s",$emailadd);
	$stmt->execute();
	$stmt->store_result();
	$stmt->bind_result($emailresult);
	while($stmt->fetch()) {
		if($emailresult==$emailadd) {$emailexists=1;}
	}
$stmt->close();
$db->close();
	return $emailexists;
}

?>
