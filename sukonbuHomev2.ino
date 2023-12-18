/*
  블루투스
    tx1,rx1 사용

  릴레이
    52 : 거실
    53 : 화장실

  SR-04 초음파 센서
    파 초 방식으로 했음
    짝수 : ECHO
    홀수 : TRIG

    24, 25 : 침대 옆
    26, 27 : 현관문
    28, 29 : 화장실
    30, 31 : 실내
    32, 33 : 잠깐 나갈때 차단용
*/

#define RELAY_LIVINGROOM  52
#define RELAY_TOILET      53

#define WAVE_BED_ECHO     24
#define WAVE_BED_TRIG     25

#define WAVE_DOOR_ECHO    26
#define WAVE_DOOR_TRIG    27

#define WAVE_TOILET_ECHO  28
#define WAVE_TOILET_TRIG  29

#define WAVE_LIVING_ECHO  30
#define WAVE_LIVING_TRIG 31

#define WAVE_CUT_ECHO     32
#define WAVE_CUT_TRIG     33

boolean doorCheck = false;
boolean livingCheck = false;
boolean cutCheck = false;


int bedCheckCount = 0;
int doorCheckCount = 0;
int toiletCheckCount = 0;
int livingCheckCount = 0;



long waveDistance(int echoPin, int trigPin){

  long duration, distance;
  digitalWrite(trigPin, HIGH);  // trigPin에서 초음파 발생(echoPin도 HIGH)        
  //delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  duration = pulseIn(echoPin, HIGH);    // echoPin 이 HIGH를 유지한 시간을 저장 한다.
  distance = ((float)(340 * duration) / 1000) / 2; 

  return distance;
}




void setup() {
  Serial.begin(9600);
  Serial1.begin(9600); // 블루투스

  // 릴레이 출력 설정
  pinMode(RELAY_LIVINGROOM,OUTPUT);
  pinMode(RELAY_TOILET,OUTPUT);

  // 초음파 입출력 설정
  pinMode(WAVE_BED_ECHO, INPUT);
  pinMode(WAVE_BED_TRIG, OUTPUT); 

  pinMode(WAVE_DOOR_ECHO, INPUT);
  pinMode(WAVE_DOOR_TRIG, OUTPUT); 

  pinMode(WAVE_TOILET_ECHO, INPUT);
  pinMode(WAVE_TOILET_TRIG, OUTPUT); 

  pinMode(WAVE_LIVING_ECHO, INPUT);
  pinMode(WAVE_LIVING_TRIG, OUTPUT); 

  pinMode(WAVE_CUT_ECHO, INPUT);
  pinMode(WAVE_CUT_TRIG, OUTPUT); 

  delay(5000);    
  Serial.println("5초 카운트가 끝났습니다.");  

}

void loop() {

  // 거리 측정 //

  long toiletDistance = waveDistance(WAVE_TOILET_ECHO, WAVE_TOILET_TRIG);

  long bedDistance = waveDistance(WAVE_BED_ECHO, WAVE_BED_TRIG);

  long doorDistance = waveDistance(WAVE_DOOR_ECHO, WAVE_DOOR_TRIG);

  long livingDistance = waveDistance(WAVE_LIVING_ECHO, WAVE_LIVING_TRIG);

  long cutDistance = waveDistance(WAVE_CUT_ECHO, WAVE_CUT_TRIG);
  
  // 거리 감지 //

  if(cutCheck == false){
    // 침대
    if(bedDistance > 0 && bedDistance <= 100){
      Serial.println("침대가 감지되었습니다.");  
        if(digitalRead(RELAY_LIVINGROOM) == LOW && bedCheckCount == 0){
          digitalWrite(RELAY_LIVINGROOM,HIGH);
          Serial1.write('W');
          bedCheckCount++;
          delay(1000);
        }
        else if(digitalRead(RELAY_LIVINGROOM) == HIGH && bedCheckCount == 0){
          digitalWrite(RELAY_LIVINGROOM,LOW);
          Serial1.write('S');
          bedCheckCount++;
          delay(1000);
        }    
    }

    // 현관
    if(doorDistance <= 300){
      Serial.println("현관이 감지되었습니다.");  
      if(livingCheck == true){
        livingCheck = false;
        Serial.println("나가는 상황으로 감지되었습니다."); 
        digitalWrite(RELAY_LIVINGROOM,LOW);
        Serial1.write('O');
        delay(1000);
      }
      else{
        doorCheck = true;
        doorCheckCount++;
      }
    }

    // 화장실
    if(toiletDistance <= 420){
      Serial.println("화장실이 감지되었습니다.");  
      if(digitalRead(RELAY_TOILET) == false && toiletCheckCount == 0){
        digitalWrite(RELAY_TOILET,HIGH);
        Serial1.write('T');
        toiletCheckCount++;
        delay(1000);
      }
      else if(digitalRead(RELAY_TOILET) == true && toiletCheckCount == 0){
        digitalWrite(RELAY_TOILET,LOW);
        Serial1.write('t');
        toiletCheckCount++;
        delay(1000);
      }
    }

    // 거실
    if(livingDistance <= 600){
      Serial.println("거실이 감지되었습니다.");  

      if(doorCheck == true){
        doorCheck = false;
        Serial.println("들어오는 상황으로 감지되었습니다."); 
        digitalWrite(RELAY_LIVINGROOM,HIGH);
        Serial1.write('I');
        delay(1000);
      }
      else{
        livingCheck = true;
        livingCheckCount++;
      }
    }
  }

  // 리밋
  if(cutDistance <= 100){
    

    if(cutCheck == true){
      Serial.println("리밋이 해제되었습니다.");  
      cutCheck = false;
      Serial1.write('c');
    }
    else{
      Serial.println("리밋이 시작되었습니다.");
      
      cutCheck = true;
      doorCheck = false;
      livingCheck = false;

      doorCheckCount = 0;
      livingCheckCount = 0;
      toiletCheckCount = 0;
      bedCheckCount = 0;
      
      Serial1.write('C');
    }
    
    delay(2000);
  }




  // 센서 감지됨으로 인한 대기 카운트 해야하는 장소 //

  //현관 카운트 (9초 + 1초)
  if(doorCheckCount != 0 && doorCheckCount<60 && doorCheck == true){
    doorCheckCount++;
    Serial.println(doorCheckCount);  

  }
  else if(doorCheckCount >= 60 && doorCheck == true){
    doorCheckCount = 0;
    doorCheck = false;
    Serial.println("현관의 카운트가 만료되었습니다.");
  }
  else{
    doorCheckCount = 0;
  }

  //거실 카운트 (9초 + 1초)
  if(livingCheckCount != 0 && livingCheckCount<60 && livingCheck == true){
    livingCheckCount++;
    Serial.println(livingCheckCount);  

  }
  else if(livingCheckCount >= 60 && livingCheck == true){
    livingCheckCount = 0;
    livingCheck = false;
    Serial.println("거실의 카운트가 만료되었습니다.");
  }
  else{
    livingCheckCount = 0;
  }

  //화장실 카운트 (3초 + 1초)
  if(toiletCheckCount != 0 && toiletCheckCount<20){
    toiletCheckCount++;
    Serial.println(toiletCheckCount);  
  }
  else if(toiletCheckCount >= 20){
    toiletCheckCount = 0;
    Serial.println("화장실의 카운트가 만료되었습니다.");  
  }


  //침대 카운트 (9초 + 1초)
  if(bedCheckCount != 0 && bedCheckCount<60){
    bedCheckCount++;
    Serial.println(bedCheckCount);
    
  }
  else if(bedCheckCount >= 60){
    bedCheckCount = 0;
    Serial.println("침대의 카운트가 만료되었습니다."); 
  }
  else{
    bedCheckCount = 0;
  }




// 거리 값 이상하면 쓰는장소 //

/*

  Serial.print("리밋 거리 : ");    
  Serial.println(cutDistance);  

  Serial.print("침대 거리 : ");    
  Serial.println(bedDistance); 

  Serial.print("현관 거리 : ");    
  Serial.println(doorDistance);   

  Serial.print("화장실 거리 : ");    
  Serial.println(toiletDistance);  

  Serial.print("거실 거리 : ");    
  Serial.println(livingDistance);  


*/

}
