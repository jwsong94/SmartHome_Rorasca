# test BLE Scanning software
# jcs 6/8/2014

import blescan
import sys

import bluetooth._bluetooth as bluez

dev_id = 0
txString = [0, 0, 0, 0]
rssi = [0, 0, 0, 0]
distance = [0, 0, 0, 0]
s = ["", "", "", "", "", "", ""]
try:
	sock = bluez.hci_open_dev(dev_id)
	print "ble thread started"

except:
	print "error accessing bluetooth device..."
    	sys.exit(1)

blescan.hci_le_set_scan_parameters(sock)
blescan.hci_enable_le_scan(sock)

while True:
	returnedList = blescan.parse_events(sock, 4)
	print "----------"
	for beacon in returnedList: 
        s = beacon.split(',')
        print rssi
		if s[1].find('11111111')>-1: 
            rssi[0] = int(s[5]) 
            txString[0] = int(s[4])
        elif s[1].find('22222222')>-1: 
            rssi[1] = int(s[5])
            txString[1] = int(s[4])
        elif s[1].find('33333333')>-1:
            rssi[2] = int(s[5])
            txString[2] = int(s[4])
        elif s[1].find('44444444')>-1:
            rssi[3] = int(s[5])
            txString[3] = int(s[4])
        else: pass

