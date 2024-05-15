#include <Wire.h>
#include <util/atomic.h>

#define zEncoderA 2
#define zEncoderB 5
const int directionPinUP = 12;
const int pwmPinUP = 3;
const int brakeUP = 9;

const int distance = A2;

const int power = 120;
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
const int stopButton = 4;
bool buttonPressed;
bool stopState = false;
// infra red sensor
bool stoppedOut = true;
bool stoppedIn = true;
float previousDistance = -1;
float diffrenceBetween = 0.15;
float diffrenceAllowed = 0.3;
float diffrenceBigger = 1;

// tilt switch sensor
int tiltSensor = 6;
bool pastTilt = false;

volatile int zPosition = 0;
int prevEncoderValueA = 0;
int prevEncoderValueB = 0;

//led light
int ledGreen = 8;
int ledYellow = 11;
int ledRed = 13;


bool send = false;

void setup() {
  // put your setup code here, to run once:
  pinMode(directionPinUP, OUTPUT);
  pinMode(pwmPinUP, OUTPUT);
  pinMode(brakeUP, OUTPUT);

  pinMode(uit, INPUT);
  pinMode(in, INPUT);
  pinMode(distance, INPUT);
  Serial.begin(9600);
}

void loop() {
  pressedOut = digitalRead(uit);
  pressedIn = digitalRead(in);
  if (pressedOut && isReleasedOut && Distance() < 7.7) {
    isReleasedOut = false;
    GoOut();
    stoppedOut = false;
    // Serial.println("OUT!!!!!!");
  } else if ((!pressedOut && !isReleasedOut) || (Distance() >= 8 && !stoppedOut)) {
    isReleasedOut = true;
    stoppedOut = true;
    Stop();
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

  if (Serial.available()) {
    String message = Serial.readStringUntil('\n');
    OnMessageReceived(message);
  } else if (!send) {
    Serial.println("Done");
    send = true;
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
void Stop() {
  analogWrite(pwmPinUP, 0);
  digitalWrite(brakeUP, HIGH);
}

float Distance() {
  float volts = analogRead(distance) * 0.0048828125;  // value from sensor * (5/1024)
  float distanceRobot = 13 * pow(volts, -1);          // worked out from datasheet graph

  if (previousDistance == -1 || (distanceRobot > previousDistance - diffrenceAllowed && distanceRobot < previousDistance + diffrenceAllowed)) {
    previousDistance = distanceRobot;
  }
  //Serial.println(previousDistance);

  return previousDistance;
}

void OnMessageReceived(String message) {
  Serial.println(message);
  if (message.equals("Uit")) {
    GoOut();
    delay(2500);
  } else if (message.equals("In")) {
    GoIn();
    delay(2500);
  }
  send = false;

}