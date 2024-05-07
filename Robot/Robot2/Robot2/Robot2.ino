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
// int microswitchDown = A4;
bool onDown = false;
// int microswitchUp = A5;
bool onUp = false;
//tilt switch
bool stopUpBool = false;

void setup() {
  // put your setup code here, to run once:
  pinMode(directionLeftRight, OUTPUT);
  pinMode(pwmPinLeftRight, OUTPUT);
  pinMode(brakeLeftRight, OUTPUT);
  pinMode(directionPinUP, OUTPUT);
  pinMode(pwmPinUP, OUTPUT);
  pinMode(brakeUP, OUTPUT);
  pinMode(IsLeft, INPUT);
  pinMode(IsRight, INPUT);
  pinMode(JoyconX, INPUT);
  pinMode(JoyconY, INPUT);
  // pinMode(microswitchDown,INPUT_PULLUP);
  // pinMode(microswitchUp,INPUT_PULLUP);

  Wire.begin(1);
  Wire.onReceive(receiveData);

  Serial.begin(9600);
}

void loop() {
  if(stopState) {
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
  } else if (x > 700 && !onDown ) {
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
}

void Up() {
  digitalWrite(directionPinUP, LOW);
  analogWrite(pwmPinUP, power + 40);
  digitalWrite(brakeUP, LOW);
}
void Right() {
  digitalWrite(directionLeftRight, HIGH);
  analogWrite(pwmPinLeftRight, power);
  digitalWrite(brakeLeftRight, LOW);
}
void Left() {
  digitalWrite(directionLeftRight, LOW);
  analogWrite(pwmPinLeftRight, power);
  digitalWrite(brakeLeftRight, LOW);
}
void StopLeft() {
  analogWrite(pwmPinLeftRight, 0);
  digitalWrite(brakeLeftRight, HIGH);
}
void StopUp() {
  analogWrite(pwmPinUP, 0);
  digitalWrite(brakeUP, HIGH);
}


void indictiveSensorReadLeft() {
  sensor_valueLeft = digitalRead(IsLeft);
  //Serial.println(sensor_valueLeft);
  if (sensor_valueLeft == HIGH ) {
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

void microSwitch(){
  // int pressed = digitalRead(microswitchDown);
  // Serial.println(pressed);
  // if(pressed == HIGH){
  //   //Serial.println("nallfajldk");
  //   onDown = true;
  // } else if (pressed == LOW){
  //   onDown = false;
  //   //Serial.println("efijsfrjirgopgi[ph[oieupstipg]0gepto]jepg]prpraop");
  // }
}

void microSwitchUp(){
  // int pressed = digitalRead(microswitchUp);
  // Serial.println(pressed);
  // if(pressed == HIGH){
  //   //Serial.println("nallfajldk");
  //   onUp = true;
  // } else if (pressed == LOW){
  //   onUp = false;
  //   //Serial.println("efijsfrjirgopgi[ph[oieupstipg]0gepto]jepg]prpraop");
  // }
}

void receiveData() {
  int function = Wire.read();
  switch(function) {
    case 1: 
      stopState = Wire.read();
      break;
    case 2: 
      stopUpBool = Wire.read();
      break;
  }
}
