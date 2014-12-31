<?php
// Change directory to the input data file folder
chdir("/opt/lampp/htdocs");

$infile = 'newtimestamp.txt';
//$outfile = 'hadoop-output.txt';
//unlink($outfile);

$name = isset($_POST['Name']) ? $_POST['Name'] : '';
$constraint = isset($_POST['Constraint']) ? $_POST['Constraint'] : '';
$wattage = isset($_POST['Wattage']) ? $_POST['Wattage'] : '';
$starttime= isset($_POST['StartTime']) ? $_POST['StartTime'] : '';
$endtime=isset($_POST['EndTime']) ? $_POST['EndTime'] : '';
$runtime=isset($_POST['RunTime']) ? $_POST['RunTime'] : '';
$line = $name.",".$constraint.",".$wattage.",".$starttime.",".$endtime.",".$runtime."\n\r";
// Write the necessary contents
//file_put_contents($infile, "Check for android");
$file = fopen($infile,"a");
fwrite($file,$line);
fclose($file);
/*file_put_contents($infile, $name);
file_put_contents($infile, $wattage);
file_put_contents($infile, $starttime);
file_put_contents($infile, $endtime);

$intime = filectime($infile);*/
echo "File written into";

// Wait for the output file

/*while (!file_exists($outfile)) 
	{
		sleep(2);
	}*/

//sleep(4);
//echo "Here";
//echo file_get_contents($outfile);
//ob_flush();
//flush();
?>
