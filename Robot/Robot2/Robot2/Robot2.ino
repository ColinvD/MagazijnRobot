const int directionPinUP = 12;
const int pwmPinUP = 3;
const int brakeUP = 9;

const int JoyconX = A2;
const int JoyconY = A3;
int x = 0;
int y = 0;


const int directionLeftRight = 13;
const int pwmPinLeftRight = 11;
const int brakeLeftRight = 8;

const int power = 215;
int highestXvalue = 0;
int highestYvalue = 0;

void setup() {
  // put your setup code here, to run once:
  pinMode(directionLeftRight, OUTPUT);
  pinMode(pwmPinLeftRight, OUTPUT);
  pinMode(brakeLeftRight, OUTPUT);
  pinMode(directionPinUP, OUTPUT);
  pinMode(pwmPinUP, OUTPUT);
  pinMode(brakeUP, OUTPUT);
  pinMode(JoyconX, INPUT);
  pinMode(JoyconY, INPUT);
  Serial.begin(9600);
}

void loop() {
  Moving();


  Serial.print("x = ");
  Serial.print(x);

  Serial.print(", y = ");
  Serial.println(y);


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
    Serial.println("hello");
  } else if (x > 700) {
    Down();
     Serial.println("Nello");

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
void StopUp(){
  analogWrite(pwmPinUP, 0);
  digitalWrite(brakeUP, HIGH);
}
