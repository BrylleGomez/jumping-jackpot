#include <SoftwareSerial.h>

#define ID          1
#define PIN         A2
#define JUMP_VAL    750

#define JUMPED      1
#define LANDED      2
 
SoftwareSerial mySerial(10, 11);     // RX, TX
const int ypin = PIN;                 // y-axis
int acmVal;                          // stores ypin sensor values
 
void setup() {
  mySerial.begin(9600);              // initialize the serial communications:
  Serial.begin(9600);
  int n;
  do {
    n = mySerial.read() - 48;
    Serial.println(n);
    delay(1);
  } while (n != 4);

}
 
void loop() {
  
  acmVal = analogRead(ypin);
  //mySerial.println(acmVal);
  //Serial.println(acmVal);
  if (acmVal > JUMP_VAL) {               //acmVal > 700 indicates a "jump"
    sendMessage(ID, JUMPED);              // send 01000001 if jump detected (ID 01, instruction 000001)
    //mySerial.println(65);
    delay(325);
    while(acmVal > 610 && acmVal < 604);
    delay(325);
    while(acmVal > 610 && acmVal < 604);
    sendMessage(ID, LANDED);              // send 01000010 if land detected (ID 01, instruction 000010)
    //mySerial.println(66);
  }
  delay(25);

  
}

void sendMessage(byte id, byte payload) {
  byte m = (id << 6) | payload;
  mySerial.println(m);
}
