rssi = [-64, -84, -63, -65]
txString = [-59, -59, -59, -59]
distance = [0.0, 0.0, 0.0, 0.0]

def calculateDistance(txs, rss):
    if rss==0: return -1
    ratio = rss*1.0/txs
    if ratio<1.0: return pow(ratio,10)
    else: return (0.89976)*pow(ratio, 7.7095)+0.111

distance[0] = calculateDistance(txString[0], rssi[0])
distance[1] = calculateDistance(txString[1], rssi[1])
distance[2] = calculateDistance(txString[2], rssi[2])
distance[3] = calculateDistance(txString[3], rssi[3])
print distance
