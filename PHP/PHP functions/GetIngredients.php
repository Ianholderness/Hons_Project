<?php

	session_start();
	include('Functions/function.php');

	$db = createConnection();

	$Barcode=$_GET['Barcode'];
	$uid = $_GET['Uid'];

	$allergen = 0;
//Gets the number of view currently in Products table
	$sql = "Select a.Views
				from Products as a
				Where a.Barcode = ?;";
	$stmt = $db->prepare($sql);
	$stmt->bind_param("i",$Barcode);
	$stmt->execute();
	$stmt->store_result();
	$stmt->bind_result($Vcount);
	$stmt->fetch();
	$Vcount = $Vcount+1;

	//echo "TEst 1 : $Vcount $Barcode date()";


//Updates the values increasing the value by 1
	$sql ="Update Products Set views = ? Where Barcode = ?;";
	$stmt = $db->prepare($sql);
	$stmt->bind_param("is",$Vcount,$Barcode);
	$stmt->execute();

//echo "TEst 2";
//Gets the product discription
	$sql = "Select a.Prod_Disc
				from Products as a
				Where a.Barcode = ?;";

	$stmt = $db->prepare($sql);
	$stmt->bind_param("i",$Barcode);
	$stmt->execute();
	$stmt->store_result();
	$stmt->bind_result($Disc);
	$stmt->fetch();
	$Pdisc = $Disc;
//echo "TEst 3 $Disc";

	$sql = "Select a.Ingred_Disc, a.Ingredid
			from Ingredients as a
			inner join Food_Ingredients as b on
			a.Ingredid = b.Ingredid
			Where Barcode = ?
			order by a.Groupid desc;";

	$stmt = $db->prepare($sql);
	$stmt->bind_param("i",$Barcode);
	$stmt->execute();
	$stmt->store_result();
	$stmt->bind_result($Ingrent, $id);
	while($row=$stmt->fetch()) {
		//echo "TEst 4 $Ingrent $id";

		$sql = "SELECT count(UID) as CountIng FROM UsrAllergen where Ingredid = ? and UID = ? and Active =1;";

		$stmt1 = $db->prepare($sql);
		$stmt1->bind_param("ii",$id,$uid);
		$stmt1->execute();
		$stmt1->store_result();
		$stmt1->bind_result($count);
		$stmt1->fetch();
		
		//Stores if the user does have an allergen marked its stored in No allergen array else it stored in the allergen array

		if($count ==0){
			$Atmp =false;
			$NoAllergen[]=array('id'=>$id,
					  	 'Ingredent' =>$Ingrent,
							 'Allergen' =>$Atmp
						);
		}else {
			$Atmp =true;
			$allergen = 1;
			$Allergen[]=array('id'=>$id,
					  	 'Ingredent' =>$Ingrent,
							 'Allergen' =>$Atmp
						);
		}
	}
	
	//Combins the two arrays with the allergens first meaning these will be at the top of the list
	if($allergen == 0){
		$tmp = $NoAllergen;
	}Else{
		$tmp = array_merge($Allergen, $NoAllergen);
	}
	$json[]=array(
			'Product_Disc' => $Pdisc,
			'AllergenFound' => $allergen,
			'Ingrent' => $tmp
			);

	echo json_encode($json);
	$stmt->close();
	$db->close();
?>
