#Processing Kafka streams with Concord
===================
Get Vagrant up and running from parent dir:

	$ ./bootstrap_vagrant.sh # only run this when you want to reconfigure
	$ vagrant ssh
	...
	vagrant@vagrant-ubuntu-trusty-64:~/$ cd /vagrant/kafka-scala-example/
	
Install SBT:

	$ echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
	$ sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 642AC823
	$ sudo apt-get update
	$ sudo apt-get install sbt

Run pre-package script:

	$ ./pre_package_concord.bash
	
