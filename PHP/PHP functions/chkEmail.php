<?php

  session_start();
  include('Functions/function.php');
  $Email = $_GET['Email'];

  //$Email = 'Test';
  $chk = checkUser($Email);

  $json[]=array(
			'chkEmail' => $chk
			);
	echo json_encode($json);
?>
