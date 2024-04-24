const int directionPinUP = 12;
const int pwmPinUP = 3;
const int brakeUP = 9;

// const int IsLeft = 10;
// const int IsLeft
const int encoderZ = 5;

const int power = 135;
unsigned long LastTime;
int waitTime = 2000;
int currentState = 4;
void setup() {
  // put your setup code here, to run once:
  pinMode(directionPinUP, OUTPUT);
  pinMode(pwmPinUP, OUTPUT);
  pinMode(brakeUP, OUTPUT);
  //pinMode(IsLeft,INPUT);
  pinMode(encoderZ, INPUT);
  Serial.begin(9600);
}

void loop() {
  // int sensor_value = digitalRead(IsLeft);
  int encoder_variable = digitalRead(encoderZ);
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

  if (millis() > LastTime + waitTime) {
    LastTime = millis();
    Something();
  }
  if(encoder_variable == HIGH){
    Serial.println("Rotation");
  }
  //Serial.println(encoder_variable);
}

void Something() {
  // put your main code here, to run repeatedly:
  switch (currentState) {
    case 0:
      digitalWrite(directionPinUP, HIGH);
      analogWrite(pwmPinUP, power);
      digitalWrite(brakeUP, LOW);
      currentState = 1;
      break;
    case 1:
      analogWrite(pwmPinUP, 0);
      digitalWrite(brakeUP, HIGH);
      currentState = 2;
      break;
    case 2:
      digitalWrite(directionPinUP, LOW);
      analogWrite(pwmPinUP, power);
      digitalWrite(brakeUP, LOW);
      currentState = 3;
      break;
    case 3:
      analogWrite(pwmPinUP, 0);
      digitalWrite(brakeUP, HIGH);
      currentState = 0;
      break;
    default:
      currentState = 0;
  }
}
