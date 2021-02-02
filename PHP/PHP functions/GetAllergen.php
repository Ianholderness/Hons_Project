<?php

	session_start();
	include('Functions/function.php');

	$db = createConnection();

  $uid = $_GET['Uid'];
	$type = $_GET['Type'];
	//echo " uid $uid <br/> Type $type<br/>";
	$sql ="Select a.Ingredid, a.Ingred_Disc, b.Group_Disc
				from Ingredients as a
    		inner join food_Groups as b on
    		a.GroupID = b.id
    		where b.Group_Disc = ?
    		order by Group_disc";

	$stmt = $db->prepare($sql);
	$stmt->bind_param("s",$type);
	$stmt->execute();
	$stmt->store_result();
	$stmt->bind_result($Ingredid, $Ingred_Disc, $Group_Disc);
	while($row=$stmt->fetch()) {

		$sql = "SELECT count(UID) as CountIng FROM UsrAllergen where Ingredid = ? and UID = ? and Active =1;";

		$stmt1 = $db->prepare($sql);
		$stmt1->bind_param("ii",$Ingredid,$uid);
		$stmt1->execute();
		$stmt1->store_result();
		$stmt1->bind_result($count);
		$stmt1->fetch();
		if($count ==0){
			$Atmp =false;
		}else {
			$Atmp =true;
		}
			$tmp[]=array('id'=>$Ingredid,
							 'disc' =>$Ingred_Disc,
							 'state' => $Atmp
						);
	}
	$json[]=array(
			'Allergen' => $tmp
			);

			echo json_encode($json);
			$stmt->close();
			$db->close();
	?>
