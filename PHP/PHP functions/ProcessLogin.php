<?php

    session_start();
    include('Functions/function.php');

    if(isset($_GET['Email']) && isset($_GET['userpass'])){

      $Email = $_GET['Email'];
      $pword = $_GET['userpass'];
      $db = createConnection();
      $sql = "Select UID, Salt, hash from tblUsers where Email = ?;";
      $stmt = $db->prepare($sql);
    	$stmt->bind_param("s",$Email);
    	$stmt->execute();
    	$stmt->store_result();
    	$stmt->bind_result($UID, $Salt, $Hash);
    	$stmt->fetch();

      $mkHash = makeHash($pword, $Salt, 50);

      //echo "password : $pword </br>hash :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; $Hash</br> mkHash : $mkHash</br> Salt : $Salt";
      if($Hash == $mkHash){

        $json[]=array(
      			'UserID' => $UID
      			);
      }else{
        $json[]=array(
      			'UserID' => 0
      			);
      }
      echo json_encode($json);
    	$stmt->close();
    	$db->close();
    }
  ?>
