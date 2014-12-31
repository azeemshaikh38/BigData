<?php

chdir("/opt/lampp/htdocs/userver");
$infile = 'finalPower.txt';
$outfile = 'hadoop-output.txt';
$tempfile = 'tempfile.txt';
$schedule = $_POST['Schedule'];
$file = @fopen($infile,"w+");

$handle = @fopen($tempfile, "r+");
fwrite($file,$schedule);
fclose($file);

while (!file_exists($outfile)) 
	{
		sleep(2);
	}
sleep(15);
$ofile = @fopen($outfile, "r+");
$array = 0;
if ($handle ) {
while ((($buffer = fgets($handle))) && (($buffer1 = fgets($ofile)))) {
  $piecest = explode(" ", $buffer,3);
  echo $piecest[0]; // piece1
  echo $piecest[1]; // piece2
  echo $buffer1;
  $pieceso = explode(" ", $buffer1,3);
  echo $pieceso[0]; // piece1
  echo $pieceso[1]; // piece2

  if($pieceso[1] > $piecest[1]){
    $array++;
    echo "The limit exceeded at".$pieceso[0];
  }
  if (!feof($handle)) {
        echo "Error: unexpected fgets() fail\n";
    }
  }
}
if($array==0)
 echo "Succeeded";
//fclose($handle);
//fclose($ofile);
//echo $schedule;
?>
