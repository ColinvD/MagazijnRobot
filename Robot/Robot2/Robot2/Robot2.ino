#include <Wire.h>
#include <util/atomic.h>
//y variabels
const int directionPinUP = 12;
const int pwmPinUP = 3;
const int brakeUP = 9;
//joystick variabels
const int JoyconY = A2;
const int JoyconX = A3;
int x = 0;
int y = 0;

bool joyconPressed = false;

//x variabls
const int directionLeftRight = 13;
const int pwmPinLeftRight = 11;
const int brakeLeftRight = 8;
//power
int power = 215;
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
int stopState = 1;
//on Button
const int onButton = A0;
bool onButtonPressed;

// microswitch
int microswitchDown = 5;
bool onDown = false;
int microswitchUp = 7;
bool onUp = false;
//tilt switch
bool stopUpBool = false;

// encoders
#define yEncoderA 2
#define yEncoderB 6
int xPos = 0;
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
bool gettingItem = false;

long int checkConnectionMillis = 0;
bool checkConnectionBool = true;

bool PickUpStep[2] = { false, false };

String stockLocation = "";

bool goToPos = false;
bool finishedPickUP = true;

int startY = 2150;
int addOnY = -515;

int startX = 4420;
int addOnX = -690;

int requestCase = 0;

// position
bool goToStartPos = true;
bool goToStartPosRobot2Finished = false;
bool goToStartPosFinished = false;
bool zInStartPos = false;



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

  attachInterrupt(digitalPinToInterrupt(yEncoderA), setEncoderY, RISING);

  Serial.begin(9600);  // pin 0 & 1
}

void loop() {
  // Serial.println(xPos);

  // Serial.print("xPos: ");
  // Serial.println(xPos);
  yPos = 0;
  ATOMIC_BLOCK(ATOMIC_RESTORESTATE) {
    yPos = yPosition;
  }
  // Serial.println(yPos);

  // on button
  int onValue = digitalRead(onButton);

  if(!onValue) {
    onButtonPressed = false;
  }

  // stop button state
  if (onValue && stopState == 1 && !onButtonPressed) {
    stopState = 0;
    onButtonPressed = true;
  }

  indictiveSensorReadLeft();
  indictiveSensorRead();

  microSwitch();
  microSwitchUp();

  if (wait(checkConnectionMillis, 300)) {
    checkConnectionMillis = millis();
    checkConnectionBool = false;
    stopState = 2;
  } else if(!checkConnectionBool) {
    checkConnectionBool = true;
    stopState = 1;
  }

  if (stopState) {
    StopUp();
    StopLeft();
  } else if (autoBool) {
    if (goToStartPos) {
      goToStartPosition();
    } else if (goToStartPosFinished) {
      if (goToPos) {
        goTo(stockLocation);
      }
      if (PickUpStep[1] && upSmallBool) {
        UpSmall();
      }
    }
  } else {
    Moving();
  }
  if(joyconPressed) {
    StopLeft();
    StopUp();
    joyconPressed = false;
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
    Up(power);
  } else if (y > 700 && !onDown) {
    Down(power);
  } else {
    StopUp();
  }
  if (x < 300 && !onRight) {
    Right(power);
  } else if (x > 700 && !onLeft) {
    Left(power);
  } else {
    StopLeft();
  }
}


void Down(int powerValue) {

  if (zInStartPos) {
    digitalWrite(directionPinUP, HIGH);
    analogWrite(pwmPinUP, powerValue);
    digitalWrite(brakeUP, LOW);
    // CheckRotation("Y", false);

  } else {
    StopUp();
  }
}

void Up(int powerValue) {
  if (zInStartPos || yPos < oldYPos + 100) {
    digitalWrite(directionPinUP, LOW);
    analogWrite(pwmPinUP, powerValue + 40);
    digitalWrite(brakeUP, LOW);
    // CheckRotation("Y", true);
  } else {
    StopUp();
  }
}

void Right(int powerValue) {
  if (zInStartPos) {
    digitalWrite(directionLeftRight, HIGH);
    analogWrite(pwmPinLeftRight, powerValue);
    digitalWrite(brakeLeftRight, LOW);
  } else {
    StopLeft();
  }
  // CheckRotation("X", false);
}
void Left(int powerValue) {
  if (zInStartPos) {
    digitalWrite(directionLeftRight, LOW);
    analogWrite(pwmPinLeftRight, powerValue);
    digitalWrite(brakeLeftRight, LOW);
  } else {
    StopLeft();
  }
  // CheckRotation("X", true);
}
void StopLeft() {
  analogWrite(pwmPinLeftRight, 0);
  digitalWrite(brakeLeftRight, HIGH);
}
void StopUp() {
  digitalWrite(brakeUP, HIGH);
  analogWrite(pwmPinUP, 0);
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

bool wait(long int mil, int wait) {
  if (millis() - mil > wait) {
    return true;
  }
  return false;
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
  checkConnectionMillis = millis();
  if (function == 1) {
    // emergency stop
    stopState = Wire.read();
    // Serial.print("stopState: ");
    // Serial.println(stopState);
  } else if (function == 2) {
    Wire.read();
    autoBool = !autoBool;
    joyconPressed = true;
    Serial.print("autoBool: ");
    Serial.println(autoBool);
  } else if (function == 3) {
    // go a litle bit up to pick up the box
    upSmallBool = Wire.read();
  } else if (function == 4) {
    // get xPos of robot
    int byte1 = Wire.read();
    int byte2 = Wire.read();
    xPos = (int16_t)(byte1 << 8) + byte2;
    // Serial.println(value);
  } else if (function == 5) {
    requestCase = Wire.read();
  } else if (function == 6) {
    Serial.println("reached");
    // z axis is back in start position
    finishedPickUP = Wire.read();
  } else if (function == 7) {
    goToPos = true;
    char receivedString[1];
    receivedString[0] = Wire.read();
    receivedString[1] = Wire.read();
    stockLocation = String(receivedString[0]) + String(receivedString[1]);
    Serial.println(stockLocation);  
  } else if (function == 8) {
    // Check if is in Start
    goToStartPosFinished = Wire.read();
  } else if (function == 9) {
    zInStartPos = Wire.read();
    if (zInStartPos) {
      oldYPos = yPos;
    }
  } else if (function == 0) {
    Wire.read();
    goToStartPos = true;
    goToStartPosRobot2Finished = false;
    goToStartPosFinished = false;
    zInStartPos = false;
  }
}

void requestEvent() {
  // Serial.println(requestCase);
  switch (requestCase) {
    case 1:
      // check if the product is on the robot
      Wire.write(upSmallBool);
      break;
    case 2:
      // check if the robot is in the right location
      if (!finishedPickUP) {
        Wire.write(PickUpStep[1]);
      } else {
        Wire.write(false);
      }
      break;
    case 3:
      Wire.write(goToStartPosRobot2Finished);
      break;
    case 4:
      Wire.write(true);
      break;
    case 5:
      // send y pos to master
      Wire.write(highByte(yPos));
      Wire.write(lowByte(yPos));
      break;
    case 6:
      // on button
      Wire.write(stopState);
      break;
    }
}

void UpSmall() {
  PickUpStep[0] = false;
  if (yPos < oldYPos + 100) {
    Up(power);
  } else {
    PickUpStep[0] = true;
    upSmallBool = false;
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
  if (b > 0) {
    yPosition--;
  } else {
    yPosition++;
  }
}

void goTo(String location) {
  char xChar = location[1];
  char yChar = toupper(location[0]);

  // Serial.print(xChar);
  // Serial.print(" + ");
  // Serial.println(yChar);

  int x = (xChar - 49) * addOnX + startX;
  int y = (yChar - 65) * addOnY + startY;


  // Serial.print(x);
  // Serial.print(" - ");
  // Serial.println(xPos);

  bool xPosBool = goToPosX(x);
  bool yPosBool = goToPosY(y);

  if (xPosBool && yPosBool) {
    goToPos = false;
    PickUpStep[1] = true;
    finishedPickUP = false;
  }
}

bool goToPosX(int x) {
  if (xPos > x + 100) {
    Right(power);
  } else if (xPos < x - 100) {
    Left(power);
  } else if (xPos > x + 5) {
    Right(150);
  } else if (xPos < x - 5) {
    Left(150);
  } else {
    StopLeft();
    return true;
  }
  return false;
}

bool goToPosY(int y) {
  if (yPos > y + 100) {
    Down(power);
  } else if (yPos < y - 100) {
    Up(power);
  } else if (yPos > y + 5) {
    Down(150);
  } else if (yPos < y - 5) {
    Up(150);
  } else {
    StopUp();
    return true;
  }
  return false;
}

bool goToStartPosition() {
  goToStartPosRobot2Finished = false;
  if (!onRight) {
    Right(power);
  } else {
    StopLeft();
  }
  if (!onDown) {
    Down(power);
  } else {
    StopUp();
  }

  if (onRight && onDown) {
    goToStartPos = false;
    goToStartPosRobot2Finished = true;
    yPosition = 0;
  }
}