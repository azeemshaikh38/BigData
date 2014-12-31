#! /bin/bash
# A script to run the Hadoop wordcount example program on all
# files 

oldTstamp=0

while true; do
newTstamp=`stat -c %Y /opt/lampp/htdocs/userver/finalPower.txt`

# Compare the Timestamps

if [ $newTstamp -gt $oldTstamp ]
then
	echo "New data to be processed \n"
	oldTstamp=$newTstamp

	# If new data is available start the hadoop processes
	cd /opt/lampp/htdocs/userver/

	# Clear all the snip directories under tmp
	sudo /usr/local/hadoop/hadoop-1.2.1/bin/hadoop dfs -rmr /opt/lampp/htdocs/userver-hdfs-output
	sudo /usr/local/hadoop/hadoop-1.2.1/bin/hadoop dfs -rmr /opt/lampp/htdocs/userver-hdfs-input
        //sudo rm hadoop-output.txt

	# Copy files into the HDFS
	echo "Copying the new data into HDFS \n"
	sudo /usr/local/hadoop/hadoop-1.2.1/bin/hadoop dfs -put /opt/lampp/htdocs/userver/finalPower.txt /opt/lampp/htdocs/userver-hdfs-input/  
        sudo /usr/local/hadoop/hadoop-1.2.1/bin/hadoop dfs -put /opt/lampp/htdocs/userver/hadoop-output.txt /opt/lampp/htdocs/userver-hdfs-input/
	

	# Run the mapreduce job
	echo "Running the map-reduce"
	echo
	sudo /usr/local/hadoop/hadoop-1.2.1/bin/hadoop jar PMR.jar org.myorg.Server /opt/lampp/htdocs/userver-hdfs-input/ /opt/lampp/htdocs/userver-hdfs-output
	echo
	sudo /usr/local/hadoop/hadoop-1.2.1/bin/hadoop dfs -get /opt/lampp/htdocs/userver-hdfs-output/part-00000 /opt/lampp/htdocs/userver/hadoop-output.txt
	sudo chmod 777 /opt/lampp/htdocs/userver/hadoop-output.txt
	echo "Output processed to /home/hduser/tmp/hadoop-output"
fi
done

exit
