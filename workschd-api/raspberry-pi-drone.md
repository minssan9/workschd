wget https://t1.daumcdn.net/cfile/cafe/99B1E5425EAD32AE1A
wget https://github.com/WiringPi/WiringPi/archive/refs/heads/master.zip
mv 99B1E5425EAD32AE1A aircopter.zip
sudo apt-get install zip unzip
unzip aircopter.zip
unzip master.zip
cd WiringPi-master
./build
gpio -v
gpio readall
cd ..