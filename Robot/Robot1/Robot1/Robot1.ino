#include <Wire.h>
#include <util/atomic.h>
#define zEncoderA 2
#define zEncoderB 5
#define xEncoderA 3
#define xEncoderB A3

String testPackage[] = { "A1", "C3", "E5" };

int xPos = 0;
int xPosition = 0;
int yPos = 0;

const int directionPinUP = 13;
const int pwmPinUP = 11;
const int brakeUP = 8;

const int distance = A2;

const int power = 140;
unsigned long LastTime;
int waitTime = 2000;
int currentState = 4;


//Button var
const int joyconButton = 4;
const int uit = 7;
const int in = 10;
bool pressedOut = false;
bool isReleasedOut = true;
bool pressedIn = false;
bool isReleasedIn = true;
bool joyconPressed = false;

//Stop Button
const int stopButton = 12;
bool buttonPressed;
int stopState = 1;
bool stopButtonConnected = true;

bool zInStartPos = true;
bool zInStartPosOld = true;
bool receivedOrder = false;

long int checkConnectionMillis = 0;
long int checkJavaConnectionMillis = 0;
long int checkStopButtonMillis = 0;

bool checkJavaConnectionBool = true;

//automatic
bool autoBool = true;
bool pickUpBool = false;

bool pickUpAction = false;
bool pickingItem = false;

bool pickUpDataSend = false;
bool extendBool = false;
long int receiveMillis;

long int checkPositionMillis = 0;
long int checkStartPostitionMillis = 0;

bool goToStartPos = true;
bool goToStartPosFinished = false;
bool StartUpStart = true;  //Going to the start position because of startup

int pickUpCount;

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
int pos = 0;


//led light
int ledGreen = A0;
int ledYellow = 9;
int ledRed = A1;


int stopValueOld;
int checkStopValueOld;

void setup() {
  // put your setup code here, to run once:
  pinMode(directionPinUP, OUTPUT);
  pinMode(pwmPinUP, OUTPUT);
  pinMode(brakeUP, OUTPUT);

  pinMode(uit, INPUT);
  pinMode(in, INPUT);
  pinMode(distance, INPUT);
  pinMode(stopButton, INPUT);
  pinMode(tiltSensor, INPUT_PULLUP);

  pinMode(zEncoderA, INPUT);
  pinMode(zEncoderB, INPUT);
  pinMode(xEncoderA, INPUT_PULLUP);
  pinMode(xEncoderB, INPUT_PULLUP);

  pinMode(ledGreen, OUTPUT);
  pinMode(ledYellow, OUTPUT);
  pinMode(ledRed, OUTPUT);

  pinMode(joyconButton, INPUT);
  digitalWrite(joyconButton, HIGH);

  Wire.begin();
  Serial.begin(250000);

  attachInterrupt(digitalPinToInterrupt(zEncoderA), setEncoder, RISING);
  attachInterrupt(digitalPinToInterrupt(xEncoderA), setEncoderX, RISING);
}

void loop() {
  if (Serial.available()) {
    // sendValue(1, 2, false);
    String message = Serial.readStringUntil('\n');
    if (message.equals("STOP")) {
      stopState = 1;
      buttonPressed = true;
      sendSmallIntValue(1, 1, stopState);
    } else if (message.equals("Unlock")) {
      stopState = 0;
      buttonPressed = true;
      sendSmallIntValue(1, 1, stopState);
    } else if (message.equals("GoToStart")) {
      goToStartPos = true;
      goToStartPosFinished = false;
      zInStartPos = false;
      sendValue(1, 0, true);
    } else if (message[0] == 'L') {
      sendString(1, 7, message.substring(1, 3));
      pickingItem = true;
      pickUpCount = message.substring(3, 4).toInt();
    } else if (message.equals("checkJavaConnection")) {
      checkJavaConnectionMillis = millis();
    }
  }
  if (wait(checkJavaConnectionMillis, 500)) {
    checkJavaConnectionBool = false;
    stopState = 2;
    sendSmallIntValue(1, 1, stopState);
    Serial.println("Stop");
  } else if (!checkJavaConnectionBool) {
    checkJavaConnectionBool = true;
    stopState = 1;
    sendSmallIntValue(1, 1, stopState);
    Serial.println("Stop");
  }

  if (!digitalRead(joyconButton) == true) {
    if (joyconPressed == false) {
      joyconPressed = true;
      sendValue(1, 2, true);
      autoBool = !autoBool;
      Stop();
      Serial.print("autoBool: ");
      Serial.println(autoBool);
    }
  } else {
    joyconPressed = false;
  }

  sendSmallIntValue(1, 5, 6);
  Wire.requestFrom(1, 6);
  if (Wire.available()) {
    bool state = Wire.read();
    if (stopState != state) {
      Serial.println("Unlock");
    }
    stopState = state;
  }

  // encoder posistion
  pos = 0;
  xPos = 0;
  ATOMIC_BLOCK(ATOMIC_RESTORESTATE) {
    pos = zPosition;
    xPos = xPosition;
  }
  sendIntValue(1, 4, xPos);

  // get y position
  sendSmallIntValue(1, 5, 5);
  Wire.requestFrom(1, 6);
  if (Wire.available()) {
    int byte1 = Wire.read();
    int byte2 = Wire.read();
    yPos = (int16_t)(byte1 << 8) + byte2;
  }
  Serial.print("X:");
  Serial.print(map(xPos, 1340, 4500, 295, 20));
  Serial.print(" Y:");
  Serial.println(map(yPos, 0, 2375, 293, 10));

  // zPos location state
  if (Distance() > 6.8) {
    zInStartPos = false;
  } else {
    zInStartPos = true;
  }
  sendValue(1, 9, zInStartPos);

  if (zInStartPos != zInStartPosOld) {
    Serial.print("InZAxis ");
    Serial.println(zInStartPos);
  }
  zInStartPosOld = zInStartPos;

  digitalWrite(ledRed, LOW);
  digitalWrite(ledGreen, LOW);

  // read stop button
  int stopValue = digitalRead(stopButton);

  if (stopValue) {
    checkStopButtonMillis = millis();
    buttonPressed = false;
  }
  // stop button state
  if (!stopValue && !stopState && !buttonPressed) {
    stopState = 1;
    buttonPressed = true;
    sendSmallIntValue(1, 1, stopState);
    Serial.println("Stop");
  }
  if (wait(checkStopButtonMillis, 400)) {
    stopState = 2;
    sendSmallIntValue(1, 1, stopState);
    Serial.println("Stop");
    stopButtonConnected = false;
  } else if (!stopButtonConnected) {
    stopButtonConnected = true;
    stopState = 1;
    sendSmallIntValue(1, 1, stopState);
    Serial.println("Stop");
  }

  pressedOut = digitalRead(uit);
  pressedIn = digitalRead(in);

  bool tiltState = shelfTilt();
  if (tiltState) {
    stopState = 1;
    sendSmallIntValue(1, 1, stopState);
    Serial.println("Stop");
  }

  sendSmallIntValue(1, 5, 4);
  Wire.requestFrom(1, 6);
  if (Wire.available()) {
    bool connection = Wire.read();
    checkConnectionMillis = millis();
  }

  if (wait(checkConnectionMillis, 400)) {
    stopState = 1;
    Serial.println("Stop");
  }

  if (stopState) {
    // emergency stop button pressed
    Stop();
    digitalWrite(ledYellow, LOW);
    digitalWrite(ledRed, HIGH);
  } else if (autoBool) {
    digitalWrite(ledYellow, LOW);
    digitalWrite(ledGreen, HIGH);
    if (goToStartPos) {
      goToStartPosition();
    } else if (goToStartPosFinished) {
      // if (pickingItem == false && i < sizeof(testPackage) / sizeof(testPackage[0])) {
      //   sendString(1, 7, testPackage[i]);
      //   pickingItem = true;
      //   i++;
      // }

      if (wait(checkPositionMillis, 200)) {
        sendSmallIntValue(1, 5, 2);
        Wire.requestFrom(1, 6);
        checkPositionMillis = millis();
        if (Wire.available()) {
          pickUpAction = Wire.read();
        }
      }
      if (pickUpAction) {
        pickUP(pickUpCount);
      }
    }

  } else {
    digitalWrite(ledYellow, HIGH);
    if (tiltState && !pastTilt) {
      pastTilt = tiltState;
      if (pressedOut) {
        Stop();
      }
      // sendValue(1, 2, tiltState);
    } else if (!tiltState && pastTilt) {
      pastTilt = tiltState;
      // sendValue(1, 2, tiltState);
    }
    // Serial.println(Distance());
    if (pressedOut && isReleasedOut && Distance() < 18.5 && !tiltState) {
      isReleasedOut = false;
      GoOut();
      stoppedOut = false;
      // Serial.println("OUT!!!!!!");
    } else if ((!pressedOut && !isReleasedOut) || (Distance() >= 18.7 && !stoppedOut)) {
      isReleasedOut = true;
      stoppedOut = true;
      Stop();
    }

    if (pressedIn && isReleasedIn && Distance() > 6.8) {
      isReleasedIn = false;
      GoIn();
      stoppedIn = false;
      //Serial.println("In!!!!!!");
    } else if ((!pressedIn && !isReleasedIn) || (Distance() <= 6.5 && !stoppedIn)) {
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

  if (previousDistance == -1 || (distanceRobot <= previousDistance + diffrenceAllowed && distanceRobot >= previousDistance - diffrenceAllowed) || distanceRobot >= previousDistance + diffrenceBigger || distanceRobot <= previousDistance - diffrenceBigger) {
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

void setEncoder() {
  int zEncoderValueB = digitalRead(zEncoderB);
  if (zEncoderValueB > 0) {
    zPosition--;
  } else {
    zPosition++;
  }
}

void setEncoderX() {
  int b = digitalRead(xEncoderB);
  if (b > 0) {
    xPosition--;
  } else {
    xPosition++;
  }
}

void sendValue(int location, int function, bool boolean) {
  Wire.beginTransmission(location);
  Wire.write(function);
  Wire.write(boolean);
  Wire.endTransmission();
}

void sendIntValue(int location, int function, int value) {
  Wire.beginTransmission(location);
  Wire.write(function);
  // Serial.println(value);
  Wire.write(highByte(value));
  Wire.write(lowByte(value));
  Wire.endTransmission();
}

void sendSmallIntValue(int location, int function, int value) {
  Wire.beginTransmission(location);
  Wire.write(function);
  Wire.write(value);
  Wire.endTransmission();
}

void sendString(int location, int function, String value) {
  Wire.beginTransmission(location);
  Wire.write(function);
  Wire.write(value[0]);
  Wire.write(value[1]);
  Wire.endTransmission();
}
// void receiveEvent() {
//   int function = Wire.read();
//   Serial.println(function);
//   switch (function) {
//     case 1:
//       pickUpBool = Wire.read();
//       break;
//   }
// }

void pickUP(int count) {
  int value = 0;
  switch (count) {
    case 1:
      value = 880;
      break;
    case 2:
      value = 610;
      break;
    case 3:
      value = 350;
      break;
  }
  if (!extendBool && pos < value) {
    pickUpBool = true;
    pickUpDataSend = true;
    GoOut();
  } else {
    extendBool = true;
    if (pickUpBool) {
      // Serial.println(pickUpDataSend);
      if (pickUpDataSend) {
        sendValue(1, 3, true);
        pickUpDataSend = false;
      }
      if (wait(receiveMillis, 200)) {
        sendSmallIntValue(1, 5, 1);
        Wire.requestFrom(1, 6);
        receiveMillis = millis();
        if (Wire.available()) {
          pickUpBool = Wire.read();
        }
      }
      Stop();
    } else {
      if (pos > 20) {
        GoIn();
      } else {
        Serial.println("Out");
        Stop();
        sendValue(1, 6, true);
        pickUpAction = false;
        extendBool = false;
        pickingItem = false;
      }
    }
  }
}

bool wait(long int mil, int wait) {
  if (millis() - mil > wait) {
    return true;
  }
  return false;
}

void goToStartPosition() {
  bool robot2Ready = false;
  if (!zInStartPos) {
    GoIn();
  } else {
    zInStartPos = true;
    Stop();
  }

  if (zInStartPos) {
    if (wait(checkStartPostitionMillis, 200)) {
      checkStartPostitionMillis = millis();
      sendSmallIntValue(1, 5, 3);
      Wire.requestFrom(1, 6);
      if (Wire.available()) {
        robot2Ready = Wire.read();
      }
    }
  }

  if (robot2Ready && zInStartPos) {
    goToStartPosFinished = true;
    goToStartPos = false;
    sendValue(1, 8, true);
    zPosition = 0;
    xPosition = 0;
    if (StartUpStart) {
      StartUpStart = false;
    } else {
      Serial.println("Done");
    }
  }
}