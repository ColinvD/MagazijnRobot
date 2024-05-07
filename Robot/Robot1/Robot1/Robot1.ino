#include <Wire.h>;
const int directionPinUP = 12;
const int pwmPinUP = 3;
const int brakeUP = 9;

const int distance = A2;

const int zEncoderA = 4;
const int zEncoderB = 5;

const int power = 135;
unsigned long LastTime;
int waitTime = 2000;
int currentState = 4;


//Button var
const int uit = 7;
const int in = 10;
bool pressedOut = false;
bool isReleasedOut = true;
bool pressedIn = false;
bool isReleasedIn = true;

//Stop Button
const int stopButton = 2;
bool buttonPressed;
bool stopState = false;
// infra red sensor
bool stoppedOut = true;
bool stoppedIn = true;
float previousDistance = -1;
float diffrenceBetween = 0.10;
float diffrenceAllowed = 0.4;
float diffrenceBigger = 0.7;

// tilt switch sensor
int tiltSensor = 6;
bool pastTilt = false;

long int zPosition = 0;
int prevEncoderValueA = 0;


void setup() {
  // put your setup code here, to run once:
  pinMode(directionPinUP, OUTPUT);
  pinMode(pwmPinUP, OUTPUT);
  pinMode(brakeUP, OUTPUT);
  //pinMode(encoderZ, INPUT);
  pinMode(uit, INPUT);
  pinMode(in, INPUT);
  pinMode(distance,INPUT);
  pinMode(stopButton,INPUT);
  pinMode(tiltSensor,INPUT_PULLUP);

  pinMode(zEncoderA,INPUT);
  pinMode(zEncoderB,INPUT);

  Wire.begin();
  Serial.begin(9600);
}

void loop() {
  int stopValue = digitalRead(stopButton);
  if (!stopValue) {
    buttonPressed = false; 
  }

  // stop button state
  if (stopValue && stopState && !buttonPressed) {
    stopState = false;
    buttonPressed = true;
    sendValue(1, 1, stopState);
  } else if (stopValue && !stopState && !buttonPressed) {
    stopState = true;
    buttonPressed = true;
    sendValue(1, 1, stopState);
  }

  if(stopState) {
    // emergency stop button pressed
    Stop();
  } else {
    setEncoder(digitalRead(directionPinUP));
    // Serial.println(zPosition);
    bool tiltState = shelfTilt();
    pressedOut = digitalRead(uit);
    pressedIn = digitalRead(in);
    if(tiltState && !pastTilt) {
      pastTilt = tiltState;
      if(pressedOut) {
        Stop();
      }
      sendValue(1, 2, tiltState);
    } else if(!tiltState && pastTilt) {
      pastTilt = tiltState;
      sendValue(1, 2, tiltState);
    }
    // Serial.println(Distance());
    if (pressedOut && isReleasedOut && Distance() < 18.4 && !tiltState) {
      isReleasedOut = false;
      GoOut();
      stoppedOut = false;
    // Serial.println("OUT!!!!!!");
    } else if ((!pressedOut && !isReleasedOut) || (Distance() >= 18.7  && !stoppedOut)) {
      isReleasedOut = true;
      stoppedOut = true;
      Stop();
    }

    if (pressedIn && isReleasedIn && Distance() > 7.2) {
      isReleasedIn = false;
      GoIn();
      stoppedIn = false;
      //Serial.println("In!!!!!!");
    } else if ((!pressedIn && !isReleasedIn) || (Distance() <= 7 && !stoppedIn)) {
      isReleasedIn = true;
      stoppedIn = true;
      Stop();
    }

    // int sensor_value = digitalRead(IsLeft);
    //int encoder_variable = digitalRead(encoderZ);
    //Serial.println(encoder_variable);
    // if(sensor_value == HIGH){
    //   digitalWrite(directionPinUP, LOW);
    //   analogWrite(pwmPinUP, power);
    //   digitalWrite(brakeUP, LOW);
    //   Serial.println("aan");
    // }
    // else {
    //   analogWrite(pwmPinUP, 0);
    //   digitalWrite(brakeUP, HIGH);
    //   Serial.println("dichtbij");
    // }

    // if (millis() > LastTime + waitTime) {
    //   LastTime = millis();
    //   Something();
    // }
    // if(encoder_variable == HIGH){
    //   Serial.println("Rotation");
    // }
    //Serial.println(encoder_variable);
  }
}

void GoIn() {
  digitalWrite(directionPinUP, HIGH);
  analogWrite(pwmPinUP, power);
  digitalWrite(brakeUP, LOW);
}

void GoOut() {
  digitalWrite(directionPinUP, LOW);
  analogWrite(pwmPinUP, power);
  digitalWrite(brakeUP, LOW);
}
void Stop(){
      analogWrite(pwmPinUP, 0);
      digitalWrite(brakeUP, HIGH);
}

float Distance(){
  float volts = analogRead(distance)*0.0048828125;  // value from sensor * (5/1024)
  float distanceRobot = 13*pow(volts, -1); // worked out from datasheet graph
  
  if(previousDistance == -1 || (distanceRobot <= previousDistance + diffrenceAllowed && distanceRobot >= previousDistance - diffrenceAllowed) || distanceRobot >= previousDistance + diffrenceBigger || distanceRobot <= previousDistance - diffrenceBigger){
    previousDistance = distanceRobot;
  }
  // Serial.println(previousDistance);

  return previousDistance;

}

bool shelfTilt() {
  int tiltValue = digitalRead(tiltSensor);
  if (tiltValue) {
    return false;
  }
  return true;
}

void setEncoder(int direction) {
  int encoderValueA = digitalRead(zEncoderA);
  int encoderValueB = digitalRead(zEncoderB);
    Serial.print(encoderValueA);
    Serial.print(" ");
    Serial.println(encoderValueB);

  if (direction && encoderValueA != prevEncoderValueA) {
    zPosition--;
  } else if(!direction && encoderValueA != prevEncoderValueA) {
    zPosition++;
  }
  prevEncoderValueA = encoderValueA;
}

void sendValue(int location, int functie, bool boolean) {
    Wire.beginTransmission(location);
    Wire.write(functie);
    Wire.write(boolean);
    Wire.endTransmission();
}