import adafruit_trellism4
import time
import supervisor

trellis = adafruit_trellism4.TrellisM4Express()


def wheel(pos):
    if pos < 0 or pos > 255:
        return 0, 0, 0
    if pos < 85:
        return int(255 - pos * 3), int(pos * 3), 0
    if pos < 170:
        pos -= 85
        return 0, int(255 - pos * 3), int(pos * 3)
    pos -= 170
    return int(pos * 3), 0, int(255 - (pos * 3))


trellis.pixels.fill((0, 0, 0))
current_press = set()

while True:
    if supervisor.runtime.serial_bytes_available and input() == 'Reset':
        trellis.pixels[0, 0] = (0, 0, 0)

    pressed = set(trellis.pressed_keys)

    for press in pressed - current_press:
        x, y = press
        if x == y == 0:
            trellis.pixels[0, 0] = wheel(200)
            print('Trigger')

    current_press = pressed
