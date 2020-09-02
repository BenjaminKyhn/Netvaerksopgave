#!/usr/bin/env python3

import socket
import Adafruit_DHT
import time
from datetime import datetime

DHT_SENSOR = Adafruit_DHT.DHT11
DHT_PIN = 22
HOST = '192.168.1.6'
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
        timestamp = datetime.now()
        timeStr = timestamp.strftime("%H:%M:%S")
        temp = str(readTemp())
        hum = str(readHum())
        print(timeStr)
        print(temp)
        print(hum)
        conn, addr = s.accept()
        print('Connected by', addr)
        conn.send((temp + hum + timeStr).encode('utf-8'))
        conn.close()
        time.sleep(900000)