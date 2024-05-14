#include <Wire.h>;
//x variabels
const int directionPinUP = 12;
const int pwmPinUP = 3;
const int brakeUP = 9;
//joystick variabels
const int JoyconX = A2;
const int JoyconY = A3;
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
const int IsLeft = 2;
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
const int encoderX = 6;
const int encoderY = 4;

int rotationsX = 0;
int rotationsY = 0;

int startStateX = HIGH;
int startStateY = HIGH;
int previousXState = -1;
int previousYState = -1;

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
  pinMode(JoyconX, INPUT);                 // pin A2
  pinMode(JoyconY, INPUT);                 // pin A3
  pinMode(microswitchDown, INPUT_PULLUP);  // pin 5
  pinMode(microswitchUp, INPUT_PULLUP);    // pin 7

  pinMode(encoderX, INPUT);  // pin 6
  pinMode(encoderY, INPUT);  // pin 4

  Wire.begin(1);  // pin A0 & A1
  Wire.onReceive(receiveData);

  Serial.begin(9600);  // pin 0 & 1
}

void loop() {
  if (stopState) {
    StopUp();
    StopLeft();
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
  x = analogRead(JoyconX);
  y = analogRead(JoyconY);
  if (x < 300 && !onUp && !stopUpBool) {
    Up();
  } else if (x > 700 && !onDown) {
    Down();
  } else {
    StopUp();
  }
  if (y < 300 && !onRight) {
    Right();
  } else if (y > 700 && !onLeft) {
    Left();
  } else {
    StopLeft();
  }
}


void Down() {
  digitalWrite(directionPinUP, HIGH);
  analogWrite(pwmPinUP, power);
  digitalWrite(brakeUP, LOW);
  CheckRotation("Y", false);
}

void Up() {
  digitalWrite(directionPinUP, LOW);
  analogWrite(pwmPinUP, power + 40);
  digitalWrite(brakeUP, LOW);
  CheckRotation("Y", true);
}

void Right() {
  digitalWrite(directionLeftRight, HIGH);
  analogWrite(pwmPinLeftRight, power);
  digitalWrite(brakeLeftRight, LOW);
  CheckRotation("X", false);
}
void Left() {
  digitalWrite(directionLeftRight, LOW);
  analogWrite(pwmPinLeftRight, power);
  digitalWrite(brakeLeftRight, LOW);
  CheckRotation("X", true);
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
  if (axis.equals("X")) {
  int encoderValueX = digitalRead(encoderX);
    if (previousXState == -1 || previousXState != encoderValueX) {
      if (previousXState != -1 || encoderValueX == startStateX) {
        if (direction) {
          rotationsX += 1;
        } else {
          rotationsX -= 1;
        }
        Serial.print("Rotations X: ");
        Serial.println(rotationsX);
      }
      previousXState = encoderValueX;
    }
  } else if (axis.equals("Y")) {
  int encoderValueY = digitalRead(encoderY);
    if (previousYState == -1 || previousYState != encoderValueY) {
      if (previousYState != -1 || encoderValueY == startStateY) {
        if (direction) {
          rotationsY += 1;
        } else {
          rotationsY -= 1;
        }
        Serial.print("Rotations Y: ");
        Serial.println(rotationsY);
      }
      previousYState = encoderValueY;
    }
  }
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
  switch (function) {
    case 1:
      stopState = Wire.read();
      break;
    case 2:
      stopUpBool = Wire.read();
      break;
  }
}
