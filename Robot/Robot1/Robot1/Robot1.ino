const int directionPinUP = 12;
const int pwmPinUP = 3;
const int brakeUP = 9;

const int distance = A2;
//const int encoderZ = 5;

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
// infra red sensor
bool stoppedOut = true;
bool stoppedIn = true;
float previousDistance = -1;
float diffrenceAllowed = 0.3;

void setup() {
  // put your setup code here, to run once:
  pinMode(directionPinUP, OUTPUT);
  pinMode(pwmPinUP, OUTPUT);
  pinMode(brakeUP, OUTPUT);
  //pinMode(encoderZ, INPUT);
  pinMode(uit, INPUT);
  pinMode(in, INPUT);
  pinMode(distance,INPUT);
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
  } else if ((!pressedOut && !isReleasedOut) || (Distance() >= 8  && !stoppedOut)) {
    isReleasedOut = true;
    stoppedOut = true;
    Stop();
  }

  if (pressedIn && isReleasedIn && Distance() > 4.30) {
    isReleasedIn = false;
    GoIn();
    stoppedIn = false;
    //Serial.println("In!!!!!!");
  } else if ((!pressedIn && !isReleasedIn) || (Distance() <= 4.15 && !stoppedIn)) {
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
  
  if(previousDistance == -1 || (distanceRobot > previousDistance - diffrenceAllowed && distanceRobot < previousDistance + diffrenceAllowed)){
    previousDistance = distanceRobot;
  }
  Serial.println(previousDistance);

  return previousDistance;

}