# Jumping Jackpot - Interactive Game

Jumping Jackpot is an interactive game developed by myself, Mueez Khan, and Amal Suleiman. The rules are simple: jump whenever the glowing lights hit the ground near you (like you would a skipping rope), and get successful jumps for an increased multiplier bonus! Beware though: the higher your multiplier is, the harder the the difficuty will get. It is based on the original Jumping Fantasy arcade game typically seen in arcades such as Chuck E. Cheese's. It is written in Java and C++, and consists of an LED ring, an Arduino Lilypad (with accelerometer), and a computer to run the Java program. All components communicate using the Zigbee protocol. 

## Demo Video

[![JUMPING JACKPOT DEMO VIDEO](http://img.youtube.com/vi/aGGAnzD7eyI/0.jpg)](http://www.youtube.com/watch?v=p5mK1N_8moQ)

## Components

### 1. Arduino with Accelerometer
An Arduino Lilypad is used powered a battery pack (of 3 AA's to provide 4.5V of power). This choice of hardware is made with mobility in consideration, as the Lilypad and its components are perfect for applications that require a lot of movement (such as jumping). An accelerometer is attached to detect a "jump" (which is sent to a computer via the Lilypad Zigbee chip), and is calibrated by recording the specific pattern of vertical "y" acceleration that results whenever a player jumps, as seen in the below code:

```
if (acmVal > 750) {               //acmVal > 750 indicates a "jump"
    sendMessage(ID, JUMPED);
    delay(325);
    while(acmVal > 610 && acmVal < 604);
    delay(325);
    while(acmVal > 610 && acmVal < 604);
    sendMessage(ID, LANDED);

  }
```

### 2. LED Strip
An LED strip is used to display the "ring of lights" that the player will use as a guide on when to jump. It is connected to another Arduino Lilypad and programmed using the Adafruit_NeoPixel library. Its task is to notify the main computer whenever the lights are at feet-level via Lilypad Zigbee, and is also capable of receiving commands from the computer as to whenever a game starts, ends, or increase/decrease difficulty (speed of the light's revolutions around the ring).

### 3. Java Game Software
The main game software runs in Java. It communicates with the accelerometer and LED strip using the Observer design pattern and changes the game difficulty (LED speed) using the Template and Strategy design patterns.

For more information, please refer to the in-depth [code description](../master/Jumping%20Jackpot%20In-Depth%20Code%20Description.docx).
