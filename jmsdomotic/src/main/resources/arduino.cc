/*
  Simple example for receiving
  
  https://github.com/sui77/rc-switch/
*/

#include <RCSwitch.h>

RCSwitch mySwitch = RCSwitch();

void setup() {
  Serial.begin(9600);
  mySwitch.enableReceive(0);  // Receiver on interrupt 0 => that is pin #2
}

void loop() {
  if (mySwitch.available()) {
    
    int value = mySwitch.getReceivedValue();
    
    if (value == 0) {
      Serial.print("Unknown encoding");
    } else { 
      Serial.print("{\"received\":");
      Serial.print( mySwitch.getReceivedValue() );
      Serial.print(", \"bits\":");
      Serial.print( mySwitch.getReceivedBitlength() );
      Serial.print(", \"protocol\":");
      Serial.print( mySwitch.getReceivedProtocol() );
      Serial.println("}");
    }

    mySwitch.resetAvailable();
  }
}