[![HitCount](http://hits.dwyl.com/Embah-V2/AloraBypasser.svg)](http://hits.dwyl.com/Embah-V2/AloraBypasser)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![RSPS_Server](https://img.shields.io/badge/Server-Alora-red)](https://www.alora.io/)
[![Download](https://img.shields.io/badge/Download-latest-blue)](https://github.com/Embah-V2/AloraBypasser/raw/master/builds/AloraBypasser-fat.jar)
# AloraBypasser
> Minimalistic sandbox that allows user's to spoof MAC address, Serial Number, and monitor any System properties that alora accesses during run time. 
   Uses ASM's remapping capabilities to circumvent hardware bans on a (mostly) vanilla client

## Usage
Run with JDK 8
```
java -jar AloraBypasser-fat.jar --path C:\path\to\alora\client.jar
```
## Optional Arguments
```
--mac [random] => use a random Vendored MAC address 
--mac [XX:XX:XX:XX:XX:XX] => use the specified MAC address
```
```
--serial [random] => use a random Serial Number 
--serial [XXXXXXXXXXXXXX] => use the specified Serial number 
```
```
--debug => enable verbose debugging
```