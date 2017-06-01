#!/usr/bin/python
#
# Convert bluetooth messages into Windows events.
#
# This is based in part on the rfcomm-server.py in the pybluez 
#    project available here: http://code.google.com/p/pybluez/
#
# Hazen 03/14
#

import bluetooth
import time
import pyautogui

server_sock = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
server_sock.bind(("",bluetooth.PORT_ANY))
server_sock.listen(1)
pyautogui.PAUSE = 0

port = server_sock.getsockname()[1]

uuid = "1a68fa50-a83b-11e3-9be7-425861b86ab6"

bluetooth.advertise_service( server_sock, "AdBtMServer",
                             service_id = uuid,
                             service_classes = [ uuid, bluetooth.SERIAL_PORT_CLASS ],
                             profiles = [ bluetooth.SERIAL_PORT_PROFILE ],
                             )
                   
print("Waiting for connection on RFCOMM channel %d" % port)

client_sock, client_info = server_sock.accept()
print("Accepted connection from ", client_info)

android_x = 0.0
android_y = 0.0
mouse_x = 0
mouse_y = 0
gain = 1000.0
start_time = 0
while True:
    data = client_sock.recv(1024)
    if (len(data) == 0):
        break
    data = data.decode("utf-8")
    messages = data.split("<>")
    for message in messages:
        if (len(message) > 0):
            message_data = message.split(",")
            if (message_data[0] == "actionmove" and len(message_data) >= 3):
            	pyautogui.moveRel(float(message_data[1]) * 50, float(message_data[2]) * 50)
            


print("disconnected")
client_sock.close()
server_sock.close()