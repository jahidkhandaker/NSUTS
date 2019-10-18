#include <SPI.h>
#include <MFRC522.h>
#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>

#define FIREBASE_HOST "nsuts-950.firebaseio.com"
#define FIREBASE_AUTH "rSEFERMR06edeGv7ztpUu92BxMC0oeHpaQhV5urh"
#define WIFI_SSID "950"
#define WIFI_PASSWORD "01676123950"

#define BUSID "Badda Express"

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


void loop() {

  // Reset the loop if no new card present on the sensor/reader. This saves the entire process when idle.
  if ( ! rfid.PICC_IsNewCardPresent())
    return;

  // Verify if the NUID has been readed
  if ( ! rfid.PICC_ReadCardSerial())
    return;

  if (rfid.uid.uidByte[0] != nuidPICC[0] || 
    rfid.uid.uidByte[1] != nuidPICC[1] || 
    rfid.uid.uidByte[2] != nuidPICC[2] || 
    rfid.uid.uidByte[3] != nuidPICC[3] ) {
    Serial.println(F("A new card has been detected."));

    // Store NUID into nuidPICC array
     for (byte i = 0; i < 4; i++) {
      nuidPICC[i] = rfid.uid.uidByte[i];
    }
    
    String CardID = "";
    for (byte i = 0; i < rfid.uid.size; i++) {              
        CardID += rfid.uid.uidByte[i];
       }
  // Halt PICC
  rfid.PICC_HaltA();

  // Stop encryption on PCD
  rfid.PCD_StopCrypto1();
  Serial.println(CardID);
  Serial.println();

  Firebase.setBool("rfid/"+CardID+"/truth", true);
  // handle error
  if (Firebase.failed()) {
      Serial.print("Bool /message failed:");
      Serial.println(Firebase.error());  
      return;
  }
    Firebase.setString("rfid/"+CardID+"/busRfid", BUSID);
  // handle error
  if (Firebase.failed()) {
      Serial.print("String /message failed:");
      Serial.println(Firebase.error());  
      return;
  }
  CardID = "";
  delay(1000);
 
  }
  else Serial.println(F("Card read previously."));
  delay(1000);
}
