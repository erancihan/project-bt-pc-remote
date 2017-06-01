import bluetooth
import pyautogui
import os

server_socket = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
server_socket.bind(bluetooth.PORT_ANY)
server_socket.listen(1)

port = server_socket.getsockname()[1]

uuid = "e424b9c1-9537-432d-94f8-8690235bd85b"

bluetooth.advertise_service(
    server_socket, "ProjectXServer",
    service_id=uuid,
    service_classes=[uuid, bluetooth.SERIAL_PORT_CLASS],
    profiles=[bluetooth.SERIAL_PORT_PROFILE]
)

print("Waiting for connection from the application")

client_socket, client_info = server_socket.accept()
print("Successfully connected to the application")

while True:
    data = client_socket.recv(1024)
    if len(data) == 0:
        break

    messages = data.split("<>")
    for message in messages:
        if len(message) > 0:
            message_data = message.split(",")
            if message_data[0] == "scroll_down":
                pyautogui.scroll(10)
            if message_data[0] == "scroll_up":
                pyautogui.scroll(-10)
            if message_data[0] == "left_click":
                pyautogui.click()
            if message_data[0] == "right_click":
                pyautogui.click(button='right')
            if message_data[0] == "mouse_move":
                pyautogui.moveRel(message_data[1], message_data[2])
            if message_data[0] == "execute_command":
                os.system(message_data[1])
