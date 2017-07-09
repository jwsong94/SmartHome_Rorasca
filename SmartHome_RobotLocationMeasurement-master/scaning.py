# test BLE Scanning software
# jcs 6/8/2014

import blescan
import sys

import bluetooth._bluetooth as bluez

dev_id = 0
txString = [0, 0, 0, 0]
rssi = [0, 0, 0, 0]
distance = [0, 0, 0, 0]

def calculateDistance(txs, rss):
    if rss==0: return -1
    ratio = rss*1.0/txs
    if ratio<1.0: return pow(ratio,10)
    else: return (0.89976)*pow(ratio, 7.7095)+0.111

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
		s=beacon.split(',')
        if s[1].find('11111111')>-1:
            rssi[0] = int(s[5])
            txString[0] = int(s[4])
            distance[0] = calculateDistance(txString[0], rssi[0])
        elif s[1].find('22222222')>-1:
            rssi[1] = int(s[5])
            txString[1] = int(s[4])
            distance[1] = calculateDistance(txString[1], rssi[1])
        elif s[1].find('33333333')>-1:
            rssi[2] = int(s[5])
            txString[2] = int(s[4])
            distance[2] = calculateDistance(txString[2], rssi[2])
        elif s[1].find('44444444')>-1:
            rssi[3] = int(s[5])
            txString[3] = int(s[4])
            distance[3] = calculateDistance(txString[3], rssi[3])
        print 'rssi'
        print rssi
        print 'txString'
        print txString
        print 'Distance'
        print distance

