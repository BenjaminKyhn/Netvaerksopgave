#!/usr/bin/env python3

import socket
import Adafruit_DHT

DHT_SENSOR = Adafruit_DHT.DHT11
DHT_PIN = 22
HOST = '192.168.43.127'
PORT = 65432

def readTemp():
    humidity, temperature = Adafruit_DHT.read_retry(DHT_SENSOR, DHT_PIN)

    if humidity is not None and temperature is not None:
        return temperature
    
def readHum():
    humidity, temperature = Adafruit_DHT.read_retry(DHT_SENSOR, DHT_PIN)

    if humidity is not None and temperature is not None:
        return humidity

with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
    s.bind((HOST, PORT))
    s.listen()        
    while True:
        print(readTemp())
        temp = str(readTemp())
        conn, addr = s.accept()
        print('Connected by', addr)
        conn.sendall(temp.encode('utf-8'))
        conn.close()
