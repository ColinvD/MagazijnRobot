const int directionPinUP = 12;
const int pwmPinUP = 3;
const int brakeUP = 9;

const int power = 200;

void setup() {
  // put your setup code here, to run once:
  pinMode(directionPinUP, OUTPUT);
  pinMode(pwmPinUP, OUTPUT);
  pinMode(brakeUP, OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:
  delay(2000);
  digitalWrite(directionPinUP, HIGH);
  analogWrite(pwmPinUP, power);
  digitalWrite(brakeUP, LOW);
  delay(2000);
  analogWrite(pwmPinUP, 0);
  digitalWrite(brakeUP, HIGH);
  delay(2000);
  digitalWrite(directionPinUP, LOW);
  analogWrite(pwmPinUP, power);
  digitalWrite(brakeUP, LOW);
  delay(6000);
  analogWrite(pwmPinUP, 0);
  digitalWrite(brakeUP, HIGH);
}
