//
// Copyright 2015 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

// FirebaseDemo_ESP8266 is a sample that demo the different functions
// of the FirebaseArduino API.
#include <SPI.h>
#include <MFRC522.h>
#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

// Set these to run example.
#define FIREBASE_HOST "nsuts-950.firebaseio.com"
#define FIREBASE_AUTH "rSEFERMR06edeGv7ztpUu92BxMC0oeHpaQhV5urh"
#define WIFI_SSID "950"
#define WIFI_PASSWORD "01676123950"

#define SS_PIN D4
#define RST_PIN D2
 
MFRC522 rfid(SS_PIN, RST_PIN); // Instance of the class

MFRC522::MIFARE_Key key; 

// Init array that will store new NUID 
byte nuidPICC[4];

void setup() {
  Serial.begin(115200);

  // connect to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());
  
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

   Serial.begin(115200);
  SPI.begin(); // Init SPI bus
  rfid.PCD_Init(); // Init MFRC522 

  for (byte i = 0; i < 6; i++) {
    key.keyByte[i] = 0xFF;
  }

}

String CardID = "";
void loop() {

  // Reset the loop if no new card present on the sensor/reader. This saves the entire process when idle.
  if ( ! rfid.PICC_IsNewCardPresent())
    return;

  // Verify if the NUID has been readed
  if ( ! rfid.PICC_ReadCardSerial())
    return;

  Serial.print(F("PICC type: "));
  MFRC522::PICC_Type piccType = rfid.PICC_GetType(rfid.uid.sak);
  Serial.println(rfid.PICC_GetTypeName(piccType));

  // Check is the PICC of Classic MIFARE type
  if (piccType != MFRC522::PICC_TYPE_MIFARE_MINI &&  
    piccType != MFRC522::PICC_TYPE_MIFARE_1K &&
    piccType != MFRC522::PICC_TYPE_MIFARE_4K) {
    Serial.println(F("Your tag is not of type MIFARE Classic."));
    return;
  }

  if (rfid.uid.uidByte[0] != nuidPICC[0] || 
    rfid.uid.uidByte[1] != nuidPICC[1] || 
    rfid.uid.uidByte[2] != nuidPICC[2] || 
    rfid.uid.uidByte[3] != nuidPICC[3] ) {
    Serial.println(F("A new card has been detected."));

    // Store NUID into nuidPICC array
     for (byte i = 0; i < 4; i++) {
      nuidPICC[i] = rfid.uid.uidByte[i];
    }

    for (byte i = 0; i < rfid.uid.size; i++) {              
        CardID += rfid.uid.uidByte[i];
       }

    
  // Halt PICC
  rfid.PICC_HaltA();

  // Stop encryption on PCD
  rfid.PCD_StopCrypto1();
  Serial.println(CardID);
  Serial.println();
////////////////////////////////////
// set value
      
  Firebase.setBool("rfid/"+CardID+"/truth", true);
  // handle error
  if (Firebase.failed()) {
      Serial.print("Bool /message failed:");
      Serial.println(Firebase.error());  
      return;
  }
  delay(1000);
  CardID = "";
 ////////////////////////////////////
  }
  else Serial.println(F("Card read previously."));
 
  delay(1000);
}
