open fpAppliances, "<", "newtimestamp.txt";
open fpDayEnergy, "<",  "lookahead.txt";

for ($i=0;$i<24;$i++) {
	$C[$i] = 0;
	$P[$i] = 0;
	$J[$i] = 50;
}

$T[0] = 0;

$read = 0;
while(<fpDayEnergy>) {
        chomp;
        if ($_ =~ /^Start of Day Ahead Energy Price Data/) {
                $read = 1; next;
        }
        if ($read==1) {
                @words=split(/,/);
                for ($i=1;$i<=24;$i++) {
                        $PHash{$i-1} = $words[$i];
                }
                $read = 0;
        }
}

$i = 0;
foreach my $name (sort { $PHash{$a} <=> $PHash{$b} } keys %PHash) {
    #printf "$name, $PHash{$name}\n";
    $P[$i] = $name;
    $i += 1;
}

#print "Sorted Power:\n";
for ($i=0;$i<24;$i++) {
       print "$P[$i] $PHash{$P[$i]}\n";
}

while (<fpAppliances>) {
	chomp;@words=split(/,/);
  	if ($words[1] == 1) {
		$j = 0;
		for ($k = 0; ($k < 24) && ($j < $words[5]); $k++) {
			for ($i = $words[3]; $i < $words[4]; $i++) {
				if ($P[$k] == $i) {
					$C[$i] += $words[2];
					$j++;
					$HourAppHash{$i}{$words[0]} = $words[2];
				}
			}
		}
	} else {
		for ($i=0;$i<$words[5];$i++) {
			$C[$P[$i]] += $words[2];
			$HourAppHash{$P[$i]}{$words[0]} = $words[2];
		}
	}
}

#print "Renewable Source:\n";
#print "Hour Wattage\n";
#for ($i=0;$i<24;$i++) {
#        print "$i $J[$i]\n";
#}

for ($i=0; $i<24; $i++) {
	if ($J[$i] < $C[$i]) {
		$Cnew[$i] = $C[$i] - $J[$i];
	} else {
		$Cnew[$i] = 0;
	}
}

#print "Total Power Usage:\n";
#print "Hour Wattage\n";
for ($i=0;$i<24;$i++) {
	print "$i $C[$i] $Cnew[$i]\n";
}

#print "Soft Constraints:\n";
#print "TaskNumber Wattage\n";
#for ($i=0;$i<$#T;$i++) {
#	print "$i $T[$i]\n";
#}

#foreach $hour (keys %HourAppHash) {
#	print "$hour ";
#	foreach $app (keys %( $HourAppHash{$hour} ) ) {
#		print "$app:$HourAppHash{$hour}{$app} ";
#	}
#	print "\n";
#}
