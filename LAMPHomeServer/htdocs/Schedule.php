
<?php 
   //throw new HttpNotFoundException;
   chdir("/opt/lampp/htdocs");
   $infile = 'newtimestamp.txt';
   $lookahead = 'lookahead.txt';
   $outfile = 'schedule-output.txt';
   $todate = trim(shell_exec('date +%Y')).trim(shell_exec('date +%m')).trim(shell_exec('date +%d'));
   $tomodate = trim(shell_exec('date +%Y')).trim(shell_exec('date +%m')).trim(shell_exec('date +%d'))+1;
   $url = @file_get_contents("192.251.13.151"."/pub/account/lmpda/".$tomodate."-da.csv");
      if($url)
      file_put_contents("lookahead.txt", file_get_contents("192.251.13.151"."/pub/account/lmpda/".$tomodate."-da.csv"));
      else
      file_put_contents("lookahead.txt", file_get_contents("192.251.13.151"."/pub/account/lmpda/".$todate."-da.csv"));
   
   $result = shell_exec('perl schedule.pl'); # in linux $result = shell_exec(perl perl.pl)
   echo $result;
   file_put_contents($outfile,$result);
?>
