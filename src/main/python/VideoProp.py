from time import sleep
sleep(10)
from omxplayer.player import OMXPlayer
from pathlib import Path
import RPi.GPIO as GPIO
import pygame
import pygame.freetype
from pygame.locals import *

pygame.init()
pygame.font.init()
pygame.mixer.init()
pygame.mixer.music.load("audio0.wav")
pygame.mixer.music.play(-1)
screen = pygame.display.set_mode(pygame.display.list_modes(32)[0], pygame.FULLSCREEN)
pygame.display.set_caption('Video Prop')
pygame.mouse.set_visible(0)
#background = pygame.image.load('background.png')
clock = pygame.time.Clock()
font = pygame.freetype.Font('audiowide.ttf', 95)
#font = pygame.freetype.SysFont('Arial', 110)

def word_wrap(surf, text, font, color=(0, 0, 0)):
    font.origin = True
    words = text.split(' ')
    width, height = surf.get_size()
    width = width - 80
    line_spacing = font.get_sized_height() + 2
    x, y = 40, line_spacing
    space = font.get_rect(' ')
    for word in words:
        bounds = font.get_rect(word)
        if x + bounds.width + bounds.x >= width:
            x, y = 40, y + line_spacing
        if x + bounds.width + bounds.x >= width:
            raise ValueError("word too wide for the surface")
        if y + bounds.height - bounds.y >= height:
            raise ValueError("text to long for the surface")
        font.render_to(surf, (x, y), None, color)
        x += bounds.width + space.width
    return x, y

def play_video():
    player = OMXPlayer('video.mp4', dbus_name='org.mpris.MediaPlayer2.omxplayer1', args=['--loop', '--no-osd'])
    sleep(2.5)
    player.set_position(5)
    player.pause()
    sleep(2)
    player.set_aspect_mode('stretch')
    #player.set_fullscreen()
    # player.set_video_pos(0, 0, 200, 200)
    player.play()
    return player

GPIO.setmode(GPIO.BCM)
GPIO.setup(17, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)
GPIO.setup(27, GPIO.IN, pull_up_down=GPIO.PUD_DOWN)

triggered = False
player = play_video()
while True:
    if (GPIO.input(17) and not triggered):
        print('trigger')
        pygame.mixer.Sound('audio1.wav').play()
        triggered = True
        player.quit()
    if (GPIO.input(27)):
        player.quit()
        print('reset')
        triggered = False
        player = play_video()
    for event in pygame.event.get():
        if event.type == QUIT or (event.type == KEYDOWN and event.key == K_ESCAPE):
            pygame.mixer.music.stop()
            pygame.quit()
            player.quit()
            quit()
#    screen.fill((0, 0, 0))
    #drawText(screen, 'I have traveled throughout my universe, which has 3 sectors. Where my hand is lit, I have visited twice adn where my hand is not lit I have traveled once. Count how many places I have landed in each sector to release you to the next compartment of the ship.', (255, 0, 0), screen.get_rect(), font)
    #text = font.render('I have traveled throughout my universe, which has 3 sectors. Where my hand is lit, I have visited twice adn where my hand is not lit I have traveled once. Count how many places I have landed in each sector to release you to the next compartment of the ship.', True, (255, 0, 0))
    #screen.blit(text, (0,0))
    word_wrap(screen, 'I have traveled throughout my universe, which has 3 sectors. Where my hand is lit, I have visited twice and where my hand is not lit I have traveled once. Count how many places I have landed in each sector to release you to the next compartment of the ship.', font, (255, 0, 0))
    pygame.display.update()
    clock.tick(1)
