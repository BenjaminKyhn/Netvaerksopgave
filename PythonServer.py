#!/usr/bin/env python3

import socket
import Adafruit_DHT
import time
from datetime import datetime
from Crypto.Cipher import AES
import base64
import sys
import os

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

# AES 'pad' byte array to multiple of BLOCK_SIZE bytes
def pad(byte_array):
    BLOCK_SIZE = 16
    pad_len = BLOCK_SIZE - len(byte_array) % BLOCK_SIZE
    return byte_array + (bytes([pad_len]) * pad_len)

# Remove padding at end of byte array
def unpad(byte_array):
    last_byte = byte_array[-1]
    return byte_array[0:-last_byte]

def encrypt(key, message):
    """
    Input String, return base64 encoded encrypted String
    """

    byte_array = message.encode("UTF-8")

    padded = pad(byte_array)

    # generate a random iv and prepend that to the encrypted result.
    # The recipient then needs to unpack the iv and use it.
    iv = os.urandom(AES.block_size)
    cipher = AES.new( key.encode("UTF-8"), AES.MODE_CBC, iv )
    encrypted = cipher.encrypt(padded)
    # Note we PREPEND the unencrypted iv to the encrypted message
    return base64.b64encode(iv+encrypted).decode("UTF-8")

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
        dataStr = temp + hum + timeStr

        encryptedStr = encrypt("boooooooooooomer", dataStr)
        print(encryptedStr)

        conn, addr = s.accept()
        print('Connected by', addr)
        conn.send(encryptedStr.encode('utf-8'))
        conn.close()
        time.sleep(2)

# https://www.novixys.com/blog/using-aes-encryption-decryption-python-pycrypto/ til at kryptere strengen