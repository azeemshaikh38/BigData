<?php
// Change directory to the input data file folder
chdir("/opt/lampp/htdocs");

$infile = 'newtimestamp.txt';
$outfile = 'newtimestampedit.txt';
$handle = @fopen($infile, "r");
$file = fopen($outfile,"w+");

//$outfile = 'hadoop-output.txt';
//unlink($outfile);
if ($handle) {
    while (($buffer = fgets($handle)) !== false) {
        if( strpos($buffer,isset($_POST['Name']) ? $_POST['Name'] : '') !== false) {
        // do stuff
          $name = isset($_POST['Name']) ? $_POST['Name'] : '';
          $constraint = isset($_POST['Constraint']) ? $_POST['Constraint'] : '';
          $wattage = isset($_POST['Wattage']) ? $_POST['Wattage'] : '';
          $starttime= isset($_POST['StartTime']) ? $_POST['StartTime'] : '';
          $endtime=isset($_POST['EndTime']) ? $_POST['EndTime'] : '';
          $runtime=isset($_POST['RunTime']) ? $_POST['RunTime'] : '';
          $line = $name.",".$constraint.",".$wattage.",".$starttime.",".$endtime.",".$runtime."\n\r";
          echo $line;
          fwrite($file,$line);
       }
       else{
        fwrite($file,$buffer);
      } 
    }
    if (!feof($handle)) {
        echo "Error: unexpected fgets() fail\n";
    }
fclose($handle);
}

$con = file_get_contents($outfile);
file_put_contents($infile,$con);
/**/
fclose($file);
//ob_flush();
//flush();
?>
