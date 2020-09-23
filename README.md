# lxc-bu

lxc container snapshot/backup scheduling/managment cli tool. inspired by lxc command structure. work in progress. 

to schedule snapshots add a cronjob:
*/5 * * * * lxc-bu e >> /var/log/lxc-bu.log

## missing features
- optional copy of snapshot to local machine
- cli autocompletion


## tech
- lxd
- graal jdk11 native-image aot
- spring-boot(-jpa) with hibernate 5.4.12
- jline

use graal 20+ & mvn 3.6.3 and run ./build.sh to build 

## demo
[![asciicast](https://asciinema.org/a/4gRokf7lQp1ZzrXjGCpKO2r2B.svg)](https://asciinema.org/a/4gRokf7lQp1ZzrXjGCpKO2r2B)
