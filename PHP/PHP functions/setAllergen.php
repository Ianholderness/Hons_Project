<?php

	session_start();
	include('Functions/function.php');

	$db = createConnection();

	$uid = $_GET['Uid'];
	$ingID = $_GET['Ing'];
	$state = $_GET['State'];

	echo "Tetst uid $uid $ingID $state <br/>";
	$sql ="Select count(UID) as rec from UsrAllergen Where Ingredid = ? and UID = ?;";
	$stmt = $db->prepare($sql);
	$stmt->bind_param("ii",$ingID, $uid);
	$stmt->execute();
	$stmt->store_result();
	$stmt->bind_result($rec);
	$stmt->fetch();
	
	if ($rec ==0) {
		$stmt->close();
		$sql = "insert into UsrAllergen(Ingredid, UID, Active)Values (?,?, 1);";
		$stmt = $db->prepare($sql);
		$stmt->bind_param("ii",$ingID, $uid);
		echo "Insert Rec $rec <br/>$sql<br/>";
	}else {
		$stmt->close();
		$sql = "Update UsrAllergen set Active =? Where Ingredid = ? and UID = ?;";
		$stmt = $db->prepare($sql);
		$stmt->bind_param("iii",$state, $ingID, $uid);
		echo "Update Rec $rec <br/>$sql<br/>";
	}
	echo "pre exe<br/>";
	$stmt->execute();
	$stmt->close();
	$db->close();
		echo "post exe<br/>";
	?>
