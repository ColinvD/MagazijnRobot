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
const int IsLeft = 10;
int sensor_value = 0;

// microswitch
int microswitchDown = 0;

void setup() {
  // put your setup code here, to run once:
  pinMode(directionLeftRight, OUTPUT);
  pinMode(pwmPinLeftRight, OUTPUT);
  pinMode(brakeLeftRight, OUTPUT);
  pinMode(directionPinUP, OUTPUT);
  pinMode(pwmPinUP, OUTPUT);
  pinMode(brakeUP, OUTPUT);
  pinMode(IsLeft, INPUT);
  pinMode(JoyconX, INPUT);
  pinMode(JoyconY, INPUT);
  pinMode(microswitchDown,INPUT);
  Serial.begin(9600);
}

void loop() {
  sensor_value = digitalRead(IsLeft);
  Moving();
  indictiveSensorRead();
  //microSwitch();

  // Serial.print("x = ");
  // Serial.print(x);

  // Serial.print(", y = ");
  // Serial.println(y);


  //  put your main code here, to run repeatedly:
  //   delay(2000);
  //   digitalWrite(directionPinUP, HIGH);
  //   analogWrite(pwmPinUP, power);
  //   digitalWrite(brakeUP, LOW);
  //   digitalWrite(directionLeftRight, HIGH);
  //   analogWrite(pwmPinLeftRight, power);
  //   digitalWrite(brakeLeftRight, LOW);
  //   delay(2000);
  //   analogWrite(pwmPinUP, 0);
  //   digitalWrite(brakeUP, HIGH);
  //   analogWrite(pwmPinLeftRight, 0);
  //   digitalWrite(brakeLeftRight, HIGH);
  //   delay(2000);
  //   digitalWrite(directionPinUP, LOW);
  //   analogWrite(pwmPinUP, power);
  //   digitalWrite(brakeUP, LOW);
  //   digitalWrite(directionLeftRight, LOW);
  //   analogWrite(pwmPinLeftRight, power);
  //   digitalWrite(brakeLeftRight, LOW);
  //   delay(6000);
  //   analogWrite(pwmPinUP, 0);
  //   digitalWrite(brakeUP, HIGH);
  //   analogWrite(pwmPinLeftRight, 0);
  //   digitalWrite(brakeLeftRight, HIGH);
}


void Moving() {
  x = analogRead(JoyconX);
  y = analogRead(JoyconY);
  if (x < 300) {
    Up();
  } else if (x > 700) {
    Down();
  } else {
    StopUp();
  }
  if (y < 300) {
    Right();
  } else if (y > 700) {
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
  analogWrite(pwmPinUP, power);
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


void indictiveSensorRead() {
  if (sensor_value == HIGH) {
    digitalWrite(brakeUP, LOW);
    Serial.println("aan");
  } else {
    // analogWrite(pwmPinUP, 0);
    // digitalWrite(brakeUP, HIGH);
    Serial.println("dichtbij");
  }
}
//   if (millis() > LastTime + waitTime) {
//     LastTime = millis();
//     Something();
//   }
// }

void microSwitch(){
  int pressed = digitalRead(0);
  if(pressed == HIGH){
    Serial.println("nallfajldk");
  }
}

// void Something() {
//   // put your main code here, to run repeatedly:
//   switch (currentState) {
//     case 0:
//       digitalWrite(directionPinUP, HIGH);
//       analogWrite(pwmPinUP, power);
//       digitalWrite(brakeUP, LOW);
//       currentState = 1;
//       break;
//     case 1:
//       analogWrite(pwmPinUP, 0);
//       digitalWrite(brakeUP, HIGH);
//       currentState = 2;
//       break;
//     case 2:
//       digitalWrite(directionPinUP, LOW);
//       analogWrite(pwmPinUP, power);
//       digitalWrite(brakeUP, LOW);
//       currentState = 3;
//       break;
//     case 3:
//       analogWrite(pwmPinUP, 0);
//       digitalWrite(brakeUP, HIGH);
//       currentState = 0;
//       break;
//     default:
//       currentState = 0;
//   }
// }
