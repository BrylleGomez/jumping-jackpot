#ifdef __AVR__
  #include <avr/power.h>
#endif

// Libraries
#include <SoftwareSerial.h>
#include <Adafruit_NeoPixel.h>

// Constants
#define ID          0
#define PIN         5
#define NUM_PX       150       //number of LEDs in the entire pixels
#define START_PX     0         // LED # where you want the LED loop to begin
#define END_PX       127       // LED # where you want the LED loop to end
#define BAND_SIZE    5         // how many LEDs are in the rotating band 
#define bottomLED1  121      // first LED of the bottom LED band that player needs to jump over
#define bottomLED2  127      // last LED of the bottom LED band that player needs to jump over
#define leftLED     33
#define rightLED    94
#define bonusLED    56
#define NUM_ROUNDS   20      

// Instructions
#define   BOTLED1     1
#define   BOTLED2     2
#define   GAMEOVER    3

// Global variables
Adafruit_NeoPixel pixels = Adafruit_NeoPixel(NUM_PX, PIN, NEO_GRB + NEO_KHZ800); 
SoftwareSerial mySerial(10, 11);     // RX, TX
int speedx;            // delay in ms; denotes speed of LED revolution around the ring; lower means faster
int dirx;              // direction of the LED; 0 for CW, 1 for CCW
byte redpx;            // value of the Red component of RGB LED lighting
byte greenpx;          // value of the Green component of RGB LED lighting
byte bluepx;           // value of the Blue component of RGB LED lighting

// Set-up
void setup() {
  pixels.begin();                     // This initializes the NeoPixel library.
  mySerial.begin(9600);              // initialize the serial communications:
  //Serial.begin(9600);

  speedx = 500;                        // set initial speed to 25
  dirx = 0;                           // set initial direction to CW

  pixels.begin();
  setAll(0,0,0);
 
}

// Loop
void loop() {

  //rainbowCycle(1);

  if (mySerial.available()) {
    int n = mySerial.read() - 48; // read an integer from console and convert from ASCII to int
    //Serial.println(n);
    if (n == 4) {                 // if instruction 4 (game start) is received 

      // Game Start
      setAll(255,0,0);
      delay(750);
      setAll(0,0,0);
      delay(250);
      setAll(255,255,0);
      delay(750);
      setAll(0,0,0);
      delay(250);
      setAll(0,255,0);
      delay(750);
      setAll(0,0,0);

      // Game Proper
      for(int j = 0; j < NUM_ROUNDS; j++) { 
        for (int i = START_PX; i <= END_PX; i++) {
          // Waits for run-time speed instructions from game thread
          if (mySerial.available()) {
            int x = mySerial.read();
            Serial.println(x);
            mySerial.flush();
            if (x != 10) {
              x -= 48;
              switch(x){
                case 1:
                  speedx = 50;
                  break;
                case 2:
                  speedx = 20;
                  break;
                case 5:
                  speedx = 1;
                  break;
                default:
                  ;  
              } // end switch
            } // end if
          } // end if

          // Set LED color for this iteration
          redpx = 150;
          greenpx = 0;
          bluepx = 0;
  
          // pixels.Color takes RGB values, from 0,0,0 up to 255,255,255
          // turns on LED lights and makes them go around the ring
          pixels.setPixelColor((i % (END_PX-START_PX+1)), pixels.Color(redpx,greenpx,bluepx)); 
          pixels.setPixelColor(((i+1) % (END_PX-START_PX+1)), pixels.Color(redpx,greenpx,bluepx));
          pixels.setPixelColor(((i+2) % (END_PX-START_PX+1)), pixels.Color(redpx,greenpx,bluepx));
          pixels.setPixelColor(((i+3) % (END_PX-START_PX+1)), pixels.Color(redpx,greenpx,bluepx));
          pixels.show(); // Show the changes made to the LED colors (above)
          delay(speedx); // dictates speed of LED rotation around the ring
          pixels.setPixelColor((i % (END_PX-START_PX+1)), pixels.Color(0,0,0)); 
          pixels.setPixelColor(((i+1) % (END_PX-START_PX+1)), pixels.Color(0,0,0));
          pixels.setPixelColor(((i+2) % (END_PX-START_PX+1)), pixels.Color(0,0,0));
          pixels.setPixelColor(((i+3) % (END_PX-START_PX+1)), pixels.Color(0,0,0));
          pixels.show(); // Turn the LEDs back off

          // Send signals at specific points in the LED ring
          if (i == bottomLED1) {               // bottomLED indicates the LED # that, once lit up, the player has to jump over
            sendMessage(ID, BOTLED1);              // send 00000001 if bottom LEDs are on (ID 00, instruction 000001)
          }
          if (i == bottomLED2) {
            sendMessage(ID, BOTLED2);             // send 00000010 if bottom LEDs are on (ID 00, instruction 000010)
          }
  
        } // end for
      } // end for

      // Game Over
      sendMessage(ID, GAMEOVER);
      for (int i = START_PX; i <= END_PX; i++) {
        pixels.setPixelColor((i % (END_PX-START_PX+1)), pixels.Color(255,0,0)); 
        pixels.setPixelColor(((i+1) % (END_PX-START_PX+1)), pixels.Color(255,0,0));
        pixels.setPixelColor(((i+2) % (END_PX-START_PX+1)), pixels.Color(255,0,0));
        pixels.setPixelColor(((i+3) % (END_PX-START_PX+1)), pixels.Color(255,0,0));
        pixels.show(); // Show the changes made to the LED colors (above) 
      }
      delay(2000);
      setAll(0,0,0);

    } // end if (n == 4)
  } // end if (mySerial.available())
} // end loop()


void sendMessage(byte id, byte payload) {
  byte m = (id << 6) | payload;
  mySerial.println(m);
}

///////////////////////////////////////////////////////////////// NeoPixel Library Functions /////////////////////////////////////////////////////////////////////////////////////////

void showpixels() {
 #ifdef ADAFRUIT_NEOPIXEL_H 
   // NeoPixel
   pixels.show();
 #endif
 #ifndef ADAFRUIT_NEOPIXEL_H
   // FastLED
   FastLED.show();
 #endif
}

void setPixel(int Pixel, byte red, byte green, byte blue) {
 #ifdef ADAFRUIT_NEOPIXEL_H 
   // NeoPixel
   pixels.setPixelColor(Pixel, pixels.Color(red, green, blue));
 #endif
 #ifndef ADAFRUIT_NEOPIXEL_H 
   // FastLED
   leds[Pixel].r = red;
   leds[Pixel].g = green;
   leds[Pixel].b = blue;
 #endif
}

void setAll(byte red, byte green, byte blue) {
  for(int i = 0; i < NUM_PX; i++ ) {
    setPixel(i, red, green, blue); 
  }
  showpixels();
}

void rainbowCycle(int SpeedDelay) {
  byte *c;
  uint16_t i, j;

  for(j=0; j<256*5; j++) { // 5 cycles of all colors on wheel
    for(i=0; i< NUM_PX; i++) {
      c=Wheel(((i * 256 / NUM_PX) + j) & 255);
      setPixel(i, *c, *(c+1), *(c+2));
    }
    showpixels();
    delay(SpeedDelay);
  }
}

byte * Wheel(byte WheelPos) {
  static byte c[3];
  
  if(WheelPos < 85) {
   c[0]=WheelPos * 3;
   c[1]=255 - WheelPos * 3;
   c[2]=0;
  } else if(WheelPos < 170) {
   WheelPos -= 85;
   c[0]=255 - WheelPos * 3;
   c[1]=0;
   c[2]=WheelPos * 3;
  } else {
   WheelPos -= 170;
   c[0]=0;
   c[1]=WheelPos * 3;
   c[2]=255 - WheelPos * 3;
  }

  return c;
}
