import bluetooth
import pyautogui
import os

server_socket = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
server_socket.bind(("",bluetooth.PORT_ANY))
server_socket.listen(1)

port = server_socket.getsockname()[1]

uuid = "e424b9c1-9537-432d-94f8-8690235bd85b"
pyautogui.PAUSE = 0

bluetooth.advertise_service(
    server_socket, "ProjectXServer",
    service_id=uuid,
    service_classes=[uuid, bluetooth.SERIAL_PORT_CLASS],
    profiles=[bluetooth.SERIAL_PORT_PROFILE]
)

print("Waiting for connection from the application")

client_socket, client_info = server_socket.accept()
print("Successfully connected to the application")

cursor_speed = 1

while True:
    data = client_socket.recv(1024)
    if len(data) == 0:
        break
    data = data.decode("utf-8")
    messages = data.split("<>")
    for message in messages:
        if len(message) > 0:
            message_data = message.split(",")
            if message_data[0] == "scroll_down":
                pyautogui.scroll(10)
            if message_data[0] == "scroll_up":
                pyautogui.scroll(-10)
            if message_data[0] == "actionleftclick":
                pyautogui.click()
            if message_data[0] == "actionrightclick":
                pyautogui.click(button='right')
            if message_data[0] == "actionmove":
                try:
                    pyautogui.moveRel(float(message_data[1]) * cursor_speed, float(message_data[2]) * cursor_speed)
                except Exception:
                    print(message_data)
            if message_data[0] == "execute_command":
                os.system(message_data[1])
            if message_data[0] == "actionspeed":
                cursor_speed = float(message_data[1]) * 0.2 


