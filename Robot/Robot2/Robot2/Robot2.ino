#include <Wire.h>
#include <util/atomic.h>
//x variabels
const int directionPinUP = 12;
const int pwmPinUP = 3;
const int brakeUP = 9;
//joystick variabels
const int JoyconY = A2;
const int JoyconX = A3;
int x = 0;
int y = 0;

//y variabls
const int directionLeftRight = 13;
const int pwmPinLeftRight = 11;
const int brakeLeftRight = 8;
//power
const int power = 215;
//indictive sensor variabls
unsigned long LastTime;
int waitTime = 2000;
int currentState = 4;
const int IsLeft = 4;
const int IsRight = 10;
int sensor_value = 0;
int sensor_valueLeft = 0;
bool onRight = false;
bool onLeft = false;

//stop Button
bool stopState = false;

// microswitch
int microswitchDown = 5;
bool onDown = false;
int microswitchUp = 7;
bool onUp = false;
//tilt switch
bool stopUpBool = false;

// encoders
const int yEncoderA = 2;
const int yEncoderB = 6;
int yPosition = 0;
int yPos = 0;
int oldYPos = -1;

int rotationsX = 0;
int rotationsY = 0;

int startStateX = HIGH;
int startStateY = HIGH;
int previousXState = -1;
int previousYState = -1;


//automatic
bool autoBool = true;
bool upSmallBool = false;
bool pickUpFinishBool = false;


void setup() {
  // put your setup code here, to run once:
  pinMode(directionLeftRight, OUTPUT);     // pin 13
  pinMode(pwmPinLeftRight, OUTPUT);        // pin 11
  pinMode(brakeLeftRight, OUTPUT);         // pin 8
  pinMode(directionPinUP, OUTPUT);         // pin 12
  pinMode(pwmPinUP, OUTPUT);               // pin 3
  pinMode(brakeUP, OUTPUT);                // pin 9
  pinMode(IsLeft, INPUT);                  // pin 2
  pinMode(IsRight, INPUT);                 // pin 10
  pinMode(JoyconY, INPUT);                 // pin A2
  pinMode(JoyconX, INPUT);                 // pin A3
  pinMode(microswitchDown, INPUT_PULLUP);  // pin 5
  pinMode(microswitchUp, INPUT_PULLUP);    // pin 7

  pinMode(yEncoderA, INPUT);  // pin 2
  pinMode(yEncoderB, INPUT);  // pin 6

  Wire.begin(1);  // pin A0 & A1
  Wire.onReceive(receiveData);
  Wire.onRequest(requestEvent);

  attachInterrupt(digitalPinToInterrupt(yEncoderA),setEncoderY,RISING);


  Serial.begin(9600);  // pin 0 & 1
}

void loop() {
  yPos = 0;
  ATOMIC_BLOCK(ATOMIC_RESTORESTATE) {
    yPos = yPosition;
  }

  if (stopState) {
    StopUp();
    StopLeft();
  } else if(autoBool) {
    if(upSmallBool) {
      UpSmall();
    }

  } else {
    Moving();
    indictiveSensorReadLeft();
    indictiveSensorRead();

    microSwitch();
    microSwitchUp();
  }

  // Serial.print("x = ");
  // Serial.print(x);

  // Serial.print(", y = ");
  // Serial.println(y);
}


void Moving() {
  y = analogRead(JoyconY);
  x = analogRead(JoyconX);
  if (y < 300 && !onUp && !stopUpBool) {
    Up();
  } else if (y > 700 && !onDown) {
    Down();
  } else {
    StopUp();
  }
  if (x < 300 && !onRight) {
    Right();
  } else if (x > 700 && !onLeft) {
    Left();
  } else {
    StopLeft();
  }
}


void Down() {
  digitalWrite(directionPinUP, HIGH);
  analogWrite(pwmPinUP, power);
  digitalWrite(brakeUP, LOW);
  // CheckRotation("Y", false);
}

void Up() {
    digitalWrite(directionPinUP, LOW);
    analogWrite(pwmPinUP, power + 40);
    digitalWrite(brakeUP, LOW);
    // CheckRotation("Y", true);
}

void Right() {
  digitalWrite(directionLeftRight, HIGH);
  analogWrite(pwmPinLeftRight, power);
  digitalWrite(brakeLeftRight, LOW);
  // CheckRotation("X", false);
}
void Left() {
  digitalWrite(directionLeftRight, LOW);
  analogWrite(pwmPinLeftRight, power);
  digitalWrite(brakeLeftRight, LOW);
  // CheckRotation("X", true);
}
void StopLeft() {
  analogWrite(pwmPinLeftRight, 0);
  digitalWrite(brakeLeftRight, HIGH);
}
void StopUp() {
  analogWrite(pwmPinUP, 0);
  digitalWrite(brakeUP, HIGH);
}

void CheckRotation(String axis, bool direction) {
  // if (axis.equals("X")) {
  // int encoderValueX = digitalRead(encoderX);
  //   if (previousXState == -1 || previousXState != encoderValueX) {
  //     if (previousXState != -1 || encoderValueX == startStateX) {
  //       if (direction) {
  //         rotationsX += 1;
  //       } else {
  //         rotationsX -= 1;
  //       }
  //       Serial.print("Rotations X: ");
  //       Serial.println(rotationsX);
  //     }
  //     previousXState = encoderValueX;
  //   }
  // } else if (axis.equals("Y")) {
  // int encoderValueY = digitalRead(encoderY);
  //   if (previousYState == -1 || previousYState != encoderValueY) {
  //     if (previousYState != -1 || encoderValueY == startStateY) {
  //       if (direction) {
  //         rotationsY += 1;
  //       } else {
  //         rotationsY -= 1;
  //       }
  //       Serial.print("Rotations Y: ");
  //       Serial.println(rotationsY);
  //     }
  //     previousYState = encoderValueY;
  //   }
  // }
}

void indictiveSensorReadLeft() {
  sensor_valueLeft = digitalRead(IsLeft);
  //Serial.println(sensor_valueLeft);
  if (sensor_valueLeft == HIGH) {
    digitalWrite(brakeLeftRight, LOW);
    // Serial.println("aan");
    onLeft = false;
  } else {
    // Serial.println("dichtbij");
    onLeft = true;
  }
}

void indictiveSensorRead() {
  sensor_value = digitalRead(IsRight);
  //Serial.println(sensor_value);
  if (sensor_value == HIGH) {
    digitalWrite(brakeLeftRight, LOW);
    //  Serial.println("DA");
    onRight = false;
  } else {
    //  Serial.println("SDADSAS");
    onRight = true;
  }
}

void microSwitch() {
  int pressed = digitalRead(microswitchDown);
  //Serial.println(pressed);
  if (pressed == HIGH) {
    //Serial.println("nallfajldk");
    onDown = true;
  } else if (pressed == LOW) {
    onDown = false;
    //Serial.println("efijsfrjirgopgi[ph[oieupstipg]0gepto]jepg]prpraop");
  }
}

void microSwitchUp() {
  int pressed = digitalRead(microswitchUp);
  //Serial.println(pressed);
  if (pressed == HIGH) {
    //Serial.println("nallfajldk");
    onUp = true;
  } else if (pressed == LOW) {
    onUp = false;
    //Serial.println("efijsfrjirgopgi[ph[oieupstipg]0gepto]jepg]prpraop");
  }
}

void receiveData() {
  int function = Wire.read();
  Serial.println(function);
  switch (function) {
    case 1:
    // emergency stop
      stopState = Wire.read();
      break;
    case 2:
    // Stop going up
      stopUpBool = Wire.read();
      break;
    case 3:
      upSmallBool = Wire.read();
      break;
    // go a litle bit up to pick up the box
  }
}

void requestEvent() {
  Wire.write(upSmallBool);
}

void UpSmall() {
  if (oldYPos == -1) {
    pickUpFinishBool = false;
    oldYPos = yPos;
  }
    if(yPos < oldYPos + 100) {
    Up();
  } else {
    pickUpFinishBool = true;
    upSmallBool = false;
    oldYPos = -1;
    
    StopUp();
  }
}

void sendValue(int location, int functie, bool boolean) {
    Wire.beginTransmission(location);
    Wire.write(functie);
    Wire.write(boolean);
    Wire.endTransmission();
}

void setEncoderY() {
  int b = digitalRead(yEncoderB);
  if(b > 0){
    yPosition--;
  }
  else{
    yPosition++;
  }

}