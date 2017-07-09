# test BLE Scanning software
# jcs 6/8/2014

import blescan
import sys

import bluetooth._bluetooth as bluez

dev_id = 0
txString = [0, 0, 0, 0]
rssi = [0, 0, 0, 0]
distance = [0, 0, 0, 0]
kal_P = [1, 1, 1, 1]
kal_K = [1.0, 1.0, 1.0, 1.0]
kal_X = [-61, -61, -61, -61]
kal_Q = 0.01*0.01
kal_R = 0.1*0.1

def KalmanFiltering(myIndex, txs, rss):
    kal_P[myIndex] = kal_P[myIndex] + kal_Q
    kal_K[myIndex] = kal_P[myIndex] / (kal_P[myIndex] + kal_R)
    kal_X[myIndex] = kal_X[myIndex] + (kal_K[myIndex] * (rss - kal_X[myIndex]))
    kal_P[myIndex] = (1 - kal_K[myIndex]) * kal_P[myIndex]

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
        beaconId = 9
        s=beacon.split(',')
        if s[1].find('11111111')>-1: beaconId = 0
        elif s[1].find('22222222')>-1: beaconId = 1
        elif s[1].find('33333333')>-1: beaconId = 2
        elif s[1].find('44444444')>-1: beaconId = 3
        if beaconId != 9:
            rssi[beaconId] = int(s[5])
            txString[beaconId] = int(s[4])
            KalmanFiltering(beaconId, txString[beaconId], rssi[beaconId])
            print 'rssi'
            print rssi
            print 'txString'
            print txString
            print 'Filtered'
            print kal_X

