<?php

	$loc = $_GET["loc"];
	//echo $loc;
 
	$username="jin";
	$password="newuserwu";

	$database="global";
	$conn = mysql_pconnect("mysql.osg.ufl.edu:3310",$username,$password);

	if (!$conn)
	{
		die('Could not connect: ' . mysql_error());
	} else {
		//echo "Connection Established"; 
		mysql_select_db($database, $conn);
		
		$query = "SELECT * FROM fawn_lookup";
		echo $query."\r\n";
		$result = mysql_query($query); //run the query
		//echo mysql_fetch_array($result)."<br>";
		echo "LocId,"."state".",Location".",station_type".",active".",latitude".",longitude".",start_date".",facility".",country".",shefID".",sys_name".",display_name".",tz_offset".",elevation"."\r\n";
		
		while($row = mysql_fetch_array($result))
		{	
			echo $row["LocID"].",".$row["state"].",".$row["Location"].",".$row["station_type"].",".$row["active"].",".$row["latitude"].",".$row["longitude"].",".$row["start_date"].",".$row["facility"].",".$row["country"].",".$row["shefID"].",".$row["sys_name"].",".$row["display_name"].",".$row["tz_offset"].",".$row["elevation"]."\r\n";
		}
		
		mysql_close;
	}
	
	
	//echo "Query performed successfully";
?>