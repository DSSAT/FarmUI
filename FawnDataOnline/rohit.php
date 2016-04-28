<!DOCTYPE html>
<html>
<body>
<?php
		// CONNECT AND LOGON TO DB//
		$username="jin";
		$password="newuserwu";
		$database="fawn_data";
		$conn = mysql_pconnect("mysql.osg.ufl.edu:3310",$username,$password);
		if(isset($_GET['runFunction']) && function_exists($_GET['runFunction']))
			call_user_func($_GET['runFunction']);
		
		function test(){
			echo("test");
		}
		if (!$conn)
		{
			die('Could not connect: ' . mysql_error());
		} 
		
		/*++++++++++++++++++++++++++++++++++++$sql = "SELECT id, firstname, lastname FROM MyGuests";
		$result = $conn->query($sql);
		
		if ($result->num_rows > 0) {
			 // output data of each row
			 while($row = $result->fetch_assoc()) {
				 echo "<br> id: ". $row["id"]. " - Name: ". $row["firstname"]. " " . $row["lastname"] . "<br>";
			 }
		} else {
			echo "0 results";
		}*/

		$conn->close();
		
		/*$dbcon=mysql_connect('if-srvv-mysql.ad.ufl.edu','if-svc-bmpmodel','phE=RUpREbut') or die("Could not connect to MYSQL Server");
		mysql_select_db('ageng_bmpmodel') or die("Could not select DB ageng_bmpmodel");*/
?>
</body>
</html>