<?php
// Change directory to the input data file folder
chdir("/opt/lampp/htdocs");

$infile = 'newtimestamp.txt';
$outfile = 'newtimestampdel.txt';
$handle = @fopen($infile, "r");
$file = fopen($outfile,"w+");

//$outfile = 'hadoop-output.txt';
//unlink($outfile);
if ($handle) {
    while (($buffer = fgets($handle)) !== false) {
        if( strpos($buffer,isset($_POST['Name']) ? $_POST['Name'] : '') !== false) {
        // do stuff
          echo $buffer;
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
fclose($file);
/**/
$con = file_get_contents($outfile);
file_put_contents($infile,$con);
//ob_flush();
//flush();
?>
