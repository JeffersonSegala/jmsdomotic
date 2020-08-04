 /*
  Simple example for receiving 
  https://github.com/sui77/rc-switch/
  ATENCAO Fazer download da biblioteca RCSwitch ATENCAO
*/

#include <RCSwitch.h>

RCSwitch mySwitch = RCSwitch();

int ledPin =  13; //atribui o pino 13 a variável ledPin 
int buzzerPin =  12; 
char data;
bool isSoundActive = false;

void setup() {
  Serial.begin(9600);
  mySwitch.enableReceive(0);  // Receiver on interrupt 0 => that is pin #2
  pinMode(ledPin, OUTPUT);
  pinMode(buzzerPin, OUTPUT);
}

void loop() {
  if (mySwitch.available()) {
    
    int value = mySwitch.getReceivedValue();
    
    if (value == 0) {
      Serial.print("Unknown encoding");
    } else { 
      Serial.print("{\"data\":");
      Serial.print( mySwitch.getReceivedValue() );
      Serial.print(", \"bits\":");
      Serial.print( mySwitch.getReceivedBitlength() );
      Serial.print(", \"protocol\":");
      Serial.print( mySwitch.getReceivedProtocol() );
      Serial.println("}");
    }

    mySwitch.resetAvailable();
  }

  
  if (Serial.available() > 0) { //verifica se existe comunicação com a porta serial
      data = Serial.read();//lê os dados da porta serial
      if (data == '1') {
        isSoundActive = true;
        for (int i = 0; i < 5; i++) {
          digitalWrite(buzzerPin, HIGH);
          delay(500);
          digitalWrite(buzzerPin, LOW);
          delay(1000);
        }
      } else if (data == '2') {
        isSoundActive = false;
        digitalWrite(buzzerPin, LOW);
      }
  }
  
   if ( isSoundActive ) {
      digitalWrite(buzzerPin, HIGH);
   }

}